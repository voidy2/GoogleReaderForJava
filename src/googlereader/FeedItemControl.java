package googlereader;

import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * フィードを管理するクラス
 * @author voidy21
 */
public class FeedItemControl {

  private AtomParameters ap;
  private GoogleReaderAPI gapi;
  public static final String SAVE_DIR = "./log/";
  private String continuation;

  public FeedItemControl(GoogleReaderAPI gapi) {
    this.gapi = gapi;
    ap = new AtomParameters();
  }

  public void saveFeedItems(Tag tag) {
    List<FeedSource> flist = gapi.getFsList().getFsList(tag);
    for ( FeedSource feedSource : flist ) {
      Element xml = gapi.getAtomFeed(feedSource.getEncodedUrl(), ap);
      loadXml(xml, feedSource);
    }
  }

  private void loadXml(Element xml, FeedSource fs) {
    NodeList nl = xml.getChildNodes();
    for ( int i = 0; i < nl.getLength(); i++ ) {
      Node n = nl.item(i);
      String nName = n.getNodeName();
      if ( nName.equals("gr:continuation") ) {
	continuation = n.getFirstChild().getNodeValue();
      } else if ( nName.equals("entry") || nName.equals("content") ) {
	FeedItem fItem = new FeedItem();
	fItem.doSetParams(n);
      }
    }
  }

  public String getContinuation() {
    return continuation;
  }
}
