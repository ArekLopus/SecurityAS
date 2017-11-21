package remoteEJB;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class SessionCDI implements Serializable {
	private static final long serialVersionUID = 1L;

	public String cdiTest() {
		return "This is SeesionCDI";
	}

}
