package framecontrol;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;

/**
 * JTreeを継承したラベルを表示するコンポーネント
 * @author voidy21
 */
@SuppressWarnings( "serial" )
public class LabelTree extends JTree {

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
    javamail.setLeafIcon("./img/anpanman.jpg");
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
