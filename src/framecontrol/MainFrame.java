package framecontrol;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

/**
 *
 * @author voidy21
 */
public class MainFrame extends JFrame
        implements KeyListener, ListSelectionListener, TreeSelectionListener {

  private static final long serialVersionUID = 1L;
  private static final String mac = "com.sun.java.swing.plaf.mac.MacLookAndFeel";
  private static final String metal = "javax.swing.plaf.metal.MetalLookAndFeel";
  private static final String motif = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
  private static final String windows = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
  private static final String gtk = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
  private static final String nimbus = "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";
  JList lst;
  JTextArea ta;
  JTree tree;
  int select;
  String[] data = { "ListA", "ListB", "ListC", "ListD", "ListE", "ListF", "ListG", "ListH" };
  String[] text = { "ListA", "ListB", "テスト\nListC", "ListD", "ListE", "ListF", "ListG", "ListH" };

  public MainFrame() {
    try {
      String currentLookAndFeel = gtk;
      try {
        UIManager.setLookAndFeel(currentLookAndFeel);
        SwingUtilities.updateComponentTreeUI(this);
      } catch ( Exception ex ) {
        ex.printStackTrace();
        System.out.println("Failed loading L&F: " + currentLookAndFeel);
      }
      setLayout(new GridLayout());
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setTitle("KeyListenerTest");
      JScrollPane sp = new JScrollPane();
      lst = new JList(data);
      sp.getViewport().setView(lst);

      this.addKeyListener(this);
      lst.addListSelectionListener(this);
      lst.addKeyListener(this);
      ta = new JTextArea();
      ta.addKeyListener(this);
      JScrollPane sp2 = new JScrollPane();
      sp2.getViewport().setView(ta);

      ta.setColumns(40);
      ta.setRows(20);
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
      tree = new JTree(root);
      tree.addTreeSelectionListener(this);
      DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
      Image rss = ImageIO.read(new File("./img/rss.png"));
      Image folder = ImageIO.read(new File("./img/Folder.png"));
      Image folderOpen = ImageIO.read(new File("./img/Folder_Open.png"));
      ImageIcon folderIcon = new ImageIcon(folder.getScaledInstance(18, 18, Image.SCALE_SMOOTH));
      ImageIcon folderOpenIcon = new ImageIcon(folderOpen.getScaledInstance(18, 18, Image.SCALE_SMOOTH));
      ImageIcon rssIcon = new ImageIcon(rss.getScaledInstance(18, 18, Image.SCALE_SMOOTH));
      renderer.setLeafIcon(rssIcon);
      renderer.setClosedIcon(folderIcon);
      renderer.setOpenIcon(folderOpenIcon);

      tree.setCellRenderer(renderer);
      JScrollPane sp3 = new JScrollPane();
      sp3.getViewport().setView(tree);

      JSplitPane splitpaneH = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,true);
      JSplitPane splitpaneV = new JSplitPane(JSplitPane.VERTICAL_SPLIT,true);
      splitpaneV.setTopComponent(sp);
      splitpaneV.setBottomComponent(sp2);
      splitpaneH.setLeftComponent(sp3);
      splitpaneH.setRightComponent(splitpaneV);
      add(splitpaneH);
      setSize(1000, 700);
      setVisible(true);
    } catch ( IOException ex ) {
      Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
    }

  }

  public void keyPressed(KeyEvent e) {
    //System.out.println("Press: " + KeyEvent.getKeyText(e.getKeyCode()));
  }

  public void keyReleased(KeyEvent e) {
    // System.out.println("Release: " + KeyEvent.getKeyText(e.getKeyCode()));
  }

  public void keyTyped(KeyEvent e) {
    // System.out.println("Type: " + e.getKeyChar());
    char getKey = e.getKeyChar();
    int current = lst.getSelectedIndex();
    if ( getKey == 'N' ) {
      if ( current != data.length - 1 ) {
        select = ++current;
      } else {
        select = 0;
      }
      lst.setSelectedValue(data[select], true);
    } else if ( getKey == 'P' ) {
      if ( current > 0 ) {
        select = --current;
      } else {
        select = data.length - 1;
      }
      lst.setSelectedValue(data[select], true);
    }
  }

  public void valueChanged(ListSelectionEvent e) {
    JList l = ( JList ) e.getSource();
    select = l.getSelectedIndex();
    ta.setText(text[select]);
  }

  public void valueChanged(TreeSelectionEvent e) {
    TreePath[] paths = e.getPaths();
    if ( e.isAddedPath() ) {
      ta.setText(paths[0].toString());
    }
  }
}

