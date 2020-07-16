package mobileoperator.operator.service;

import mobileoperator.operator.model.model.UserServiceModel;

import java.math.BigDecimal;
import java.util.List;

public interface UserService {
    UserServiceModel register(UserServiceModel userServiceModel);

    UserServiceModel findByUsername(String username);

    List<UserServiceModel> findAllWithSimilarName(String username);

    List<UserServiceModel> findAllUsers();

    UserServiceModel findUser(String id);

    UserServiceModel addServiceToUser(UserServiceModel user, String id);

    void initFirstOperator();

    BigDecimal getTotalPrice(String id);

    void changeRole(String id);

    void pay(String id);

    void checkUsersPaymentDue();
}
