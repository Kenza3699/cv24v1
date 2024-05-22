package fr.univrouen.cv24.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.univrouen.cv24.entities.Langue;

import java.util.List;

@Repository
public interface LangueRepository extends JpaRepository<Langue, Long> {
    List<Langue> findByIdentiteId(Long identiteId);

    void deleteByIdentiteId(Long identiteId);
}
