import com.github.davidfantasy.mybatisplus.generatorui.GeneratorConfig;
import com.github.davidfantasy.mybatisplus.generatorui.MybatisPlusToolsApplication;
import com.github.davidfantasy.mybatisplus.generatorui.mbp.NameConverter;

public class TestApplication {


    public static void main(String[] args) {
        GeneratorConfig config = GeneratorConfig.builder().jdbcUrl("jdbc:mysql://localhost:3306/autoapi")
                .userName("root")
                .password("root")
                .port(8068)
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
        MybatisPlusToolsApplication.run(config);
    }


}
