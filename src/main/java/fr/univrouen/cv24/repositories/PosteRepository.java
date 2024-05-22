package fr.univrouen.cv24.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.univrouen.cv24.entities.Poste;

import java.util.Optional;

@Repository
public interface PosteRepository extends JpaRepository<Poste, Long> {
    Optional<Poste> findByIdentiteId(Long identiteId);

    void deleteByIdentiteId(Long identiteId);
}
