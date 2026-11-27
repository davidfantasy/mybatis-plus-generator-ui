package com.github.davidfantasy.mybatisplus.generatorui;

import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.ITypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.github.davidfantasy.mybatisplus.generatorui.dto.Constant;
import com.github.davidfantasy.mybatisplus.generatorui.dto.GenSetting;
import com.github.davidfantasy.mybatisplus.generatorui.dto.UserConfig;
import com.github.davidfantasy.mybatisplus.generatorui.mbp.MbpGenerator;
import com.github.davidfantasy.mybatisplus.generatorui.service.UserConfigStore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * 使用 H2 内存数据库验证代码生成主链路，避免依赖外部数据库。
 */
class GeneratorIntegrationTest {

    private static final String TABLE_NAME = "t_user_info";

    @TempDir
    Path projectRoot;

    @TempDir
    Path fakeUserHome;

    private String basePackage;

    private String originalUserHome;

    private AnnotationConfigApplicationContext context;

    @BeforeEach
    void setUp() throws IOException {
        basePackage = "com.example.gen" + UUID.randomUUID().toString().replace("-", "");
        originalUserHome = System.getProperty("user.home");
        System.setProperty("user.home", fakeUserHome.toString());
        Files.createDirectories(projectRoot.resolve("src/main/java"));
        Files.createDirectories(projectRoot.resolve("src/main/resources"));
    }

    @AfterEach
    void tearDown() {
        if (context != null) {
            context.close();
        }
        System.setProperty("user.home", originalUserHome);
    }

    @Test
    void generatesAllFileTypesWithConfiguredOptions() throws IOException {
        RecordingTypeConvert typeConvert = new RecordingTypeConvert();
        MbpGenerator generator = createGenerator(typeConvert);
        UserConfigStore store = context.getBean(UserConfigStore.class);

        UserConfig userConfig = store.getDefaultUserConfig();
        userConfig.getEntityStrategy().setEntityColumnConstant(true);
        userConfig.getMapperXmlStrategy().setBaseResultMap(true);
        userConfig.getMapperXmlStrategy().setEnableCache(true);
        store.saveUserConfig(userConfig);

        generate(generator, allFileTypes());

        String packagePath = basePackage.replace('.', '/');
        Path entity = projectRoot.resolve("src/main/java/" + packagePath + "/entity/UserInfo.java");
        Path mapper = projectRoot.resolve("src/main/java/" + packagePath + "/mapper/UserInfoMapper.java");
        Path mapperXml = projectRoot.resolve("src/main/resources/mapper/UserInfoMapper.xml");
        Path service = projectRoot.resolve("src/main/java/" + packagePath + "/service/IUserInfoService.java");
        Path serviceImpl = projectRoot.resolve("src/main/java/" + packagePath + "/service/impl/UserInfoServiceImpl.java");
        Path controller = projectRoot.resolve("src/main/java/" + packagePath + "/controller/UserInfoController.java");

        assertThat(entity).exists();
        assertThat(mapper).exists();
        assertThat(mapperXml).exists();
        assertThat(service).exists();
        assertThat(serviceImpl).exists();
        assertThat(controller).exists();
        assertThat(generatedFileCount()).isEqualTo(6);

        String entitySource = read(entity);
        assertThat(entitySource).contains("@TableName(\"t_user_info\")");
        assertThat(entitySource).contains("private Long id;");
        assertThat(entitySource).contains("private String userName;");
        assertThat(entitySource).contains("private Date createdAt;");
        assertThat(entitySource).contains("public static final String ID = \"id\";");
        assertThat(entitySource).contains("public static final String USER_NAME = \"user_name\";");

        assertThat(typeConvert.calls.get()).isGreaterThanOrEqualTo(3);

        String mapperXmlSource = read(mapperXml);
        assertThat(mapperXmlSource).contains("<cache type=\"org.apache.ibatis.cache.impl.PerpetualCache\"/>");
        assertThatCode(() -> org.apache.ibatis.cache.impl.PerpetualCache.class.getConstructor(String.class))
                .doesNotThrowAnyException();
    }

