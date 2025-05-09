package test.com.github.davidfantasy.mybatisplus.generatorui;

import com.github.davidfantasy.mybatisplus.generatorui.common.MybatisPlusToolsApplication;
import com.github.davidfantasy.mybatisplus.generatorui.common.core.properties.GeneratorConfig;
import com.github.davidfantasy.mybatisplus.generatorui.common.mbp.NameConverter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.github.davidfantasy.mybatisplus.generatorui")
public class TestApplication {

    public static void main(String[] args) {
        GeneratorConfig config = GeneratorConfig.builder().jdbcUrl("jdbc:mysql://192.168.0.101:32888/test?allowMultiQueries=true&useUnicode=true&useSSL=false&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&autoReconnect=true&nullCatalogMeansCurrent=true&&allowPublicKeyRetrieval=true")
                .userName("root")
                .password("admin")
                .port(18068)
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .basePackage("com.github.davidfantasy.mybatisplus.generatorui.example")
                //数据库表前缀，生成entity名称时会去掉
                .tablePrefix("t_")
                .nameConverter(new NameConverter() {
                    @Override
                    public String serviceNameConvert(String entityName) {
                        return entityName + "Service";
                    }

                    @Override
                    public String controllerNameConvert(String entityName) {
                        return entityName + "Action";
                    }
                })
                .build();
        MybatisPlusToolsApplication.run(TestApplication.class, args, config);
    }


}
