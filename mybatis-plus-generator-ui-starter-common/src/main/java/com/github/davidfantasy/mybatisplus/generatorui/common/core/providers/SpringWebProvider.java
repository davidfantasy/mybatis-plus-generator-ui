package com.github.davidfantasy.mybatisplus.generatorui.common.core.providers;

import com.github.davidfantasy.mybatisplus.generatorui.common.core.configuration.SpringDocConfigProperties;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;
import java.util.Set;

/**
 * The type Spring web provider.
 *
 * @author bnasslahsen
 */
public abstract class SpringWebProvider implements ApplicationContextAware {

    /**
     * The Application context.
     */
    protected ApplicationContext applicationContext;

    /**
     * The Handler methods.
     */
    protected Map handlerMethods;

    /**
     * Gets handler methods.
     *
     * @return the handler methods
     */
    public abstract Map getHandlerMethods();

    /**
     * Find path prefix string.
     *
     * @param springDocConfigProperties the spring doc config properties
     * @return the string
     */
    public abstract String findPathPrefix(SpringDocConfigProperties springDocConfigProperties);

    /**
     * Gets active patterns.
     *
     * @param requestMappingInfo the request mapping info
     * @return the active patterns
     */
    public abstract Set<String> getActivePatterns(Object requestMappingInfo);

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
