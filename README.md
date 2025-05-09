## 注意：
该项目目前已经进入维护期，不会再增加新的特性。推荐使用 [resource2code](https://github.com/davidfantasy/resource2code) 项目。该项目通过 AI 模型辅助生成代码，并支持自定义各类代码生成规则，灵活引用数据库等外部资源，帮助开发者高效完成代码构建任务。

## mybatis-plus-generator-ui

提供交互式的Web UI用于生成兼容mybatis-plus框架的相关功能代码，包括Entity,Mapper,Mapper.xml,Service,Controller等
，可以自定义模板以及各类输出参数，也可通过SQL查询语句直接生成代码。

## 使用方法

1. 引入maven的相关依赖，注意scope只需要写test就可以了

```xml

<dependency>
    <groupId>com.github.davidfantasy</groupId>
    <artifactId>mybatis-plus-generator-ui</artifactId>
    <version>2.0.5</version>
    <scope>test</scope>
</dependency>
```

2. 在项目的test目录新建一个启动类，代码示例如下：

```java
public class GeneratorUIServer {

    public static void main(String[] args) {
        GeneratorConfig config = GeneratorConfig.builder().jdbcUrl("jdbc:mysql://192.168.1.211:3306/example")
                .userName("root")
                .password("root")
                .driverClassName("com.mysql.cj.jdbc.Driver")
                //数据库schema，MSSQL,PGSQL,ORACLE,DB2类型的数据库需要指定
                .schemaName("myBusiness")
                //数据库表前缀，生成entity名称时会去掉(v2.0.3新增)
                .tablePrefix("t_")
                //如果需要修改entity及其属性的命名规则，以及自定义各类生成文件的命名规则，可自定义一个NameConverter实例，覆盖相应的名称转换方法，详细可查看该接口的说明：                
                .nameConverter(new NameConverter() {
                    /**
                     * 自定义Service类文件的名称规则，entityName是NameConverter.entityNameConvert处理表名后的返回结果，如有特别的需求可以自定义实现
                     */
                    @Override
                    public String serviceNameConvert(String entityName) {
                        return entityName + "Service";
                    }

                    /**
                     * 自定义Controller类文件的名称规则
                     */
                    @Override
                    public String controllerNameConvert(String entityName) {
                        return entityName + "Action";
                    }
                })
                //所有生成的java文件的父包名，后续也可单独在界面上设置
                .basePackage("com.github.davidfantasy.mybatisplustools.example")
                .port(8068)
                .build();
        MybatisPlusToolsApplication.run(config);
    }

}
```
3. 运行该启动类，启动一个Generator Server。然后访问[http://localhost:8068](http://localhost:8068/)（端口是可配置的）即可进入到管理界面。

**GeneratorConfig**还包含一些基本的配置参数以及各个可扩展的接口，比如自定义模板参数，数据库日期类型与Java类型的映射关系等，具体的说明可查看源码注释。

**重要提示**：如果需要自定义entity,servie这些生成文件的类名，只需要自己实现NameConverter中对应的方法即可，可参考com.github.davidfantasy.mybatisplus.generatorui.mbp.NameConverter中默认方法的实现！！

#### 重要更新

**1.4.0** 版本之后，可支持将GeberatorUIServer独立部署为一个单独的spring boot项目，通过页面指定目标项目根目录的方式为多个项目提供源码生成服务。

**2.0.0** 版本已经支持最新的mybatis-plus 3.5.X，才用重构后新的MBP作为代码生成引擎。

## 主要功能

1. **数据表的查询和浏览**：可以直接浏览和查询配置的数据源的数据表信息，可选择一个或多个生成模板代码：

![数据表查询](https://gitee.com/davidfantasy/mybatis-plus-generator-ui/raw/master/imgs/table-list.png)

2. **输出文件的配置**
   ：内置Entity,Mapper,Service,Controller等6种类型代码的模板配置，可以上传模板进行替换，并修改各类参数，配置参数已经按照影响的文件类型重新进行了分类，并加入了部分文本说明；也可以自行添加其它类型的自定义输出文件。所有的配置项都会按照项目包名进行保存，只需一次性设置就可以了。

![输出文件类型配置](https://gitee.com/davidfantasy/mybatis-plus-generator-ui/raw/master/imgs/output-config.png)

![文件输出选项配置](https://gitee.com/davidfantasy/mybatis-plus-generator-ui/raw/master/imgs/strategy.png)

3. **代码生成选项**：将每次生成代码时可能变动的内容加入到代码生成选项中，方便调整每次的生成策略，比如：是否覆盖原文件，生成文件的种类等等：

![代码生成选项](https://gitee.com/davidfantasy/mybatis-plus-generator-ui/raw/master/imgs/generator-options.png)

4. **SQL结果集自动生成代码**：通过输入查询SQL，可自动在Mapper（Xml及Java）中生成对应的查询方法，DTO对象和ResultMap（结果集映射配置）

![SQL编辑](https://gitee.com/davidfantasy/mybatis-plus-generator-ui/raw/master/imgs/SQL-edit.png)

![SQL代码生成选项](https://gitee.com/davidfantasy/mybatis-plus-generator-ui/raw/master/imgs/SQL-generator-options.png)

5. **动态SQL增强**：自动识别含有mybatis动态参数的where条件，转换为mybatis的动态SQL条件

![动态SQL增强](https://gitee.com/davidfantasy/mybatis-plus-generator-ui/raw/master/imgs/dynamicsql.png)

## 常见问题

**Q:下载源码中怎么直接运行？**

**不建议直接下载源码运行**
，该项目是设计为直接嵌入到对应的业务项目中使用，可以自动识别项目路径等环境变量。如需采用源码运行，需要另行编译src\frontend中的静态资源（源码中不包含已编译的页面），在src\frontend文件夹中执行：

~~~shell
yarn install
yarn run build
~~~

然后修改src\test\java\TestApplication中数据库的相关配置，并运行。

**Q:支持哪些类型的数据库？**

支持几乎所有主流的数据库，具体可参考mybatis-plus-generator框架的文档。需要自行引入数据库的driver包，并在
GeneratorConfig中指定driverClassName。

**Q:怎么自定义模板参数？**

在GeneratorConfig中自定义TemplateVaribleInjecter，返回需要在模板中使用的参数，例如：

```java
 GeneratorConfig config=GeneratorConfig.builder()
        .templateVaribleInjecter(new TemplateVaribleInjecter(){
@Override
public Map<String, Object> getCustomTemplateVaribles(TableInfo tableInfo){
        Map<String, Object> params=new HashMap<>();
        return params;
        }
        })
```

**Q:保存的配置是存储到什么地方的？**

所有的用户保存的配置是按照basePackage分组保存到user.home目录的.mybatis-plus-generator-ui中的，不同项目的配置不会互相影响。

**Q:启动报错问题排查**

大部分的启动问题都是由于依赖冲突导致的，因为mybatis-plus-generator-ui也依赖于springboot和mybatis-plus，请先检查依赖包版本是否相差过大；1.X的版本仅支持最高mp的3.4.X版本，在高版本的springboot(大于2.4)上启动也会有问题，2.0.0版本已经解决了相关的兼容性问题；

欢迎大家关注我的公众号（飞空之羽的技术手札），我会在上面定期分享一些关于技术的经验和感悟~

![二维码](https://github.com/davidfantasy/mybatis-plus-generator-ui/blob/master/imgs/wechat.jpg)