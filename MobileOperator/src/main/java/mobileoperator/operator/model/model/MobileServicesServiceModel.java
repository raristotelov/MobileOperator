package mobileoperator.operator.model.model;

import mobileoperator.operator.model.entity.ServiceCategory;

import java.math.BigDecimal;

public class MobileServicesServiceModel extends BaseServiceModel{
    private String name;
    private ServiceCategory category;
    private String description;
    private BigDecimal price;

    public MobileServicesServiceModel() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ServiceCategory getCategory() {
        return category;
    }

    public void setCategory(ServiceCategory category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
