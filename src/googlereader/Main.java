/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package googlereader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author voidy21
 */
public class Main {

  /**
   * @param args the command line arguments
   * @throws FileNotFoundException
   * @throws IOException
   */
  public static void main(String[] args) 
          throws FileNotFoundException, IOException {
    FileReader fr = new FileReader(new File("userdata.txt"));
    BufferedReader br = new BufferedReader(fr);
    String username = br.readLine();
    String password = br.readLine();
    GoogleReaderAPI gapi = new GoogleReaderAPI(username,password);
    gapi.getUnreadCount();
    gapi.getFsList().save("user/02449446468507871574/label/情報");
    new framecontrol.MainFrame();
  }
}
