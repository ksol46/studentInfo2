package ch09;


import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

/**
 * Servlet implementation class studentController
 */

//studentController s = new studentController();
//서블릿 객체 생성은 톰캣에서 해준다.
@WebServlet("/studentControl")
public class studentController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	StudentDAO dao;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config); //서블릿 초기화, 서버를 종료 시키기전까지 무조건 최초로 한 번만 실행이 된다.
		dao = new StudentDAO(); //StudentDAO 객체가 딱 한번만 만들어진다. -> 공유해서 쓸 수 있다.
	}

	@Override							//request : view에서 보내준 정보들이 들어있음
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8"); //한글데이터 깨짐 방지
		String action = request.getParameter("action"); //쿼리스트링으로 줬음
		//action을 실행하면 결국 insert를 가져온다. (?action=insert)
		String view = "";
		
		view = insert(request,response); //request와 response 객체를 매개변수로 넘겨준다.
		
		//getServletContext():ServletContext를 얻어옴
		//getRequestDispatcher(이동할 페이지):이동 할 페이지의 경로 지정
		getServletContext().getRequestDispatcher("/ch09/" + view)//view : studentInfo.jsp 페이지명이 들어있음.
		.forward(request,response);
		//forward : 브라우저 내부에서 페이지를 이동함. 주소가 변하지 않는다.
	}
    
	//request 데이터 받아옴 -> DAO에 있는 insert 실행(DB에 insert가 됨) -> 페이지명(studentInfo.jsp) 리턴.
	public String insert(HttpServletRequest request, HttpServletResponse response) {
		Student s = new Student();
		try {
			BeanUtils.populate(s, request.getParameterMap());
								//getParameterMap : hashMap처럼 키와 벨류 값으로 가져올 수 있다?
			
			/* 데이터 타입도 바꿔줘야 하고 개수가 많아질때 하나하나 이렇게 넣어줄수가 없음..
			 * 그래서 라이브러리에서 불러옴(BeanUtils)
			 * BeanUtils.populate(s, request.getParameterMap()); --> 아래 코드의 역할을 대신 해준다.
			//view에서 정보를 받아오려면 무조건 getParameter로 받아와야한다!!!
			s.setUsername(request.getParameter("username"));
			s.setEmail(request.getParameter("email"));
			s.setUniv(request.getParameter("univ"));
			s.setBirth(request.getParameter("birth")); //-> 데이터 타입이 String이 아니라서 빨간줄이 뜸.
			*/
				//상위클래스로 만들어준다. 줄여준다.
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		dao.insert(s); //컨트롤러는 DAO한테 있는 메소드를 사용한다. DAO한테 데이터 베이스 관련 데이터를 요청한다.
		return "studentInfo.jsp";
	}

}
