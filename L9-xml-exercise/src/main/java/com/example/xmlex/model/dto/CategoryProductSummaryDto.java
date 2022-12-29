package com.example.xmlex.model.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "categories")
@XmlAccessorType(XmlAccessType.FIELD)
public class CategoryProductSummaryDto {

    @XmlElement(name = "category")
    private List<CategoryByNameDto> category;

    public CategoryProductSummaryDto() {

    }

    public List<CategoryByNameDto> getCategory() {
        return category;
    }

    public void setCategory(List<CategoryByNameDto> category) {
        this.category = category;
    }
}
