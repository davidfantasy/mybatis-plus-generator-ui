package com.github.davidfantasy.mybatisplus.generatorui.service;

import com.github.davidfantasy.mybatisplus.generatorui.common.ServiceException;
import com.github.davidfantasy.mybatisplus.generatorui.dto.ElementPosition;
import com.github.davidfantasy.mybatisplus.generatorui.dto.MapperElement;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.dom4j.*;
import org.dom4j.dom.DOMComment;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Mapper XML的相关操作接口
 */
@Component
public class MapperXmlParser {

    private SAXReader reader = new SAXReader();

    private OutputFormat format = OutputFormat.createPrettyPrint();


    /**
     * 在Mapper文件中添加代码节点
     *
     * @param mapperPath mapper.xml所在的位置
     * @param elements   需要添加的节点内容
     */
    public String addElementInMapper(String mapperPath, MapperElement... elements) throws IOException, DocumentException {
        File mapperFile = new File(mapperPath);
        if (!mapperFile.exists()) {
            throw new ServiceException("XML文件不存在：" + mapperPath);
        }
        Document doc = reader.read(new FileInputStream(mapperFile));
        List<Node> mapperNodes = doc.getRootElement().content();
        List<Node> newNodes = Lists.newArrayList();
        //从原有节点中剔除与即将添加的节点ID相同的节点
        for (Node node : mapperNodes) {
            if (node instanceof Element) {
                boolean equals = false;
                Element oldEle = (Element) node;
                for (MapperElement newEle : elements) {
                    if (!Strings.isNullOrEmpty(newEle.getId())
                            && newEle.getId().equals(oldEle.attributeValue("id"))) {
                        equals = true;
                        newEle.setExisted(true);
                    }
                }
                if (!equals) {
                    newNodes.add(node);
                }
            } else {
                newNodes.add(node);
            }
        }
        for (MapperElement ele : elements) {
            Element newEle = DocumentHelper.parseText(ele.getContent().trim()).getRootElement();
            if (ele.getLocation().equals(ElementPosition.FIRST)) {
                newNodes.add(0, newEle);
                //仅在首次添加时才添加注释，避免注释重复添加
                if (!ele.isExisted()) {
                    newNodes.add(0, new DOMComment(ele.getComment()));
                }
            } else {
                //仅在首次添加时才添加注释，避免注释重复添加
                if (!ele.isExisted()) {
                    newNodes.add(new DOMComment(ele.getComment()));
                }
                newNodes.add(newEle);
            }
        }
        doc.getRootElement().setContent(newNodes);
        XMLWriter writer = new XMLWriter(new FileWriter(mapperFile), format);
        writer.write(doc);
        writer.flush();
        return doc.getRootElement().attributeValue("namespace");
    }

    private Element getElementById(Document doc, String id) {
        List<Element> elements = doc.getRootElement().elements();
        for (Element ele : elements) {
            if (id.equals(ele.attributeValue("id"))) {
                return ele;
            }
        }
        return null;
    }


}
