package project.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import project.model.Speciality;

public interface SpecilityRepository extends JpaRepository<Speciality, Long> {
}
