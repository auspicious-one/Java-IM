package Server;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;


/**
 * 接收文件服务
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
				 * 文件存储位置  
				 */
				fos = new FileOutputStream(new File(filePath));    
				inputByte = new byte[1024];   
				System.out.println("开始接收数据...");  
				while ((length = dis.read(inputByte, 0, inputByte.length)) > 0) {
					fos.write(inputByte, 0, length);
					fos.flush();    
				}
				System.out.println("完成接收："+filePath);
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
