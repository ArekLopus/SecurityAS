package programaticLogin;

import java.io.IOException;
import java.io.Serializable;
import java.security.Principal;
import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Named
@SessionScoped
public class ProgrammaticBeanCDI implements Serializable {
	private static final long serialVersionUID = -6746806481518377966L;
	
	@Inject
	Principal principal;

	public String login() throws ServletException, IOException {
		
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		
		if(req.getUserPrincipal() == null) {
			req.login("arek", "arek");
			req.authenticate(res);			
		}
		
		if(req.getUserPrincipal() != null) {
		
			Logger.getLogger(getClass().getName()).info("Pr CDI - isUserInRole Admin: "+req.isUserInRole("Administrator"));
			Logger.getLogger(getClass().getName()).info("Pr CDI - isUserInRole Dev: "+req.isUserInRole("Developer"));
			Logger.getLogger(getClass().getName()).info("Pr CDI - isSecure: "+req.isSecure());
			Logger.getLogger(getClass().getName()).info("Pr CDI - getAuthtype: "+req.getAuthType());
			Logger.getLogger(getClass().getName()).info("Pr CDI - getRemoteUser: "+req.getRemoteUser());
			if(req.getUserPrincipal() != null) {
				Logger.getLogger(getClass().getName()).info("Pr CDI - getUserPrincipal: "+req.getUserPrincipal().getName());
			} else {
				Logger.getLogger(getClass().getName()).info("Pr CDI - getUserPrincipal: "+req.getUserPrincipal());
			}
			Logger.getLogger(getClass().getName()).info("Pr CDI - getRequestUrl: "+req.getRequestURL());
			Logger.getLogger(getClass().getName()).info("Pr CDI - getCookies: "+req.getCookies().length);
		}
		return null;
	}
	
	
	private String name;
	private String surname;
	public String login2() throws ServletException, IOException {
		
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		
		if(req.getUserPrincipal() == null) {
			try {
				req.login(name, surname);
				req.authenticate(res);
			} catch(ServletException ex) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error Logging In!"));
			}
		}
		
		if(req.getUserPrincipal() != null) {
		
			Logger.getLogger(getClass().getName()).info("Pr CDI - isUserInRole Admin: "+req.isUserInRole("Administrator"));
			Logger.getLogger(getClass().getName()).info("Pr CDI - isUserInRole Dev: "+req.isUserInRole("Developer"));
			Logger.getLogger(getClass().getName()).info("Pr CDI - isSecure: "+req.isSecure());
			Logger.getLogger(getClass().getName()).info("Pr CDI - getAuthtype: "+req.getAuthType());
			Logger.getLogger(getClass().getName()).info("Pr CDI - getRemoteUser: "+req.getRemoteUser());
			if(req.getUserPrincipal() != null) {
				Logger.getLogger(getClass().getName()).info("Pr CDI - getUserPrincipal: "+req.getUserPrincipal().getName());
			} else {
				Logger.getLogger(getClass().getName()).info("Pr CDI - getUserPrincipal: "+req.getUserPrincipal());
			}
			Logger.getLogger(getClass().getName()).info("Pr CDI - getRequestUrl: "+req.getRequestURL());
			Logger.getLogger(getClass().getName()).info("Pr CDI - getCookies: "+req.getCookies().length);
		}
		return null;
	}
	

	public String logout() throws ServletException {
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		if(req != null) {
			req.logout();
			return null;
		}
		return null;
	}



	public Principal getPrincipal() {
		return principal;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getSurname() {
		return surname;
	}


	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	
	
}
