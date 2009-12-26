package fileio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * ファイルの入出力関連
 * @author voidy21
 */
public class FileIO {
  String saveDir;

  public FileIO(String saveDir) {
    this.saveDir = saveDir;
  }
  
  public void doSave(
    String saveFilename,
    ArrayList<String> writeString) {
    FileWriter fw = null;
    try {
      String filename = saveDir + saveFilename;
      File directory = new File("./", saveDir);
      if ( !directory.exists() ) {
	directory.mkdirs();
      }
      fw = new FileWriter(filename);
      for ( String str : writeString ) {
	fw.write(str);
      }
    } catch ( IOException ex ) {
      ex.printStackTrace();
    } finally {
      try {
	fw.close();
      } catch ( IOException ex ) {
	ex.printStackTrace();
      }
    }
  }

  public ArrayList<String> doRead(
    String readFilename) {
    ArrayList<String> strs = new ArrayList<String>();
    FileReader fr = null;
    try {
      String filename = saveDir + readFilename;
      File directory = new File("./", saveDir);
      if ( !directory.exists() ) {
	throw new IOException("Error : ディレクトリが存在しない");
      }
      fr = new FileReader(filename);
      BufferedReader br = new BufferedReader(fr);
      String line;
      while ( (line = br.readLine()) != null ) {
	strs.add(line);
      }
    } catch ( IOException ex ) {
      ex.printStackTrace();
    }
    return strs;
  }
}
