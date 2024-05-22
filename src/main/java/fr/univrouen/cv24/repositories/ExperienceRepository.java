package fr.univrouen.cv24.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.univrouen.cv24.entities.Experience;

import java.util.List;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience, Long> {

    List<Experience> findByIdentiteId(Long identiteId);

    void deleteByIdentiteId(Long identiteId);
}
