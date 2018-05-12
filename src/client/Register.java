package client;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Timestamp;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import protocal.Datagrame;


public class Register extends JFrame {
	
	private JTextField tuname;
	private JPasswordField tpawd;
	private JPasswordField tpawd_conf;
	Datagrame dg = new Datagrame();
	
	public Register() {
		setTitle("\u6CE8\u518C");
		getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("\u8BF7\u586B\u5199\u4E2A\u4EBA\u4FE1\u606F");
		lblNewLabel.setFont(new Font("微软雅黑", Font.PLAIN, 29));
		lblNewLabel.setBounds(167, 10, 224, 43);
		getContentPane().add(lblNewLabel);
		
		JLabel unamelab = new JLabel("\u7528 \u6237 \u540D\uFF1A");
		unamelab.setFont(new Font("宋体", Font.PLAIN, 20));
		unamelab.setBounds(68, 89, 100, 24);
		getContentPane().add(unamelab);
		
		JLabel pawdlab = new JLabel("\u5BC6    \u7801\uFF1A");
		pawdlab.setFont(new Font("宋体", Font.PLAIN, 20));
		pawdlab.setBounds(68, 143, 100, 24);
		getContentPane().add(pawdlab);
		
		JLabel pawdconf = new JLabel("\u786E\u8BA4\u5BC6\u7801\uFF1A");
		pawdconf.setFont(new Font("宋体", Font.PLAIN, 20));
		pawdconf.setBounds(68, 199, 100, 24);
		getContentPane().add(pawdconf);
		
		tuname = new JTextField();
		tuname.setBounds(181, 89, 224, 24);
		getContentPane().add(tuname);
		tuname.setColumns(10);
		
		tpawd = new JPasswordField();
		tpawd.setBounds(181, 146, 224, 24);
		getContentPane().add(tpawd);
		
		tpawd_conf = new JPasswordField();
		tpawd_conf.setBounds(181, 202, 224, 24);
		getContentPane().add(tpawd_conf);
		
		JButton button = new JButton("\u73B0\u5728\u5C31\u6CE8\u518C");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = tuname.getText();
				String password = String.valueOf(tpawd.getPassword());
				String password1 = String.valueOf(tpawd_conf.getPassword());
				if(!username.equals("") && !password.equals("") && password.equals(password1)){
					try {
						Socket s = new Socket(InetAddress.getLocalHost(), 8098);
						
						//打开IO
						OutputStream os = s.getOutputStream();
						InputStream is = s.getInputStream();
						dg.setIdentifier(0);
						dg.setUsername(username);
						dg.setPassword(password);
						
						ObjectOutputStream oos = new ObjectOutputStream(os);
						oos.writeObject(dg);
						s.shutdownOutput();
						
						//等待回复
						BufferedReader br = new BufferedReader(new InputStreamReader(is));
						byte result =  (byte) br.read();
						System.out.println(result);
						if(result == 1){
							s.close();
							JOptionPane.showMessageDialog(null,"注册成功！");
							dispose();
						}
						else{
							JOptionPane.showMessageDialog(null, "用户名已存在", "消息", JOptionPane.ERROR_MESSAGE); 
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}else{
					JOptionPane.showMessageDialog(null, "两次密码不一致！", "消息", JOptionPane.ERROR_MESSAGE); 
				}
				
			}
		});
		button.setFont(new Font("微软雅黑", Font.BOLD, 22));
		button.setBounds(181, 268, 168, 51);
		getContentPane().add(button);
	}
}
