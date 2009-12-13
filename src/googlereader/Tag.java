package googlereader;

/**
 *
 * @author voidy21
 */
public class Tag {

  private String name;
  private int unreadCount = 0;

  public Tag(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public int getUnreadCount() {
    return unreadCount;
  }

  public void setUnreadCount(int unreadCount) {
    this.unreadCount = unreadCount;
  }
}
