package com.gyh.filter;//package com.jzy.gyh.api.gate.filter;
//
// import com.jzy.gyh.api.gate.core.CoreHeaderInterceptor;
// import com.netflix.zuul.ZuulFilter;
//import com.netflix.zuul.context.RequestContext;
// import org.apache.commons.codec.binary.Base64;
// import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.servlet.http.HttpServletRequest;
//
///**
// * 核心过滤器
// * Created by wf on 2018/1/19.
// */
//public class PreFilter extends ZuulFilter {
//    private static Logger log = LoggerFactory.getLogger(PreFilter.class);
//
//    /**
//     * 过滤器类型
//     *   pre：可以在请求被路由之前调用。
//         routing：在路由请求时候被调用。
//         post：在routing和error过滤器之后被调用。
//         error：处理请求时发生错误时被调用。
//     * @return
//     */
//    @Override
//    public String filterType() {
//        return "pre";
//    }
//
//    /**
//     * 通过int值来定义过滤器的执行顺序，数值越小优先级越高。
//     * @return
//     */
//    @Override
//    public int filterOrder() {
//        return 0;
//    }
//
//    /**
//     * 返回一个boolean类型来判断该过滤器是否要执行。我们可以通过此方法来指定过滤器的有效范围。
//     * @return
//     */
//    @Override
//    public boolean shouldFilter() {
//        return true;
//    }
//
//    /**
//     * 过滤器的具体逻辑。在该函数中，我们可以实现自定义的过滤逻辑，
//     * 来确定是否要拦截当前的请求，不对其进行后续的路由，或是在请求路由返回结果之后，对处理结果做一些加工等。
//     * @return
//     */
//    @Override
//    public Object run() {
//        RequestContext ctx = RequestContext.getCurrentContext();
//        HttpServletRequest request = ctx.getRequest();
//        log.info("send {} request to {}", request.getMethod(), request.getRequestURL().toString());
//         //用户登录授权处理 start ==========================================
//
//
//         //用户登录授权处理 end ============================================
//
//        // 将用户信息传递给后续微服务
//        //用户json串
//        String token = "token-value";
//        CoreHeaderInterceptor.initHystrixRequestContext(token); // zuul本身调用微服务
//        ctx.addZuulRequestHeader(CoreHeaderInterceptor.HEADER_LABEL, token); // 传递给后续微服务
//
//
////        ctx.addZuulRequestHeader("Authorization", "Basic " + getBase64Credentials("hio_oauth", "hio_secret"));
//
//        return null;
//    }
//    private String getBase64Credentials(String username, String password) {
//        String plainCreds = username + ":" + password;
//        byte[] plainCredsBytes = plainCreds.getBytes();
//        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
//        return new String(base64CredsBytes);
//    }
//}
