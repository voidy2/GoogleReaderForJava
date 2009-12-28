package framecontrol;

import googlereader.FeedItem;
import googlereader.FeedSource;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

/**
 *
 * @author voidy21
 */
@SuppressWarnings( "serial" )
public class ContentView extends JPanel {

  private ArrayList<ItemTitleLabel> titleBars;
  HtmlPane content;
  ItemTitleLabel titleLabel;
  JScrollPane sp;
  FeedSource fs;
  private int itemCount = 0;

  public ContentView() {
    setDoubleBuffered(true);
    content = new HtmlPane("teset");
    titleLabel = new ItemTitleLabel("test");
    this.setLayout(new BorderLayout());
    this.add(titleLabel, BorderLayout.PAGE_START);
    this.add(content, BorderLayout.CENTER);
  }

  public void setContents(FeedSource fs) {
    this.fs = fs;
    titleBars = new ArrayList<ItemTitleLabel>();
    List<FeedItem> fiList = fs.getItems();
    int length = fiList.size();
    // if(length > 10){
    //  length = 10;
    // }
    for ( int i = 0; i < length; i++ ) {
      FeedItem feedItem = fiList.get(i);
      String title = feedItem.getTitle();
      ItemTitleLabel newTitle = new ItemTitleLabel(title);
      titleBars.add(newTitle);
    }
    viewContents(0);
  }

  public void viewContents(int count) {
    itemCount = count;
    setCompornent(titleBars.get(itemCount), fs.getItems().get(itemCount).getSummary());
  }

  private void setCompornent(ItemTitleLabel newLabel, String str) {
    removeAll();
    titleLabel = newLabel.clone();
    content = new HtmlPane(str);

    this.add(titleLabel, BorderLayout.PAGE_START);
    sp = new JScrollPane();
    sp.getViewport().setView(content);
    this.add(sp, BorderLayout.CENTER);
    this.revalidate();
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
  }
}
