package googlereader;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import stringutils.StringUtils;

/**
 *
 * @author voidy21
 */
public class AtomPrameters {

  public static final String START_TIME = "ot";
  public static final String ORDER = "r";
  public static final String EXCLUDE_TARGET = "xt";
  public static final String COUNT = "n";
  public static final String CONTINUATION = "c";
  public static final String CLIENT = "client";
  public static final String TIMESTAMP = "ck";
  private int count = 20;
  private String client = "";
  private String order = "d";  // 'd' or 'o'
  private long start_time = -1; // Only works for order r=o mode
  private long timestamp = -1;
  private Tag exclude_target = null;
  private String continuation = null;

  public AtomPrameters() {
  }

  public String getParameters() {
    String[] prams = new String[7];
    if ( count != 20 ) {
      prams[0] = (COUNT + "=" + count);
    }
    if ( !client.equals("") ) {
      prams[1] = (CLIENT + "=" + client);
    }
    if ( !order.equals("d") ) {
      prams[2] = (ORDER + "=" + order);
    }
    if ( start_time != -1 ) {
      prams[3] = (START_TIME + "=" + start_time);
    }
    if ( timestamp != -1 ) {
      prams[4] = (TIMESTAMP + "=" + timestamp);
    }
    if ( exclude_target != null ) {
      try {
        prams[5] = (EXCLUDE_TARGET + "=" +
                URLEncoder.encode(exclude_target.getName(), "UTF-8"));
      } catch ( UnsupportedEncodingException ex ) {
        System.out.println(ex);
      }
    }
    if ( continuation != null ) {
      prams[6] = (CONTINUATION + "=" + continuation);
    }
    return "?" + StringUtils.join(prams, "&");
  }

  public void setClient(String client) {
    this.client = client;
  }

  public void setContinuation(String continuation) {
    this.continuation = continuation;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public void setExclude_target(Tag exclude_target) {
    this.exclude_target = exclude_target;
  }

  public void setOrder(String order) {
    this.order = order;
  }

  public void setStart_time(long start_time) {
    this.start_time = start_time;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }
}
