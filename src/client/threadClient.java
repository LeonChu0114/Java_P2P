package client;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.PrivateKey;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import model.User;
import protocal.Datagrame;
import util.UserDao;

public class threadClient extends Thread{
	
	private Socket s;
	UserDao us = new UserDao();
	private User clientUser = new User();
	private MainFrame mf = null;
	
	public threadClient(MainFrame mf, User user,Socket s){
		this.s = s;
		clientUser = user;
		this.mf = mf;
	}
	
	public void run(){
		InputStream is = null;
		OutputStream os = null;
		try {
			//构建SocketIO
			is = s.getInputStream();
			os = s.getOutputStream();
			
			//读入数据报对象
			ObjectInputStream ois = new ObjectInputStream(is);
			Datagrame dg = null;
			try {
				dg = (Datagrame)ois.readObject();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			int identifier = dg.getIdentifier();
			
			switch(identifier){
			case 4:
				//标识符为5时，为服务器发来的用户上线广播
				System.out.println("A user logged in");
				try {
					this.receiveChat(dg);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 5:
				//标识符为5时，查询用户文件列表
				System.out.println("The client needs to get the file list");
				try {
					this.respFileList(dg);
				} catch(Exception e) {
					e.printStackTrace();
				}
				break;
			case 6:
				//标识符为6时，下载文件
				System.out.println("The client needs to download");
				try {
					this.respDownload(dg);
				} catch(Exception e) {
					e.printStackTrace();
				}
				break;
			}		
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void respDownload(Datagrame dg) throws Exception {
		String filename = dg.getFilename();
		File f = new File(System.getProperty("user.dir")+"\\"+clientUser.getUsername()+"\\"+filename);
		ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
		oos.writeObject(f);
		s.shutdownOutput();
	}
	
	public void receiveChat(Datagrame dg){
		JOptionPane.showMessageDialog(mf,dg.getMessage());
	}
	
	public void respFileList(Datagrame dg) throws Exception{
		String path = System.getProperty("user.dir")+"\\"+clientUser.getUsername();
		File f = new File(path);
		ArrayList<File> files = new ArrayList<File>();
		if(!f.exists()) {
			System.out.println(path+"no exists");
		} else {
			File fa[] = f.listFiles();
			for(int i=0; i<fa.length; i++) {
				File fs = fa[i];
				if(fs.isDirectory()){
					continue;
				} else {
					files.add(fs);
				}
			}
		}
		ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
		oos.writeObject(files);
		s.shutdownOutput();
	}
}
