package framecontrol;

import googlereader.FeedSourceList;
import googlereader.GoogleReaderAPI;
import googlereader.Tag;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;

/**
 * JTreeを継承したラベルを表示するコンポーネント
 * @author voidy21
 */
@SuppressWarnings( "serial" )
public class LabelTree extends JTree {
  private GoogleReaderAPI gapi;

  public LabelTree(GoogleReaderAPI gapi) {
    this.gapi = gapi;
    setDoubleBuffered(true);
    doMakeLabelTree();
  }

  private void doMakeLabelTree() {
    Collection<Tag> labels = gapi.getFsList().getTagList().values();
    LabelTreeNode root = new LabelTreeNode("GoogleReader");
    for(Tag label : labels){
      String labelName = label.getSmartName();
      LabelTreeNode tree = new LabelTreeNode(labelName);
      root.add(tree);
      ArrayList<String> feeds = readLabelFeed(labelName);
      for(String feed : feeds) {
         LabelTreeNode leaf = new LabelTreeNode(feed);
         tree.add(leaf);
      }
    }
    LabelTreeNode tree = new LabelTreeNode("(empty)");
    root.add(tree);
    ArrayList<String> emptyFeeds = readLabelFeed("empty");
    for(String feed : emptyFeeds) {
         LabelTreeNode leaf = new LabelTreeNode(feed);
         tree.add(leaf);
    }

    DefaultTreeModel model = new DefaultTreeModel(root);
    setModel(model);
    doSetCellRenderer();
  }

  private ArrayList<String> readLabelFeed(String filename) {
    FileReader fr = null;
    String filePath = FeedSourceList.SAVE_DIR + filename + ".label";
    try {
      ArrayList<String> feeds = new ArrayList<String>();
      fr = new FileReader(filePath);
      BufferedReader br = new BufferedReader(fr);
      String line;
      while ( (line = br.readLine()) != null) {
        feeds.add(line);
      }
      return feeds;
    } catch ( IOException ex ) {
      ex.printStackTrace();
    } finally {
      try {
        fr.close();
      } catch ( IOException ex ) {
        ex.printStackTrace();
      }
    }
    return null;
  }

  private void doSetCellRenderer() {
    LabelTreeCellRenderer renderer = new LabelTreeCellRenderer();
    setCellRenderer(renderer);
  }
}
