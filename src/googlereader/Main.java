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
    //new GoogleReaderAPI(username,password);
    new framecontrol.MainFrame();
  }
}
