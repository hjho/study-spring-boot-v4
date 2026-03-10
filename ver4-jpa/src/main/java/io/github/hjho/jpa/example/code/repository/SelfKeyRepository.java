package io.github.hjho.jpa.example.code.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.hjho.jpa.model.entity.SelfKey;

public interface SelfKeyRepository extends JpaRepository<SelfKey, String> {

}
