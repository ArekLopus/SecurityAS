package filter;

import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@WebFilter(filterName = "AuthFilter", urlPatterns = { "*.xhtml" } )
public class LogFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        
        Enumeration<String> headersReq = req.getHeaderNames();
        while(headersReq.hasMoreElements()) {
        	String s = headersReq.nextElement();
        	Logger.getLogger(getClass().getName()).info("--- Header Req: "+s+" = "+req.getHeader(s));
        }
        
        Collection<String> headersRes = res.getHeaderNames();
        for(String s : headersRes) {
        	Logger.getLogger(getClass().getName()).info("--- Header Res: "+s+" = "+req.getHeader(s));
        }
        
        chain.doFilter(request, response);
    }

	@Override
	public void destroy() {}

	@Override
	public void init(FilterConfig arg0) throws ServletException {}

}