package mobileoperator.operator.service.impl;

import mobileoperator.operator.model.entity.MobileService;
import mobileoperator.operator.model.entity.Role;
import mobileoperator.operator.model.entity.User;
import mobileoperator.operator.model.model.UserServiceModel;
import mobileoperator.operator.repository.UserRepository;
import mobileoperator.operator.service.MobileServiceService;
import mobileoperator.operator.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final MobileServiceService mobileServiceService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, MobileServiceService mobileServiceService, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.mobileServiceService = mobileServiceService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void initFirstOperator() {
        if (this.userRepository.count() == 0) {
            User user = new User();
            user.setUsername("user");
            user.setPassword("123456789");
            user.setFullName("User User");
            user.setEmail("user@first.com");
            user.setRole(Role.OPERATOR);

            this.userRepository.saveAndFlush(user);
        }
    }

    @Override
    public BigDecimal getTotalPrice(String id) {
        User user = this.userRepository.findById(id).orElse(null);

        BigDecimal sum = new BigDecimal("0");

        if (user != null) {
            for (MobileService service : user.getServices()) {
                sum = sum.add(service.getPrice());
            }
        }

        return sum;
    }

    @Override
    public void changeRole(String id) {
        User user = this.userRepository.findById(id).orElse(null);

        if (user != null) {
            user.setRole(Role.OPERATOR);
            user.getServices().clear();
            user.setPaymentDate(null);

            this.userRepository.saveAndFlush(user);
        }

    }

    @Override
    public void pay(String id) {
        User user = this.userRepository.findById(id).orElse(null);

        if (user != null) {
            user.setPaymentDate(user.getPaymentDate().plusMonths(1));
            this.userRepository.saveAndFlush(user);
        }
    }

    @Override
    @Scheduled(fixedRate = 86400000)
    public void checkUsersPaymentDue() {
        List<User> users = this.userRepository.findAll();

        for (User user : users) {
            if(user.getPaymentDate() != null){
                if(user.getPaymentDate().compareTo(LocalDate.now()) < 0){
                    user.setPaymentDate(null);
                    user.getServices().clear();
                    this.userRepository.saveAndFlush(user);
                }
            }
        }
    }

    @Override
    public UserServiceModel register(UserServiceModel userServiceModel) {
        if (this.userRepository.findAll().size() == 0) {
            User user = this.modelMapper.map(userServiceModel, User.class);

            user.setRole(Role.OPERATOR);

            this.userRepository.saveAndFlush(user);
        } else {
            User user = this.modelMapper.map(userServiceModel, User.class);

            user.setRole(Role.USER);

            this.userRepository.saveAndFlush(user);
        }

        return userServiceModel;
    }

    @Override
    public UserServiceModel findByUsername(String username) {
        User user = this.userRepository.findByUsername(username).orElse(null);

        return user == null ? null : this.modelMapper.map(user, UserServiceModel.class);
    }

    @Override
    public List<UserServiceModel> findAllWithSimilarName(String name) {
        List<User> users = this.userRepository.findAllWithSimilarName(name, Role.USER);

        return users.stream().map(user -> this.modelMapper.map(user, UserServiceModel.class)).collect(Collectors.toList());
    }

    @Override
    public List<UserServiceModel> findAllUsers() {
        return this.userRepository.findAllByRole(Role.USER)
                .stream()
                .map(user -> this.modelMapper.map(user, UserServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserServiceModel findUser(String id) {
        User user = this.userRepository.findById(id).orElse(null);
        return user == null ? null : this.modelMapper.map(user, UserServiceModel.class);
    }

    @Override
    public UserServiceModel addServiceToUser(UserServiceModel userServiceModel, String id) {
        User user = this.userRepository.findById(userServiceModel.getId()).orElse(null);

        if (user != null) {
            if (user.getServices().size() == 0) {
                user.setPaymentDate(LocalDate.now().plusMonths(1));
            }

            MobileService mobileService = this.mobileServiceService.findById(id);
            if (mobileService != null) {
                user.getServices().add(mobileService);
            }

            this.userRepository.saveAndFlush(user);
        }

        User updated = this.userRepository.findById(userServiceModel.getId()).orElse(null);

        if (updated != null) {
            return this.modelMapper.map(updated, UserServiceModel.class);
        } else {
            return null;
        }
    }


}
