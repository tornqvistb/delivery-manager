package se.lanteam.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import se.lanteam.constants.PropertyConstants;
import se.lanteam.constants.SessionConstants;
import se.lanteam.domain.CustomerGroup;
import se.lanteam.repository.CustomerGroupRepository;
import se.lanteam.repository.ErrorRepository;
import se.lanteam.repository.PropertyRepository;

public class SessionFilter implements Filter {
	
	@Autowired
	private PropertyRepository propertyRepo;
	@Autowired
	private ErrorRepository errorRepo;
	@Autowired
	private CustomerGroupRepository customerGroupRepo;
	
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    	SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                filterConfig.getServletContext());
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession(); 
        String errors = String.valueOf(errorRepo.findErrorsByArchived(false).size());        
        session.setAttribute(SessionConstants.ERROR_COUNT, errors);
        CustomerGroup customerGroup = (CustomerGroup) session.getAttribute(SessionConstants.CURRENT_CUSTOMER_GROUP);
        if (customerGroup == null) {
            Long defaultGroupId = propertyRepo.findById(PropertyConstants.DEFAULT_COMPANY_GROUP_ID).getNumberValue();
        	session.setAttribute(SessionConstants.CURRENT_CUSTOMER_GROUP, customerGroupRepo.findById(defaultGroupId));
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }
    
}
