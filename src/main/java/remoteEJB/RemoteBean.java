package remoteEJB;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class RemoteBean implements RemoteIntf, Serializable {
	private static final long serialVersionUID = 1L;
	
	@Inject
	SessionBean sb;
	
	@Inject
	SessionCDI cdi;
	
	@Override
	public String getInfoo() {
		return "Info from SessionBean: "+sb.sbTest()+",<br/> Info from SessionCDI: "+cdi.cdiTest();
	}
	
	@Override
	public String getInfoo2() {
		return "Info from SessionBean: "+sb.sbTest();
	}
	
	@Override
	public Map<Object, Object> getSystemProperties() {
		return new HashMap<>(System.getProperties());
	}

	public SessionBean getSb() {
		return sb;
	}
	
	
	
}
