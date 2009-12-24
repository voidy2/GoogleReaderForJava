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

  private Icon LeafIcon;
  private Icon OpenIcon;
  private Icon ClosedIcon;
  private int iconsize = 18;

  public LabelTreeNode(Object obj) {
    super(obj);
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

  public void setClosedIcon(String filename) {
    setClosedIcon(doGetImageIcon(filename));
  }

  public void setLeafIcon(String filename) {
    setLeafIcon(doGetImageIcon(filename));
  }

  public void setOpenIcon(String filename) {
    setOpenIcon(doGetImageIcon(filename));
  }

  private Icon doGetImageIcon(String filename) {
    ImageIcon icon = null;
    try {
      Image image = ImageIO.read(new File(filename));
      icon = new ImageIcon(image.getScaledInstance(iconsize, iconsize, Image.SCALE_SMOOTH));
    } catch ( IOException ex ) {
      ex.printStackTrace();
    }
    return icon;
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
