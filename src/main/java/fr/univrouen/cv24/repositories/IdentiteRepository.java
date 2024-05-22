package fr.univrouen.cv24.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.univrouen.cv24.entities.Identite;

@Repository
public interface IdentiteRepository extends JpaRepository<Identite, Long> {
}
