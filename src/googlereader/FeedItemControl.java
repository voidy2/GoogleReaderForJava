package googlereader;

import java.util.List;

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
    System.out.println(flist.size());
    for ( FeedSource feedSource : flist ) {
      System.out.println(feedSource.getTitle());
    }
  }
}
