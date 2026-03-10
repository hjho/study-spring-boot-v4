package io.github.hjho.jpa.example.code.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.hjho.jpa.model.entity.AutoKey;

public interface AutoKeyRepository extends JpaRepository<AutoKey, Long> {

}
