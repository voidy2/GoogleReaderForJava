/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package framecontrol;

import java.awt.Component;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *
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

    return super.getTreeCellRendererComponent(
            tree, value,
            sel, expanded, leaf,
            row,
            hasFocus);
  }
}
