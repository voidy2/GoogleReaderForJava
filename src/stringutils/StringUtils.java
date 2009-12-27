package stringutils;

/**
 *
 * @author voidy21
 */
public class StringUtils {

  /**
   * 配列で渡された文字列を指定した文字で連結する
   * @param arry 結合したい文字列
   * @param with 連結文字
   * @return 連結後の文字列を返す
   */
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
  /**
   * 文字列から改行文字などを除去する
   * @param str 与える文字列
   * @return 改行文字などが除去された文字列
   */
  public static String avoidWaste(String str) {
    str = str.replaceAll("\r", "\n");//復帰文字を消す
    str = str.replaceAll("\n", "");//改行文字を消す
    str = str.replaceAll("\t", "");//タブ文字を消す
    return str;
  }
}

