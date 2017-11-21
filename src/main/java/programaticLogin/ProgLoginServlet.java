package programaticLogin;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Login")
public class ProgLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	public ProgLoginServlet() {
        super();
    }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter(); 
		pw.println("<h2>Programatic Login Servlet (not arka2 LM)</h2>");
		
		if(request.getUserPrincipal() == null) {
			request.login("arek", "arek");
			request.authenticate(response);			
		}
		
		if(request.getUserPrincipal() != null) {
			pw.println("Principal: "+request.getUserPrincipal().getName());
			pw.println("<br/>Admin?: "+request.isUserInRole("Administrator"));
			pw.println("<br/><form>");
			pw.println("<form>");
			pw.println("<br/><input type='button' value='Redirect' onclick=\"window.location.href='/Security/basicAuth/index.xhtml'; return false;\" >");
			pw.println("</form>");
		} else {
			pw.println("No Principal");
		}
		//response.sendRedirect(request.getContextPath()+"/basicAuth/index.xhtml");
	}
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}

}
