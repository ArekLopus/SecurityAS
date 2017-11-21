package remoteEJB;

import javax.ejb.Stateful;

@Stateful
public class SessionBean {

	public String sbTest() {
		return "This is SeesionBean";
	}

}
