package arka;

import java.io.Serializable;
import java.security.Principal;
import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Named
@SessionScoped
public class StartBeanCDI implements Serializable {
	private static final long serialVersionUID = -6746806481518377966L;
	
	private String name = "Guest";
	
	@Inject
	Principal pr;

	@Inject
	SecurityBeanEJB sb;
	
	public String getPrincipal() {
		if(pr != null) { 
			name = pr.getName();
			return pr.getName();
		}
		else return "NoPrincipal";
	}
	
	public void info() {
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		Logger.getLogger(getClass().getName()).info("------------------------------------------------------------------");
		Logger.getLogger(getClass().getName()).info("CDI - isUserInRole Admin: "+req.isUserInRole("Administrator"));
		Logger.getLogger(getClass().getName()).info("CDI - isUserInRole Devel: "+req.isUserInRole("Developer"));
		Logger.getLogger(getClass().getName()).info("CDI - isSecure: "+req.isSecure());
		Logger.getLogger(getClass().getName()).info("CDI - getAuthtype: "+req.getAuthType());
		Logger.getLogger(getClass().getName()).info("CDI - getRemoteUser: "+req.getRemoteUser());
		if(req.getUserPrincipal() != null) {
			Logger.getLogger(getClass().getName()).info("CDI - getUserPrincipal: "+req.getUserPrincipal().getName());
		} else {
			Logger.getLogger(getClass().getName()).info("CDI - getUserPrincipal: "+req.getUserPrincipal());
		}
		Logger.getLogger(getClass().getName()).info("CDI - getRequestUrl: "+req.getRequestURL());
		Logger.getLogger(getClass().getName()).info("CDI - getCookies: "+req.getCookies().length);
		Logger.getLogger(getClass().getName()).info("------------------------------------------------------------------");
	}
	
	public void checkEJB() {
		sb.check();
	}
	
	public String logout() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		if(session != null) {
			session.invalidate();
			return "/index.xhtml?faces-redirect=true";
		}
		return null;
	}
	public String logout2() throws ServletException {
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		if(req != null) {
			req.logout();
			return "/index.xhtml?faces-redirect=true";
		}
		return null;
	}
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
