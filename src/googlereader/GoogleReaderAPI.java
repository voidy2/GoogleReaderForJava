package googlereader;

import static googlereader.Const.*;
import java.io.BufferedReader;
import java.io.IOException;
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
    //this.getUnreadFeed();
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
    return System.currentTimeMillis();
  }

  public int getUnreadCount() {
    int unreadCount = 0;
    try {
      String url = URI_PREFIXE_API + API_LIST_UNREAD_COUNT;
      NetworkAccess na =
                    new NetworkAccess(url, "GET", null,
              "Cookie", "SID=" + sid + ";T=" + t_token);
      DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = dbfactory.newDocumentBuilder();
      Document doc = builder.parse(na.access());
      Element root = doc.getDocumentElement();
      dispDom(root, 0);
      FeedSourceList fsList = new FeedSourceList(root);
      unreadCount = fsList.getUnreadCount();

      //System.out.println(root.getFirstChild().getAttributes().item(0));
      //System.out.println(root.getElementsByTagName("list").item(0).getNodeName());
    } catch ( ParserConfigurationException ex ) {
      System.out.println("不正なストリーム\n" + ex);
    } catch ( SAXException ex ) {
      System.out.println("パースエラー\n" + ex);
    } catch ( IOException ex ) {
      System.out.println("不正なストリーム\n" + ex);
    }
    return unreadCount;
  }

  public void getUnreadFeed() {
    try {
      String url = URI_PREFIXE_ATOM + ATOM_STATE_READING_LIST;
      NetworkAccess na =
                    new NetworkAccess(url, "GET", null,
              "Cookie", "SID=" + sid + ";T=" + t_token);
      DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = dbfactory.newDocumentBuilder();
      Document doc = builder.parse(na.access());
      Element root = doc.getDocumentElement();
      dispDom(root, 0);
      Entries entries = new Entries(root);
      dispEntries(entries);

    } catch ( ParserConfigurationException ex ) {
      System.out.println("不正なストリーム\n" + ex);
    } catch ( SAXException ex ) {
      System.out.println("パースエラー\n" + ex);
    } catch ( IOException ex ) {
      System.out.println("不正なストリーム\n" + ex);
    }
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
