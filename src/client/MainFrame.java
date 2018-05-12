package client;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.PublicKey;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;

import model.User;
import protocal.Datagrame;
import server.threadServer;
import util.UpLoad;

import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

import java.awt.event.MouseAdapter;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.ScrollPaneConstants;

public class MainFrame extends JFrame {
	private JTextField location;
	private ArrayList<User> us = new ArrayList<User>();
	private JTable table_1 = new JTable();
	private Socket s1;
	private JTable table;
	private JTextField textField_3;
	private String askusername;
	private int askport;
	private String askfilename;
	private User user = new User();
	private MainFrame mf = this;
	private boolean threadflag = false;
	
	public MainFrame(User user) {
		this.user = user;
		setTitle("\u56FE\u50CF\u52A0\u5BC6\u7CFB\u7EDF\u5BA2\u6237\u7AEF");
		getContentPane().setLayout(null);
		
		JLabel label = new JLabel("\u76EE\u5F55\uFF1A");
		label.setBounds(651, 151, 60, 24);
		label.setFont(new Font("宋体", Font.PLAIN, 20));
		getContentPane().add(label);
		
		location = new JTextField();
		location.setBounds(718, 154, 202, 24);
		getContentPane().add(location);
		location.setColumns(10);
		
		JButton button_1 = new JButton("\u6211\u4E5F\u8981\u5171\u4EAB\u6587\u4EF6");
		button_1.setBounds(697, 56, 189, 27);
		button_1.setFont(new Font("宋体", Font.PLAIN, 20));
		
		//文件夹中选择文件
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("请选择文件");
				FileSystemView fsv = FileSystemView.getFileSystemView();
				fileChooser.setCurrentDirectory(fsv.getHomeDirectory());
				fileChooser.setApproveButtonText("确定");
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int result = fileChooser.showOpenDialog(null);
				if (JFileChooser.APPROVE_OPTION == result) {
				    	   String path=fileChooser.getSelectedFile().getPath();
				    	   location.setText(path);
				   }
			}
		});
		getContentPane().add(button_1);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(14, 179, 274, 233);
		getContentPane().add(scrollPane_2);
		scrollPane_2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane_2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		
		JButton searchbtn = new JButton("\u67E5\u8BE2\u5728\u7EBF\u7528\u6237");
		searchbtn.setBounds(82, 102, 161, 27);
		searchbtn.setFont(new Font("宋体", Font.PLAIN, 20));
		//向服务器发送查询请求
		searchbtn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				Socket s;
				Datagrame dg = new Datagrame();
				try {
					s = new Socket(InetAddress.getLocalHost(),8098);
					dg.setIdentifier(3);
					ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
					oos.writeObject(dg);
					s.shutdownOutput();
					
					
					//等待服务器返回
					ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
					us = (ArrayList<User>)ois.readObject();
					Object[][] playerInfo = new Object[us.size()][3];
					String[] Names = {"用户名","IP地址","端口号"};
					//以Names和playerInfo为参数，创建一个表格
					int count = 0;
					for(int i=0; i<us.size(); i++){ 
						playerInfo[i][0] = us.get(i).getUsername();
						playerInfo[i][1] = InetAddress.getLocalHost();
						playerInfo[i][2] = us.get(i).getPort();
					}
					table_1 = new JTable(playerInfo, Names);
					scrollPane_2.setViewportView(table_1);
					table_1.updateUI();
					repaint();
					table_1.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							System.out.println("clicked");
							int r = table_1.getSelectedRow();
							User u = new User();
							u = us.get(r);
							askusername = u.getUsername();
							System.out.println("请求的是"+askusername);
							askport = u.getPort();
						}
					});
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}				
				//结束输出，等待服务器输入
				catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}				
			}
		});
		getContentPane().add(searchbtn);
		
		JLabel label_3 = new JLabel("\u67E5\u8BE2\u7ED3\u679C\uFF1A");
		label_3.setBounds(14, 151, 100, 18);
		label_3.setFont(new Font("宋体", Font.PLAIN, 20));
		getContentPane().add(label_3);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(334, 56, 293, 235);
		getContentPane().add(scrollPane);
		
		JButton btnJi = new JButton("\u5EFA\u7ACB\u8FDE\u63A5");        //点击建立连接后
		btnJi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					System.out.println("askport is "+askport);
					s1 = new Socket(InetAddress.getLocalHost(), askport);
					s1.setKeepAlive(true);
					
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				JOptionPane.showMessageDialog(null,"成功建立连接！！！");
				String files[] = null;
				Datagrame dg = new Datagrame();
				ArrayList<File> respfiles = new ArrayList<File>();
				try {
					ObjectOutputStream oos = new ObjectOutputStream(s1.getOutputStream());
					dg.setIdentifier(5);
					oos.writeObject(dg);
					
					//等待回复
					ObjectInputStream ois =  new ObjectInputStream(s1.getInputStream());
					respfiles = (ArrayList<File>)ois.readObject();
					s1.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				Object[][] playerInfo = new Object[respfiles.size()][2];
				String[] Names = {"文件名","文件大小"};
				//以Names和playerInfo为参数，创建一个表格
				int count = 0;
				System.out.println("文件个数："+respfiles.size());
				for(int i=0; i<respfiles.size(); i++){
					playerInfo[i][0] = respfiles.get(i).getName();
					playerInfo[i][1] = Integer.toString((int)respfiles.get(i).length());
				}
				table = new JTable(playerInfo, Names);
				scrollPane.setViewportView(table);
				table.updateUI();
				repaint();
				table.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						System.out.println("clicked");						
						int r = table.getSelectedRow();
						textField_3.setText((String) playerInfo[r][0]);
					}
				});			
			}
		});
		btnJi.setFont(new Font("宋体", Font.PLAIN, 20));
		btnJi.setBounds(185, 422, 113, 27);
		getContentPane().add(btnJi);
		
		JLabel userlab = new JLabel();
		userlab.setFont(new Font("宋体", Font.PLAIN, 20));
		userlab.setText(user.getUsername()+",您好!");
		userlab.setBounds(14, 13, 333, 33);
		getContentPane().add(userlab);
		
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(308, 0, 16, 560);
		getContentPane().add(separator);
		
		JButton btnNewButton = new JButton("\u4E0A\u4F20\u6587\u4EF6");   //上传文件
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Datagrame dg = new Datagrame();
				String str = location.getText();
				String[] files = str.split("\\\\");
				String filename = files[files.length-1];
				File file = new File(str);
				dg.setIdentifier(2);
				dg.setFilename(filename);
				dg.setFilesize(file.length());
				dg.setUsername(user.getUsername());
				dg.setF(file);
				try {
					s1 = new Socket(InetAddress.getLocalHost(), 8098);
					s1.setKeepAlive(true);
					OutputStream os = s1.getOutputStream();
					ObjectOutputStream oos = new ObjectOutputStream(os);
					oos.writeObject(dg);
					s1.shutdownOutput();
					
					//等待服务器回应
					InputStream is = s1.getInputStream();
					int result = is.read();
					if(result == 1){
						JOptionPane.showMessageDialog(null,"图片上传成功！！");
					}
					is.close();
					oos.close();
					os.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}				
			}
		});
		btnNewButton.setFont(new Font("宋体", Font.PLAIN, 20));
		btnNewButton.setBounds(797, 205, 123, 27);
		getContentPane().add(btnNewButton);
		
		JButton button_2 = new JButton("\u9000\u51FA\u7CFB\u7EDF");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Socket s;
				String username = user.getUsername();
				Datagrame dg = new Datagrame();
				dg.setIdentifier(7);
				dg.setUsername(username);
				try {
					s = new Socket(InetAddress.getLocalHost(), 8098);
					ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
					oos.writeObject(dg);
					s.shutdownOutput();
					s.close();
					dispose();
					System.exit(0);
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}							
			}
		});
		button_2.setFont(new Font("宋体", Font.PLAIN, 20));
		button_2.setBounds(836, 422, 113, 27);
		getContentPane().add(button_2);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setOrientation(SwingConstants.VERTICAL);
		separator_2.setBounds(636, 0, 16, 560);
		getContentPane().add(separator_2);
		
		JLabel lblTa = new JLabel("Ta\u5171\u4EAB\u7684\u6587\u4EF6");
		lblTa.setFont(new Font("宋体", Font.PLAIN, 20));
		lblTa.setBounds(334, 20, 148, 18);
		getContentPane().add(lblTa);
		
		
		
		JButton btnNewButton_1 = new JButton("\u4E0B\u8F7D\u6587\u4EF6");   //下载文件
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String filename = textField_3.getText();
				OutputStream os;
				Datagrame dg = new Datagrame();
				dg.setIdentifier(6);
				dg.setFilename(filename);
				try {
					s1 = new Socket(InetAddress.getLocalHost(), askport);
					ObjectOutputStream oos = new ObjectOutputStream(s1.getOutputStream());
					oos.writeObject(dg);
					s1.shutdownOutput();
					
					//接收
					UpLoad ul = new UpLoad();
					InputStream is = s1.getInputStream();
					ObjectInputStream ois = new ObjectInputStream(is);
					File reveivef = (File) ois.readObject();
					byte[] data = ul.readFile(reveivef);
					ul.byte2file(data, System.getProperty("user.dir")+"\\"+user.getUsername()+"\\"+filename);
					JOptionPane.showMessageDialog(null,"文件下载成功！！");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnNewButton_1.setFont(new Font("宋体", Font.PLAIN, 20));
		btnNewButton_1.setBounds(514, 422, 113, 27);
		getContentPane().add(btnNewButton_1);
		
		JLabel label_4 = new JLabel("\u8981\u4E0B\u8F7D\u7684\u6587\u4EF6\u540D\uFF1A");
		label_4.setFont(new Font("宋体", Font.PLAIN, 20));
		label_4.setBounds(334, 310, 189, 18);
		getContentPane().add(label_4);
		
		textField_3 = new JTextField();
		textField_3.setEditable(false);
		textField_3.setColumns(10);
		textField_3.setBounds(334, 338, 293, 24);
		getContentPane().add(textField_3);
		
		JLabel label_2 = new JLabel();
		label_2.setText("您当前登录端口为："+user.getPort());
		label_2.setFont(new Font("宋体", Font.PLAIN, 20));
		label_2.setBounds(14, 59, 333, 33);
		getContentPane().add(label_2);
		
		this.addWindowListener(new WindowAdapter(){
			   public void windowClosing(WindowEvent we){
				   threadflag = true;
			   }
		});
		
		ExecutorService cachedThreadPool = Executors.newCachedThreadPool();  
        cachedThreadPool.execute(new Runnable() {  
            public void run() {  
            	try {
            		System.out.println("这是接听的线程");
					ServerSocket ss = new ServerSocket(user.getPort());
					System.out.println("客户端等待其他客户请求，绑定地址："+user.getPort());
					Socket socket = new Socket();
					while(true){
						socket = ss.accept();
						new threadClient(mf,user,socket).start();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }  
        });   
        cachedThreadPool.execute(new Runnable() {  
            public void run() {  
            	System.out.println("这是发送心跳包的线程");
            	int i=0;
            	try {
					while(!threadflag){
						System.out.println("已发送心跳包"+(++i)+"个");
						Socket heartbeat = new Socket(InetAddress.getLocalHost(),8098);
						Datagrame hbp = new Datagrame();
						hbp.setIdentifier(9);
						hbp.setUsername(user.getUsername());
						hbp.setTs(System.currentTimeMillis());
						ObjectOutputStream oos = new ObjectOutputStream(heartbeat.getOutputStream());
						oos.writeObject(hbp);
						heartbeat.shutdownOutput();
						Thread.sleep(500);
						
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
					
            }  
        });  
	}
}
