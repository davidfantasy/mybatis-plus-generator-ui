package ${package.Controller};

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import df.authority.jwtshiro.annotation.RequiresPerms;
import ${cfg.basePackage}.core.Result;
import ${cfg.basePackage}.core.ResultGenerator;
import ${package.Entity}.${table.entityName};
import ${package.Service}.${table.entityName}Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

/**
 * <p>
 * ${table.comment!} 前端控制器
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@RestController
@RequestMapping("/api/${cfg.entityUrlMapping}")
@RequiresPerms("${table.entityName}")
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {

    @Autowired
    private ${table.entityName}Service ${table.entityPath}Service;

    @GetMapping("/list")
    @RequiresPerms("read")
    public Result list(QueryWrapper<${table.entityName}> queryWrapper, Page<${table.entityName}> page) {
       Page<${table.entityName}> aPage = ${table.entityPath}Service.page(page, queryWrapper);
       return ResultGenerator.genSuccessResult(aPage);
    }

    @GetMapping("/detail-by-id")
    @RequiresPerms("read")
    public Result detailById(@RequestParam String id) {
       ${table.entityName} ${table.entityPath} = ${table.entityPath}Service.getById(id);
       return ResultGenerator.genSuccessResult(${table.entityPath});
    }

    @GetMapping("/detail-by-query")
    @RequiresPerms("read")
    public Result detailByQuery(QueryWrapper<${table.entityName}> queryWrapper) {
       ${table.entityName} ${table.entityPath} = ${table.entityPath}Service.getOne(queryWrapper);
       return ResultGenerator.genSuccessResult(${table.entityPath});
    }

    @PostMapping("/add")
    @RequiresPerms("write")
    public Result add(@RequestBody ${table.entityName} ${table.entityPath}) {
       ${table.entityPath}Service.save(${table.entityPath});
       return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    @RequiresPerms("write")
    public Result delete(@RequestBody ${table.entityName} ${table.entityPath}) {
       ${table.entityPath}Service.removeById(${table.entityPath}.getId());
       return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    @RequiresPerms("write")
    public Result update(@RequestBody ${table.entityName} ${table.entityPath}) {
       ${table.entityPath}Service.updateById(${table.entityPath});
       return ResultGenerator.genSuccessResult();
    }
}
</#if>
