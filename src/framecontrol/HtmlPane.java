package framecontrol;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JEditorPane;
import javax.swing.JTextPane;

/**
 * HTMlを表示するパネル
 * @author voidy21
 */
@SuppressWarnings( "serial" )
public class HtmlPane extends JTextPane {

  String type = "text/html";
  public HtmlPane(){
    super();
    setContentType(type);
  }
  public HtmlPane(String text) {
    super();
    setContentType(type);
    setText(text);
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
  }
}
