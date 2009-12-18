package googlereader;

import static googlereader.Const.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import stringutils.StringUtils;
import networkaccess.NetworkAccess;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author voidy21
 */
public class GoogleReaderAPI {

  private String userId;
  private String password;
  private String sid;
  private String t_token;
  private FeedSourceList fsList;

  /**
   *
   * @param userId
   * @param password
   */
  public GoogleReaderAPI(String userId, String password) {
    this.userId = userId;
    this.password = password;
    this.loginAuth();
    System.out.println(sid);
    System.out.println(t_token);
    System.out.println(this.getUnreadCount());
    //this.getLabelFeed("情報");
    //this.getUnreadFeed();
    AtomPrameters ap = new AtomPrameters();
    ap.setCount(20);

    ap.setExclude_target(new Tag("user/-/state/com.google/read"));
    //ap.setExclude_target(new Tag("user/-/state/com.google/starred"));
    //ap.setOrder("o");
    //ap.setStart_time(this.getTimestamp()-100000000L);
    String params = ap.getParameters();
    System.out.println(params);
    //this.getLabelFeed("情報", params);
    //this.getUnreadFeed(params);
    this.addStar("tag:google.com,2005:reader/item/7af6fe4290be09b3");
  }

  private void loginAuth() {
    this.setSid();
    this.setT_Token();
  }

  private void setSid() {
    try {
      String[] params = { "Email=" + URLEncoder.encode(userId, "UTF-8"),
                          "Passwd=" + URLEncoder.encode(password, "UTF-8"),
                          "service=reader" };
      String param = StringUtils.join(params, "&");
      NetworkAccess na = new NetworkAccess(URI_LOGIN, "POST", param);
      BufferedReader in =
                     new BufferedReader(new InputStreamReader(na.access()));
      String line;
      while ( (line = in.readLine()) != null ) {
        String response = line.substring(0, 4);
        if ( response.equals("SID=") ) {
          this.sid = line.substring(4);
          break;
        }
      }
      in.close();
    } catch ( UnsupportedEncodingException ex ) {
      System.out.println("URLEncoderに失敗\n" + ex);
    } catch ( IOException ex ) {
      System.out.println("不正なストリーム\n" + ex);
    }
  }

  private void setT_Token() {
    try {
      String url = URI_PREFIXE_API + API_TOKEN;
      NetworkAccess na =
                    new NetworkAccess(url, "GET", null, "Cookie", "SID=" + sid);
      BufferedReader in =
                     new BufferedReader(new InputStreamReader(na.access()));
      String line;
      while ( (line = in.readLine()) != null ) {
        this.t_token = line;
      }
      in.close();
    } catch ( IOException ex ) {
      System.out.println("不正なストリーム\n" + ex);
    }
  }

  public long getTimestamp() {
    return System.currentTimeMillis() / 1000L;
  }

  public Element getFeed(String url) {
    try {
      NetworkAccess na = new NetworkAccess(url, "GET", null,
              "Cookie", "SID=" + sid + ";T=" + t_token);
      DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = dbfactory.newDocumentBuilder();
      Document doc = builder.parse(na.access());
      Element root = doc.getDocumentElement();
      return root;
    } catch ( ParserConfigurationException ex ) {
      System.out.println("不正なストリーム\n" + ex);
    } catch ( SAXException ex ) {
      System.out.println("パースエラー\n" + ex);
    } catch ( IOException ex ) {
      System.out.println("不正なストリーム\n" + ex);
    }
    return null;
  }

  public int getUnreadCount() {
    String url = URI_PREFIXE_API + API_LIST_UNREAD_COUNT;
    Element root = getFeed(url);
    fsList = new FeedSourceList(root);
    return fsList.getUnreadCount();
  }

  public void getUnreadFeed(String params) {
    String url = URI_PREFIXE_ATOM + ATOM_STATE_READING_LIST + params;
    Element root = getFeed(url);
    //dispDom(root, 0);
    dispEntries(new Entries(root));
  }

  public void getStarredFeed(String params) {
    String url = URI_PREFIXE_ATOM + ATOM_STATE_STARRED + params;
    Element root = getFeed(url);
    //dispDom(root, 0);
    dispEntries(new Entries(root));
  }

  public void getLabelFeed(String tag, String params) {
    try {
      String url = URI_PREFIXE_ATOM + ATOM_PREFIXE_LABEL + URLEncoder.encode(tag, "UTF-8") + params;
      Element root = getFeed(url);
      //dispDom(root, 0);
      dispEntries(new Entries(root));
    } catch ( UnsupportedEncodingException ex ) {
      System.out.println(ex);
    }
  }

  private boolean editApi(String target, String[] postArgs) {
    try {
      String params = StringUtils.join(postArgs, "&");
      String url = URI_PREFIXE_API + target;
      NetworkAccess na = new NetworkAccess(url, "POST",
              params, "Cookie", "SID=" + sid + ";T=" + t_token);
      BufferedReader br = new BufferedReader(
              new InputStreamReader(na.access()));
      String returnStr = br.readLine();
      if ( returnStr.equals("OK") ) {
        return true;
      }
    } catch ( IOException ex ) {
      System.out.println(ex);
    } catch ( NullPointerException ex ) {
      System.out.println(ex);
    }
    return false;
  }

  public void addStar(String id) {
    String[] postArgs = {
      "i=" + id,
      "a=" + ATOM_STATE_STARRED,
      "T=" + this.t_token
    };
    this.editApi(API_EDIT_TAG, postArgs);
  }

  public void removeStar(String id) {
    String[] postArgs = {
      "i=" + id,
      "r=" + ATOM_STATE_STARRED,
      "T=" + this.t_token
    };
    this.editApi(API_EDIT_TAG, postArgs);
  }

  public void dispDom(Node n, int c) {
    c++;
    int len = n.getChildNodes().getLength();
    if ( len != 0 ) {
      for ( int i = 0; i < len; i++ ) {
        Node node = n.getChildNodes().item(i);
        for ( int j = 1; j < c; j++ ) {
          System.out.print("  ");
        }
        switch ( node.getNodeType() ) {
          case Node.ELEMENT_NODE: {
            System.out.print(node.getNodeName());
            if ( node.getAttributes() != null ) {
              for ( int k = 0; k < node.getAttributes().getLength(); k++ ) {
                System.out.print(" (" + node.getAttributes().item(k).
                        getNodeValue() + ")");
              }
            }
            System.out.println();
            break;
          }
          case Node.TEXT_NODE: {
            System.out.println(node.getNodeValue());
            break;
          }
        }
        dispDom(node, c);
      }
    }
  }

  public void dispEntries(Entries entries) {
    System.out.println(entries.getTitle());
    System.out.println(entries.getUpdated());
    List<Feed> feedList = entries.getFeedList();
    for ( Feed f : feedList ) {
      System.out.println("--------------------------------------------");
      System.out.println(f.getSourceTitle());
      System.out.println(f.getSourceLink());
      System.out.println(f.getTitle());
      System.out.println(f.getId());
      System.out.println(f.getUpdated());
      System.out.println(f.getAuthor());
      System.out.println(f.getLink());
      List<Tag> tags = f.getTags();
      for ( Tag tag : tags ) {
        System.out.println("\ttag : " + tag.getName());
      }
      System.out.println(f.getSummary());
    }
  }
}
