package com.xxl.job.core.executor.impl;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.executor.XxlJobExecutor;
import com.xxl.job.core.glue.GlueFactory;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.handler.impl.MethodJobHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * xxl-job executor (for spring)
 *
 * @author xuxueli 2018-11-01 09:24:52
 */
public class XxlJobSpringExecutor extends XxlJobExecutor implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(XxlJobSpringExecutor.class);

    @Override
    public void start() throws Exception {

        // init JobHandler Repository
        //initJobHandlerRepository(applicationContext);

        // init JobHandler Repository (for method)
        initJobHandlerMethodRepository(applicationContext);

        // refresh GlueFactory
        GlueFactory.refreshInstance(1);


        // super start
        super.start();
    }

//    private void initJobHandlerRepository(ApplicationContext applicationContext){
//        if (applicationContext == null) {
//            return;
//        }
//
//        // init job handler action
//        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(JobHandler.class);
//
//        if (serviceBeanMap!=null && serviceBeanMap.size()>0) {
//            for (Object serviceBean : serviceBeanMap.values()) {
//                if (serviceBean instanceof IJobHandler){
//                    String name = serviceBean.getClass().getAnnotation(JobHandler.class).value();
//                    IJobHandler handler = (IJobHandler) serviceBean;
//                    if (loadJobHandler(name) != null) {
//                        throw new RuntimeException("xxl-job jobhandler naming conflicts.");
//                    }
//                    registJobHandler(name, handler);
//                }
//            }
//        }
//    }


    private void initJobHandlerMethodRepository(ApplicationContext applicationContext) {
        if (applicationContext == null) {
            return;
        }
        // init job handler from method
        String[] beanDefinitionNames = applicationContext.getBeanNamesForType(Object.class, false, true);
        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean = applicationContext.getBean(beanDefinitionName);

            Map<Method, XxlJob> annotatedMethods = null;   // referred to ：org.springframework.context.event.EventListenerMethodProcessor.processBean
            try {
                annotatedMethods = MethodIntrospector.selectMethods(bean.getClass(),
                        new MethodIntrospector.MetadataLookup<XxlJob>() {
                            @Override
                            public XxlJob inspect(Method method) {
                                return AnnotatedElementUtils.findMergedAnnotation(method, XxlJob.class);
                            }
                        });
            } catch (Throwable ex) {
                logger.error("xxl-job method-jobhandler resolve error for bean[" + beanDefinitionName + "].", ex);
            }
            if (annotatedMethods==null || annotatedMethods.isEmpty()) {
                continue;
            }

            for (Map.Entry<Method, XxlJob> methodXxlJobEntry : annotatedMethods.entrySet()) {
                Method executeMethod = methodXxlJobEntry.getKey();
                XxlJob xxlJob = methodXxlJobEntry.getValue();
                // regist
                registJobHandler(xxlJob, bean, executeMethod);
            }
        }
    }

    // ---------------------- applicationContext ----------------------
    private static ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

}
