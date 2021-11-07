package com.foodCoverer.repository;


import com.foodCoverer.model.Allergen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AllergenRepository extends JpaRepository<Allergen, UUID> {

    @Override
    List<Allergen> findAll();

    @Override
    Optional<Allergen> findById(UUID uuid);

    @Override
    <S extends Allergen> S saveAndFlush(S entity);

    @Override
    void delete(Allergen allergen);

}
