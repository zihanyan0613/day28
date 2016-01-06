package com.yzh.jdbc.home;

import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.junit.Test;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.yzh.jdbc.JDBCUtils;

public class JdbcTest {

	@Test
	public void testC3P0() throws  Exception {
	
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		
		cpds.setDriverClass("com.mysql.jdbc.Driver"); 
		cpds.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/jdbc");
		cpds.setUser("root"); 
	    cpds.setPassword("0613");
	    
	    Connection connection = cpds.getConnection();
	    System.out.println(connection);
	   
	}

	/*
	 * 把属性放到配置文件中读取
	 */
	@Test
	public void testDBCPWithDataSourceFactory() throws Exception {
		Connection connection = null;

		Properties properties = new Properties();
		InputStream inStream = JdbcTest.class.getClassLoader()
				.getResourceAsStream("dbcp.properties");
		properties.load(inStream);
		System.out.println(properties);
		
		// properties.setProperty("driverClassName", "com.mysql.jdbc.Driver");
		// properties.setProperty("username", "root");
		// properties.setProperty("password", "0613");
		// properties.setProperty("url", "jdbc:mysql://127.0.0.1:3306/jdbc");
		DataSource dataSource = BasicDataSourceFactory
				
				.createDataSource(properties);
		connection = dataSource.getConnection();
		System.out.println(connection);

		// connection = dataSource.getConnection();
		// System.out.println(connection);
		//
		// connection = dataSource.getConnection();
		// System.out.println(connection);
		//
		// connection = dataSource.getConnection();
		// System.out.println(connection);
		//
		// connection = dataSource.getConnection();
		// System.out.println(connection);
		//
		// connection = dataSource.getConnection();
		// System.out.println(connection);
	}

	/*
	 * 数据库连接池
	 */
	@Test
	public void testDBCP() throws SQLException {
		final BasicDataSource dataSource = new BasicDataSource();
		Connection connection = null;
		// 准备四个基本的属性
		dataSource.setUsername("root");
		dataSource.setPassword("0613");
		dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/jdbc");
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");

		// 一些可选属性
		// 1.初始连接数
		dataSource.setInitialSize(5);
		// 2. 指定最大的连接数: 同一时刻可以同时向数据库申请的连接数
		dataSource.setMaxActive(5);
		// 3.指定小连接数: 在数据库连接池中保存的最少的空闲连接的数量
		dataSource.setMaxIdle(2);
		// 4.最长等待时间
		dataSource.setMaxWait(5000);

		// 获取连接
		connection = dataSource.getConnection();
		System.out.println(connection.getClass());

		connection = dataSource.getConnection();
		System.out.println(connection.getClass());

		connection = dataSource.getConnection();
		System.out.println(connection.getClass());

		connection = dataSource.getConnection();
		System.out.println(connection.getClass());

		Connection connection2 = dataSource.getConnection();
		System.out.println(connection2);

		new Thread() {
			public void run() {
				Connection conn = null;
				try {
					conn = dataSource.getConnection();
					System.out.println(conn.getClass());
				} catch (Exception e) {
					e.printStackTrace();
				}

			};
		}.start();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		connection2.close();

	}

