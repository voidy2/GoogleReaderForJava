package framecontrol;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * ラベルごとにアイコンを変更するためのDefaultTreeCellRendererを継承するレンダラ
 * @author voidy21
 */
@SuppressWarnings( "serial" )
public class LabelTreeCellRenderer extends DefaultTreeCellRenderer {

  @Override
  public Component getTreeCellRendererComponent(
          JTree tree, Object value,
          boolean sel, boolean expanded, boolean leaf,
          int row, boolean hasFocus) {
    LabelTreeNode node = ( LabelTreeNode ) value;
    setLeafIcon(node.getLeafIcon());
    setOpenIcon(node.getOpenIcon());
    setClosedIcon(node.getClosedIcon());
    setBackgroundSelectionColor(Color.YELLOW);
    setTextSelectionColor(Color.BLUE);
    setFont(new Font("メイリオ", Font.PLAIN, 16));

    return super.getTreeCellRendererComponent(
            tree, value,
            sel, expanded, leaf,
            row,
            hasFocus);
  }
}
