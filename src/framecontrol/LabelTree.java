package framecontrol;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
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
  private int iconsize = 18;

  public LabelTree() {
    setDoubleBuffered(true);
    doSetIconSize(iconsize);
    DefaultMutableTreeNode root = new DefaultMutableTreeNode("チンパンジー");
    DefaultMutableTreeNode swing = new DefaultMutableTreeNode("あなたとは");
    DefaultMutableTreeNode java2d = new DefaultMutableTreeNode("違うんです");
    DefaultMutableTreeNode java3d = new DefaultMutableTreeNode("客観的な");
    DefaultMutableTreeNode javamail = new DefaultMutableTreeNode("視点で");
    DefaultMutableTreeNode swingSub1 = new DefaultMutableTreeNode("物事を");
    DefaultMutableTreeNode swingSub2 = new DefaultMutableTreeNode("見ることが");
    DefaultMutableTreeNode swingSub3 = new DefaultMutableTreeNode("できるんです");
    swing.add(swingSub1);
    swing.add(swingSub2);
    swing.add(swingSub3);
    root.add(swing);
    root.add(java2d);
    root.add(java3d);
    root.add(javamail);
    DefaultTreeModel model = new DefaultTreeModel(root);
    this.setModel(model);
  }

  /**
   * アイコンサイズを設定して画像をセットする
   * @param iconsize 設定するアイコンサイズ
   */
  public void doSetIconSize(int iconsize) {
    try {
      DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
      Image rss = ImageIO.read(new File(RSS_ICON_FILE));
      Image folder = ImageIO.read(new File(LABEL_CLOSE_ICON_FILE));
      Image folderOpen = ImageIO.read(new File(LABEL_OPEN_ICON_FILE));
      ImageIcon folderIcon = new ImageIcon(folder.getScaledInstance(iconsize, iconsize, Image.SCALE_SMOOTH));
      ImageIcon folderOpenIcon = new ImageIcon(folderOpen.getScaledInstance(iconsize, iconsize, Image.SCALE_SMOOTH));
      ImageIcon rssIcon = new ImageIcon(rss.getScaledInstance(iconsize, iconsize, Image.SCALE_SMOOTH));
      renderer.setLeafIcon(rssIcon);
      renderer.setClosedIcon(folderIcon);
      renderer.setOpenIcon(folderOpenIcon);
      this.setCellRenderer(renderer);
    } catch ( IOException ex ) {
      System.err.println(ex);
    }
  }
}
