/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  * Copyright 2019-2022 the original author or authors.
 *  *  *  *  *
 *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *
 *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *
 *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  * limitations under the License.
 *  *  *  *
 *  *  *
 *  *
 *
 */

package com.github.davidfantasy.mybatisplus.generatorui.webmvc.ui;


import com.github.davidfantasy.mybatisplus.generatorui.common.core.utils.SpringDocUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import static com.github.davidfantasy.mybatisplus.generatorui.common.core.utils.Constants.MVC_SERVLET_PATH;
import static com.github.davidfantasy.mybatisplus.generatorui.common.core.utils.Constants.MYBATIS_GENERATOR_UI_PATH;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;
import static org.springframework.web.servlet.view.UrlBasedViewResolver.FORWARD_URL_PREFIX;

/**
 * Home redirection to swagger api documentation
 */
@Controller
public class UiHome {

    @Value(MYBATIS_GENERATOR_UI_PATH)
    private String mybatisGeneratorUiPath;

    @Value(MVC_SERVLET_PATH)
    private String mvcServletPath;

    /**
     * Index string.
     *
     * @return the string
     */
    @GetMapping(DEFAULT_PATH_SEPARATOR)
    public String index() {
        StringBuilder uiRootPath = new StringBuilder();
		if (SpringDocUtils.isValidPath(mvcServletPath)) {
			uiRootPath.append(mvcServletPath);
		}

        return FORWARD_URL_PREFIX + uiRootPath + mybatisGeneratorUiPath;
    }
}