package test.org.gf;

import org.beetl.core.Resource;
import org.beetl.core.resource.FileResourceLoader;

public class Test {

    public static void main(String[] args) {
        String templateStoreDir = "C:\\Users\\david\\.mybatis-plus-generator-ui\\com.github.davidfantasy.mybatisplustools.example\\template";
        FileResourceLoader fileResourceLoader = new FileResourceLoader(templateStoreDir);
        Resource r = fileResourceLoader.getResource("test.xml.20200526111249");
        System.out.println(r.toString());
    }

}
