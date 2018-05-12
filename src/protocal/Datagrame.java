package protocal;

import java.io.File;
import java.io.Serializable;
import java.sql.Timestamp;

public class Datagrame implements Serializable{
	private int identifier; // 报文标识符，确定是哪种类型的请求
	private String username; //报文的发送方username
	private String password; //报文发送方password
	private int port; //发送方端口
	private File f;//发送方发送的文件
	private String filename;//发送文件的文件名
	private long filesize;//发送文件的大小
	private String message;//想要发送的信息
	private long ts;
	
	public int getIdentifier() {
		return identifier;
	}
	public void setIdentifier(int identifier) {
		this.identifier = identifier;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public File getF() {
		return f;
	}
	public void setF(File f) {
		this.f = f;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public long getFilesize() {
		return filesize;
	}
	public void setFilesize(long filesize) {
		this.filesize = filesize;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public long getTs() {
		return ts;
	}
	public void setTs(long ts) {
		this.ts = ts;
	}
	
	
}
