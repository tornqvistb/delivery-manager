package se.lanteam.config;

import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import se.lanteam.filter.SessionFilter;

@Configuration
public class WebConfig {

	@Bean
	public FilterRegistrationBean greetingFilterRegistrationBean() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setName("sessionfilter");
		SessionFilter sessionFilter = new SessionFilter();
		registrationBean.setFilter(sessionFilter);
		registrationBean.setOrder(1);
		return registrationBean;
	}

	/*
	 * Way to implement second filter
	@Bean
	public FilterRegistrationBean helloFilterRegistrationBean() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setName("hello");
		HelloFilter helloFilter = new HelloFilter();
		registrationBean.setFilter(helloFilter);
		registrationBean.setOrder(2);
		return registrationBean;
	}
	*/
}
