package framecontrol;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;

/**
 * JTreeを継承したラベルを表示するコンポーネント
 * @author voidy21
 */
@SuppressWarnings( "serial" )
public class LabelTree extends JTree {

  public final static String RSS_ICON_FILE = "./img/rss.png";
  public final static String LABEL_CLOSE_ICON_FILE = "./img/Folder.png";
  public final static String LABEL_OPEN_ICON_FILE = "./img/Folder_Open.png";


  public LabelTree() {
    setDoubleBuffered(true);
    LabelTreeNode root = new LabelTreeNode("チンパンジー");
    LabelTreeNode swing = new LabelTreeNode("あなたとは");
    LabelTreeNode java2d = new LabelTreeNode("違うんです");
    LabelTreeNode java3d = new LabelTreeNode("客観的な");
    LabelTreeNode javamail = new LabelTreeNode("視点で");
    LabelTreeNode swingSub1 = new LabelTreeNode("物事を");
    LabelTreeNode swingSub2 = new LabelTreeNode("見ることが");
    LabelTreeNode swingSub3 = new LabelTreeNode("できるんです");
    java.awt.Image folderOpen;
    try {
      folderOpen = ImageIO.read(new File("./img/anpanman.jpg"));
      javax.swing.Icon OpenIcon = new ImageIcon(folderOpen.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
      javamail.setLeafIcon(OpenIcon);
    } catch ( IOException ex ) {
      Logger.getLogger(LabelTree.class.getName()).log(Level.SEVERE, null, ex);
    }
   
    swing.add(swingSub1);
    swing.add(swingSub2);
    swing.add(swingSub3);
    root.add(swing);
    root.add(java2d);
    root.add(java3d);
    root.add(javamail);
    DefaultTreeModel model = new DefaultTreeModel(root);

    this.setModel(model);
    doSetCellRenderer();
  }

  private void doSetCellRenderer() {
    LabelTreeCellRenderer renderer = new LabelTreeCellRenderer();
    setCellRenderer(renderer);
  }
}
