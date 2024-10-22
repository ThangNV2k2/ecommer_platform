package com.doan.backend.repositoryImplement;

import com.doan.backend.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SizeRepository extends JpaRepository<Size, String> {
    Optional<Size> findByName(String name);
}
