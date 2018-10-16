package com.gyh.filter;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Configuration
@Order(1)
@WebFilter(filterName = "urlfilter", urlPatterns = "/*")
public class HeaderFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		String accessToken = request.getParameter("access_token");
		if (accessToken == null || accessToken.trim().length() == 0) {
			accessToken = request.getParameter("accessToken");
		}
		HttpServletRequest req = (HttpServletRequest) request;
		MutableHttpServletRequest mreq = new MutableHttpServletRequest(req);
		mreq.putHeader("Authorization", "Bearer " + accessToken);

		chain.doFilter(mreq, response);
	}

	@Override
	public void destroy() {

	}

}
