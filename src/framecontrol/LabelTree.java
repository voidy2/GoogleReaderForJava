package framecontrol;

import googlereader.FeedSource;
import googlereader.FeedSourceList;
import googlereader.GoogleReaderAPI;
import googlereader.Tag;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
    LabelTreeNode root = new LabelTreeNode(
      "GoogleReader(" + gapi.getFsList().getUnreadCount() + ")");
    root.setOpenIcon(OpenIcon);
    root.setClosedIcon(ClosedIcon);
    for ( Tag label : labels ) {
      if ( label.isState() ) {
	continue;
      }
      String labelName = label.getSmartName();
      int uc = label.getUnreadCount();
      if ( uc == 0 ) {
	continue;
      }
      System.err.println(labelName + " : 読み込み完了");
      LabelTreeNode tree = new LabelTreeNode(labelName + "(" + uc + ")");
      tree.setOpenIcon(OpenIcon);
      tree.setClosedIcon(ClosedIcon);
      List<FeedSource> feeds = fs.getFsList(new Tag(label.getName()));
      LabelTreeNode leaf = null;
      for ( FeedSource feed : feeds ) {
	String title = feed.getTitle();
	int ucf = feed.getUnreadCount();
	if ( ucf == 0 ) {
	  continue;
	}
	leaf = new LabelTreeNode("(" + ucf + ")" + title);
	leaf.setFeedSource(feed);
	leaf.setLeafIcon(ImageGet.readImage(feed.getHtmlUrl()));
	tree.add(leaf);
      }
      root.add(tree);
    }
    LabelTreeNode tree = new LabelTreeNode("(empty)");
    root.add(tree);
    List<FeedSource> emptyFeeds = fs.getFsList(new Tag("empty"));
    LabelTreeNode leaf = null;
    for ( FeedSource feed : emptyFeeds ) {
      String title = feed.getTitle();
      int ucf = feed.getUnreadCount();
      if ( ucf == 0 ) {
	continue;
      }
      leaf = new LabelTreeNode("(" + ucf + ")" + title);
      leaf.setFeedSource(feed);
      leaf.setLeafIcon(ImageGet.readImage(feed.getHtmlUrl()));
      tree.add(leaf);
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
