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

public class Client_CustomLM_TO_FIX {

	public Client_CustomLM_TO_FIX() {}
	
	public static void main(String[] args) throws NamingException {
		//String host = "127.0.0.1";
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
		
		String ejb2 = "ejb:/Security/SecurityBeanEJB!arka.SecurityBeanRemote";
		//String ejb2 = "/Security/SecurityBeanEJB!arka.SecurityBeanRemote";
		
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
	
	//ZAMIAST pass 'darek' przekazuje do sprawdzenia RemotingConnectionCredential w MyLoginModuleDB.validatePassword()
	// -----darek, org.jboss.as.security.remoting.RemotingConnectionCredential@77d8b82b, digestCallback
	
	private static Context createRemoteEjbContext(String host, String port) throws NamingException {

		java.util.Hashtable<Object, Object> props = new java.util.Hashtable<>();
		
		props.put("remote.connection.default.username", "darek");  
        props.put("remote.connection.default.password", "darek");
        
        props.put("remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED", false);
        props.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS", true);
        props.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT", false);
        props.put("jboss.naming.client.connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT", true);

        //props.put("remote.connection.default.connect.options.org.xnio.Options.SASL_DISALLOWED_MECHANISMS", "JBOSS-LOCAL-USER");
        
		props.put(INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
		props.put(URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");

		props.put("jboss.naming.client.ejb.context", false);	//'ejb:' w URL (tak czy nie) 
		props.put("org.jboss.ejb.client.scoped.context", true);
		props.put("remote.connection.default.protocol", "http-remoting");
		
		props.put("endpoint.name", "client-endpoint");
		props.put("remote.connections", "default");

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
