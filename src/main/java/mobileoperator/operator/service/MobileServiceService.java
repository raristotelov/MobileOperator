package mobileoperator.operator.service;

import mobileoperator.operator.model.entity.MobileService;
import mobileoperator.operator.model.model.MobileServicesServiceModel;

import java.util.List;

public interface MobileServiceService {
    MobileServicesServiceModel save(MobileServicesServiceModel mobileServicesServiceModel);

    List<MobileServicesServiceModel> findAllServices();

    List<MobileServicesServiceModel> getInactiveServices(String id);

    MobileService findById(String id);

    void delete(String id);
}
