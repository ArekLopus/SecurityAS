package loginModuleSalted;

import java.security.acl.Group;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import javax.sql.DataSource;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.jboss.security.PicketBoxLogger;
import org.jboss.security.PicketBoxMessages;
import org.jboss.security.auth.spi.UsernamePasswordLoginModule;
import org.jboss.security.auth.spi.Util;
import org.jboss.security.plugins.TransactionManagerLocator;

//Just as an examle
public class DatabaseServerLoginModule_example extends UsernamePasswordLoginModule {
   // see AbstractServerLoginModule
   private static final String DS_JNDI_NAME = "dsJndiName";
   private static final String ROLES_QUERY = "rolesQuery";
   private static final String SUSPEND_RESUME = "suspendResume";
   private static final String PRINCIPALS_QUERY = "principalsQuery";
   private static final String TRANSACTION_MANAGER_JNDI_NAME = "transactionManagerJndiName";

   private static final String[] ALL_VALID_OPTIONS =
   {
      DS_JNDI_NAME,ROLES_QUERY,SUSPEND_RESUME,PRINCIPALS_QUERY,TRANSACTION_MANAGER_JNDI_NAME
   };
   
   protected String dsJndiName;	   /** The JNDI name of the DataSource to use */
   protected String principalsQuery = "select Password from Principals where PrincipalID=?";	   /** The sql query to obtain the user password */
   protected String rolesQuery;	   /** The sql query to obtain the user roles */
   protected boolean suspendResume = true;	   /** Whether to suspend resume transactions during database operations */
   protected String txManagerJndiName = "java:/TransactionManager";		   /** The JNDI name of the transaction manager */
   protected TransactionManager tm = null;	   /** The TransactionManagaer instance to be used */

   /**
    * Initialize this LoginModule.
    * 
    * @param options -
    * dsJndiName: The name of the DataSource of the database containing the Principals, Roles tables
    * principalsQuery: The prepared statement query, equivalent to:
    *    "select Password from Principals where PrincipalID=?"
    * rolesQuery: The prepared statement query, equivalent to:
    *    "select Role, RoleGroup from Roles where PrincipalID=?"
    */
   public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String,?> sharedState, Map<String,?> options)  {
      addValidOptions(ALL_VALID_OPTIONS);
      super.initialize(subject, callbackHandler, sharedState, options);
      dsJndiName = (String) options.get(DS_JNDI_NAME);
      if( dsJndiName == null )
         dsJndiName = "java:/DefaultDS";
      Object tmp = options.get(PRINCIPALS_QUERY);
      if( tmp != null )
         principalsQuery = tmp.toString();
      tmp = options.get(ROLES_QUERY);
      if( tmp != null )
         rolesQuery = tmp.toString();
      tmp = options.get(SUSPEND_RESUME);
      if( tmp != null )
         suspendResume = Boolean.valueOf(tmp.toString()).booleanValue();
          
      //Get the Transaction Manager JNDI Name
      String jname = (String) options.get(TRANSACTION_MANAGER_JNDI_NAME);
      if(jname != null)
         this.txManagerJndiName = jname;

      PicketBoxLogger.LOGGER.traceDBCertLoginModuleOptions(dsJndiName, principalsQuery, rolesQuery, suspendResume);

      try {
         if(this.suspendResume)
            tm = this.getTransactionManager();
      } catch (NamingException e) {
         throw PicketBoxMessages.MESSAGES.failedToGetTransactionManager(e);
      }
   }

   /** Get the expected password for the current username available via
    * the getUsername() method. This is called from within the login()
    * method after the CallbackHandler has returned the username and
    * candidate password.
    * @return the valid password String
    */
   protected String getUsersPassword() throws LoginException {
      String username = getUsername();
      String password = null;
      Connection conn = null;
      PreparedStatement ps = null;
      ResultSet rs = null;
      
      Transaction tx = null;
      if (suspendResume) {
         //tx = TransactionDemarcationSupport.suspendAnyTransaction();
         try {
            if(tm == null)
               throw PicketBoxMessages.MESSAGES.invalidNullTransactionManager();
            tx = tm.suspend();
         } catch (SystemException e) {
            throw new RuntimeException(e);
         }
      }

      try {
         InitialContext ctx = new InitialContext();
         DataSource ds = (DataSource) ctx.lookup(dsJndiName);
         conn = ds.getConnection();
         // Get the password
         PicketBoxLogger.LOGGER.traceExecuteQuery(principalsQuery, username);
         ps = conn.prepareStatement(principalsQuery);
         ps.setString(1, username);
         rs = ps.executeQuery();
         if( rs.next() == false ) {
            throw PicketBoxMessages.MESSAGES.noMatchingUsernameFoundInPrincipals();
         }
         
         password = rs.getString(1);
         password = convertRawPassword(password);
      }
      catch(NamingException ex) {
         LoginException le = new LoginException(PicketBoxMessages.MESSAGES.failedToLookupDataSourceMessage(dsJndiName));
         le.initCause(ex);
         throw le;
      } catch(SQLException ex) {
         LoginException le = new LoginException(PicketBoxMessages.MESSAGES.failedToProcessQueryMessage());
         le.initCause(ex);
         throw le;
      } finally {
         if (rs != null) {
            try {
               rs.close();
            } catch(SQLException e) {
            	
            }
         }
         if( ps != null ) {
            try {
               ps.close();
            } catch(SQLException e) {
            	
            }
         }
         if( conn != null ) {
            try {
               conn.close();
            } catch (SQLException ex) {
            	
            }
         }
         if (suspendResume) {
            //TransactionDemarcationSupport.resumeAnyTransaction(tx);
            try {
               tm.resume(tx);
            } catch (Exception e) {
               throw new RuntimeException(e);
            } 
         }
      }
      return password;
   }

   /** Execute the rolesQuery against the dsJndiName to obtain the roles for
    the authenticated user.
     
    @return Group[] containing the sets of roles
    */
   
   //https://github.com/picketbox/picketbox/blob/master/security-jboss-sx/jbosssx/src/main/java/org/jboss/security/auth/spi/Util.java
   
   protected Group[] getRoleSets() throws LoginException
   {
      if (rolesQuery != null)
      {
         String username = getUsername();
         PicketBoxLogger.LOGGER.traceExecuteQuery(rolesQuery, username);
         //Group[] roleSets = org.jboss.security.auth.spi.Util.getRoleSets(username, dsJndiName, txManagerJndiName, rolesQuery, this,
         //      suspendResume);
         //return roleSets;
         return new Group[0];
      }
      return new Group[0];
   }
   
   /** A hook to allow subclasses to convert a password from the database
    into a plain text string or whatever form is used for matching against
    the user input. It is called from within the getUsersPassword() method.
    @param rawPassword - the password as obtained from the database
    @return the argument rawPassword
    */
   protected String convertRawPassword(String rawPassword)
   {
      return rawPassword;
   }
   
   protected TransactionManager getTransactionManager() throws NamingException
   {
      TransactionManagerLocator tml = new TransactionManagerLocator();
      return tml.getTM(this.txManagerJndiName);
   } 
}
