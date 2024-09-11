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

package com.github.davidfantasy.mybatisplus.generatorui.common.core.utils;


import cn.hutool.core.util.StrUtil;

/**
 * The type Spring doc utils.
 *
 * @author bnasslahsen
 */
public class SpringDocUtils {

    /**
     * The constant springDocConfig.
     */
    private static final SpringDocUtils springDocConfig = new SpringDocUtils();


    /**
     * Is valid path boolean.
     *
     * @param path the path
     * @return the boolean
     */
    public static boolean isValidPath(String path) {
        if (StrUtil.isNotBlank(path) && !path.equals("/")) {
            return true;
        }
        return false;
    }


}

