package framecontrol;

import googlereader.FeedItem;
import googlereader.FeedSource;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

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
    // if(length > 10){
    //  length = 10;
    // }
    for ( Iterator<FeedItem> it = fiList.iterator(); it.hasNext(); ) {
      FeedItem feedItem = it.next();
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
