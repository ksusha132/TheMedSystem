package project.controller;

import javassist.NotFoundException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.model.Doctor;
import project.model.Speciality;
import project.model.User;
import project.repository.DoctorRepository;
import project.repository.SpecilityRepository;
import project.services.DoctorService;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@Validated
@Transactional
public class DoctorController {

    private static final String UPLOADED_FOLDER = new File("").getAbsolutePath()
            .concat("/src/main/webapp/images/doctors/");

    @Autowired
    DoctorService doctorService;

    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    SpecilityRepository specilityRepository;

    @Autowired
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @GetMapping(path = "/doctor/{id_doc}") // через список специальностей выбираем специальность и передаем id
    public String getDoctors(Model model, @PathVariable Long id_doc) {
        Doctor doctor = doctorService.findById(id_doc);
        Speciality speciality = specilityRepository.findOne(doctor.getSpeciality().getId_spec());
        model.addAttribute("doctor", doctor);
        model.addAttribute("speciality", speciality);
        return "doctor";
    }

    @GetMapping(path = "/doctorSN/{secondName}")
    public String getDoctorsBySecondName(Model model, @PathVariable String secondName) throws NotFoundException {
        List<Doctor> doctors = doctorService.findBySecondName(secondName);
        model.addAttribute("doctors", doctors);
        return "doctors";
    }

    @GetMapping(path = "/doctorSP/{id_spec}")
    public String getDoctorsBySpeciality(Model model, @PathVariable Long id_spec) throws NotFoundException {
        List<Doctor> doctors = doctorService.findBySpeciality(id_spec);
        model.addAttribute("doctors", doctors);
        return "doctors";
    }

    @GetMapping(path = "/doctors")
    public String getAllDoctors(Model model) throws NotFoundException {
        List<Doctor> doctors = doctorService.getAllDoctors();
        model.addAttribute("doctors", doctors);
        return "doctors";
    }

    @GetMapping(path = "/doctorDEL/{id_doc}")
    public String deleteDoctor(@PathVariable Long id_doc) throws NotFoundException {
        doctorService.delete(id_doc);
        return  "redirect:/doctors";
    }

    @GetMapping(path = "/updateDoctor/{id_doc}")
    public String updateDoctor(Model model, @PathVariable Long id_doc) {
        model.addAttribute("doctor", doctorService.findById(id_doc));
        model.addAttribute("specialities", specilityRepository.findAll());
        return "updateDoctor";
    }


    @PostMapping(path = "/updateDoctor/{id_doc}")
    public String updateDoctor(@ModelAttribute Doctor doctor, @PathVariable Long id_doc) {
        doctorService.editDoctor(doctor);
        return "redirect:/doctor/" + doctor.getId_doc();
    }

    @GetMapping(path = "/createDoctor")
    public String createDoctorView(Model model) {
        model.addAttribute("doctor", new Doctor()); // название модели
        model.addAttribute("specialities", specilityRepository.findAll());
        return "createDoctor"; // return view
    }

    @PostMapping(path = "/createDoctor")
    public String createDoctor(@ModelAttribute Doctor doctor) {
        doctorService.createDoctor(doctor);
        return "redirect:/doctor/" + doctor.getId_doc();
    }

    @GetMapping(path = "/searchDoctor")
    public String searchDoctor(Model model){
        model.addAttribute("specialities", specilityRepository.findAll() );
        return "findDoctor";
    }

    @PostMapping(path = "/searchDoctorById")
    public String searchDoctorById(@RequestParam Long id_doc){
        return "redirect:/doctor/" + id_doc;
    }

    @PostMapping(path = "/searchDoctorBySecName")
    public String searchDoctorBySecName(@RequestParam String secondName, Model model){
        model.addAttribute("doctors", doctorService.findBySecondName(secondName));
        return "doctors";
    }

    @PostMapping(path = "/searchDoctorByIdSpec")
    public String searchDoctorByIdSpec(@RequestParam Long id_spec, Model model){
        model.addAttribute("doctors", doctorService.findBySpeciality(id_spec));
        return "doctors";
    }

    @PostMapping("/doctor/updatePhoto/{id_doc}")
    public String updatePhoto(@PathVariable Long id_doc, @RequestParam("photo") MultipartFile file,
                              HttpServletRequest request, RedirectAttributes redirectAttributes) {

        Doctor doctor = doctorRepository.findOne(id_doc);
        String photo = "";
        if (file != null && !file.isEmpty()) {
            try {
                // Get the file and save it somewhere
                byte[] bytes = file.getBytes();
                Path path = Paths.get(UPLOADED_FOLDER + id_doc + '.' + file.getOriginalFilename().split("\\.")[1]);
                Files.write(path, bytes);
                photo = id_doc + "." + file.getOriginalFilename().split("\\.")[1];
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            photo = doctor.getPhoto();
        }
        doctor.setPhoto(photo);
        doctorRepository.save(doctor);
        redirectAttributes.addFlashAttribute("messages", "Photo was successfully updated");

        return "redirect:" + request.getHeader("Referer");

    }

    @PostMapping("/doctor/{id_doc}/deletePhoto")
    @ResponseBody
    public String deletePhoto(@PathVariable Long id_doc, HttpServletRequest request) {
        Doctor doctor = doctorRepository.findOne(id_doc);
        doctor.setPhoto(null);
        doctorRepository.save(doctor);
        return "deleted";
    }
}
