package framecontrol;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JLabel;

/**
 *
 * @author voidy21
 */
@SuppressWarnings( "serial" )
public class ItemTitleLabel extends JLabel implements Cloneable {

  public ItemTitleLabel(String title) {
    super(title);
    this.setOpaque(true);
    this.setBackground(Color.BLACK);
    this.setForeground(Color.WHITE);
    this.setHorizontalAlignment(JLabel.LEFT);
    this.setSize(this.getWidth(), 20);
  }

  @Override
  protected ItemTitleLabel clone() {
    try {
      return ( ItemTitleLabel ) super.clone();
    } catch ( CloneNotSupportedException ex ) {
      System.out.println(ex);
    }
    return null;
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
  }
}
