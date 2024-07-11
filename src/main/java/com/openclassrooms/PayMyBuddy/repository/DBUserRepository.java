package com.openclassrooms.PayMyBuddy.repository;

import com.openclassrooms.PayMyBuddy.model.DBUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DBUserRepository extends JpaRepository<DBUser, Long> {

    public DBUser findByUsername(String username);

    public Optional<DBUser> findById(Long id);

    @Query(value = "SELECT u.* FROM user u WHERE u.id IN (SELECT c.connection_id FROM connection c WHERE c.user_id = :userId)", nativeQuery = true)
    List<DBUser> findConnectionsByUserId(@Param("userId") Long userId);

}
