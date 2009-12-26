package googlereader;

import java.util.List;
import org.w3c.dom.Element;

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
    List<FeedSource> flist = gapi.getFsList().getFsList(tag);
    for ( FeedSource feedSource : flist ) {
      System.err.println(feedSource.getTitle());
      Element xml = gapi.getAtomFeed(feedSource.getUrl(), ap);
      gapi.dispDom(xml, 0);
    }
  }
}
