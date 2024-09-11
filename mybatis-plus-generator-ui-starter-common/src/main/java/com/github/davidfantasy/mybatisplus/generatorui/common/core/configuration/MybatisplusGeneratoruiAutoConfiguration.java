package com.github.davidfantasy.mybatisplus.generatorui.common.core.configuration;

import com.baomidou.mybatisplus.generator.config.ITypeConvert;
import com.github.davidfantasy.mybatisplus.generatorui.common.core.properties.GeneratorConfig;
import com.github.davidfantasy.mybatisplus.generatorui.common.core.properties.SpringDocConfigProperties;
import com.github.davidfantasy.mybatisplus.generatorui.common.util.Constants;
import com.github.davidfantasy.mybatisplus.generatorui.common.mbp.NameConverter;
import com.github.davidfantasy.mybatisplus.generatorui.common.mbp.TemplateVaribleInjecter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

@Slf4j
@Lazy(false)
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = Constants.MYBATISPLUS_GENETATORUI_ENABLED, matchIfMissing = true)
@ConditionalOnWebApplication
@Import({ApplicationConfigure.class})
@EnableConfigurationProperties({SpringDocConfigProperties.class})
public class MybatisplusGeneratoruiAutoConfiguration {
    /**
     * Local spring doc parameter name discoverer local variable table parameter name discoverer.
     *
     * @return the local variable table parameter name discoverer
     */
    @Bean
    @Lazy(false)
    LocalVariableTableParameterNameDiscoverer localSpringDocParameterNameDiscoverer() {
        return new LocalVariableTableParameterNameDiscoverer();
    }


    /**
     * 注入项目配置
     *
     * @return 项目配置的对象
     */
    @Bean
    public GeneratorConfig generatorConfig(@Autowired SpringDocConfigProperties properties,
                                           ObjectProvider<NameConverter> nameConverterObjectProvider,
                                           ObjectProvider<ITypeConvert> typeConvertObjectProvider,
                                           ObjectProvider<TemplateVaribleInjecter> templateVaribleInjecterObjectProvider

    ) {
        return GeneratorConfig.builder()
                .jdbcUrl(properties.getJdbcUrl())
                .userName(properties.getUserName())
                .password(properties.getPassword())
                .port(properties.getPort())
                .driverClassName(properties.getDriverClassName())
                .basePackage(properties.getBasePackage())
                .schemaName(properties.getSchemaName())
                //数据库表前缀，生成entity名称时会去掉
                .tablePrefix(properties.getTablePrefix())
                .idType(properties.getIdType())
                .dateType(properties.getDateType())
                .nameConverter(nameConverterObjectProvider.getIfAvailable())
                .typeConvert(typeConvertObjectProvider.getIfAvailable())
                .templateVaribleInjecter(templateVaribleInjecterObjectProvider.getIfAvailable())
                .build();
    }
}
