package Server;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;


/**
 * �����ļ�����
 * @author admin_Hzw
 *
 */
public class ReceiveFile {
	 
	public static void receiveFile(Socket socket, String filename) throws IOException {
		byte[] inputByte = null;
		int length = 0;
		DataInputStream dis = null;
		FileOutputStream fos = null;
		String filePath = "F:/temp/"+filename;
		try {
			try {
				dis = new DataInputStream(socket.getInputStream());
				File f = new File("F:/temp");
				if(!f.exists()){
					f.createNewFile();  
				}
				/*  
				 * �ļ��洢λ��  
				 */
				fos = new FileOutputStream(new File(filePath));    
				inputByte = new byte[1024];   
				System.out.println("��ʼ��������...");  
				while ((length = dis.read(inputByte, 0, inputByte.length)) > 0) {
					fos.write(inputByte, 0, length);
					fos.flush();    
				}
				System.out.println("��ɽ��գ�"+filePath);
			} finally {
				if (fos != null)
					fos.close();
				if (dis != null)
					dis.close();
				if (socket != null)
					socket.close(); 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
