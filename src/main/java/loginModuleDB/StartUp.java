package loginModuleDB;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Singleton
@Startup
public class StartUp {
	
	private String arek_sha512 = "327824c473a8f364215f2a8f981bbcfee1c1770647e54906a7fa64e7938249e7c3dc41205da6396124ddce3e3464294415e56a2d3715b636eea61718f80e33e8";
	private String marek_sha512 = "c343cef31694ea637b573fbdbaed949263a3be396e918aa0b694dc0e64c14a105eb15719b1814a1bdeaa1af57368f42c5e171d0e3197118076cb49e9e1efc889"; 
	private String darek_salted = "801da838431c2a1ebb15b07daaa65950d6f344ba6b3fddd6aeb15d02fc5c702eb9922f5748667f30370cb02c2489e23b79db5bc24f1562f4e6d40877ce08cf9d";
	
	@PersistenceContext
	EntityManager em;
	
	@PostConstruct
	public void startUp() {
		Logger.getLogger(getClass().getName()).info("StartUp started");
		User user = new User("arek", "garek", arek_sha512 , "Administrator");
		em.persist(user);
		Logger.getLogger(getClass().getName()).info("User found: "+em.find(User.class, user.getId()));
		
		User user2 = new User("marek", "warek", marek_sha512 , "Administrator");
		em.persist(user2);
		Logger.getLogger(getClass().getName()).info("User found: "+em.find(User.class, user2.getId()));
		//arka2 LM
		User user3 = new User("darek", "rarek", darek_salted , "Administrator, Developer");
		em.persist(user3);
		Logger.getLogger(getClass().getName()).info("User found: "+em.find(User.class, user3.getId()));
	}

}
