package cn.com.fanyev5.baseservice.es.model.index;

import com.google.common.collect.Lists;

import cn.com.fanyev5.baseservice.base.config.xml.es.ESConfigs;

import java.util.List;

/**
 * ES Field集合
 *
 * @author fanqi427@gmail.com
 * @since 2013-12-17
 */
public class ESFieldInfo {

    /**
     * 索引ID节点
     */
    private ESConfigs.FieldElement idElement;

    /**
     * 节点
     */
    private List<ESConfigs.FieldElement> fieldElements;

    public ESFieldInfo(List<ESConfigs.FieldElement> fieldElements) {
        if (fieldElements != null && !fieldElements.isEmpty()) {
            this.fieldElements = Lists.newArrayList();
            for (ESConfigs.FieldElement fieldElement : fieldElements) {
                if (fieldElement.isId()) {
                    idElement = fieldElement;
                    continue;
                }
                this.fieldElements.add(fieldElement);
            }
        }
    }

    public ESConfigs.FieldElement getIdElement() {
        return idElement;
    }

    public void setIdElement(ESConfigs.FieldElement idElement) {
        this.idElement = idElement;
    }

    public List<ESConfigs.FieldElement> getFieldElements() {
        return fieldElements;
    }

    public void setFieldElements(List<ESConfigs.FieldElement> fieldElements) {
        this.fieldElements = fieldElements;
    }
}