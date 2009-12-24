package networkaccess;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 画像をURLから取得する
 * @author voidy21
 */
public class ImageGet {

  public static final String FAVICON_API = "http://favicon.hatena.ne.jp/?url=";
  public static final String SAVE_DIR = "./img/favicon/";

  /**
   * faviconを指定したURLに指定したファイル名で保存する
   * @param urlString 指定するURL
   * @param saveName 指定するファイル名
   */
  public static void saveFavicon(String urlString, String saveName) {
    String faviconUrl = FAVICON_API + urlString;
    InputStream in = null;
    OutputStream out = null;
    try {
      URL url = new URL(faviconUrl);
      in = url.openStream();
      out = new FileOutputStream(saveName);
      byte[] buf = new byte[1024];
      int len = 0;
      while ( (len = in.read(buf)) > 0 ) {
        out.write(buf, 0, len);
      }
      in.close();
      out.close();
    } catch ( MalformedURLException ex ) {
      ex.printStackTrace();
    } catch ( IOException ex ) {
      ex.printStackTrace();
    }
  }

  /**
   * faviconを指定したURLから保存する。
   * ハッシュ文字列化されたurlString.pngでSAVE_DIR以下に保存される
   * SAVE_DIRが存在しない場合は作成する
   * @param urlString 指定するURL
   */
  public static void saveFavicon(String urlString) {
    File directory1 = new File("./", SAVE_DIR);
    if ( !directory1.exists() ) {
      directory1.mkdirs();
    }
    String hash = doGetHashString(urlString);
    String saveName = SAVE_DIR + hash + ".png";
    saveFavicon(urlString, saveName);
  }

  /**
   * 与えられた文字列をハッシュ文字列に変換する
   * @param str 指定する文字列
   * @return ハッシュ文字列
   */
  public static String doGetHashString(String str) {
    return hexString(doGetHash(str));
  }

  /**
   * 与えられた文字列をハッシュバイト列に変換する
   * @param str 指定する文字列
   * @return ハッシュバイト列
   */
  public static byte[] doGetHash(String str) {
    byte[] hash = null;
    try {
      MessageDigest md5 = MessageDigest.getInstance("MD5");
      md5.update(str.getBytes());
      hash = md5.digest();
    } catch ( NoSuchAlgorithmException ex ) {
      ex.printStackTrace();
    }
    return hash;
  }

  /**
   * 与えられたハッシュバイト列をハッシュ文字列に変換する
   * @param bin 指定するハッシュ文字列
   * @return ハッシュ文字列
   */
  public static String hexString(byte[] bin) {
    String s = "";
    int size = bin.length;
    for ( int i = 0; i < size; i++ ) {
      int n = bin[i];
      if ( n < 0 ) {
        n += 256;
      }
      String hex = Integer.toHexString(n);
      if ( hex.length() == 1 ) {
        hex = "0" + hex;
      }
      s += hex;
    }
    return s;
  }
}
