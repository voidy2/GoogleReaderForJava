package framecontrol;

import googlereader.GoogleReaderAPI;
import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

/**
 *
 * @author voidy21
 */
@SuppressWarnings( "serial" )
public class MainFrame extends JFrame
        implements KeyListener, ListSelectionListener, TreeSelectionListener {

  private static final String mac = "com.sun.java.swing.plaf.mac.MacLookAndFeel";
  private static final String metal = "javax.swing.plaf.metal.MetalLookAndFeel";
  private static final String motif = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
  private static final String windows = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
  private static final String gtk = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
  private static final String nimbus = "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";
  JList lst;
  JTextArea ta;
  LabelTree tree;
  GoogleReaderAPI gapi;
  int select;
  String[] data = { "ListA", "ListB", "ListC", "ListD", "ListE", "ListF", "ListG", "ListH" };
  String[] text = { "ListA", "ListB", "テスト\nListC", "ListD", "ListE", "ListF", "ListG", "ListH" };

  public MainFrame(GoogleReaderAPI gapi) {
    this.gapi = gapi;
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

    tree = new LabelTree(gapi);
    tree.addTreeSelectionListener(this);
    JScrollPane sp3 = new JScrollPane();
    sp3.getViewport().setView(tree);

    JSplitPane splitpaneH = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
    JSplitPane splitpaneV = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
    splitpaneV.setTopComponent(sp);
    splitpaneV.setBottomComponent(sp2);
    splitpaneV.setContinuousLayout(true);
    splitpaneH.setLeftComponent(sp3);
    splitpaneH.setRightComponent(splitpaneV);
    splitpaneH.setContinuousLayout(true);
    add(splitpaneH);
    setBounds(100, 100, 1000,700);
    setVisible(true);
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

