package framecontrol;

import googlereader.FeedItem;
import googlereader.FeedItemControl;
import googlereader.FeedSource;
import googlereader.GoogleReaderAPI;
import googlereader.Tag;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.*;
import java.security.KeyStore;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
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
  LabelTree tree;
  int treeSelect = 0;
  JScrollPane sp;
  JScrollPane sp3;
  GoogleReaderAPI gapi;
  int select;
  Vector<FeedItem> feedItems;
  FeedItemControl fItemCon;
  ContentView cv;
  JSplitPane splitpaneH;
  JSplitPane splitpaneV;

  public MainFrame(GoogleReaderAPI gapi) {
    this.gapi = gapi;
    this.fItemCon = new FeedItemControl(gapi);
    Collection<Tag> tags = gapi.getFsList().getTagList().values();
    for ( Tag tag : tags ) {
      if ( !tag.isState() ) {
	fItemCon.readFeedItems(tag);
      }
    }
    //saveAllとかreadAllとか作りたい
    fItemCon.readFeedItems(new Tag("empty"));
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
    sp = new JScrollPane();
    lst = new JList();
    sp.getViewport().setView(lst);

    this.addKeyListener(this);
    lst.addListSelectionListener(this);
    lst.addKeyListener(this);

    tree = new LabelTree(gapi);
    tree.addTreeSelectionListener(this);
    tree.addKeyListener(this);

    InputMap imc = tree.getInputMap();
    KeyStroke upKey = KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0);
    KeyStroke downKey = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0);
    KeyStroke rightKey = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0);
    imc.put(KeyStroke.getKeyStroke('P'),  imc.get(upKey));
    imc.put(KeyStroke.getKeyStroke('N'), imc.get(downKey));
    imc.put(KeyStroke.getKeyStroke(KeyEvent.VK_O,InputEvent.CTRL_DOWN_MASK), imc.get(rightKey));
       System.out.println(Arrays.toString(tree.getRegisteredKeyStrokes()));
    sp3 = new JScrollPane();
    sp3.getViewport().setView(tree);

    splitpaneH = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
    splitpaneV = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
    splitpaneV.setTopComponent(sp);
    cv = new ContentView();
    splitpaneV.setBottomComponent(cv);
    splitpaneV.setContinuousLayout(true);
    splitpaneH.setLeftComponent(sp3);
    splitpaneH.setRightComponent(splitpaneV);
    splitpaneH.setContinuousLayout(true);
    add(splitpaneH);
    setBounds(100, 100, 1000, 700);
    setVisible(true);
  }

  public void keyPressed(KeyEvent e) {
    //System.out.println("Press: " + KeyEvent.getKeyText(e.getKeyCode()));
  }

  public void keyReleased(KeyEvent e) {
    // System.out.println("Release: " + KeyEvent.getKeyText(e.getKeyCode()));
  }

  public synchronized void keyTyped(KeyEvent e) {
    //System.out.println("Type: " + e.getKeyChar());
    char getKey = e.getKeyChar();
    int current = lst.getSelectedIndex();
    switch ( getKey ) {
      case 'j':
	if ( feedItems == null ) {
	  return;
	}
	if ( current != feedItems.size() - 1 ) {
	  select = ++current;
	}
	lst.setSelectedValue(feedItems.get(select), true);
	break;
      case 'k':
	if ( feedItems == null ) {
	  return;
	}
	if ( current > 0 ) {
	  select = --current;
	}
	lst.setSelectedValue(feedItems.get(select), true);
	break;
      case 'N':
	treeSelect = tree.getSelectionRows()[0];
	tree.setSelectionRow(treeSelect);
	break;
      case 'P':
	treeSelect = tree.getSelectionRows()[0];
	tree.setSelectionRow(treeSelect);
	break;
    }
  }

  public void valueChanged(ListSelectionEvent e) {
    JList l = ( JList ) e.getSource();
    FeedItem item = ( FeedItem ) l.getSelectedValue();
    if ( item != null ) {
      select = l.getSelectedIndex();
      cv.viewContents(l.getSelectedIndex());
    }
  }

  public synchronized void valueChanged(TreeSelectionEvent e) {
    LabelTree tree1 = ( LabelTree ) e.getSource();
    LabelTreeNode node = ( LabelTreeNode ) tree1.getLastSelectedPathComponent();
    if ( node.isLeaf() ) {
      if ( node != null ) {
	showItems(node.getFeedSource());
	sp.getVerticalScrollBar().setValue(0);
	cv.setContents(node.getFeedSource());
	select = 0;
	lst.setSelectedValue(feedItems.get(select), true);
      }
    }
  }

  private void showItems(FeedSource fs) {
    List<FeedItem> fItems = fs.getItems();
    feedItems = new Vector<FeedItem>();
    for ( FeedItem item : fItems ) {
      feedItems.add(item);
    }
    lst.setListData(feedItems);
  }
}

