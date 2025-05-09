package test.com.github.davidfantasy.mybatisplus.generatorui;

import com.github.davidfantasy.mybatisplus.generatorui.common.util.Constants;
import org.junit.jupiter.api.Assertions;
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
        return mockMvc.perform(MockMvcRequestBuilders.get(Constants.API_DOCS_URL)
                        .contentType(MediaType.APPLICATION_JSON) //指定请求的contentType头信息
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(200))).andReturn();
    }
}
