package com.yj.jdbc.exer;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class JDBCUtils {

	public static void close(Connection connection){
		if(connection != null){
			try {
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public static void close(Statement statement){
		if(statement != null){
			try {
				statement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public static void close(ResultSet resultSet){
		if(resultSet != null){
			try {
				resultSet.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	//通用的连接方法
	public static Connection getConnection() throws Exception{
		
		Connection connection = null;
		//加载配置信息
		Properties properties = new Properties();
		InputStream inStream = JDBCUtils.class.getClassLoader().getResourceAsStream("jdbc.properties");
		properties.load(inStream);
		//准备连接数据库的四个字符串

		String driverClass = properties.getProperty("jdbc.driverClass");
		String url = properties.getProperty("jdbc.url");
		String user = properties.getProperty("jdbc.user");
		String password = properties.getProperty("jdbc.password");
		
		Class.forName(driverClass);
		
		connection = DriverManager.getConnection(url, user, password);
		
		return connection;
	}
}
