package googlereader;

import java.util.List;
import org.w3c.dom.Node;

/**
 *
 * @author voidy21
 */
public class Feed {

  String title;
  String author;
  List<String> tags;
  String published;
  String updated;
  String link;
  String source;
  String summary;

  Feed(Node xmlFeed) {
  }

  public String getAuthor() {
    return author;
  }

  public String getLink() {
    return link;
  }

  public String getPublished() {
    return published;
  }

  public String getSource() {
    return source;
  }

  public String getSummary() {
    return summary;
  }

  public List<String> getTags() {
    return tags;
  }

  public String getTitle() {
    return title;
  }

  public String getUpdated() {
    return updated;
  }
}
