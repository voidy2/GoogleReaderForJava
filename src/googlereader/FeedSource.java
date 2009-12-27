package googlereader;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author voidy21
 */
public class FeedSource {

  private int unreadCount;
  private String url;
  private String title;
  private String htmlUrl;
  private String sortId;
  private String link;
  private List<Tag> tags = new ArrayList<Tag>();
  private List<FeedItem> items = new ArrayList<FeedItem>();

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

  public String getHtmlUrl() {
    return htmlUrl;
  }

  public void setHtmlUrl(String htmlUrl) {
    this.htmlUrl = htmlUrl;
  }

  public String getSortId() {
    return sortId;
  }

  public void setSortId(String sortId) {
    this.sortId = sortId;
  }

  public List<FeedItem> getItems() {
    return items;
  }

  public void setItems(List<FeedItem> items) {
    this.items = items;
  }
  

  public String getUrl() {
    return url;
  }

  /**
   * feed/ から始まるURLエンコードされたURLを返す
   * @return URLエンコードされたURL
   */
  public String getEncodedUrl() {
    try {
      return URLEncoder.encode(url, "UTF-8");
    } catch ( UnsupportedEncodingException ex ) {
      ex.printStackTrace();
    }
    return null;
  }

  public String getStateUrl() {
    try {
      return URLEncoder.encode(url, "UTF-8").replaceFirst("feed%2F", "feed/");
    } catch ( UnsupportedEncodingException ex ) {
      ex.printStackTrace();
    }
    return null;
  }

  public void setUrl(String url) {
    this.url = url;
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
      String name = tag1.getSmartName();
      if ( name != null ) {
        if ( name.equals(tag.getSmartName()) ) {
          return true;
        }
      }
    }
    return false;
  }
}
