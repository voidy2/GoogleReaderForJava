package stringutils;

/**
 *
 * @author voidy21
 */
public class StringUtils {

  public static String join(String[] arry, String with) {
    StringBuilder buf = new StringBuilder();
    for ( String s : arry ) {
      if ( s != null ) {
        if ( buf.length() > 0 ) {
          buf.append(with);
        }
        buf.append(s);
      }
    }
    return buf.toString();
  }
}

