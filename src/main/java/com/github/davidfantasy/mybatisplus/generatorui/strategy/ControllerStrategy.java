package com.github.davidfantasy.mybatisplus.generatorui.strategy;

import lombok.Data;

@Data
public class ControllerStrategy {

    /**
     * 自定义继承的Controller类全称，带包名
     */
    private String superControllerClass;

    /**
     * 生成 <code>@RestController</code> 控制器
     * <pre>
     *      <code>@Controller</code> -> <code>@RestController</code>
     * </pre>
     */
    private boolean restControllerStyle = false;

    /**
     * 驼峰转连字符
     * <pre>
     *      <code>@RequestMapping("/managerUserActionHistory")</code> -> <code>@RequestMapping("/manager-user-action-history")</code>
     * </pre>
     */
    private boolean controllerMappingHyphenStyle = false;

}
