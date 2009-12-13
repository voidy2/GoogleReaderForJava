package googlereader;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author voidy21
 */
public class FeedSource {

  private int unreadCount;
  private String title;
  private String link;
  private List<Tag> tags = new ArrayList<Tag>();

  public FeedSource(String title, String link) {
    this.title = title;
    this.link = link;
  }

  public FeedSource() {
  }

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public int getUnreadCount() {
    return unreadCount;
  }

  public void setUnreadCount(int unreadCount) {
    this.unreadCount = unreadCount;
  }

  public List<Tag> getTags() {
    return tags;
  }

  public void appendTag(String tag) {
    this.tags.add(new Tag(tag));
  }

  public boolean isExistTag(Tag tag) {
    for ( Tag tag1 : tags ) {
      if ( tag1.getName().equals(tag.getName()) ) {
        return true;
      }
    }
    return false;
  }
}
