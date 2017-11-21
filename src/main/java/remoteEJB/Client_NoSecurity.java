package remoteEJB;

import java.util.Map;

import static javax.naming.Context.INITIAL_CONTEXT_FACTORY;
import static javax.naming.Context.PROVIDER_URL;
import static javax.naming.Context.URL_PKG_PREFIXES;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Client_NoSecurity {

	public Client_NoSecurity() {
		
	}

	
	public static void main(String[] args) throws NamingException {
		
		String host = "127.0.0.1";
		//String host = "localhost";
		String port = "8080"; // Wildfly HTTP port
		
		 
		
		Context remotingContext;
		try {
			remotingContext = createRemoteEjbContext(host, port);
		} catch (NamingException e) {
			System.err.println("Error setting up remoting context");
			e.printStackTrace();
			return;
		}
		
		//Syntax: ejb:${appName}/${moduleName}/${beanName}!${remoteView}
		// appName = name of EAR deployment (or empty for single EJB/WAR deployments)
		// moduleName = name of EJB/WAR deployment
		String ejbUrl = "ejb:/Security/RemoteBean!remoteEJB.RemoteIntf";
		String ejbUrl1 = "/Security/RemoteBean!remoteEJB.RemoteIntf";
		
		RemoteIntf service;
		try {
			service = createEjbProxy(remotingContext, ejbUrl, RemoteIntf.class);
			//System.out.println(service.getInfoo());		//NIE DZIAŁA Z SessionCDI! (To działa z servleta)
			System.out.println(service.getInfoo2());
		} catch (NamingException e) {
			System.err.println("Error resolving bean");
			e.printStackTrace();
			return;
		} catch (ClassCastException e) {
			System.err.println("Resolved EJB is of wrong type");
			e.printStackTrace();
			return;
		}
		
		Map<Object, Object> systemProperties;
		try {
			systemProperties = service.getSystemProperties();
		} catch (Exception e) {
			System.err.println("Error accessing remote bean");
			e.printStackTrace();
			return;
		}

		System.out.println("Wildfly Home Dir: " + systemProperties.get("jboss.home.dir"));
		

    } 
	
	
	
	private static Context createRemoteEjbContext(String host, String port) throws NamingException {

		java.util.Hashtable<Object, Object> props = new java.util.Hashtable<>();

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
