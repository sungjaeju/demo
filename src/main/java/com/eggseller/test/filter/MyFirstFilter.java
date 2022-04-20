package com.eggseller.test.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

//@Component
@Slf4j
public class MyFirstFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		log.info("###################### init filter");
		Filter.super.init(filterConfig);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		log.info("##################### doFilter - 1");
		chain.doFilter(request, response);
		log.info("##################### doFilter - 2");
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		log.info("##################### destory Filter");
		Filter.super.destroy();
	}

}
