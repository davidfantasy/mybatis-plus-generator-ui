# mybatis-plus-generator-ui
 提供交互式的Web UI用于生成兼容mybatis-plus框架的相关功能代码，包括Entity,Mapper,Mappmer.xml,Service,Controller等
 ，可以自定义模板以及各类输出参数。
 
# 使用方法
1. 引入maven的相关依赖，注意scope只需要写test就可以了
~~~xml
 <dependency>
    <groupId>com.github.davidfantasy</groupId>
    <artifactId>mybatis-plus-generator-ui</artifactId>
    <version>1.0.1</version>
    <scope>test</scope>
 </dependency>
~~~
2. 在项目的test目录新建一个启动类，代码示例如下：
~~~java
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
~~~
**GeneratorConfig**的可配置参数可参考源码

3. 运行该启动类，启动一个Generator Server。然后访问 [http://localhost:8068](http://localhost:8068) 即可进入到管理界面。根据需要可以对模板和生成文件进行设置即可:

![table列表](https://s1.ax1x.com/2020/05/26/tiEUBj.png)

![文件配置](https://s1.ax1x.com/2020/05/26/tiElNt.png)

![策略配置](https://s1.ax1x.com/2020/05/26/tiAxcF.png)