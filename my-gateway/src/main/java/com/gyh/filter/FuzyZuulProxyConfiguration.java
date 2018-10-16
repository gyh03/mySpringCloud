package com.gyh.filter;//package com.jzy.gyh.api.gate.filter;
///*
//* Do not forget to remove EnableZuulProxy annotation
//* since it imports default ZuulProxyConfiguration
//*/
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
//import org.springframework.cloud.client.discovery.DiscoveryClient;
//import org.springframework.cloud.netflix.zuul.ZuulProxyConfiguration;
//import org.springframework.cloud.netflix.zuul.filters.Route;
//import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
//import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
//import org.springframework.cloud.netflix.zuul.filters.ZuulProperties.ZuulRoute;
//import org.springframework.cloud.netflix.zuul.filters.discovery.DiscoveryClientRouteLocator;
//import org.springframework.cloud.netflix.zuul.filters.discovery.ServiceRouteMapper;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.util.AntPathMatcher;
//import org.springframework.util.PathMatcher;
//
///**
// * 重写zuul拦截规则
// *
// * @author coffee<br/>
// *         2018年2月5日下午3:27:46
// */
//@Configuration
//@EnableCircuitBreaker
//public class FuzyZuulProxyConfiguration extends ZuulProxyConfiguration {
//
//	@Autowired
//	private DiscoveryClient discovery;
//
//	@Autowired
//	private ServiceRouteMapper serviceRouteMapper;
//
//	@Bean
//	@Primary
//	public RouteLocator routeFuzyLocator() {
//		return new FuzyDiscoveryRouteLocator(super.server.getServletPrefix(), this.discovery, super.zuulProperties, this.serviceRouteMapper);
//	}
//
//	public static class FuzyDiscoveryRouteLocator extends DiscoveryClientRouteLocator {
//
//		private PathMatcher pathMatcher = new AntPathMatcher();
//
//		public FuzyDiscoveryRouteLocator(String servletPath, DiscoveryClient discovery, ZuulProperties properties, ServiceRouteMapper serviceRouteMapper) {
//			super(servletPath, discovery, properties, serviceRouteMapper);
//		}
//
//		@Override
//		public Route getMatchingRoute(String path) {
//			// Route(id=gyh-app-oa-2, fullPath=/platform-app/sys/quick/entry/queryPortalList, path=/entry/queryPortalList,
//			// location=PortalService:/platform-app/oa/sys/quick/, prefix=/platform-app/sys/quick, retryable=null, sensitiveHeaders=[], customSensitiveHeaders=false)
//			Route route = super.getMatchingRoute(path);
//			ZuulRoute zuulRoute = null;
//			for (String pattern : locateRoutes().keySet()) {
//				if (pathMatcher.match(pattern, path)) {
//					// PortalService:/platform-app/oa/sys/quick/
//					// 如果serviceId后面加冒号了 按照新规则组装path
//					if (route.getLocation().startsWith("http") == false && route.getLocation().contains(":")) {
//						zuulRoute = locateRoutes().get(pattern);
//						String serviceId = zuulRoute.getServiceId();
//						String[] sep = serviceId.split(":");
//						route.setPath(sep[1] + route.getPath());
//						route.setLocation(sep[0]);
//						break;
//					}
//				}
//			}
//			return route;
//		}
//	}
//
//}