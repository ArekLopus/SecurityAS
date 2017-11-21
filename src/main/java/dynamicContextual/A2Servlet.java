package dynamicContextual;

import java.io.IOException;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.security.auth.Subject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.transaction.Transactional;

@WebServlet(urlPatterns="/contextual2", asyncSupported=true)
public class A2Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    public A2Servlet() {
        super();
    }
    
    @EJB
    DynamicContextualTest dct;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.getWriter().println("<h2>Dynamic Contextual 2 test</h2>");
		response.getWriter().println("<br/>");
		dct.submitJob(new CallableTask(5));
	}
	
	
	class CallableTask implements Callable<Long> {
		private int id;

		public CallableTask(int id) {
			this.id = id;
		}

		public Long call() {
			long summation = 0;
			
			for (int i = 1; i <= id; i++) {
				summation += i;
			}
			System.out.println("Hello");
			Subject subject = Subject.getSubject(AccessController.getContext());
			System.out.println("Subject size: "+subject.getPrincipals().size());
			logInfo(subject, summation); // Log Traces Subject identity
			return new Long(summation);
		}

		private void logInfo(Subject subject, long summation) {
			Logger.getLogger(getClass().getName()).info("Subject: "+subject+", summation: "+summation);
		}

	}
	
}
