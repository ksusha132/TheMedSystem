package project.model;


import project.dto.UserDTO;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;
import java.util.Comparator;


@Entity
@Table(name = "doctor")
public class Doctor implements Serializable {
    private static final long serialVersionUID = 1L;

    public Doctor() {

    }

    public Doctor(Long id_doc, String name, String secondName, String patronimic, String university, String timeFrom, String timeTo,
                  Speciality speciality, String photo) {
        this.id_doc = id_doc;
        this.name = name;
        this.secondName = secondName;
        this.patronimic = patronimic;
        this.university = university;
        this.timeFrom = Time.valueOf(timeFrom);
        this.timeTo = Time.valueOf(timeTo);
        this.speciality = speciality;
        this.photo = photo;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_doc", nullable = false, insertable = true, updatable = true)
    private Long id_doc;

    @Column(name = "name", nullable = false, insertable = true, updatable = true, length = 60)
    private String name;

    @Column(name = "secondName", nullable = false, insertable = true, updatable = true, length = 60)
    private String secondName;

    @Column(name = "patronimic", nullable = false, insertable = true, updatable = true, length = 60)
    private String patronimic;

    @Column(name = "university", nullable = false, insertable = true, updatable = true, length = 60)
    private String university;


    @Column(name = "timeFrom", nullable = false, insertable = true, updatable = true)
    private Time timeFrom;

    @Column(name = "timeTo", nullable = false, insertable = true, updatable = true)
    private Time timeTo;

    @Column(length = 255)
    private String photo;

    /* @OneToMany(targetEntity = Registration.class, mappedBy = "doctor", fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Collection<Registration> registrationList;
    */

    @ManyToOne
    @JoinColumn(name = "id_spec", nullable = false, updatable = true, insertable = true)
    private Speciality speciality;

    public Speciality getSpeciality() {
        return speciality;
    }

    public void setSpeciality(Speciality speciality) {
        this.speciality = speciality;
    }


    public Long getId_doc() {
        return id_doc;
    }

    public void setId_doc(Long id_doc) {
        this.id_doc = id_doc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getPatronimic() {
        return patronimic;
    }

    public void setPatronimic(String patronimic) {
        this.patronimic = patronimic;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public Time getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(String timeFrom) {
        this.timeFrom = Time.valueOf(timeFrom);
    }

    public Time getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(String timeTo) {
        this.timeTo = Time.valueOf(timeTo);
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public static Comparator<Doctor> DoctorBySecondName = new Comparator<Doctor>() {
        @Override
        public int compare(Doctor lhs, Doctor rhs) {
            String sName1 = lhs.getSecondName().toUpperCase();
            String sName2 = rhs.getSecondName().toUpperCase();
            return sName1.compareTo(sName2);
        }
    };
}
