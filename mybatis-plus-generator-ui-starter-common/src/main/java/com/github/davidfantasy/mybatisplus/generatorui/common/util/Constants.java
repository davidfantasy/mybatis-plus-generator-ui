package com.github.davidfantasy.mybatisplus.generatorui.common.util;

import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;

/**
 * The type Constants.
 *
 * @author bnasslahsen
 */
public final class Constants {

    /**
     * The constant SPRINGDOC_PREFIX.
     */
    public static final String MYBATISPLUS_GENETATORUI_PREFIX = "mybatisplus-genetatorui";

    /**
     * The constant DEFAULT_API_DOCS_URL.
     * 不能删除，后面有用
     */
    public static final String DEFAULT_API_DOCS_URL = "/v3/api-docs";

    /**
     * The constant API_DOCS_URL.
     */
    public static final String API_DOCS_URL = "${springdoc.api-docs.path:#{T(com.github.davidfantasy.mybatisplus.generatorui.common.util.Constants).DEFAULT_API_DOCS_URL}}";

    /**
     * The constant DOT.
     */
    public static final String DOT = ".";

    /**
     * The constant SPRINGDOC_ENABLED.
     */
    public static final String MYBATISPLUS_GENETATORUI_ENABLED = MYBATISPLUS_GENETATORUI_PREFIX + ".enabled";

    /**
     * The constant NULL.
     */
    public static final String NULL = ":#{null}";

    /**
     * The constant SPRING_MVC_SERVLET_PATH.
     */
    public static final String SPRING_MVC_SERVLET_PATH = "spring.mvc.servlet.path";

    /**
     * The constant MVC_SERVLET_PATH.
     */
    public static final String MVC_SERVLET_PATH = "${" + SPRING_MVC_SERVLET_PATH + NULL + "}";

    /**
     * The constant INDEX_PAGE.
     */
    public static final String INDEX_PAGE = DEFAULT_PATH_SEPARATOR + "index.html";

    /**
     * The constant DEFAULT_MYBATIS_GENERATOR_UI_PATH.
     * 不能删除，后面有用
     */
    public static final String DEFAULT_MYBATISPLUS_GENERATORUI_PATH = INDEX_PAGE;

    /**
     * The constant SWAGGER_UI_PATH.
     */
    public static final String MYBATISPLUS_GENERATORUI_PATH = "${springdoc.swagger-ui.path:#{T(com.github.davidfantasy.mybatisplus.generatorui.common.util.Constants).DEFAULT_MYBATISPLUS_GENERATORUI_PATH}}";

    /**
     * The constant SPRINGDOC_USE_ROOT_PATH.
     */
    public static final String SPRINGDOC_USE_ROOT_PATH = "springdoc.swagger-ui.use-root-path";

    /**
     * Instantiates a new Constants.
     */
    private Constants() {
        super();
    }

}
