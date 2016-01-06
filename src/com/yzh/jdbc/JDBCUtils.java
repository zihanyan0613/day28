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
	
	//���ò��Զ��ύ
	public static void beginTx(Connection connection){
		if(connection != null){
			try {
				connection.setAutoCommit(false);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	//�������ݿ�
	//update insert delete
	public static void update(String sql,Object ... args){
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			//��ȡ����
			connection = getConnection();
			preparedStatement = connection.prepareStatement(sql);

			//��ȡ�ɱ����
			if(args != null && args.length > 0){
				for(int i = 0; i < args.length; i++){
					preparedStatement.setObject(i+1, args[i]);
				}
			}
			//����SQL���
			preparedStatement.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			close(preparedStatement);
			close(connection);
		}
		
	}
	
	//��ȡ���ӵķ���
	public static Connection getConnection(){
		
		Connection connection = null;
		try {
			//��ȡ������Ϣ
			Properties properties = new Properties();
			InputStream inStream = JDBCUtils.class.getClassLoader().getResourceAsStream("jdbc.properties");
			properties.load(inStream);
			
			//׼�����ӵ��ĸ��ַ���
			String diverClass = properties.getProperty("jdbc.driverClass");
			String url = properties.getProperty("jdbc.url");
			String user = properties.getProperty("jdbc.user");
			String password = properties.getProperty("jdbc.password");
			
			//��������
			Class.forName(diverClass);
			
			connection = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return connection;
	}

	//�ر����ݿ���Դ
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
