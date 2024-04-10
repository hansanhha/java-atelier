## Static Web Page

웹 서버에서 정적 웹 페이지의 렌더링을 하고 반환하는 기술

## CGI(Common Gateway Interface)

웹 서버의 동적 컨텐츠 처리를 위한 기술

1. 요청
2. 웹 서버 수신 및 대응되는 CGI 프로그램 확인
3. 운영체제에 의해 CGI 프로그램 실행(동적 처리)
4. 렌더링된 HTML 반환

단점
- 요청마다 CGI 프로세스가 실행됨
- 스크립트 언어 CGI 프로그램의 경우 매번 스크립트를 해석해야 됨
- C, Perl, Shell Script로 개발했는데, 이 언어들은 대규모 웹 서버 설계에 적합하지 않음

웹 서버에서 임의의 프로그램을 실행할 수 있기 때문에 간혹 쓰이는 경우도 있음 

## Server Side Scripting

웹 서버 자체에서 동적 페이지를 처리하는 기술

요청마다 프로세스를 생성하여 운영체제를 통해 실행하지 않고 웹 서버 내의 스레드를 사용하여직접 처리

자바같은 객체지향 언어으로 대규모 웹 서버를 개발

Servlet
- Java EE 사양의 일부로 HTTP를 처리하고 응답을 생성하는 서버 컴포넌트
- HTML 페이지 생성, 세션 관리, DB 처리 등
- 자바로 작성되며 JVM 위에서 실행

JSP
- JSP는 HTML 페이지에 자바 코드를 삽입하는 방식, Servlet은 자바 클래스에 HTML을 삽입하는 방식
- 자바에서 HTML 관련 코드를 Servlet보다 편리하게 작성할 수 있는 Server Side Scripting 기술
- Servlet을 기반으로 하며 JSP 페이지는 Servlet으로 변환되어 실행됨
- MVC 아키텍처를 지원함
- JSP - View, Servlet - Controller
