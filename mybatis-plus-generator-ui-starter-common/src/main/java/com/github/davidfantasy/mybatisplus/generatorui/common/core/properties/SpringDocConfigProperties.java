/*
 *
 *  *
 *  *  *
 *  *  *  * Copyright 2019-2022 the original author or authors.
 *  *  *  *
 *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  * You may obtain a copy of the License at
 *  *  *  *
 *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *
 *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  * See the License for the specific language governing permissions and
 *  *  *  * limitations under the License.
 *  *  *
 *  *
 *
 */

package com.github.davidfantasy.mybatisplus.generatorui.common.core.properties;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.github.davidfantasy.mybatisplus.generatorui.common.util.Constants;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Lazy;

/**
 * 关于生成器的配置文件
 *
 * @author bnasslahsen
 */
@Data
@Lazy(false)
@ConfigurationProperties(prefix = Constants.MYBATISPLUS_GENETATORUI_PREFIX)
@ConditionalOnProperty(name = Constants.MYBATISPLUS_GENETATORUI_ENABLED, matchIfMissing = true)
public class SpringDocConfigProperties {
    /**
     * 服务启动的端口号
     */
    private Integer port = 8068;

    /**
     * 生成的文件所保存的包路径
     */
    private String basePackage = "generatorui.mydefault";

    /**
     * 数据库地址
     */
    private String jdbcUrl;

    /**
     * 数据库schema,POSTGRE_SQL,ORACLE,DB2，MSSQL类型的数据库需要指定
     */
    private String schemaName;

    /**
     * 数据库用户名
     */
    private String userName;

    /**
     * 数据库密码
     */
    private String password;

    /**
     * 数据库驱动名
     */
    private String driverClassName = "com.mysql.cj.jdbc.Driver";

    /**
     * 数据库时间类型与java class的对应策略
     */
    private DateType dateType;

    /**
     * 表前缀，如果设置后会在生成entity名称时去掉该后缀
     */
    private String tablePrefix;

    /**
     * 全局指定数据表中ID的生成模式，影响自动生成的Entity中ID字段的注解
     */
    private IdType idType;

}
