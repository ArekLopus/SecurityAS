package arka;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.annotation.security.RunAs;
import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.security.auth.Subject;

import org.jboss.ejb3.annotation.SecurityDomain;

@Stateless
@LocalBean
@DeclareRoles({"Developer", "Administrator", "guest"})
@RolesAllowed({"Administrator", "Developer"})
//@RunAs("Administrator")
//@SecurityDomain("other")
//@SecurityDomain("arka")
//@org.jboss.ejb3.annotation.SecurityDomain
public class SecurityBeanEJB implements Serializable, SecurityBeanRemote{
	private static final long serialVersionUID = 1L;

	private Logger log =  Logger.getLogger(getClass().getName());
	
	@Resource
	SessionContext ctx;
	
	//@DenyAll
	public void check() {
		log.info("EJB - Admin? "+ctx.isCallerInRole("Administrator"));
		log.info("EJB - Deleloper? "+ctx.isCallerInRole("Developer"));
		log.info("EJB - guest? "+ctx.isCallerInRole("guest"));
		log.info("EJB - Principal name: "+ctx.getCallerPrincipal().getName());
	}
	
	public String checkRemote() {
		return "SecurityBean - Principal name: "+ctx.getCallerPrincipal().getName()+",\nSecurityBean - Admin? "+ctx.isCallerInRole("Administrator");
	}
}
