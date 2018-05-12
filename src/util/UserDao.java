package util;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.User;


public class UserDao {
	String sql;
	
	//登录验证
	public int login(String username, String password) {
		Connection conn;
		PreparedStatement stmt;
		ResultSet rs;
		try {
			conn = DBHelper.getConnection();
			sql = "select * from user where username='"+username+"' and password='"+password+"'";
			System.out.println(sql);
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if(rs.next())
			{
				System.out.println("登陆成功！");
				return 1;	//返回1代表登陆成功
							
			}else{
				return 0;	//返回0代表用户名或密码不正确
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	
	//注册时查看该用户是否被注册
	public boolean ifUserIDExist(String username){
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		sql = "select * from user where username = '"+username+"'";
		System.out.println(sql);
		try {
			conn = DBHelper.getConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if(rs.next()){
				return true;            //有相同用户名返回true 此时不能注册
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;              //未找到相同的用户名 可以注册
	}
	
	//注册
	public void register(String username, String password) {
		Connection conn = null;
		PreparedStatement stmt = null;
		String sql = "insert into user values('" + username + "','" + password+"')";
		System.out.println(sql);
		try {
			conn = DBHelper.getConnection();
			stmt = conn.prepareStatement(sql);
			stmt.execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	
	/*public ArrayList<Comment> searchAll() {
		ArrayList<Comment> cms = new ArrayList<Comment>();
		sql = "select * from comment";
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DBHelper.getConnection();
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()) {
				Comment cm = new Comment();
				cm.setId(rs.getInt("id"));
				System.out.println(rs.getInt("id"));
				cm.setFilename(rs.getString("filename"));
				cm.setUsername(rs.getString("username"));
				cm.setPort(rs.getInt("port"));
				cm.setDescription(rs.getString("description"));
				cm.setPublicKey(rs.getString("publickey"));
				cms.add(cm);
			}
			System.out.println("UserDao:");
			for(int i=0; i<cms.size(); i++){
				System.out.println(cms.get(i).getId());
				System.out.println(cms.get(i).getFilename());
				System.out.println(cms.get(i).getUsername());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return cms;
	}*/
}
