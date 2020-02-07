package com.iisigroup.product.iip.utility.filePathControl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class logbackFileHandler {
//	public static void main(String[] args) throws IOException {
//		System.out.println(reloadLogBack("<Configuration><appender>123</appender></Configuration>","C:\\Users\\Administrator\\Documents\\DigiSightTest\\utility\\target\\classes\\logback.xml"));
//	}
	public static String reloadLogConfig(String XML,String FilePath) throws IOException {
		FilePath=FilePath.replace("\\", "/");
		FileWriter writeConfig=new FileWriter(FilePath);
		BufferedWriter writer =new BufferedWriter(writeConfig);
		writer.write(XML);
		writer.close();
		FileReader ReadConfig=new FileReader(FilePath);
		BufferedReader reader=new BufferedReader(ReadConfig);
		String result=reader.readLine();
		reader.close();
		return result;
	}
}
