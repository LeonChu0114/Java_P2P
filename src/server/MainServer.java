package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import client.threadClient;
import model.User;
import protocal.Datagrame;

public class MainServer {
	
	private final static int TCP_port = 8098;
	
	//服务器维护一个用户表，保存用户名及其所在端口
	public final static HashMap<String,Integer> users = new HashMap<String,Integer>();
	public final static HashMap<String,Long> hb = new HashMap<String, Long>();

	public static void main(String[] args) throws Exception {
		ServerSocket ss = new ServerSocket(TCP_port);
		System.out.println("********服务器启动********");
		Socket socket = new Socket();
		ExecutorService cachedThreadPool = Executors.newCachedThreadPool();  
        cachedThreadPool.execute(new Runnable() {  
            public void run() {  
            	while(true){
            		long now = System.currentTimeMillis();
            		//System.out.println("现在是"+now);
            		Iterator iter = MainServer.hb.entrySet().iterator();
            		//遍历心跳包检测
            		while(iter.hasNext()){
            			Entry entry = (Entry)iter.next();
            			String auser = (String)entry.getKey();
            			long past = (long)entry.getValue();
            			//System.out.println("用户："+auser+"上次一发送的心跳包为："+past);
            			if((now-past)>50000){
            				System.out.println("用户"+auser+"过长时间未发送心跳包，自动断开连接");
            				MainServer.hb.remove(auser);
            			}
            		}
            		try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            	}
            }  
        });   
		while(true){
			socket = ss.accept();
			new threadServer(socket).start();
		}

	}

}
