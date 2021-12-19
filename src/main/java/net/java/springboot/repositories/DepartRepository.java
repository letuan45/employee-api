package net.java.springboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.java.springboot.model.Depart;

@Repository
public interface DepartRepository extends JpaRepository<Depart, Long> {

}
