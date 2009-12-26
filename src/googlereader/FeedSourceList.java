package googlereader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import networkaccess.ImageGet;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author voidy21
 */
public class FeedSourceList {

  private List<FeedSource> fsList = new ArrayList<FeedSource>();
  private HashMap<String, Tag> tagMap = new HashMap<String, Tag>();
  private HashMap<String, Integer> unreadMap = new HashMap<String, Integer>();
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
    for ( int i = 0; i < nl.getLength(); i++ ) {
      String attr = nl.item(i).getAttributes().item(0).getNodeValue();
      String label = nl.item(i).getFirstChild().getNodeValue();
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

  public void doSetUnreadCount(Node xmlUnreadList) {
    if ( xmlUnreadList.hasChildNodes() ) {
      NodeList nList = xmlUnreadList.getChildNodes();
      for ( int i = 0; i < nList.getLength(); i++ ) {
        Node n = nList.item(i);
        String nodeName = n.getNodeName();
        if ( nodeName.equals("list") ) {
          NodeList nl = n.getChildNodes();
          for ( int j = 0; j < nl.getLength(); j++ ) {
            doSetUnreadCountItem(nl.item(j));
          }
        }
      }
      doSetUnreadCount();
    }
  }

  private void doSetUnreadCountItem(Node n) {
    NodeList nl = n.getChildNodes();
    String url = "";
    for ( int i = 0; i < nl.getLength(); i++ ) {
      String nodeName = nl.item(i).getNodeName();
      if ( nodeName.equals("string") ) {
        String source = nl.item(i).getFirstChild().getNodeValue();
        url = source;
      } else if ( nodeName.equals("number") ) {
        String attr = nl.item(i).getAttributes().item(0).getNodeValue();
        if ( attr.equals("count") ) {
          Integer count = new Integer(
                  nl.item(i).getChildNodes().item(0).getNodeValue());
	    unreadMap.put(url, count);
        }
      }
    }
  }

  private void doSetUnreadCount() {
    int allUnreadCount = 0;
    int size = fsList.size();
    for ( int i = 0; i < size; ++i ) {
      FeedSource feedSource = fsList.get(i);
      int count = 0;
      if ( unreadMap.containsKey(feedSource.getUrl()) ) {
        count = unreadMap.get(feedSource.getUrl());
        feedSource.setUnreadCount(count);
      }
      //ureadMapに無いものは0にする
      feedSource.setUnreadCount(count);
      allUnreadCount += count;
    }
    size = unreadMap.size();
    for ( String s : unreadMap.keySet() ) {
      if ( s.substring(0, 4).equals("user") ) {
        int count = unreadMap.get(s);
        if ( tagMap.containsKey(s) ) {
          tagMap.get(s).setUnreadCount(count);
        }
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
    Tag tag = new Tag(label);
    String filename = tag.getSmartName() + ".label";
    List<FeedSource> fslist = getFsList(tag);
    String[] ws = new String[fslist.size()];
    int x = 0;
    for ( FeedSource feedSource : getFsList(tag) ) {
      ws[x] = feedSource.getTitle() + "\n";
      ws[x] += feedSource.getHtmlUrl() + "\n";
      x++;
    }
    doSave(filename, ws);
  }

  public void saveFavicons() {
    for ( FeedSource fs : fsList ) {
      ImageGet.saveFavicon(fs.getHtmlUrl());
    }
  }

  public void saveAllLabel() {
    for ( String label : tagMap.keySet() ) {
      save(label);
    }
    save("empty");
  }

  public ArrayList<String> readLabelFeed(String filename) {
    String filePath = filename + ".label";
    ArrayList<String> strs = doRead(filePath);
    ArrayList<String> feeds = new ArrayList<String>();
    for ( String line : strs ) {
      feeds.add(line);
    }
    return feeds;
  }

  public void saveTags() {
    String filename = "taglist";
    String[] ws = new String[tagMap.size()];
    int x = 0;
    for ( String tag : tagMap.keySet() ) {
      ws[x++] = tag + "\n";
    }
    doSave(filename, ws);
  }

  public void readTags() {
    String filename = "taglist";
    ArrayList<String> strs = doRead(filename);
    for ( String line : strs ) {
      tagMap.put(line, new Tag(line));
    }
    System.out.println("tags : 読み込み完了");
  }

  public void readSubscriptions() {
    String filename = "sublist";
    ArrayList<String> strs = doRead(filename);
    int x = 0;
    FeedSource fs = null;
    for ( String line : strs ) {
      if ( x % 6 == 0 ) {
        fs = new FeedSource();
      }
      readCaseSetSubs(x % 6, line, fs);
      if ( x % 6 - 1 == 0 ) {
        fsList.add(fs);
      }
      x++;
    }
  }

  private void readCaseSetSubs(int x, String line, FeedSource fs) {
    switch ( x ) {
      case 0:
        fs.setUrl(line);
        break;
      case 1:
        fs.setTitle(line);
        break;
      case 2:
        fs.setHtmlUrl(line);
        break;
      case 3:
        fs.setSortId(line);
        break;
      case 4:
        int count = Integer.parseInt(line);
        fs.setUnreadCount(count);
        break;
      case 5:
        String[] tags = line.split(",");
        for ( String tag : tags ) {
          fs.getTags().add(new Tag(tag));
        }
    }
  }

  public void saveSubscriptions() {
    String filename = "sublist";
    String[] ws = new String[fsList.size()];
    int x = 0;
    for ( FeedSource fs : fsList ) {
      ws[x] = fs.getUrl() + "\n";
      ws[x] += fs.getTitle() + "\n";
      ws[x] += fs.getHtmlUrl() + "\n";
      ws[x] += fs.getSortId() + "\n";
      ws[x] += fs.getUnreadCount() + "\n";
      int size = fs.getTags().size();
      for ( int i = 0; i < size; i++ ) {
        ws[x] += fs.getTags().get(i).getName();
        if ( i != size - 1 ) {
          ws[x] += ",";
        }
      }
      ws[x] += "\n";
      x++;
    }
    doSave(filename, ws);
  }

  private void doSave(String saveFilename, String[] writeString) {
    FileWriter fw = null;
    try {
      String filename = SAVE_DIR + saveFilename;
      File directory = new File("./", SAVE_DIR);
      if ( !directory.exists() ) {
        directory.mkdirs();
      }
      fw = new FileWriter(filename);
      for ( String str : writeString ) {
        fw.write(str);
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

  private ArrayList<String> doRead(String readFilename) {
    ArrayList<String> strs = new ArrayList<String>();
    FileReader fr = null;
    try {
      String filename = SAVE_DIR + readFilename;
      File directory = new File("./", SAVE_DIR);
      if ( !directory.exists() ) {
        throw new IOException("Error : ディレクトリが存在しない");
      }
      fr = new FileReader(filename);
      BufferedReader br = new BufferedReader(fr);
      String line;
      while ( (line = br.readLine()) != null ) {
        strs.add(line);
      }
    } catch ( IOException ex ) {
      ex.printStackTrace();
    }
    return strs;
  }
}
