package se.lanteam.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import se.lanteam.constants.SessionConstants;
import se.lanteam.domain.CustomerGroup;
import se.lanteam.domain.SystemUser;
import se.lanteam.repository.ErrorRepository;

public class SessionFilter implements Filter {
	
	@Autowired
	private ErrorRepository errorRepo;
	
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    	SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                filterConfig.getServletContext());
    }
    @Override  
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    	
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String contextPath = request.getContextPath();
        String uri = request.getRequestURI();
        HttpSession session = request.getSession(); 
        String errors = String.valueOf(errorRepo.findErrorsByArchived(false).size());        
        session.setAttribute(SessionConstants.ERROR_COUNT, errors);
        
        SystemUser user = (SystemUser) session.getAttribute(SessionConstants.SYSTEM_USER);
        
        // Mobile login
        if (user == null && redirectPatternInUriMobile(uri)) {
        	HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        	httpResponse.sendRedirect(contextPath + "/login-mobile");
        	return;        	
        }        
        
        if (user == null && redirectPatternInUriUser(uri)) {
        	HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        	httpResponse.sendRedirect(contextPath + "/login");
        	return;        	
        }        
        
        CustomerGroup customerGroup = (CustomerGroup) session.getAttribute(SessionConstants.CURRENT_CUSTOMER_GROUP);        
        if (customerGroup == null && redirectPatternInUriCustomer(uri)) {
        	HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        	httpResponse.sendRedirect(contextPath + "/choose-customer-group");
        } else {        
	        filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    private boolean redirectPatternInUriCustomer(String uri) {
    	boolean result = false;
    	if (!uri.contains("choose-customer-group")
    			&& !uri.contains("customer-groups/activate")
    			&& !uri.contains("login")
    			&& !isResourceUri(uri)) {
    		result = true;
    	}
    	return result;
    }

    private boolean redirectPatternInUriUser(String uri) {
    	boolean result = false;
    	if (!uri.contains("login")
    			&& !uri.contains("login/confirm")
    			&& !isResourceUri(uri)) {
    		result = true;
    	}
    	return result;
    }

    private boolean redirectPatternInUriMobile(String uri) {
    	boolean result = false;
    	if (uri.contains("mobile")
    			&& !uri.contains("login-mobile")
    			&& !isResourceUri(uri)) {
    		result = true;
    	}
    	return result;
    }
    
    
    private boolean isResourceUri(String uri) {
    	return (uri.contains("/css/") || uri.contains("/js/") || uri.contains("/img/"));  
    }
    @Override
    public void destroy() {
    }
    
}
