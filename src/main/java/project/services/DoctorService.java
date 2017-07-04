package project.services;


import project.model.Doctor;

import java.util.List;


public interface DoctorService {
    Doctor findById(Long id_doc);
    List<Doctor> findBySecondName(String secondName);
    List<Doctor> findBySpeciality(Long id_spec);
    void delete(Long id_doc);
    String editDoctor(Doctor doctor);
    String createDoctor(Doctor doctor);
    List<Doctor> getAllDoctors();
}
