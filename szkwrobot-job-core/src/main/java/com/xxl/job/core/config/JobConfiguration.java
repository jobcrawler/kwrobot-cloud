package com.xxl.job.core.config;

import com.alibaba.cloud.nacos.discovery.NacosDiscoveryClient;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import com.xxl.job.core.util.DiscoveryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

/**
 * @author HanKeQi
 * @Description
 * @date 2020/4/13 1:01 PM
 **/
@ComponentScan("com.xxl.job.core.endpoint")
public class JobConfiguration implements InitializingBean {

    private static Logger logger = LoggerFactory.getLogger(DiscoveryUtil.class);

//    private  String logPath;
//
//    private  int logRetentionDays;


    /**
     * init 初始化
     * @return
     */
    @Bean(initMethod = "start", destroyMethod = "destroy")
    public XxlJobSpringExecutor xxlJobExecutor() {
        logger.info(">>>>>>>>>>> xxl-job config init.");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        return xxlJobSpringExecutor;
    }

    /**
     *
     * see com.xxl.job.core.util.RestTemplateClient
     * 负载均衡
     * @return
     */
    @LoadBalanced
    @Bean
    public RestTemplate restTemplateNacosJob() {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectionRequestTimeout(10 * 1000);
        requestFactory.setReadTimeout(10 * 1000);
        return new RestTemplate(requestFactory);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        new DiscoveryUtil(ApplicationContextHolder.getBean(NacosDiscoveryClient.class));
    }
}
