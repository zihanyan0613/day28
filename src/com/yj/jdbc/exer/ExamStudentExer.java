package com.yj.jdbc.exer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.junit.Test;

public class ExamStudentExer {

	@Test
	public void delete(){
		System.out.println("请输入学生的考号: ");
		Scanner scanner = new Scanner(System.in);
		
		Connection conn = null;
		//准备SQL语句
		String sql = "delete from examstudent where ExamCard = ?";
		QueryRunner queryRunner = new QueryRunner();
		while(true){
			try {
				conn = JDBCUtils.getConnection();
				String examCard = scanner.next();
				int rs = queryRunner.update(conn, sql, examCard);
				if(rs == 0){
					System.out.println("查无此人,请重新输入!");
				}else{
					return;
				}
			}catch (Exception e) {
				e.printStackTrace();
			} finally{
				JDBCUtils.close(conn);
			}
		}
		
	}

	@Test
	public void testQuery(){
		//获取选择查询的类型
		String type = getTypeFromConsole();
		
		//根据类型进行查询操作
		Student stu = getStudentFromQuery(type);
		
		//打印查询的信息
		if(stu == null){
			System.out.println("查无此人");
		}else{
			System.out.println("============查询结果============");
			System.out.println(stu);
		}
		
	}

	private Student getStudentFromQuery(String type) {
		Scanner scanner = new Scanner(System.in);
		if(type.equals("a")){
			System.out.println("请输入准考证号: ");
			String examCard = scanner.next();
			//准备SQL语句
		//	String sql = "select * from examstudent where ExamCard = ?";
			
			String sql = "select FlowID flowId, Type type,IDCard idCard ,ExamCard examCard ,"
					+ "StudentName studentName,Location location,Grade grade  "
					+ "from examstudent where ExamCard = ?";
			
//			String sql = "SELECT FlowID id, Type,type, IDCard idCard, ExamCard examCard, "
//					+ "StudentName studentName, Location location, Grade grade "
//					+ "FROM examstudent "
//					+ "WHERE ExamCard = ?";
//			
			//获得Dbutils子类
			
			Student stu = Query(sql, examCard);
			System.out.println(stu);
			return stu;
			
		}else{
			System.out.println("请输入身份证号: ");
			String idCard = scanner.next();
			//准备SQL语句
			String sql = "select * from examstudent where IDCard = ?";
			//获得Dbutils子类
			Student stu = Query(sql, idCard);
			return stu;
			
		}
	}

	private Student Query(String sql, String examCard){
		Student stu = null;
		Connection conn = null;
		try {
			QueryRunner queryRunner = new QueryRunner();
			conn = JDBCUtils.getConnection();
			ResultSetHandler<Student> rsh = new BeanHandler<Student>(Student.class);
			stu = queryRunner.query(conn, sql, rsh, examCard);
			return stu;
		}catch(Exception e) {
			e.printStackTrace();
		} finally{
			JDBCUtils.close(conn);
		}
		return stu;
		
	}

	private String getTypeFromConsole() {
		System.out.println("请选择您要输入的类型: ");
		System.out.println("a:准考证号");
		System.out.println("b.身份证号");
		Scanner scanner = new Scanner(System.in);
		while(true){
			String type = scanner.nextLine();
			if(type.equals("a") || type.equals("b")){
				return type;
			}else{
				System.out.println("您输入的有误,请重新输入!");
			}
		}

	}
	
	@Test
	public void testAddNewStudent(){
		addNewStudent();
	}
	
	public void addNewStudent(){
		System.out.println("请输入考生信息:");
		//从控制台获取信息
		Student student = studenFromConsole();
		//System.out.println(student);
		//把获取的信息，存入数据库
	    //准备SQL语句
		String sql = "insert into examstudent(Type,IDCard,ExamCard,StudentName,Location,Grade) "
				+ "values(?,?,?,?,?,?)";
		
		//获得Dbutils子类对象 QueryRunner
		QueryRunner queryRunner = new QueryRunner();
		
		//执行SQL语句
		//获取Connection对象
		Connection conn = null;
		try {
			conn = JDBCUtils.getConnection();
			queryRunner.update(conn, sql, student.getType(),
										  student.getIdCard(),
										  student.getExamCard(),
										  student.getStudentName(),
										  student.getLocation(),
										  student.getGrade());
			System.out.println("信息录入成功！");
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			JDBCUtils.close(conn);
		}
	
	}

	private Student studenFromConsole() {
		System.out.println("请输入考生信息: ");
		Scanner scanner = new Scanner(System.in);

		System.out.print("Type: ");
		int type = scanner.nextInt();
		
		System.out.print("IDCard: ");
		String idCard = scanner.next();
		
		System.out.print("ExamCard: ");
		String examCard = scanner.next();
		
		System.out.print("StudentName: ");
		String studentName = scanner.next();
		
		System.out.print("Location: ");
		String location = scanner.next();
		
		System.out.print("Grade: ");
		int grade = scanner.nextInt();
		
		Student student = new Student(0,type, idCard, examCard, studentName, location, grade);
		return student;
	}
}
