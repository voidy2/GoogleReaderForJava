package framecontrol;

import googlereader.FeedSourceList;
import googlereader.GoogleReaderAPI;
import googlereader.Tag;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import networkaccess.ImageGet;

/**
 * JTreeを継承したラベルを表示するコンポーネント
 * @author voidy21
 */
@SuppressWarnings( "serial" )
public class LabelTree extends JTree {

  private GoogleReaderAPI gapi;
  private Image rss;
  private Image folder;
  private Image folderOpen;
  private Icon LeafIcon;
  private Icon OpenIcon;
  private Icon ClosedIcon;
  private int iconsize = 18;
  public final static String RSS_ICON_FILE = "./img/rss.png";
  public final static String LABEL_CLOSE_ICON_FILE = "./img/Folder.png";
  public final static String LABEL_OPEN_ICON_FILE = "./img/Folder_Open.png";

  public LabelTree(GoogleReaderAPI gapi) {
    try {
      this.gapi = gapi;
      rss = ImageIO.read(new File(RSS_ICON_FILE));
      folder = ImageIO.read(new File(LABEL_CLOSE_ICON_FILE));
      folderOpen = ImageIO.read(new File(LABEL_OPEN_ICON_FILE));
      LeafIcon = new ImageIcon(rss.getScaledInstance(iconsize, iconsize, Image.SCALE_SMOOTH));
      OpenIcon = new ImageIcon(folderOpen.getScaledInstance(iconsize, iconsize, Image.SCALE_SMOOTH));
      ClosedIcon = new ImageIcon(folder.getScaledInstance(iconsize, iconsize, Image.SCALE_SMOOTH));
      setDoubleBuffered(true);
      doMakeLabelTree();
    } catch ( IOException ex ) {
      Logger.getLogger(LabelTree.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private void doMakeLabelTree() {
    FeedSourceList fs = gapi.getFsList();
    Collection<Tag> labels = fs.getTagList().values();
    LabelTreeNode root = new LabelTreeNode("GoogleReader");
    root.setOpenIcon(OpenIcon);
    root.setClosedIcon(ClosedIcon);
    for ( Tag label : labels ) {
      if ( label.isState() ) {
        continue;
      }
      String labelName = label.getSmartName();
      System.err.println(labelName + " : 読み込み完了");
      LabelTreeNode tree = new LabelTreeNode(labelName);
      tree.setOpenIcon(OpenIcon);
      tree.setClosedIcon(ClosedIcon);
      ArrayList<String> feeds = fs.readLabelFeed(labelName);
      LabelTreeNode leaf = null;
      for ( int i = 0; i < feeds.size(); ++i ) {
        String feed = feeds.get(i);
        if ( i % 2 == 0 ) {
          leaf = new LabelTreeNode(feed);
        } else {
          leaf.setLeafIcon(ImageGet.readImage(feed));
          tree.add(leaf);
          System.out.println(ImageGet.doGetHashString(feed) + ".png");
        }
      }
      root.add(tree);
    }
    LabelTreeNode tree = new LabelTreeNode("(empty)");
    root.add(tree);
    ArrayList<String> emptyFeeds = fs.readLabelFeed("empty");
    LabelTreeNode leaf = null;
    for ( int i = 0; i < emptyFeeds.size(); ++i ) {
      String feed = emptyFeeds.get(i);
      if ( i % 2 == 0 ) {
        leaf = new LabelTreeNode(feed);
      } else {
        leaf.setLeafIcon(ImageGet.readImage(feed));
        tree.add(leaf);
        System.out.println(ImageGet.doGetHashString(feed) + ".png");
      }
    }

    DefaultTreeModel model = new DefaultTreeModel(root);
    setModel(model);
    doSetCellRenderer();
  }

  private void doSetCellRenderer() {
    LabelTreeCellRenderer renderer = new LabelTreeCellRenderer();
    setCellRenderer(renderer);
  }
}
