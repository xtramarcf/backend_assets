package de.fortmeier.asset_management.iam;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for accessing user data from the database.
 */
@Repository
public interface IamRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUserName(String userName);
}
