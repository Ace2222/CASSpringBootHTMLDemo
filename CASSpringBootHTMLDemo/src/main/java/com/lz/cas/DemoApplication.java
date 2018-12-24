package com.lz.cas;

import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Cas30ProxyReceivingTicketValidationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	// cas url的前缀
	private static final String CAS_SERVER_URL_PREFIX = "https://test.nomalis.com:8443/cas";

	// 本机的名称
	private static final String SERVER_NAME = "http://localhost:8080";

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	/**
	 * 登录过滤器
	 * 
	 * @return
	 */
	@Bean
	public FilterRegistrationBean filterSingleRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new SingleSignOutFilter());
		// 设定匹配的路径
		registration.addUrlPatterns("/*");
		Map<String, String> initParameters = new HashMap<String, String>();
		initParameters.put("casServerUrlPrefix", CAS_SERVER_URL_PREFIX);
		registration.setInitParameters(initParameters);
		// 设定加载的顺序
		registration.setOrder(1);
		return registration;
	}

	/**
	 * 过滤验证器
	 * 
	 * @return
	 */
	@Bean
	public FilterRegistrationBean filterValidationRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new Cas30ProxyReceivingTicketValidationFilter());
		// 设定匹配的路径
		registration.addUrlPatterns("/*");
		Map<String, String> initParameters = new HashMap<String, String>();
		initParameters.put("casServerUrlPrefix", CAS_SERVER_URL_PREFIX);
		initParameters.put("serverName", SERVER_NAME);
		initParameters.put("useSession", "true");
		registration.setInitParameters(initParameters);
		// 设定加载的顺序
		registration.setOrder(1);
		return registration;
	}

	/**
	 * 授权过滤器
	 * 
	 * @return
	 */
	@Bean
	public FilterRegistrationBean filterAuthenticationRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new AuthenticationFilter());
		// 设定匹配的路径
		registration.addUrlPatterns("/*");
		Map<String, String> initParameters = new HashMap<String, String>();
		initParameters.put("casServerLoginUrl", CAS_SERVER_URL_PREFIX);
		initParameters.put("serverName", SERVER_NAME);
		initParameters.put("ignorePattern", "/main|index.html");
		initParameters.put("ignoreUrlPatternType", "com.lz.cas.auth.SimpleUrlPatternMatcherStrategy");

		registration.setInitParameters(initParameters);
		// 设定加载的顺序
		registration.setOrder(1);
		return registration;
	}

	/**
	 * wraper过滤器
	 * 
	 * @return
	 */
	@Bean
	public FilterRegistrationBean filterWrapperRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new HttpServletRequestWrapperFilter());
		// 设定匹配的路径
		registration.addUrlPatterns("/*");
		// 设定加载的顺序
		registration.setOrder(1);
		return registration;
	}

	/**
	 * 添加监听器
	 * 
	 * @return
	 */
	@Bean
	public ServletListenerRegistrationBean<EventListener> singleSignOutListenerRegistration() {
		ServletListenerRegistrationBean<EventListener> registrationBean = new ServletListenerRegistrationBean<EventListener>();
		registrationBean.setListener(new SingleSignOutHttpSessionListener());
		registrationBean.setOrder(1);
		return registrationBean;
	}


}
