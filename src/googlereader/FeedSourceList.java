package googlereader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
  public static final String SAVE_DIR = "./label/";

  public FeedSourceList() {
  }

  public void setSubscription(Node xml) {
    NodeList nl = doGetNodeList(xml);
    int end = nl.getLength();
    for ( int j = 0; j < end; ++j ) {
      setItem(nl.item(j));
    }
  }

  public void setLabels(Node xml) {
    NodeList nl = doGetNodeList(xml);
    int end = nl.getLength();
    for ( int j = 0; j < end; ++j ) {
      doSetLabelItem(nl.item(j));
    }
  }

  private void doSetLabelItem(Node n) {
    NodeList nl = n.getChildNodes();
    FeedSource fs = new FeedSource();
    for ( int i = 0; i < nl.getLength(); i++ ) {
      String attr = nl.item(i).getAttributes().item(0).getNodeValue();
      String label = nl.item(i).getChildNodes().item(0).getNodeValue();
      if ( attr.equals("id") ) {
        this.tagMap.put(label, new Tag(label));
      }
    }
  }

  private NodeList doGetNodeList(Node xml) {
    if ( xml.hasChildNodes() ) {
      NodeList nList = xml.getChildNodes();
      for ( int i = 0; i < nList.getLength(); i++ ) {
        Node n = nList.item(i);
        String nodeName = n.getNodeName();
        if ( nodeName.equals("list") ) {
          return n.getChildNodes();
        }
      }
    }
    return null;
  }

  void setItem(Node n) {
    NodeList nl = n.getChildNodes();
    FeedSource fs = new FeedSource();
    for ( int i = 0; i < nl.getLength(); ++i ) {
      String nodeName = nl.item(i).getNodeName();
      if ( nodeName.equals("string") ) {
        String attr = nl.item(i).getAttributes().item(0).getNodeValue();
        String value = nl.item(i).getChildNodes().item(0).getNodeValue();
        if ( attr.equals("id") ) {
          fs.setUrl(value);
        } else if ( attr.equals("title") ) {
          fs.setTitle(value);
        } else if ( attr.equals("sortid") ) {
          fs.setSortId(value);
        } else if ( attr.equals("htmlUrl") ) {
          fs.setHtmlUrl(value);
        }
      } else if ( nodeName.equals("list") ) {
        NodeList category = nl.item(i).getChildNodes();
        for ( int j = 0; j < category.getLength(); ++j ) {
          Node nls = category.item(j).getChildNodes().item(0);
          String tag = nls.getChildNodes().item(0).getNodeValue();
          fs.appendTag(tag);
        }
      }
    }
    fsList.add(fs);
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
      if ( !feedSource.getTags().isEmpty() ) {
        if ( feedSource.isExistTag(label) ) {
          labelFsList.add(feedSource);
        }
      } else {
        if ( label.getName().equals("empty") ) {
          labelFsList.add(feedSource);
        }
      }
    }
    return labelFsList;
  }

  public void save(String label) {
    FileWriter fw = null;
    try {
      Tag tag = new Tag(label);
      String filename = SAVE_DIR + tag.getSmartName() + ".label";
      File directory = new File("./", SAVE_DIR);
      if ( !directory.exists() ) {
        directory.mkdirs();
      }
      fw = new FileWriter(filename);
      for ( FeedSource feedSource : getFsList(tag) ) {
        fw.write(feedSource.getTitle() + "\n");
      }
    } catch ( IOException ex ) {
      ex.printStackTrace();
    } finally {
      try {
        fw.close();
      } catch ( IOException ex ) {
        ex.printStackTrace();
      }
    }
  }

  public void saveAllLabel() {
    for ( String label : tagMap.keySet() ) {
      save(label);
    }
    save("empty");
  }

  public ArrayList<String> readLabelFeed(String filename) {
    FileReader fr = null;
    String filePath = SAVE_DIR + filename + ".label";
    try {
      ArrayList<String> feeds = new ArrayList<String>();
      fr = new FileReader(filePath);
      BufferedReader br = new BufferedReader(fr);
      String line;
      while ( (line = br.readLine()) != null ) {
        feeds.add(line);
        System.out.println(line);
      }
      fr.close();
      br.close();
      return feeds;
    } catch ( IOException ex ) {
      ex.printStackTrace();
    }
    return null;
  }

  public void saveTags() {
    FileWriter fw = null;
    try {
      String filename = SAVE_DIR + "taglist";
      File directory = new File("./", SAVE_DIR);
      if ( !directory.exists() ) {
        directory.mkdirs();
      }
      fw = new FileWriter(filename);
      for ( String tag : tagMap.keySet() ) {
        fw.write(tag + "\n");
      }
    } catch ( IOException ex ) {
      ex.printStackTrace();
    } finally {
      try {
        fw.close();
      } catch ( IOException ex ) {
        ex.printStackTrace();
      }
    }
  }

  public void readTags() {
    FileReader fr = null;
    try {
      String filename = SAVE_DIR + "taglist";
      File directory = new File("./", SAVE_DIR);
      if ( !directory.exists() ) {
        return;
      }
      fr = new FileReader(filename);
      BufferedReader br = new BufferedReader(fr);
      String line;
      while ( (line = br.readLine()) != null ) {
        tagMap.put(line, new Tag(line));
      }
    } catch ( IOException ex ) {
      ex.printStackTrace();
    }
    System.out.println("tags : 読み込み完了");
  }
}
