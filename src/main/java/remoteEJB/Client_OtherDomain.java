package remoteEJB;

import java.util.Map;

import static javax.naming.Context.INITIAL_CONTEXT_FACTORY;
import static javax.naming.Context.PROVIDER_URL;
import static javax.naming.Context.URL_PKG_PREFIXES;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import arka.SecurityBeanEJB;
import arka.SecurityBeanRemote;

public class Client_OtherDomain {

	public Client_OtherDomain() {}
	
	public static void main(String[] args) throws NamingException {
		
		String host = "localhost";
		String port = "8080"; // Wildfly HTTP port
		
		Context remotingContext;
		try {
			remotingContext = createRemoteEjbContext(host, port);
		} catch (NamingException e) {
			System.err.println("Error setting up remoting context");
			e.printStackTrace();
			return;
		}
		
		//MUSIO być <s:security-domain>other</s:security-domain> w jboss-ejb3.xml
		//ALBO 		@org.jboss.ejb3.annotation.SecurityDomain("other")
		String ejb2 = "ejb:/Security/SecurityBeanEJB!arka.SecurityBeanRemote";
		
		SecurityBeanRemote service2;
		try {
			service2 = createEjbProxy(remotingContext, ejb2, SecurityBeanRemote.class);
			System.out.println(service2.checkRemote());
		} catch (NamingException e) {
			System.err.println("Error resolving bean");
			e.printStackTrace();
			return;
		} catch (ClassCastException e) {
			System.err.println("Resolved EJB is of wrong type");
			e.printStackTrace();
			return;
		}
		
    } 
	
	
	
	private static Context createRemoteEjbContext(String host, String port) throws NamingException {

		java.util.Hashtable<Object, Object> props = new java.util.Hashtable<>();
		
		props.put("remote.connection.default.username", "arek");  	//new
        props.put("remote.connection.default.password", "arek");  	//new
        
		props.put(INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
		props.put(URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");

		props.put("jboss.naming.client.ejb.context", false);	//'ejb:' w URL (tak czy nie) 
		props.put("org.jboss.ejb.client.scoped.context", true);

		props.put("endpoint.name", "client-endpoint");
		props.put("remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED", false);
		props.put("remote.connections", "default");
		props.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS", false);

		props.put(PROVIDER_URL, "http-remoting://" + host + ":" + port);
		props.put("remote.connection.default.host", host);
		props.put("remote.connection.default.port", port);
		
		return new InitialContext(props);
	}

	@SuppressWarnings("unchecked")
	private static <T> T createEjbProxy(Context remotingContext, String ejbUrl, Class<T> ejbInterfaceClass)
			throws NamingException, ClassCastException {
		Object resolvedproxy = remotingContext.lookup(ejbUrl);
		return (T) resolvedproxy;
	}
	
}
