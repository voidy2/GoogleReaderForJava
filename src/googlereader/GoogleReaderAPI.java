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
  private FeedSourceList fsList;

  /**
   * コンストラクタ
   * @param userId Googleアカウントのユーザ名
   * @param password Googleアカウントのパスワード
   */
  public GoogleReaderAPI(String userId, String password) {
    this.userId = userId;
    this.password = password;
    this.loginAuth();
  }

  /**
   * コンストラクタから呼ばれるログイン処理(SIDとTトークンの設定)
   */
  private void loginAuth() {
    this.setSid();
    this.setT_Token();
  }

  /**
   * SIDを取得して設定する
   */
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

  /**
   * Tトークンを取得して設定する
   */
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

  /**
   * Unix時間(秒)を返す
   * @return 1970年1月1日0時0分0秒からの現在までの経過時間(秒)
   */
  public long getTimestamp() {
    return System.currentTimeMillis() / 1000L;
  }

  /**
   * 指定したURLからXMLFeedを取得する
   * @param url 指定するURL
   * @return 取得したxml
   */
  private Element getFeed(String url) {
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

  /**
   * 指定したATOMFeedを取得する
   * @param atomState 取得したいFeedアイテムの状態
   * 　(例: スターの付いたアイテムを開きたい場合 : /user/-/state/com.google/starred)
   * @param ap 指定する付加パラメータ
   * @return 取得したXMLフィード
   */
  public Element getAtomFeed(String atomState, AtomPrameters ap) {
    String url = URI_PREFIXE_ATOM + atomState + ap.getParameters();
    return getFeed(url);
  }

  /**
   * だいたいの未読記事数を返す
   * @return 未読記事数
   */
  public int getUnreadCount() {
    //だいたいなのは1000件以上未読がある場合はきちんとした数字をAPIが返してくれないため
    String url = URI_PREFIXE_API + API_LIST_UNREAD_COUNT;
    Element root = getFeed(url);
    fsList = new FeedSourceList(root);
    return fsList.getUnreadCount();
  }

  /**
   * 未読アイテムを返す
   * @param ap 付加指定するパラメータ
   */
  public void doGetUnreadFeed(AtomPrameters ap) {
    ap.setExclude_target(new Tag(ATOM_STATE_READ));
    Element root = getAtomFeed(ATOM_STATE_READING_LIST, ap);
    //dispDom(root, 0);
    dispEntries(new Entries(root));
  }

  /**
   * スター付きアイテムを返す
   * @param ap 付加指定するパラメータ
   */
  public void doGetStarredFeed(AtomPrameters ap) {
    Element root = getAtomFeed(ATOM_STATE_STARRED, ap);
    //dispDom(root, 0);
    dispEntries(new Entries(root));
  }

  /**
   * 指定したラベルのアイテムを返す
   * @param tag 指定するラベル
   * @param ap 付加指定するパラメータ
   */
  public void doGetLabelFeed(Tag tag, AtomPrameters ap) {
    try {
      String tagName = URLEncoder.encode(tag.getName(), "UTF-8");
      Element root = getAtomFeed(tagName, ap);
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

  /**
   * 指定した記事idのスターを付ける
   * @param id 指定する記事id
   */
  public void addStar(String id) {
    String[] postArgs = {
      "i=" + id,
      "a=" + ATOM_STATE_STARRED,
      "T=" + this.t_token
    };
    this.editApi(API_EDIT_TAG, postArgs);
  }

  /**
   * 指定した記事idのスターを外す
   * @param id 指定する記事id
   */
  public void delStar(String id) {
    String[] postArgs = {
      "i=" + id,
      "r=" + ATOM_STATE_STARRED,
      "T=" + this.t_token
    };
    this.editApi(API_EDIT_TAG, postArgs);
  }

  /**
   * 指定した記事idを既読にする
   * @param id 指定する記事id
   */
  public void doSetRead(String id) {
    String[] postArgs = {
      "i=" + id,
      "a=" + ATOM_STATE_READ,
      "T=" + this.t_token
    };
    this.editApi(API_EDIT_TAG, postArgs);
  }

  /**
   * 指定した記事idを未読にする
   * @param id 指定する記事id
   */
  public void doSetUnread(String id) {
    String[] postArgs = {
      "i=" + id,
      "a=" + ATOM_STATE_UNREAD,
      "T=" + this.t_token
    };
    this.editApi(API_EDIT_TAG, postArgs);
  }

  /**
   * 指定した記事idにラベルを付加する
   * @param id 指定する記事id
   * @param label 追加するラベル
   */
  public void addLabel(String id, Tag label) {
    String[] postArgs = {
      "i=" + id,
      "a=" + label.getName(),
      "T=" + this.t_token
    };
    this.editApi(API_EDIT_TAG, postArgs);
  }

  /**
   * 指定した記事idのラベルを外す
   * @param id 指定する記事id
   * @param label 削除するラベル
   */
  public void delLabel(String id, Tag label) {
    String[] postArgs = {
      "i=" + id,
      "r=" + label.getName(),
      "T=" + this.t_token
    };
    this.editApi(API_EDIT_TAG, postArgs);
  }

  /**
   * デバッグ用のDOM表示メソッド
   * 指定したNodeから再帰的に下層を表示していく
   * @param n ノード(rootを指定すると良い)
   * @param c インデント数(最初は0を指定すると良い)
   */
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

  /**
   * デバッグ用のエントリー表示メソッド
   * フィード情報を表示していく
   * @param entries 指定するエントリー
   */
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
