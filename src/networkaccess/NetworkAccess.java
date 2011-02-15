/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package networkaccess;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author voidy21
 */
public class NetworkAccess {

  String url;
  String headerKey, headerValue;
  String reqestMethod;
  String param;
  HttpURLConnection con;

  public NetworkAccess(String url, String reqestMethod, String param) {
    this.url = url;
    try {
        this.con = ( HttpURLConnection ) new URL(url).openConnection();
    } catch (Exception e) {

    }
    this.reqestMethod = reqestMethod;
    this.param = param;
  }

  public NetworkAccess(String url, String reqestMethod, String param,
          String headerKey, String headerValue) {
    this.url = url;
    try {
        this.con = ( HttpURLConnection ) new URL(url).openConnection();
    } catch (Exception e) {

    }
    this.headerKey = headerKey;
    this.headerValue = headerValue;
    this.reqestMethod = reqestMethod;
    this.param = param;
  }

  public NetworkAccess setRequestProperty(String key, String value) {
    this.con.setRequestProperty(key, value);
    return this;
  }

  public InputStream access() {
    try {
      if ( headerKey != null ) {
        con.setRequestProperty(headerKey, headerValue);
      }
      con.setDoOutput(true);
      con.setRequestMethod(reqestMethod);
      if ( param != null ) {
        PrintWriter pw = new PrintWriter(con.getOutputStream());
        pw.print(param);
        pw.close();
      }
      return con.getInputStream();
    } catch ( MalformedURLException ex ) {
      System.out.println("URLが無効\n" + ex);
    } catch ( IOException ex ) {
      System.out.println("URLConnectionに失敗\n" + ex);
    }
    return null;
  }

}
