package com.example.exemplo.Model.Repository;

import com.example.exemplo.Model.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CargoRepository extends JpaRepository<Cargo, Long> {
    Optional<Cargo> findByName(String name);
}
