package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBHelper {
	//***********JDBC连接数据库***********//
		private static final String driver = "com.mysql.jdbc.Driver";
		private static final String url="jdbc:mysql://localhost:3306/peers?useUnicode=true&characterEncoding=UTF-8";
		private static final String username="root";
		private static final String password = "123456";
		
		private static Connection conn = null;
		
		//静态代码，负责加载驱动
		static{
			try{
				Class.forName(driver);
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
		}
		
		public static Connection getConnection() throws Exception{
			if(conn == null){
				conn = DriverManager.getConnection(url, username, password);
				return conn;
			}
			return conn;
		}
}
