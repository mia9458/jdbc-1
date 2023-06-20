package koreait.jdbc.day2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class StudentDeleteMenu {

	public static void main(String[] args) {
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String user = "iclass";
		String password = "1234";
		System.out.println("::::::::::::::::::::학생 정보 삭제 메뉴입니다.::::::::::::::::::::");
		System.out.println("<<지정된 학번으로 학생 정보를 삭제할 수 있습니다.");
		
		try(
			Connection conn= DriverManager.getConnection(url, user, password);
		){
		
			deleteStudent(conn);
		}catch (Exception e) {
			System.out.println("오류 메시지 = " + e);
		}
		
		
	}

	//@SuppressWarnings("resource")   //리소스와 관련 경고는 표시하지 않게 해주세요.
	private static void deleteStudent(Connection conn) {
		Scanner sc = new Scanner(System.in);
		String stuno;
		String sql="delete from TBL_STUDENT where stuno = ?";
		
		System.out.println("학생번호 0000 입력은 삭제 취소 입니다.");
		System.out.print("학번을 입력하세요 >>> ");
		stuno = sc.nextLine();

		if (stuno.equals("0000")) {
			System.out.println("학생 정보 삭제를 취소합니다.");
			sc.close();
			return;			//리턴에 값이 없을 때는 단순하게 메소드 종료로 실행됩니다.
		}
		
		try (
			PreparedStatement ps = conn.prepareStatement(sql);
			){  //매개변수의 순서와 형식을 확인하고 전달하는 setXXX 메소드 실행합니다.
			ps.setString(1, stuno);
			int count = ps.executeUpdate();		//*리턴값은 반영된 행의 개수 -> 새로운 메소드 써보기
					
			System.out.println("학생 정보 삭제 " + count + " 건이 완료되었습니다.");
		}catch (SQLException e) {
			System.out.println("데이터 삭제에 문제가 생겼습니다. 상세내용 -" + e.getMessage());
		}
		
		sc.close();
	}

}
