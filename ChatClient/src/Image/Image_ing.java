package Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageProducer;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JPanel;

public class Image_ing extends JFrame{
	private Image img;
	public void paint(Graphics g){
		g.drawImage(img, 40, 40, this);
	}
	public static void main(String[] args) {
		try {
			URL url=new URL("http://www.baidu.com/img/bd_logo1.png");
			Image_ing urlt=new Image_ing();
			urlt.img=urlt.createImage((ImageProducer)url.getContent());
			urlt.setSize(600,600);
			urlt.setVisible(true);
		} catch (MalformedURLException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
	}

}
