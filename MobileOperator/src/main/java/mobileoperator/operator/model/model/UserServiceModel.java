package mobileoperator.operator.model.model;

import mobileoperator.operator.model.entity.BaseEntity;

import java.time.LocalDate;
import java.util.Set;

public class UserServiceModel extends BaseEntity {
    private String username;
    private String email;
    private String fullName;
    private String password;
    private String role;
    private LocalDate paymentDate;
    private Set<MobileServicesServiceModel> services;

    public UserServiceModel() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Set<MobileServicesServiceModel> getServices() {
        return services;
    }

    public void setServices(Set<MobileServicesServiceModel> services) {
        this.services = services;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
