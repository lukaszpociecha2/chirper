package pl.coderslab.chirper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.coderslab.chirper.entity.UserRole;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    UserRole findByName(String name);
}
