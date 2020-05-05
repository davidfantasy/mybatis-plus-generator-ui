package ${package.Service};

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${superServiceImplClassPackage};
import org.springframework.stereotype.Service;

/**
 * <p>
 * ${table.comment!} 服务实现类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Service
public class ${table.entityName}Service extends ${superServiceImplClass}<${table.mapperName}, ${entity}> {

}
