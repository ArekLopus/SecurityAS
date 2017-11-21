package arka;

import javax.ejb.Remote;

@Remote
public interface SecurityBeanRemote {
	public String checkRemote();
}
