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
    System.out.println(title);
    System.out.println(updated);
    System.out.println(feedList.get(0).getSourceTitle());
    System.out.println(feedList.get(0).getSourceLink());
    System.out.println(feedList.get(0).getTitle());
    System.out.println(feedList.get(0).getUpdated());
    System.out.println(feedList.get(0).getAuthor());
    System.out.println(feedList.get(0).getLink());
    List<String> tags = feedList.get(0).getTags();
    for ( String tag : tags ) {
      System.out.println("tag : " + tag);
    }
    System.out.println(feedList.get(0).getSummary());
  }
}
