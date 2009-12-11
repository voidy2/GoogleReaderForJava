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
     int len = root.getChildNodes().getLength();
     for(int i=0;i<len;i++){
       Node node = root.getChildNodes().item(i);
       if(node.hasChildNodes()){
         if(node.getNodeName().equals("title")){
           title = node.getFirstChild().getNodeValue();
         }
       }
     }
     System.out.println(title);
  }

}
