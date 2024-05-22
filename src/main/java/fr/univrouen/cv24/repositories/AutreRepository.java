package fr.univrouen.cv24.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.univrouen.cv24.entities.Autre;

import java.util.List;

@Repository
public interface AutreRepository extends JpaRepository<Autre, Long> {
    List<Autre> findByIdentiteId(Long identiteId);

    void deleteByIdentiteId(Long identiteId);
}
