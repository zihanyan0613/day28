package com.yzh.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.junit.Test;

public class JDBCUtils {

	public static void commit(Connection connection){
		if(connection != null){
			try {
				connection.commit();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void rollback(Connection connection) {
		if(connection != null){
			try {
				connection.rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//设置不自动提交
	public static void beginTx(Connection connection){
		if(connection != null){
			try {
				connection.setAutoCommit(false);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	//操作数据库
	//update insert delete
	public static void update(String sql,Object ... args){
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			//获取连接
			connection = getConnection();
			preparedStatement = connection.prepareStatement(sql);

			//获取可变参数
			if(args != null && args.length > 0){
				for(int i = 0; i < args.length; i++){
					preparedStatement.setObject(i+1, args[i]);
				}
			}
			//处理SQL语句
			preparedStatement.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			close(preparedStatement);
			close(connection);
		}
		
	}
	
	//获取连接的方法
	public static Connection getConnection(){
		
		Connection connection = null;
		try {
			//读取配置信息
			Properties properties = new Properties();
			InputStream inStream = JDBCUtils.class.getClassLoader().getResourceAsStream("jdbc.properties");
			properties.load(inStream);
			
			//准备连接的四个字符串
			String diverClass = properties.getProperty("jdbc.driverClass");
			String url = properties.getProperty("jdbc.url");
			String user = properties.getProperty("jdbc.user");
			String password = properties.getProperty("jdbc.password");
			
			//加载驱动
			Class.forName(diverClass);
			
			connection = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return connection;
	}

	//关闭数据库资源
	public static void close(Connection connection){
		try {
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void close(Statement statement){
		try {
			statement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
