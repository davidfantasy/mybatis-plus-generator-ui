import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.google.common.base.CaseFormat;
import com.google.common.collect.Maps;
import com.github.davidfantasy.mybatisplus.generatorui.core.ProjectConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static com.github.davidfantasy.mybatisplus.generatorui.core.ProjectConstant.MODEL_NAME;

// 演示例子，执行 main 方法控制台输入模块表名回车自动生成对应项目目录中
public class CodeGenerator {

    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner;
        scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotBlank(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        String sourcePath = projectPath + "/" + MODEL_NAME + "/src/main/java/";
        String resourcesPath = projectPath + "/" + MODEL_NAME + "/src/main/resources/";
        gc.setOutputDir(sourcePath);
        gc.setAuthor("david");
        gc.setOpen(false);
        // gc.setSwagger2(true); 实体属性 Swagger2 注解
        mpg.setGlobalConfig(gc);
        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://192.168.1.211:3306/monitor?useUnicode=true&useSSL=false&characterEncoding=utf8");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("root");
        mpg.setDataSource(dsc);
        //生成代码的不同输出包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(ProjectConstant.BASE_PACKAGE);
        pc.setController(ProjectConstant.CONTROLLER_PACKAGE);
        pc.setEntity(ProjectConstant.MODEL_PACKAGE);
        pc.setMapper(ProjectConstant.MAPPER_PACKAGE);
        mpg.setPackageInfo(pc);
        //自定义额外的模板参数
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                String entityName = this.getConfig().getTableInfoList().get(0).getEntityName();
                String urlMapping = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, entityName);
                urlMapping = urlMapping.replace("_", "/");
                Map<String, Object> map = Maps.newHashMap();
                map.put("entityUrlMapping", urlMapping);
                map.put("basePackage", ProjectConstant.BASE_PACKAGE);
                this.setMap(map);
            }
        };
        // 自定义输出service文件，默认的配置无法修改名称
        String serviceTemplatePath = "/templates/service.java.ftl";
        String servicePackagePath = (ProjectConstant.BASE_PACKAGE + "." + ProjectConstant.SERVICE_PACKAGE).replace(".", "/");
        List<FileOutConfig> focList = new ArrayList<>();
        focList.add(new FileOutConfig(serviceTemplatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return sourcePath + "/" + servicePackagePath
                        + "/" + tableInfo.getEntityName() + "Service" + StringPool.DOT_JAVA;
            }
        });
        //自定义输出mapper xml文件,生成到resources目录
        focList.add(new FileOutConfig("/templates/mapper.xml.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return resourcesPath + "/mapper/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        cfg.setFileOutConfigList(focList);

        mpg.setCfg(cfg);
        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        //配置自定义输出模板
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        //templateConfig.setController("/templates/controller.java");
        //service,xml改为自定义生成，不使用默认的配置
        templateConfig.setServiceImpl(null);
        templateConfig.setService(null);
        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);
        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        // 公共父类
        // strategy.setSuperControllerClass("你自己的父类控制器,没有就不用设置!");
        // 写于父类中的公共字段
        //strategy.setSuperEntityColumns("id");
        strategy.setInclude(scanner("请输入数据库表名").split(","));
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix("rtm_");
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }

}