package googlereader;

import fileio.FileIO;
import java.util.ArrayList;
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
  private FileIO fileIo = new FileIO(SAVE_DIR);
  private String continuation = "";

  public FeedItemControl(GoogleReaderAPI gapi) {
    this.gapi = gapi;
    ap = new AtomParameters();
  }

  public void getFeedItems(Tag tag) {
    List<FeedSource> flist = gapi.getFsList().getFsList(tag);
    for ( FeedSource feedSource : flist ) {
      int uc = feedSource.getUnreadCount();
      ap.setCount(uc);
      ap.setExclude_target(new Tag(Const.ATOM_STATE_READ));
      ap.setContinuation(continuation);
      Element xml = gapi.getAtomFeed(feedSource.getEncodedUrl(), ap);
      loadXml(xml, feedSource);
    }
  }

  public void saveFeedItems(Tag tag) {
    List<FeedSource> flist = gapi.getFsList().getFsList(tag);
    for ( FeedSource feedSource : flist ) {
      List<FeedItem> fItems = feedSource.getItems();
      int uc = fItems.size();
      ArrayList<String> ws = new ArrayList<String>();
      String filename = feedSource.getEncodedUrl() + ".log";
      ws.add(uc + "\n\n");
      for ( FeedItem fi : fItems ) {
	String w = fi.getTitle() + "\n";
	w += fi.getTimestamp() + "\n";
	w += fi.getLink() + "\n";
	w += "[[\n";
	w += fi.getSummary() + "\n";
	w += "]]\n";
	List<Tag> tags = fi.getTags();
	int size = tags.size();
	for ( int i = 0; i < size; i++ ) {
	  w += tags.get(i).getName();
	  if ( i != size - 1 ) {
	    w += ",";
	  }
	}
	w += "\n";
	ws.add(w);
      }
      fileIo.doSave(filename, ws);
    }
  }

  public void readFeedItems(Tag tag) {
    List<FeedSource> flist = gapi.getFsList().getFsList(tag);
    for ( FeedSource feedSource : flist ) {
      String filename = feedSource.getEncodedUrl() + ".log";
      ArrayList<String> strs = fileIo.doRead(filename);
      doSetFeedItems(strs, feedSource);
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
	fs.getItems().add(fItem);
      }
    }
  }

  public String getContinuation() {
    return continuation;
  }

  private void readCaseSetSubs(int x, String line, FeedItem fItem) {
    switch ( x ) {
      case 0:
	fItem.setTitle(line);
	break;
      case 1:
	fItem.setTimestamp(Long.parseLong(line));
	break;
      case 2:
	fItem.setLink(line);
	break;
      case 3:
	fItem.setSummary(line);
	break;
      case 4:
	String[] tags = line.split(",");
	for ( String tag : tags ) {
	  fItem.getTags().add(new Tag(tag));
	}
    }
  }

  private void doSetFeedItems(ArrayList<String> strs, FeedSource feedSource) {
    strs.remove(0);
    strs.remove(0);
    int x = 0;
    FeedItem fItem = null;
    String content = "";
    for ( String line : strs ) {
      if ( x % 5 == 0 ) {
	fItem = new FeedItem();
      }
      if ( x % 5 == 3 ) {
	if ( line.equals("]]") ) {
	  readCaseSetSubs(3, content, fItem);
	  content = "";
	} else {
	  if ( !line.equals("[[") ) {
	    content += line;
	  }
	  continue;
	}
      } else {
	readCaseSetSubs(x % 5, line, fItem);
      }
      if ( x % 5 - 1 == 0 ) {
	feedSource.getItems().add(fItem);
      }
      x++;
    }
  }
}
