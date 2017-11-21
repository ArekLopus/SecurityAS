package remoteEJB;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import static javax.naming.Context.INITIAL_CONTEXT_FACTORY;
import static javax.naming.Context.PROVIDER_URL;
import static javax.naming.Context.URL_PKG_PREFIXES;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Client_Embeddable_TO_FIX {

	public Client_Embeddable_TO_FIX() {
		
	}
	public static final String APP_NAME = "javax.ejb.embeddable.appName";
    public static final String MODULES = "javax.ejb.embeddable.modules";
    public static final String PROVIDER = "javax.ejb.embeddable.provider";
	
	public static void main(String[] args) throws NamingException {
		
		String host = "127.0.0.1";
		String port = "8080"; // Wildfly HTTP port
		
//		Context remotingContext;
//		try {
//			remotingContext = createRemoteEjbContext(host, port);
//		} catch (NamingException e) {
//			System.err.println("Error setting up remoting context");
//			e.printStackTrace();
//			return;
//		}
		
      Map<String, Object> properties = new HashMap<>();
      //properties.put(EJBContainer.MODULES, new File("/home/arek/NetBeansProjects/WebAppRest/dist/WebAppRest.war"));
      // Creates an Embedded Container and get the JNDI context
      try (EJBContainer ec = EJBContainer.createEJBContainer(Client_Embeddable_TO_FIX.createRemoteEjbContext(host, port))) {
      Context ctx = (Context) ec.getContext();
//INFO: Portable JNDI names for EJB DemoBean: [java:global/classes/DemoBean!ejbs.DemoBeanLocal, java:global/classes/DemoBean]
//      // Looks up the EJB with the no-interface view
//      //ItemEJB itemEJB = (ItemEJB) ctx.lookup("java:global/classes/ItemEJB ");
          //DemoBean db1 = InitialContext.doLookup("java:global/classes/DemoBean");
          RemoteIntf db1 = InitialContext.doLookup("java:global/classes/RemoteBean!remote.RemoteIntf");
          System.out.println("------------------------------------------------");
          System.out.println(db1.getInfoo());
          //ec.close();
      }
    
    } 
	
	
	
	private static java.util.Hashtable createRemoteEjbContext(String host, String port) throws NamingException {

		java.util.Hashtable<Object, Object> props = new java.util.Hashtable<>();

		props.put(INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
		props.put(URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");

		props.put("jboss.naming.client.ejb.context", false);
		props.put("org.jboss.ejb.client.scoped.context", true);
		
		props.put("endpoint.name", "client-endpoint");
		props.put("remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED", false);
		props.put("remote.connections", "default");
		props.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS", false);

		props.put(PROVIDER_URL, "http-remoting://" + host + ":" + port);
		props.put("remote.connection.default.host", host);
		props.put("remote.connection.default.port", port);

		return props;
	}

	@SuppressWarnings("unchecked")
	private static <T> T createEjbProxy(Context remotingContext, String ejbUrl, Class<T> ejbInterfaceClass)
			throws NamingException, ClassCastException {
		Object resolvedproxy = remotingContext.lookup(ejbUrl);
		return (T) resolvedproxy;
	}
	
}
