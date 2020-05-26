package com.github.davidfantasy.mybatisplus.generatorui.mbp;

/**
 * 自定义各类名称转换的规则
 */
public interface NameConverter {

    /**
     * 自定义表名到实体类名的转换规则
     *
     * @param tableName 表名称
     * @return
     */
    String entityNameConvert(String tableName);

    /**
     * 自定义表字段名到实体类属性名的转换规则
     *
     * @param fieldName 表字段名称
     * @return
     */
    String propertyNameConvert(String fieldName);

    /**
     * 自定义生成文件的文件名
     *
     * @param fileType   在页面上输入的输出文件标识
     * @param entityName 关联的entity名称，由{@link NameConverter#entityNameConvert}生成
     * @return
     */
    String outputFileNameConvert(String fileType, String entityName);


}