	@Test
	public void testBatch() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String sql = null;
		try {

			connection = JDBCUtils.getConnection();
			sql = "insert into customers values(?,?,?)";
			preparedStatement = connection.prepareStatement(sql);
			// 开始事务
			JDBCUtils.beginTx(connection);
			Date date = new Date(new java.util.Date().getTime());
			long begin = System.currentTimeMillis();
			for (int i = 0; i < 100000; i++) {
				preparedStatement.setInt(1, i + 1);
				preparedStatement.setString(2, "name");
				preparedStatement.setDate(3, date);

				// 积攒SQl
				preparedStatement.addBatch();
				if ((i + 1) % 300 == 0) {
					preparedStatement.executeBatch();
					preparedStatement.clearBatch();
				}

			}

			if (100000 % 300 != 0) {
				preparedStatement.executeBatch();
				preparedStatement.clearBatch();
			}

			long end = System.currentTimeMillis();
			System.out.println("end - begin = " + (end - begin));
			// 提交事务
			JDBCUtils.commit(connection);
		} catch (Exception e) {
			e.printStackTrace();
			JDBCUtils.rollback(connection);

		} finally {
			JDBCUtils.close(preparedStatement);
			JDBCUtils.close(connection);
		}
	}

	@Test
	public void testPreparedStatement() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String sql = null;
		try {

			connection = JDBCUtils.getConnection();
			sql = "insert into customers values(?,?,?)";
			preparedStatement = connection.prepareStatement(sql);
			// 开始事务
			JDBCUtils.beginTx(connection);
			Date date = new Date(new java.util.Date().getTime());
			long begin = System.currentTimeMillis();
			for (int i = 0; i < 100000; i++) {
				preparedStatement.setInt(1, i + 1);
				preparedStatement.setString(2, "name");
				preparedStatement.setDate(3, date);

				preparedStatement.executeUpdate();

			}

			long end = System.currentTimeMillis();
			System.out.println("end - begin = " + (end - begin));
			// 提交事务
			JDBCUtils.commit(connection);
		} catch (Exception e) {
			e.printStackTrace();
			JDBCUtils.rollback(connection);

		} finally {
			JDBCUtils.close(preparedStatement);
			JDBCUtils.close(connection);
		}
	}

	@Test
	public void testBatchWithStatement() {
		Connection connection = null;
		Statement statement = null;

		try {
			connection = JDBCUtils.getConnection();
			statement = connection.createStatement();
			// 开始事务
			JDBCUtils.beginTx(connection);
			long begin = System.currentTimeMillis();
			for (int i = 0; i < 10; i++) {
				String sql = "insert into customers values(" + (i + 1)
						+ ",' name _ " + i + "'," + "'27-9月 -15')";
				statement.executeUpdate(sql);
			}
			long end = System.currentTimeMillis();
			System.out.println("end - begin = " + (end - begin));
			// 提交事务
			JDBCUtils.commit(connection);
		} catch (Exception e) {
			e.printStackTrace();
			JDBCUtils.rollback(connection);

		} finally {

			JDBCUtils.close(connection);
			JDBCUtils.close(statement);
		}
	}

	@Test
	public void testTransaction() {
		Connection connection = null;
		try {
			connection = JDBCUtils.getConnection();
			// 开始事务
			connection.setAutoCommit(false);
			String sql = "update users set balance = balance - 500 where id = 1";
			update(connection, sql);

			// int i = 10/0;

			String sql1 = "update users set balance = balance + 500 where id = 2";
			update(connection, sql1);
			// 结束事务
			connection.commit();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			JDBCUtils.close(connection);
		}
	}

	// 操作数据库
	// update insert delete
	public static void update(Connection connection, String sql, Object... args) {
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(sql);

			// 获取可变参数
			if (args != null && args.length > 0) {
				for (int i = 0; i < args.length; i++) {
					preparedStatement.setObject(i + 1, args[i]);
				}
			}
			// 处理SQL语句
			preparedStatement.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCUtils.close(preparedStatement);
		}

	}

	@Test
	public void testBlob() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = JDBCUtils.getConnection();
			String sql = "insert into customers(id,name,email,birth,picture) values(?,?,?,?,?)";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, 30);
			preparedStatement.setString(2, "Jerry");
			preparedStatement.setString(3, "jerry@163.com");
			preparedStatement.setDate(4,
					new Date(new java.util.Date().getTime()));
			InputStream inputStream = new FileInputStream("shuo.jpg");
			preparedStatement.setBlob(5, inputStream);
			preparedStatement.executeUpdate();

			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.close(preparedStatement);
			JDBCUtils.close(connection);
		}

	}

	@Test
	public void testGetBlob() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = JDBCUtils.getConnection();
			String sql = "select * from customers where id = 30";
			preparedStatement = connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				int id = resultSet.getInt(1);
				String name = resultSet.getString(2);
				String email = resultSet.getString(3);
				Date date = resultSet.getDate(4);
				System.out.println(id + "," + name + "," + email + "," + date);

				Blob blob = resultSet.getBlob(5);
				InputStream inputStream = blob.getBinaryStream();
				OutputStream out = new FileOutputStream("li.jpg");
				byte[] buf = new byte[100];
				int len = 0;
				while ((len = inputStream.read(buf)) != -1) {
					out.write(buf, 0, len);
				}

				out.close();
				inputStream.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.close(preparedStatement);
			JDBCUtils.close(connection);

		}

	}

}