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
    GoogleReaderAPI gapi = new GoogleReaderAPI(username, password);
    gapi.getFsList().readTags();
    gapi.getFsList().readSubscriptions();

    //System.out.println(gapi.getUnreadCount());
    //gapi.doGetLabelList();
    //gapi.doGetSubscriptionFeedList();
    gapi.getUnreadCount();
    //gapi.getFsList().saveAllLabel();
    //gapi.getFsList().saveTags();
    //gapi.getFsList().saveSubscriptions();

    //gapi.getFsList().saveFavicons();
    //購読リストと未読数を保存するべき
    FeedItemControl fic = new FeedItemControl(gapi);
    //fic.getFeedItems(new Tag(Const.ATOM_PREFIXE_LABEL + "情報"));


    new framecontrol.MainFrame(gapi);
  }
}