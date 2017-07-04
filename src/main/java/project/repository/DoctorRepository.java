package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import project.model.Doctor;

import java.util.List;
import java.util.Optional;

@Transactional
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

}
