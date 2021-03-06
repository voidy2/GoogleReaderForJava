package googlereader;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author voidy21
 */
public class Feed {

  private String title;
  private String id;
  private String author;
  private List<Tag> tags = new ArrayList<Tag>();
  private String published;
  private String updated;
  private String link;
  private String summary;
  private FeedSource feedSource;

  Feed(Node xmlFeed) {
    if ( xmlFeed.hasChildNodes() ) {
      NodeList nList = xmlFeed.getChildNodes();
      for ( int i = 0; i < nList.getLength(); i++ ) {
        Node n = nList.item(i);
        String nodeName = n.getNodeName();
        setItem(n, nodeName);
      }
    }
  }

  private void setItem(Node n, String nodeName) {
    if ( nodeName.equals("title") ) {
      title = n.getFirstChild().getNodeValue();
    } else if ( nodeName.equals("id") ) {
      id = n.getFirstChild().getNodeValue();
    } else if ( nodeName.equals("updated") ) {
      updated = n.getFirstChild().getNodeValue();
    } else if ( nodeName.equals("published") ) {
      published = n.getFirstChild().getNodeValue();
    } else if ( nodeName.equals("author") ) {
      author = n.getFirstChild().getFirstChild().getNodeValue();
    } else if ( nodeName.equals("category") ) {
      String category = n.getAttributes().item(0).getNodeValue();
      tags.add(new Tag(category));
    } else if ( nodeName.equals("link") ) {
      link = n.getAttributes().item(0).getNodeValue();
    } else if ( nodeName.endsWith("summary") || nodeName.equals("content") ) {
      summary = n.getFirstChild().getNodeValue();
    } else if ( nodeName.equals("source") ) {
      NodeList sourceList = n.getChildNodes();
      String sourceTitle = "";
      String sourceLink = "";
      for ( int j = 0; j < sourceList.getLength(); j++ ) {
        Node sn = sourceList.item(j);
        String nsName = sn.getNodeName();
        if ( nsName.equals("title") ) {
          sourceTitle = sn.getFirstChild().getNodeValue();
        } else if ( nsName.equals("link") ) {
          sourceLink = sn.getAttributes().item(0).getNodeValue();
        }
      }
      feedSource = new FeedSource(sourceTitle, sourceLink);
    }
  }

  public String getAuthor() {
    return author;
  }

  public String getId() {
    return id;
  }

  public String getLink() {
    return link;
  }

  public String getPublished() {
    return published;
  }

  public String getSourceLink() {
    return feedSource.getLink();
  }

  public String getSummary() {
    return summary;
  }

  public List<Tag> getTags() {
    return tags;
  }

  public String getTitle() {
    return title;
  }

  public String getSourceTitle() {
    return feedSource.getTitle();
  }

  public String getUpdated() {
    return updated;
  }
}
