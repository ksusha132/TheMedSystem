package project.services;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.model.Doctor;
import project.repository.DoctorRepository;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private SessionFactory sessionFactory;


    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Doctor findById(Long id_doc) {

        //return doctorRepository.findOne(id_doc);

        Session session = getSessionFactory().getCurrentSession();
        String s = "from Doctor where id_doc = " + id_doc;
        Query query = session.createQuery(s);
        Object result = query.uniqueResult();
        Doctor doctor = (Doctor) result;
        return doctor;
    }

    @Override
    public List<Doctor> findBySecondName(String secondName) {
       /* Session session;
        try {
            session = sessionFactory.getCurrentSession(); // @Transactional helped
        } catch (HibernateException e) {
            session = sessionFactory.openSession();
        }*/

        Session session = getSessionFactory().getCurrentSession();
        String s = "from Doctor where secondName = " + "'" + secondName + "'"; // HQL называть как в моделе а получить специальность можно join
        Query query = session.createQuery(s);
        List<Doctor> list = query.list();

        return list;
    }

    @Override
    public List<Doctor> findBySpeciality(Long id_spec) {
        Session session = getSessionFactory().getCurrentSession();
        String s = "from Doctor where id_spec = " + id_spec; // HQL называть как в моделе а получить специальность можно join
        Query query = session.createQuery(s);
        List<Doctor> list = query.list();
        Collections.sort(list, Doctor.DoctorBySecondName); // cоритрую по фамилии
        return list;
    }

    @Override
    public void delete(Long id_doc) {
        doctorRepository.delete(id_doc);
    }

    @Override
    public String editDoctor(Doctor doctor) {
        Session session = getSessionFactory().getCurrentSession();
        session.update(doctor);
        return "ok";
    }

    @Override
    public String createDoctor(Doctor doctor) {
        if (doctor.getId_doc() == null) {
            doctorRepository.save(doctor); // службный метод save!
            return "ok";
        } else
            return null;
    }

    @Override
    public List<Doctor> getAllDoctors() {
        Session session = getSessionFactory().getCurrentSession();
        String s = "from Doctor"; // HQL называть как в моделе а получить специальность можно join
        Query query = session.createQuery(s);
        List<Doctor> list = query.list();
        Collections.sort(list, Doctor.DoctorBySecondName); // cортирую по фамилии
        return list;
    }
}
