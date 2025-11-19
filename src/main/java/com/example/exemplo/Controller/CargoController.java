package com.example.exemplo.Controller;

import com.example.exemplo.Model.Cargo;
import com.example.exemplo.Model.Repository.CargoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cargos")
public class CargoController {
    @Autowired
    private CargoRepository cargoRepository;

    /** Lista todos os cargos */
    @GetMapping
    public List<Cargo> list() {
        return cargoRepository.findAll();
    }

    /** Encontra um cargo por Id */
    @GetMapping("/{id}")
    public ResponseEntity<Cargo> read(@PathVariable Long id) {
        Optional<Cargo> cargo = cargoRepository.findById(id);
        return cargo.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /** Cria um novo cargo */
    @PostMapping
    public ResponseEntity<Cargo> create(@RequestBody Cargo cargo) {
        if (cargo.getName() != null) {
            Optional<Cargo> existing = cargoRepository.findByName(cargo.getName());
            if (existing.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        }
        Cargo saved = cargoRepository.save(cargo);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        return ResponseEntity.created(location).body(saved);
    }

    /** Atualiza o cargo com base no id na requisição */
    @PutMapping("/{id}")
    public ResponseEntity<Cargo> update(@PathVariable Long id, @RequestBody Cargo details) {
        Optional<Cargo> opt = cargoRepository.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Cargo cargo = opt.get();

        if (details.getName() != null && !details.getName().equalsIgnoreCase(cargo.getName())) {
            Optional<Cargo> byName = cargoRepository.findByName(details.getName());
            if (byName.isPresent() && !byName.get().getId().equals(id)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            cargo.setName(details.getName());
        }

        Cargo saved = cargoRepository.save(cargo);
        return ResponseEntity.ok(saved);
    }

    /** Deleta o cargo com base no id na requisição */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Cargo> opt = cargoRepository.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        cargoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
