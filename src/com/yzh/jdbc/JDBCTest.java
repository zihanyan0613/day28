package com.yzh.jdbc;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.Driver;
import java.util.Date;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.junit.Test;

public class JDBCTest {

	@Test
	public void testQuery2(){
		QueryRunner queryRunner = new QueryRunner();
		//��ȡ����
		Connection conn = null;
		String sql = "select id, name,description as \"desc\",create_date as \"createDate\" "
				+ "from products where id = ?";
//		String sql = "SELECT id, name, description as \"desc\", create_date as \"createDate\" "
//				+ "FROM products WHERE id = ?";
		try {
			conn = JDBCUtils.getConnection();
			ResultSetHandler<Product> rsh = new BeanHandler(Product.class);
			Product product = queryRunner.query(conn, sql, rsh, 1);
			System.out.println(product);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			JDBCUtils.close(conn);
		}
		
	}
	@Test
	public void testQuery(){
		//����Dbutil�������
		QueryRunner queryRunner = new QueryRunner();
		//��ȡ����
		Connection conn = null;
		String sql = "select id,name,description as \"desc\",create_date as \"createDate\" from products";
		try {
			conn = JDBCUtils.getConnection();
			ResultSetHandler<List<Product>> rsh = new BeanListHandler(Product.class);
			//ִ�� query(String sql, ResultSetHandler<T> rsh, Object... params)
		    List<Product> result = queryRunner.query(conn, sql, rsh);
		    System.out.println(result);
		    
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			JDBCUtils.close(conn);
		}
	
	}
	
	
	
	@Test
	public void testDbutilsUpdate(){
		//����Dbutils�������
		QueryRunner queryRunner = new QueryRunner();
		//�õ�Connection����
		Connection conn = null;
		String sql = "insert into products(name,description,create_date) values(?,?,?)";
		try {
			conn = JDBCUtils.getConnection();
			queryRunner.update(conn, sql, "iphone6s","apple",new Date());
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			JDBCUtils.close(conn);
		}
		
	}
}
