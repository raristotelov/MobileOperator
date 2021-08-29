package mobileoperator.operator.model.binding;

import mobileoperator.operator.model.entity.ServiceCategory;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ServiceAddBindingModel {
    private String name;
    private ServiceCategory category;
    private String description;
    private BigDecimal price;

    public ServiceAddBindingModel() {

    }

    @Length(min = 3, max = 25, message = "Length must be between 3 and 25 symbols!")
    @NotNull
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull(message = "Enter valid category!")
    public ServiceCategory getCategory() {
        return category;
    }

    public void setCategory(ServiceCategory category) {
        this.category = category;
    }

    @Length(min = 3, max = 100, message = "Description must be between 3 and 100 symbols!")
    @NotNull
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @DecimalMin(value = "0", message = "Price must be more than 0!")
    @NotNull
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
