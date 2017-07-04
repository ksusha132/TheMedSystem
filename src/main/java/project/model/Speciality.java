package project.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Entity
@Table(name = "speciality")
public class Speciality implements Serializable {
    private static final long serialVersionUID = 1L;

    public Speciality() {

    }


    public Speciality(Long id_spec, String name) {
        this.id_spec = id_spec;
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @PrimaryKeyJoinColumn(name = "id_spec")
    private Long id_spec;

    @Column(name = "name", nullable = false, insertable = true, updatable = true, length = 30)
    private String name;

    @OneToMany(targetEntity = Doctor.class, mappedBy = "speciality", fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Collection<Doctor> doctorList;

    public Collection<Doctor> getDoctorList() {
        return doctorList;
    }

    public void setDoctorList(Collection<Doctor> doctorList) {
        this.doctorList = doctorList;
    }


    public Long getId_spec() {
        return id_spec;
    }

    public void setId_spec(Long id_spec) {
        this.id_spec = id_spec;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
