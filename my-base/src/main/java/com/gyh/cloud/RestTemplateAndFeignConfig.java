package com.gyh.cloud;

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
    public RestTemplate restTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        // 设置拦截器
        restTemplate.getInterceptors().add(new RestTemplateIntercepter());
        List<HttpMessageConverter<?>>  converters = restTemplate.getMessageConverters();
        // 设置 MappingJackson2HttpMessageConverter 的支持类型
        HttpMessageConverter httpMessageConverter =
                converters.stream().filter(c->(c instanceof MappingJackson2HttpMessageConverter))
                        .findAny().orElse(new MappingJackson2HttpMessageConverter());
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = ((MappingJackson2HttpMessageConverter)httpMessageConverter);
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
     * feign 拦截器
     *
     * @return
     */
    @Bean
    public FeignRequestInterceptor feignRequestInterceptor() {
        return new FeignRequestInterceptor();
    }
}
