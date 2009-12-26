package googlereader;

import java.util.ArrayList;
import java.util.List;

/**
 * Feedの1つ1つの記事を扱うクラス
 * @author voidy21
 */
public class FeedItem {
  private String title;
  private List<Tag> tags = new ArrayList<Tag>();
  private long timestamp;
  private String link;
  private FeedSource source;
  private String summary;

  public FeedItem(FeedSource source) {
    this.source = source;
  }

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public FeedSource getSource() {
    return source;
  }

  public void setSource(FeedSource source) {
    this.source = source;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public List<Tag> getTags() {
    return tags;
  }

  public void setTags(List<Tag> tags) {
    this.tags = tags;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
}
