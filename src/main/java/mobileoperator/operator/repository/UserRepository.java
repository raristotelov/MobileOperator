package mobileoperator.operator.repository;

import mobileoperator.operator.model.entity.Role;
import mobileoperator.operator.model.entity.User;
import mobileoperator.operator.model.model.UserServiceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User AS u WHERE u.fullName LIKE concat('%', :name, '%') AND u.role = :role")
    List<User> findAllWithSimilarName(String name, Role role);

    List<User> findAllByRole(Role role);
}
