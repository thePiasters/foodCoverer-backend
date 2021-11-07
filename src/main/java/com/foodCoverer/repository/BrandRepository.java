package com.foodCoverer.repository;

import com.foodCoverer.model.Brand;
import com.foodCoverer.model.User;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public interface BrandRepository extends JpaRepository<Brand, UUID> {

    @Override
    boolean existsById(UUID uuid);

    @Override
    List<Brand> findAll();

    @Override
    Optional<Brand> findById(UUID uuid);

    Optional<Brand> findByName(String name);

    List<Brand> findBrandByUser(User user);

    @Override
    void delete(Brand vendor);

    @Override
    <S extends Brand> S saveAndFlush(S entity);
}

