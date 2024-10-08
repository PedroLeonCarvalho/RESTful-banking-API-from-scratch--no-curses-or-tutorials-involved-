package com.banking_api.banking_api.repository;

import com.banking_api.banking_api.domain.user.User;
import com.banking_api.banking_api.dtos.UserDto;
import jakarta.persistence.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserRepository extends   JpaRepository <User, Long> {
@Query("select u from User u where u.active = true")
    List<User> listActiveUsers();

    Page<User> findAllByActiveTrue(Pageable pageable);


    UserDetails findByUsername(String username);
}
