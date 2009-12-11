package googlereader;

import java.util.List;
import org.w3c.dom.Node;

/**
 *
 * @author voidy21
 */
public class Entries {
  String title;
  List<Feed> feedList;
  public Entries(Node root) {
     String len = root.getChildNodes().item(3).getFirstChild().getNodeValue();
     System.out.println(len);
  }

}
