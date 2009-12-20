package framecontrol;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author voidy21
 */
public class MainFrame extends JFrame
        implements KeyListener, ListSelectionListener {

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
    String currentLookAndFeel = gtk;
    try {
      UIManager.setLookAndFeel(currentLookAndFeel);
      SwingUtilities.updateComponentTreeUI(this);
    } catch ( Exception ex ) {
      ex.printStackTrace();
      System.out.println("Failed loading L&F: " + currentLookAndFeel);
    }

    getContentPane().setLayout(new FlowLayout());
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setTitle("KeyListenerTest");
    JScrollPane sp = new JScrollPane();

    lst = new JList(data);
    sp.getViewport().setView(lst);
    sp.setPreferredSize(new Dimension(300, 400));
    
    this.addKeyListener(this);
    lst.addListSelectionListener(this);
    lst.addKeyListener(this);
    ta = new JTextArea();
    ta.addKeyListener(this);
    JScrollPane sp2 = new JScrollPane();
    sp2.getViewport().setView(ta);
    sp2.setPreferredSize(new Dimension(400, 400));
    ta.setColumns(40);
    ta.setRows(20);
    
    JScrollPane sp3 = new JScrollPane();
    tree = new JTree();
    sp3.getViewport().setView(tree);
    sp3.setPreferredSize(new Dimension(200,400));

    add(sp3);   
    add(sp);
    add(sp2);
    setSize(940, 440);
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
}

