package fr.univrouen.cv24.repositories;

import fr.univrouen.cv24.entities.Diplome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiplomeRepository extends JpaRepository<Diplome, Long> {
    List<Diplome> findByIdentiteId(Long identiteId);

    void deleteByIdentiteId(Long identiteId);
}
