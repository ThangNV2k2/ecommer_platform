package com.doan.backend.repositoryImplement;

import com.doan.backend.entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ColorRepository extends JpaRepository<Color, String> {
    Optional<Color> findByName(String name);
}
