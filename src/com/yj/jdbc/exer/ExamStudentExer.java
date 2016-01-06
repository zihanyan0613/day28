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
		System.out.println("������ѧ���Ŀ���: ");
		Scanner scanner = new Scanner(System.in);
		
		Connection conn = null;
		//׼��SQL���
		String sql = "delete from examstudent where ExamCard = ?";
		QueryRunner queryRunner = new QueryRunner();
		while(true){
			try {
				conn = JDBCUtils.getConnection();
				String examCard = scanner.next();
				int rs = queryRunner.update(conn, sql, examCard);
				if(rs == 0){
					System.out.println("���޴���,����������!");
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
		//��ȡѡ���ѯ������
		String type = getTypeFromConsole();
		
		//�������ͽ��в�ѯ����
		Student stu = getStudentFromQuery(type);
		
		//��ӡ��ѯ����Ϣ
		if(stu == null){
			System.out.println("���޴���");
		}else{
			System.out.println("============��ѯ���============");
			System.out.println(stu);
		}
		
	}

	private Student getStudentFromQuery(String type) {
		Scanner scanner = new Scanner(System.in);
		if(type.equals("a")){
			System.out.println("������׼��֤��: ");
			String examCard = scanner.next();
			//׼��SQL���
		//	String sql = "select * from examstudent where ExamCard = ?";
			
			String sql = "select FlowID flowId, Type type,IDCard idCard ,ExamCard examCard ,"
					+ "StudentName studentName,Location location,Grade grade  "
					+ "from examstudent where ExamCard = ?";
			
//			String sql = "SELECT FlowID id, Type,type, IDCard idCard, ExamCard examCard, "
//					+ "StudentName studentName, Location location, Grade grade "
//					+ "FROM examstudent "
//					+ "WHERE ExamCard = ?";
//			
			//���Dbutils����
			
			Student stu = Query(sql, examCard);
			System.out.println(stu);
			return stu;
			
		}else{
			System.out.println("���������֤��: ");
			String idCard = scanner.next();
			//׼��SQL���
			String sql = "select * from examstudent where IDCard = ?";
			//���Dbutils����
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
		System.out.println("��ѡ����Ҫ���������: ");
		System.out.println("a:׼��֤��");
		System.out.println("b.���֤��");
		Scanner scanner = new Scanner(System.in);
		while(true){
			String type = scanner.nextLine();
			if(type.equals("a") || type.equals("b")){
				return type;
			}else{
				System.out.println("�����������,����������!");
			}
		}

	}
	
	@Test
	public void testAddNewStudent(){
		addNewStudent();
	}
	
	public void addNewStudent(){
		System.out.println("�����뿼����Ϣ:");
		//�ӿ���̨��ȡ��Ϣ
		Student student = studenFromConsole();
		//System.out.println(student);
		//�ѻ�ȡ����Ϣ���������ݿ�
	    //׼��SQL���
		String sql = "insert into examstudent(Type,IDCard,ExamCard,StudentName,Location,Grade) "
				+ "values(?,?,?,?,?,?)";
		
		//���Dbutils������� QueryRunner
		QueryRunner queryRunner = new QueryRunner();
		
		//ִ��SQL���
		//��ȡConnection����
		Connection conn = null;
		try {
			conn = JDBCUtils.getConnection();
			queryRunner.update(conn, sql, student.getType(),
										  student.getIdCard(),
										  student.getExamCard(),
										  student.getStudentName(),
										  student.getLocation(),
										  student.getGrade());
			System.out.println("��Ϣ¼��ɹ���");
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			JDBCUtils.close(conn);
		}
	
	}

	private Student studenFromConsole() {
		System.out.println("�����뿼����Ϣ: ");
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
