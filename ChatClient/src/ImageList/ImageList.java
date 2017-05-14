package ImageList;

import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;

public class ImageList extends JLabel implements ListCellRenderer {
	Icon[] icons;
	
	public ImageList(Icon[] icons) {
		// TODO Auto-generated constructor stub
		this.icons = icons;
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		String s = value.toString();
		setText(s);
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));// ������Ϊ5�Ŀհױ߿�
		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}
		if(index>4){
			index=(int)(Math.random()*4);
			setIcon(icons[index]);// ����ͼƬ
		}else{
			setIcon(icons[index]);// ����ͼƬ
		}
		setEnabled(list.isEnabled());
		setFont(list.getFont());
		setOpaque(true);
		return this;
	}
}
