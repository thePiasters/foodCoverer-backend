package com.foodCoverer.repository;


import com.foodCoverer.model.User;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, String> {

    @Override
    List<User> findAll();

    @Override
    Optional<User> findById(String id);

    @Override
    <S extends User> S saveAndFlush(S user);

    @Override
    void delete(User subcategory);
}
