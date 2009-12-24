package googlereader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author voidy21
 */
public class FeedSourceList {

  private List<FeedSource> fsList = new ArrayList<FeedSource>();
  private HashMap<String, Tag> tagMap = new HashMap<String, Tag>();
  private int unreadCount;
  private String currentTag = null;

  public FeedSourceList(Node xmlUnreadList) {
    if ( xmlUnreadList.hasChildNodes() ) {
      NodeList nList = xmlUnreadList.getChildNodes();
      for ( int i = 0; i < nList.getLength(); i++ ) {
        Node n = nList.item(i);
        String nodeName = n.getNodeName();
        if ( nodeName.equals("list") ) {
          NodeList nl = n.getChildNodes();
          for ( int j = 0; j < nl.getLength(); j++ ) {
            setItem(nl.item(j));
          }
        }
      }
      doSetUnreadCount();
    }
  }

  private void setItem(Node n) {
    NodeList nl = n.getChildNodes();
    FeedSource fs = new FeedSource();
    for ( int i = 0; i < nl.getLength(); i++ ) {
      String nodeName = nl.item(i).getNodeName();
      if ( nodeName.equals("string") ) {
        String source = nl.item(i).getFirstChild().getNodeValue();
        if ( source.substring(0, 4).equals("user") ) {
          fs.appendTag(source);
          currentTag = source;
          this.tagMap.put(source, new Tag(source));
        } else {
          fs.setTitle(source);
          fs.appendTag(currentTag);
        }
      } else if ( nodeName.equals("number") ) {
        String attr = nl.item(i).getAttributes().item(0).getNodeValue();
        if ( attr.equals("count") ) {
          int count = Integer.parseInt(
                  nl.item(i).getChildNodes().item(0).getNodeValue());
          fs.setUnreadCount(count);
        }
      }
    }
    if ( fs != null ) {
      fsList.add(fs);
    }
  }

  private void doSetUnreadCount() {
    int allUnreadCount = 0;
    int size = fsList.size();
    for ( int i = 0; i < size; ++i ) {
      FeedSource feedSource = fsList.get(i);
      int count = feedSource.getUnreadCount();
      if ( feedSource.getTags().get(0).getName() != null ) {
        Tag tag = tagMap.get(feedSource.getTags().get(0).getName());
        int tagUnreadCount = tag.getUnreadCount();
        tag.setUnreadCount(tagUnreadCount + count);
        //同じURLのフィードはカウントしないようにしたい
        //ここに処理を書く
        //HashMapを使うといいと思う
        //
        allUnreadCount += count;
      } else {
        allUnreadCount += count;
      }
    }
    unreadCount = allUnreadCount;
  }

  public List<FeedSource> getFsList() {
    return fsList;
  }

  public HashMap<String, Tag> getTagList() {
    return tagMap;
  }

  public int getUnreadCount() {
    return unreadCount;
  }

  public List<FeedSource> getFsList(Tag label) {
    List<FeedSource> labelFsList = new ArrayList<FeedSource>();
    for ( FeedSource feedSource : fsList ) {
      if ( feedSource.getTitle() != null ) {
        if ( feedSource.isExistTag(label) ) {
          labelFsList.add(feedSource);
        }
      }
    }
    return labelFsList;
  }

  public void save(String label) {
    Tag tag = new Tag(label);
    //保存する処理をあとで書く！！！
    for ( FeedSource feedSource : getFsList(tag) ) {
      System.out.println(feedSource.getTitle());
    }
  }
}
