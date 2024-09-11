package com.github.davidfantasy.mybatisplus.generatorui;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Objects;

/**
 * 打印扫描到的接口
 *
 * @author llq
 * @Description ApplicationListener实现类
 */
// @Component
public class ControllerPrinter implements ApplicationListener<ApplicationEvent> {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        System.out.println("-----------------------");
        Arrays.stream(applicationContext.getBeanDefinitionNames())
                .filter(name -> Objects.requireNonNull(applicationContext.getType(name))
                        .isAnnotationPresent(RestController.class))
                .forEach(controllerName -> {
                    Class<?> controllerClass = applicationContext.getType(controllerName);
                    System.out.println("Controller: " + controllerClass.getName());

                    Arrays.stream(controllerClass.getDeclaredMethods())
                            .filter(method ->
                                    null != AnnotationUtils.findAnnotation(method, RequestMapping.class)
                            )
                            .forEach(method -> {
                                RequestMapping annotation = AnnotatedElementUtils.getMergedAnnotation(method, RequestMapping.class);
                                System.out.printf("\tMethod: %s%n\t\tRequestMapping: %s%n",
                                        method.getName(),
                                        StrUtil.join(",", AnnotationUtils.getValue(annotation, "value")));
                            });
                });
    }
}
