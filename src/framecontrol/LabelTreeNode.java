package framecontrol;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * faviconをそれぞれ保持しているTreeNode
 * @author voidy21
 */
@SuppressWarnings( "serial" )
public class LabelTreeNode extends DefaultMutableTreeNode {

  public final static String RSS_ICON_FILE = "./img/rss.png";
  public final static String LABEL_CLOSE_ICON_FILE = "./img/Folder.png";
  public final static String LABEL_OPEN_ICON_FILE = "./img/Folder_Open.png";
  private Icon LeafIcon;
  private Icon OpenIcon;
  private Icon ClosedIcon;
  private int iconsize = 18;

  public LabelTreeNode(Object obj) {
    super(obj);
    doSetDefaultIcons();
  }

  private void doSetDefaultIcons() {
    try {
      Image rss = ImageIO.read(new File(RSS_ICON_FILE));
      Image folder = ImageIO.read(new File(LABEL_CLOSE_ICON_FILE));
      Image folderOpen = ImageIO.read(new File(LABEL_OPEN_ICON_FILE));
      LeafIcon = new ImageIcon(rss.getScaledInstance(iconsize, iconsize, Image.SCALE_SMOOTH));
      OpenIcon = new ImageIcon(folderOpen.getScaledInstance(iconsize, iconsize, Image.SCALE_SMOOTH));
      ClosedIcon = new ImageIcon(folder.getScaledInstance(iconsize, iconsize, Image.SCALE_SMOOTH));
    } catch ( IOException ex ) {
      System.err.println(ex);
    }
  }

  public void setClosedIcon(Icon ClosedIcon) {
    this.ClosedIcon = ClosedIcon;
  }

  public void setLeafIcon(Icon LeafIcon) {
    this.LeafIcon = LeafIcon;
  }

  public void setOpenIcon(Icon OpenIcon) {
    this.OpenIcon = OpenIcon;
  }

  public void setIconsize(int iconsize) {
    this.iconsize = iconsize;
  }

  public Icon getLeafIcon() {
    return LeafIcon;
  }

  public Icon getOpenIcon() {
    return OpenIcon;
  }

  public Icon getClosedIcon() {
    return ClosedIcon;
  }
}
