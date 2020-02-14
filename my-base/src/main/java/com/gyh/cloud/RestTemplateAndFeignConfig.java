package com.gyh.cloud;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author guoyanhong
 * @date 2018/10/17 17:29
 */
@Configuration
public class RestTemplateAndFeignConfig {

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        // 设置拦截器
        restTemplate.getInterceptors().add(new RestTemplateIntercepter());
        List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
        // 设置 MappingJackson2HttpMessageConverter 的支持类型
        HttpMessageConverter httpMessageConverter =
                converters.stream().filter(c -> (c instanceof MappingJackson2HttpMessageConverter))
                        .findAny().orElse(new MappingJackson2HttpMessageConverter());
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = ((MappingJackson2HttpMessageConverter) httpMessageConverter);
        List<MediaType> mediaTypes = mappingJackson2HttpMessageConverter.getSupportedMediaTypes();
        List<MediaType> newNediaTypes = new ArrayList<>();
        newNediaTypes.addAll(mediaTypes);
        newNediaTypes.add(MediaType.TEXT_PLAIN);
        // 加入text/html类型的支持
        newNediaTypes.add(MediaType.TEXT_HTML);
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(newNediaTypes);
        return restTemplate;
    }

    /**
     * 使用其他类型的负载均衡策略，默认为轮询：RoundRobinRule
     * RoundRobinRule ：轮询
     * RandomRule : 随机
     * WeightedResponseTimeRule : 根据平均响应时间计算所有服务的权重，响应时间越快服务权重越大被选中的概率越高。刚启动时如果统计信息不足，则使用RoundRobinRule策略，等统计信息足够，会切换到WeightedResponseTimeRule
     * RetryRule ：先按照RoundRobinRule的策略获取服务，如果获取服务失败则在指定时间内会进行重试，获取可用的服务
     * BestAvailableRule ：会先过滤掉由于多次访问故障而处于断路器跳闸状态的服务，然后选择一个并发量最小的服务
     * AvailabilityFilteringRule : 会先过滤掉由于多次访问故障而处于断路器跳闸状态的服务，还有并发的连接数量超过阈值的服务，然后对剩余的服务列表按照轮询策略进行访问
     * ZoneAvoidanceRule ：复合判断server所在区域的性能和server的可用性选择服务器
     * 自定义规则：解析源码：https://github.com/Netflix/ribbon/blob/master/ribbon-loadbalancer/src/main/java/com/netflix/loadbalancer/RandomRule.java
     *
     * @return
     */
    @Bean
    public IRule iRule() {
        return new RandomRule();
    }

    /**
     * feign 拦截器
     *
     * @return
     */
    @Bean
    public FeignRequestInterceptor feignRequestInterceptor() {
        return new FeignRequestInterceptor();
    }
}
