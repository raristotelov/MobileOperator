package mobileoperator.operator.service.impl;

import mobileoperator.operator.model.entity.MobileService;
import mobileoperator.operator.model.entity.User;
import mobileoperator.operator.model.model.MobileServicesServiceModel;
import mobileoperator.operator.repository.MobileServiceRepository;
import mobileoperator.operator.repository.UserRepository;
import mobileoperator.operator.service.MobileServiceService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MobileServiceServiceImpl implements MobileServiceService {
    private final MobileServiceRepository mobileServiceRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public MobileServiceServiceImpl(MobileServiceRepository mobileServiceRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.mobileServiceRepository = mobileServiceRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public MobileServicesServiceModel save(MobileServicesServiceModel mobileServicesServiceModel) {
        MobileService mobileService = this.modelMapper.map(mobileServicesServiceModel, MobileService.class);
        this.mobileServiceRepository.saveAndFlush(this.modelMapper.map(mobileServicesServiceModel, MobileService.class));

        return mobileServicesServiceModel;
    }

    @Override
    public List<MobileServicesServiceModel> findAllServices() {
        return this.mobileServiceRepository.findAll().stream()
                .map(mobileService -> this.modelMapper.map(mobileService, MobileServicesServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<MobileServicesServiceModel> getInactiveServices(String id) {
        User user = this.userRepository.findById(id).orElse(null);
        List<MobileService> mobileServices = this.mobileServiceRepository.findAllByUsersNotContaining(user);

        return mobileServices.stream()
                .map(mobileService -> this.modelMapper.map(mobileService, MobileServicesServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public MobileService findById(String id) {
        return this.mobileServiceRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(String id) {
        this.userRepository.findAll().stream().forEach(user -> {
            user.setServices(user.getServices().stream().filter(mobileService -> !mobileService.getId().equals(id)).collect(Collectors.toSet()));
            this.userRepository.saveAndFlush(user);
        });

        this.mobileServiceRepository.deleteById(id);
    }
}
