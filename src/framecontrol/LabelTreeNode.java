package framecontrol;

import java.util.Enumeration;
import javax.swing.tree.TreeNode;

/**
 * faviconをそれぞれ保持しているTreeNode
 * @author voidy21
 */
public class LabelTreeNode implements TreeNode {

  @Override
  public TreeNode getChildAt(int childIndex) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public int getChildCount() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public TreeNode getParent() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public int getIndex(TreeNode node) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean getAllowsChildren() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean isLeaf() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Enumeration children() {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
