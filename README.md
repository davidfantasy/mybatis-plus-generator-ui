## mybatis-plus-generator-ui
 提供交互式的Web UI用于生成兼容mybatis-plus框架的相关功能代码，包括Entity,Mapper,Mappmer.xml,Service,Controller等
 ，可以自定义模板以及各类输出参数。
 
## 使用方法

1.  引入maven的相关依赖，注意scope只需要写test就可以了

```text-xml
 <dependency>
    <groupId>com.github.davidfantasy</groupId>
    <artifactId>mybatis-plus-generator-ui</artifactId>
    <version>1.0.3</version>
    <scope>test</scope>
 </dependency>
```

2.  在项目的test目录新建一个启动类，代码示例如下：

```source-java
public class GeberatorUIServer {

    public static void main(String[] args) {
        GeneratorConfig config = GeneratorConfig.builder().jdbcUrl("jdbc:mysql://192.168.1.211:3306/example")
                .userName("root")
                .password("root")
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .basePackage("com.github.davidfantasy.mybatisplustools.example")
                .port(8068)
                .build();
        MybatisPlusToolsApplication.run(config);
    }

}
```

**GeneratorConfig**包含一些基本的配置参数以及各个可扩展的接口，比如文件的定制或者自定义模板参数，这些都不太好通过UI进行操作。

3.  运行该启动类，启动一个Generator Server。然后访问 [http://localhost:8068](http://localhost:8068/) （端口是可配置的）即可进入到管理界面。
## 主要功能
1. **数据表的查询和浏览**：可以直接浏览和查询配置的数据源的数据表信息，可选择一个或多个生成模板代码：

![数据表查询](https://gitee.com/davidfantasy/mybatis-plus-generator-ui/raw/master/imgs/table-list.png)

2. **输出文件的配置**：内置Entity,Mapper,Service,Controller等6种类型代码的模板配置，可以上传模板进行替换，并修改各类参数，配置参数已经按照影响的文件类型重新进行了分类，并加入了部分文本说明；也可以自行添加其它类型的自定义输出文件。所有的配置项都会按照项目包名进行保存，只需一次性设置就可以了。

![输出文件类型配置](https://gitee.com/davidfantasy/mybatis-plus-generator-ui/raw/master/imgs/output-config.png)

![文件输出选项配置](https://gitee.com/davidfantasy/mybatis-plus-generator-ui/raw/master/imgs/strategy.png)

3. **代码生成选项**：将每次生成代码时可能变动的内容加入到代码生成选项中，方便调整每次的生成策略，比如：是否覆盖原文件，生成文件的种类等等：

![代码生成选项](https://gitee.com/davidfantasy/mybatis-plus-generator-ui/raw/master/imgs/generator-options.png)
