package util;

import java.io.File;

public class Test {
	
	public String getRoot(){
		String str = this.getClass().getResource("/").getFile();  
		return str;
	}

	public static void main(String[] args) {
		File dirFile = new File("C:\\Users\\Administrator\\Desktop\\1.txt");
		
	}

}
