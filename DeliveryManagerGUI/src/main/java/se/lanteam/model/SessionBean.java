package se.lanteam.model;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import se.lanteam.domain.CustomerGroup;
import se.lanteam.domain.SystemUser;

@Component
@Scope(value="session", proxyMode=ScopedProxyMode.TARGET_CLASS)
public class SessionBean {

	private CustomerGroup customerGroup;
	private SystemUser systemUser;

	public CustomerGroup getCustomerGroup() {
		return customerGroup;
	}

	public void setCustomerGroup(CustomerGroup customerGroup) {
		this.customerGroup = customerGroup;
	}

	public SystemUser getSystemUser() {
		return systemUser;
	}

	public void setSystemUser(SystemUser systemUser) {
		this.systemUser = systemUser;
	}
	
	
}
