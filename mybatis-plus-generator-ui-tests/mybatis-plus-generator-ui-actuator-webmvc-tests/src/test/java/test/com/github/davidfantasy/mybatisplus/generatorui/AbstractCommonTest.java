package test.com.github.davidfantasy.mybatisplus.generatorui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = { "management.endpoints.enabled-by-default=false" })
public abstract class AbstractCommonTest {

	@Autowired
	protected MockMvc mockMvc;

	protected static String getContent(String fileName) {
		try {
			Path path = Paths.get(AbstractCommonTest.class.getClassLoader().getResource(fileName).toURI());
			byte[] fileBytes = Files.readAllBytes(path);
			return new String(fileBytes, StandardCharsets.UTF_8);
		}
		catch (Exception e) {
			throw new RuntimeException("Failed to read file: " + fileName, e);
		}
	}

	protected abstract MvcResult getMvcResult() throws Exception;
}
