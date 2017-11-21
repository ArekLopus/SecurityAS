package jaxrs;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;


@Path("test")
public class TestRS {
	
	@GET
	@Produces("text/html")
	public String testRS() {
		return "Just Testing!";
		
	}

}
