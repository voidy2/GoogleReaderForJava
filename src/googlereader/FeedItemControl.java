package googlereader;

import org.w3c.dom.Node;

/**
 * フィードを管理するクラス
 * @author voidy21
 */
public class FeedItemControl {

  private AtomParameters ap;
  private GoogleReaderAPI gapi;
  public static final String SAVE_DIR = "./log/";

  public FeedItemControl(GoogleReaderAPI gapi) {
    this.gapi = gapi;
    ap = new AtomParameters();
  }

  public void saveFeedItems(Tag tag) {
    Node xml = gapi.doGetLabelFeed(tag, ap);
  }
}
