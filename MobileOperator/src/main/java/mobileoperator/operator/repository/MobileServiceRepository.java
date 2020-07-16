package mobileoperator.operator.repository;

import mobileoperator.operator.model.entity.MobileService;
import mobileoperator.operator.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MobileServiceRepository extends JpaRepository<MobileService, String> {

    List<MobileService> findAllByUsersNotContaining(User user);
}
