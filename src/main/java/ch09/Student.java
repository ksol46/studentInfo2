package ch09;

import java.sql.Date; //-> SQL로 import 해야한다..!

//엔티티 클래스는 데이터베이스와 대응된다. 컬럼명 = 속성
public class Student {
	private int id;
	private String username;
	private String univ;
	private Date birth;
	private String email;
	
	//모두 다 getter setter로 만들어주기.
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUniv() {
		return univ;
	}
	public void setUniv(String univ) {
		this.univ = univ;
	}
	public Date getBirth() {
		return birth;
	}
	public void setBirth(Date birth) {
		this.birth = birth;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
