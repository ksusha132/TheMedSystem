package project.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;
import java.util.Comparator;

@Entity
@Table(name = "Appointment")
public class Appointment implements Serializable {

    public Appointment(Long id_doc, Long id_user, String appointmentTime) {
        this.appointmentTime = Time.valueOf(appointmentTime);
        this.id_upp = id_upp;
    }

    //

    @Id
    @Column(name = "id_uppoint", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_upp;

    @Column(name = "appointmentTime", nullable = false, insertable = true, updatable = true)
    private Time appointmentTime;

    // getters and setters

    public Long getId_upp() {
        return id_upp;
    }

    public void setId_upp(Long id_upp) {
        this.id_upp = id_upp;
    }

    public Time getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = Time.valueOf(appointmentTime);
    }

    // relations

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user", nullable = false, updatable = true, insertable = true)
    @JsonBackReference
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_doc", nullable = false, updatable = true, insertable = true)
    @JsonBackReference
    private Doctor doctor; // throw doctor i can get time and speciality and name doctor


    //


    public static Comparator<Appointment> AppointmentByDoctor = new Comparator<Appointment>() { // comrare speciality
        @Override
        public int compare(Appointment lhs, Appointment rhs) {
            String sName1 = lhs.doctor.getSpeciality().getName().toUpperCase();
            String sName2 = rhs.doctor.getSpeciality().getName().toUpperCase();
            return sName1.compareTo(sName2);
        }
    };
}
