package loginModuleSalted;

import java.security.acl.Group;
import java.util.Map;
import java.util.logging.Logger;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

import org.jboss.security.SimpleGroup;
import org.jboss.security.SimplePrincipal;
import org.jboss.security.auth.spi.DatabaseServerLoginModule;

import util.ParseRoles;
import util.SaltedMessageToSHA;
 
public class MyLoginModuleDB extends DatabaseServerLoginModule {
	
    private String hashAlgorithm;
    
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map sharedState, Map options) {
    	hashAlgorithm = (String) options.get("hashAlgorithm");
        super.initialize(subject, callbackHandler, sharedState, options);
        
    }
    
    //(required) The UsernamePasswordLoginModule modules compares the result of this method with the actual password.
    //To jest password z DB
    @Override
    protected String getUsersPassword() throws LoginException {
    	String pass = super.getUsersPassword();
    	Logger.getLogger(getClass().getName()).info("----- getUsersPassword(): ..."+pass.substring(pass.length()-15, pass.length()));
    	return pass;
    }
 
    //NIE POTRZEBA - wszystko robimy w createPasswordHash - do testów ew.
    //(optional) Override if you want to change how the password are compared
    //or if you need to perform some conversion on them. 
    @Override
    protected boolean validatePassword(String passFromClient, String passFromDb) {

    	Logger.getLogger(getClass().getName()).info("passFromCl: "+passFromClient);
    	Logger.getLogger(getClass().getName()).info("passFromDb: "+passFromDb);
    	
    	if(passFromClient.equals(passFromDb)) return true;
        
        return false;
    }

	@Override
	protected String createPasswordHash(String name, String pass, String smtg) throws LoginException {
		Logger.getLogger(getClass().getName()).info("-----"+name+", "+pass+", "+smtg);
		String saltedPass = SaltedMessageToSHA.hashStringSalted(pass, hashAlgorithm, "UTF-8"); 
		return saltedPass;
	}

	
//  @Override
//  protected Group[] getRoleSets() throws LoginException {
//	  Logger.getLogger(getClass().getName()).info("-----role"+super.getRoleSets()[0]);
//	  return super.getRoleSets();
//  }
	
    @Override
    protected Group[] getRoleSets() throws LoginException {
    	
    	Group[] groups = super.getRoleSets();
    	if (groups.length == 0) {
    		return groups;
    	}
    	
    	Group group = groups[0];
    	String rolesInGroup = group.members().nextElement().getName();
    	Logger.getLogger(getClass().getName()).info("----- Roles before parse: "+rolesInGroup);
    	
    	group = new SimpleGroup("Roles");
        try {
        	String[] roles = ParseRoles.parse(rolesInGroup);
        	for (String role: roles) {
        		group.addMember(new SimplePrincipal(role));
        	}
        } catch (Exception e) {
            throw new LoginException("Failed to create group member for " + group);
        }
        
        return new Group[] { group };
    }
	
	
	
//    @Override
//    protected Group[] getRoleSets() throws LoginException {
//        SimpleGroup group = new SimpleGroup("Roles");
//        try {
//            // userGroup picked up by MongoDB Cursor earlier
//            group.addMember(new SimplePrincipal(userGroup));
//        } catch (Exception e) {
//            throw new LoginException("Failed to create group member for " + group);
//        }
//        return new Group[] { group };
//    }
 
}