package ch09;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StudentDAO {
	// Connection : 데이터 베이스랑 연결을 해준다. 인터페이스.
	Connection conn = null;

	// PreparedStatement : 쿼리문에 실행을 담당해준다. 실행을 시켜준다.
	// SQL문이 컴파일 되어서 이곳에 저장이 되고 실행을 시켜줌.
	PreparedStatement pstmt;

	// final : 절대 바꿀 수 없다.
	/*
	 * JDBC : 자바와 DB를 연결해주는 API, 오라클을 쓰기 때문에 ojbcd6.jar 사용했음 DB마다 사용하는 jar는 각 다르고
	 * 연결하려면 꼭 필요하다!
	 */
	final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
	final String JDBC_URL = "jdbc:oracle:thin:@localhost:1521:xe";

	/*
	 * port번호 중요함 오라클 처음 세팅하는 과정에서 포트번호가 나오고 그때 지정해줌 확인은 db에 들어가서 로그인 정보 보면 됨.
	 */
	// DB를 연결해주는 메소드
	public void open() {
		try {
			Class.forName(JDBC_DRIVER); // -> jdbc 드라이버 로드를 하는 과정..
			conn = DriverManager.getConnection(JDBC_URL, "test", "test1234"); //url, 접속아이디, 비밀번호
			/* 위에 conn을 가져온다.( ,"아이디","비밀번호"), DB에 연결해줌
			 * DriverManager : JDBC 드라이버 집합을 관리하기 위한 기본 서비스입니다.
			 * getConnection : 지정된 데이터베이스 URL에 대한 연결 설정을 시도합니다.
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// DB를 닫아주는 메소드
	public void close() {
		try {
			/*
			 * <중요> pstmt, conn은 리소스(데이터를 읽고 쓰는 객체) 이므로 사용 후 반드시 닫아줘야 한다. 닫아주지 않으면 메모리를 잡아먹기
			 * 때문.
			 */
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	//학생 정보를 다 불러온다. (select*from)
	//Student 클래스에 담아서 불러올것임.
	public ArrayList<Student> getAll() {
		open(); //DB를 오픈한다.
		ArrayList<Student> students = new ArrayList<>(); //student객체를 담을 리스트 준비.
		
	try {
		pstmt = conn.prepareStatement("select * from student"); //실행 할 쿼리문을 입력하고 준비한다.
	//ResultSet : 데이터를 받는 역할을 한다. 라이브러리에서 import해옴.
		ResultSet rs = pstmt.executeQuery(); //쿼리문 실행(select문 사용시 사용)
			//next : boolean을 리턴해주고, 한 행씩 값이 있는지 없는지 판단한다.
		while(rs.next()) {
			Student s = new Student();
					//get..() : 자바의 타입으로 가져온다.
			s.setId(rs.getInt("id"));
			s.setUsername(rs.getString("username")); //이름
			s.setUniv(rs.getString("univ")); //
			s.setBirth(rs.getDate("birth"));
			s.setEmail(rs.getString("email"));
			//arraylist students에 s객체를 저장한다.
			students.add(s);
	//while문이 for문 역할을 한다. -> 내용 더 추가해보기.
		}
		
	} catch (SQLException e) {
		e.printStackTrace();
	} finally { //finally : try catch문에서 무조건적으로 실행되어야 하는 곳. 꼭 닫아야 하기 때문에 여기에 close를 해준다.
		close();
	}
		
		return students;
	
	//try catch문을 사용하는 이유 : 에러가 나야 문제를 바로잡을 수 있기 때문에
	//getAll() 메소드를 실행하면 학생정보가 다 나오고 리턴받고 끝난다.
		
		
}
	
	
	
	
	//학생 정보를 입력 (insert)
	public void insert(Student s) {
		open();							//insert할 정보를 ?로 표시한다. 지금은 4개라서 ? 4개를 써줬음
										// ? : 어떤 데이터가 들어올지 모른다.
		String sql = "insert into student values(id_seq.nextval,?,?,?,?)";
		
		try {
			pstmt = conn.prepareStatement(sql); //실행할 쿼리문 입력
			//오라클의 데이터 타입으로 변환을 해준다.
			pstmt.setString(1, s.getUsername()); //setString : 지정된 매게변수를 스트링 값으로 출력한다.
			pstmt.setString(2, s.getUniv());//pstmt.setString(값을 넣어줄 위치, 넣어줄 데이터)
			pstmt.setDate(3, s.getBirth());
			pstmt.setString(4, s.getEmail());
			//-------------------------------> 여기까지가 준비했고
			
			pstmt.executeUpdate(); //insert, delete, update 실행시 -> 여기서 데이터가 넣어졌음.
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		
	}
	
	
	
}
