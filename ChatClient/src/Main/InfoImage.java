package Main;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class InfoImage {
	public static Icon InfoImage(){
			String img[]={"pinfo1","pinfo2","pinfo3","pinfo4","pinfo5"};
			int i=(int)(Math.random()*4)+1;
			Icon welcome=new ImageIcon("person_info/"+img[i]+".jpg");
			return welcome;
	}
}
