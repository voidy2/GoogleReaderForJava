package googlereader;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Node;

/**
 *
 * @author voidy21
 */
public class Entries {

  String title;
  String updated;
  List<Feed> feedList = new ArrayList<Feed>();

  public Entries(Node root) {
    int len = root.getChildNodes().getLength();
    for ( int i = 0; i < len; i++ ) {
      Node node = root.getChildNodes().item(i);
      if ( node.hasChildNodes() ) {
        String nodeName = node.getNodeName();
        if ( nodeName.equals("title") ) {
          title = node.getFirstChild().getNodeValue();
        }
        if ( nodeName.equals("updated") ) {
          updated = node.getFirstChild().getNodeValue();
        }
        if ( nodeName.equals("entry") ) {
          Feed feed = new Feed(node);
          feedList.add(feed);
        }
      }
    }
  }

  public List<Feed> getFeedList() {
    return feedList;
  }

  public String getTitle() {
    return title;
  }

  public String getUpdated() {
    return updated;
  }
}
