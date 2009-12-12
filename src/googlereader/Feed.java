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

  String title;
  String sourceTitle;
  String author;
  List<String> tags = new ArrayList<String>();
  String published;
  String updated;
  String link;
  String sourceLink;
  String summary;

  Feed(Node xmlFeed) {
    if ( xmlFeed.hasChildNodes() ) {
      NodeList nList = xmlFeed.getChildNodes();
      for ( int i = 0; i < nList.getLength(); i++ ) {
        Node n = nList.item(i);
        String nodeName = n.getNodeName();
        if ( nodeName.equals("title") ) {
          title = n.getFirstChild().getNodeValue();
        }
        else if ( nodeName.equals("updated") ) {
          updated = n.getFirstChild().getNodeValue();
        }
        else if ( nodeName.equals("published") ) {
          published = n.getFirstChild().getNodeValue();
        }
        else if ( nodeName.equals("author") ) {
          author = n.getFirstChild().getFirstChild().getNodeValue();
        }
        else if ( nodeName.equals("category") ) {
          String category = n.getAttributes().item(0).getNodeValue();
          tags.add(category);
        }
        else if ( nodeName.equals("link") ) {
          link = n.getAttributes().item(0).getNodeValue();
        }
        else if( nodeName.equals("content")) {
          summary = n.getFirstChild().getNodeValue();
        }
        else if ( nodeName.equals("source") ) {
          NodeList sourceList = n.getChildNodes();
          for ( int j = 0; j < sourceList.getLength(); j++ ) {
            Node sn = sourceList.item(j);
            String nsName = sn.getNodeName();
            if ( nsName.equals("title") ) {
              sourceTitle = sn.getFirstChild().getNodeValue();
            }
            else if ( nsName.equals("link") ) {
              sourceLink = sn.getAttributes().item(0).getNodeValue();
            }
          }
        }
      }

    }
  }

  public String getAuthor() {
    return author;
  }

  public String getLink() {
    return link;
  }

  public String getPublished() {
    return published;
  }

  public String getSourceLink() {
    return sourceLink;
  }

  public String getSummary() {
    return summary;
  }

  public List<String> getTags() {
    return tags;
  }

  public String getTitle() {
    return title;
  }

  public String getSourceTitle() {
    return sourceTitle;
  }

  public String getUpdated() {
    return updated;
  }
}
