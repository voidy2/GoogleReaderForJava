package googlereader;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Feedの1つ1つの記事を扱うクラス
 * @author voidy21
 */
public class FeedItem {

  private String title;
  private List<Tag> tags = new ArrayList<Tag>();
  private long timestamp;
  private String link;
  private String summary;

  public FeedItem() {
  }

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public List<Tag> getTags() {
    return tags;
  }

  public void setTags(List<Tag> tags) {
    this.tags = tags;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void doSetParams(Node n) {
    String ts = n.getAttributes().item(0).getNodeValue();
    setTimestamp(Long.parseLong(ts));
    NodeList nl = n.getChildNodes();
    for ( int i = 0; i < nl.getLength(); i++ ) {
      Node nn = nl.item(i);
      doSetParamsSub(nn);
    }
  }

  private void doSetParamsSub(Node nn) {
    String nName = nn.getNodeName();
    if ( nName.equals("title") ) {
      String s = nn.getFirstChild().getNodeValue();
      setTitle(s);
    } else if ( nName.equals("category") ) {
      NamedNodeMap attr = nn.getAttributes();
      int len = attr.getLength();
      if ( len == 3 ) {
	String reader = attr.item(1).getNodeValue();
	if ( reader.equals(Const.URI_PREFIXE_READER) ) {
	  String tag = attr.item(2).getNodeValue();
	  tags.add(new Tag(tag));
	}
      }
    } else if ( nName.equals("link") ) {
      String s = nn.getAttributes().item(0).getNodeValue();
      setLink(s);
    } else if ( nName.equals("summary")) {
      String s = nn.getFirstChild().getNodeValue();
      setSummary(s);
    }
  }
}