    @Test
    void generatesOnlySelectedFileTypes() throws IOException {
        MbpGenerator generator = createGenerator(new RecordingTypeConvert());
        UserConfigStore store = context.getBean(UserConfigStore.class);
        store.saveUserConfig(store.getDefaultUserConfig());

        generate(generator, Collections.singletonList(Constant.FILE_TYPE_ENTITY));

        String entityPath = "src/main/java/" + basePackage.replace('.', '/') + "/entity/UserInfo.java";
        assertThat(projectRoot.resolve(entityPath)).exists();
        assertThat(generatedFileCount()).isEqualTo(1);
    }

    @Test
    void usesConfiguredCustomTemplate() throws IOException {
        MbpGenerator generator = createGenerator(new RecordingTypeConvert());
        UserConfigStore store = context.getBean(UserConfigStore.class);

        UserConfig userConfig = store.getDefaultUserConfig();
        userConfig.getEntityInfo().setTemplatePath("classpath:codetpls/custom-entity.java.btl");
        store.saveUserConfig(userConfig);

        generate(generator, Collections.singletonList(Constant.FILE_TYPE_ENTITY));

        String entityPath = "src/main/java/" + basePackage.replace('.', '/') + "/entity/UserInfo.java";
        String entitySource = read(projectRoot.resolve(entityPath));
        assertThat(entitySource).contains("CUSTOM_ENTITY_TEMPLATE_MARKER");
        assertThat(entitySource).contains("public class UserInfo");
    }

    private MbpGenerator createGenerator(ITypeConvert typeConvert) {
        String dbName = "mpg" + UUID.randomUUID().toString().replace("-", "");
        GeneratorConfig config = GeneratorConfig.builder()
                .jdbcUrl("jdbc:h2:mem:" + dbName + ";DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1")
                .userName("sa")
                .password("")
                .driverClassName("org.h2.Driver")
                .basePackage(basePackage)
                .tablePrefix("t_")
                .typeConvert(typeConvert)
                .build();

        context = new AnnotationConfigApplicationContext();
        context.registerBean(GeneratorConfig.class, () -> config);
        context.register(ApplicationConfigure.class, UserConfigStore.class, MbpGenerator.class);
        context.refresh();

        JdbcTemplate jdbcTemplate = context.getBean(JdbcTemplate.class);
        jdbcTemplate.execute("create table " + TABLE_NAME
                + " (id bigint primary key, user_name varchar(64), created_at timestamp)");
        return context.getBean(MbpGenerator.class);
    }

    private void generate(MbpGenerator generator, List<String> fileTypes) {
        GenSetting genSetting = new GenSetting();
        genSetting.setRootPath(projectRoot.toString());
        genSetting.setAuthor("test");
        genSetting.setOverride(true);
        genSetting.setModuleName("");
        genSetting.setChoosedOutputFiles(fileTypes);
        genSetting.setChoosedControllerMethods(Collections.emptyList());
        generator.genCodeBatch(genSetting, Collections.singletonList(TABLE_NAME));
    }

    private List<String> allFileTypes() {
        return Arrays.asList(
                Constant.FILE_TYPE_ENTITY,
                Constant.FILE_TYPE_MAPPER,
                Constant.FILE_TYPE_MAPPER_XML,
                Constant.FILE_TYPE_SERVICE,
                Constant.FILE_TYPE_SERVICEIMPL,
                Constant.FILE_TYPE_CONTROLLER);
    }

    private long generatedFileCount() throws IOException {
        try (Stream<Path> paths = Files.walk(projectRoot)) {
            return paths.filter(Files::isRegularFile).count();
        }
    }

    private String read(Path path) throws IOException {
        return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
    }

    /**
     * 记录调用次数，并按 H2 的字段类型返回稳定的 Java 类型。
     */
    private static class RecordingTypeConvert implements ITypeConvert {

        private final AtomicInteger calls = new AtomicInteger();

        @Override
        public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
            calls.incrementAndGet();
            String type = fieldType == null ? "" : fieldType.toUpperCase();
            if (type.contains("CHAR") || type.contains("TEXT")) {
                return DbColumnType.STRING;
            }
            if (type.contains("BIGINT")) {
                return DbColumnType.LONG;
            }
            if (type.contains("TIMESTAMP")) {
                return DbColumnType.DATE;
            }
            if (type.contains("INT")) {
                return DbColumnType.INTEGER;
            }
            return DbColumnType.STRING;
        }
    }
}
