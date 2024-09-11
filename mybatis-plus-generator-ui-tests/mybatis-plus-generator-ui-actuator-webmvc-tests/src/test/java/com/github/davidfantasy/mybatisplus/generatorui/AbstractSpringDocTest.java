package com.github.davidfantasy.mybatisplus.generatorui;

import com.github.davidfantasy.mybatisplus.generatorui.common.GeneratorConfig;
import com.github.davidfantasy.mybatisplus.generatorui.common.core.utils.Constants;
import com.github.davidfantasy.mybatisplus.generatorui.common.mbp.NameConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = TestApplication.class)
public abstract class AbstractSpringDocTest extends AbstractCommonTest {

    public static String className;

    /**
     * 必须要这一段初始化代码，
     * 不然jdbc不会配置，
     * 启动不了测试
     */
    @BeforeAll
    public static final void setUp() {
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
        MybatisPlusToolsApplication.run(TestApplication.class, new String[0], config);
    }

    @Test
    public void testApp() throws Exception {
        className = getClass().getSimpleName();
        String testNumber = className.replaceAll("[^0-9]", "");
        MvcResult mockMvcResult = getMvcResult();
        String result = mockMvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        String expected = getContent("results/app" + testNumber + ".json");
        Assertions.assertEquals(expected, result);
    }

    @Override
    protected MvcResult getMvcResult() throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(Constants.DEFAULT_API_DOCS_URL)
                        .contentType(MediaType.APPLICATION_JSON) //指定请求的contentType头信息
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200))).andReturn();
    }
}
