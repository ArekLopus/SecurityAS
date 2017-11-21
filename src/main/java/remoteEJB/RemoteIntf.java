package remoteEJB;

import java.util.Map;

import javax.ejb.Remote;

@Remote
public interface RemoteIntf {

	public String getInfoo();
	public String getInfoo2();
	Map<Object, Object> getSystemProperties();

}
