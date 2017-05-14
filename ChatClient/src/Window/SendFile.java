package Window;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

import Login.LoginView;
import net.sf.json.JSONObject;

public class SendFile {
	int length=0;
	private FileInputStream fis;
	byte[] sendBytes = null;  
	double sumL = 0 ;  
	
	public SendFile(String path, String filename) throws IOException {
		File file = new File(path);
		long l = file.length();
		fis = new FileInputStream(file);
		sendBytes = new byte[1024];
		while ((length = fis.read(sendBytes, 0, sendBytes.length)) > 0) {
			sumL += length;
			System.out.println("ÒÑ´«Êä£º" + ((sumL / l) * 100) + "%");
			JSONObject json=new JSONObject();
			json.put("type", "file");
			json.put("filename", filename);
			LoginView.SendMessage(json);
			LoginView.SendFile(sendBytes, length);
		}
	}
}
