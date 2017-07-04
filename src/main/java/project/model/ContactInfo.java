package project.model;

import com.fasterxml.jackson.annotation.JsonBackReference;


import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "ContactInfo")
public class ContactInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    public ContactInfo() {

    }

    public ContactInfo(String value, User user, ContactType contactType, Integer id_contactInfo) {
        this.value = value;
        this.user = user;
        this.contactType = contactType;
        this.id_contactInfo = id_contactInfo;
    }

    @Id
    @Column(name = "id_contactInfo", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_contactInfo;

    public Integer getId_contactInfo() {
        return id_contactInfo;
    }

    public void setId_contactInfo(Integer id_contactInfo) {
        this.id_contactInfo = id_contactInfo;
    }

    @ManyToOne
    @JoinColumn(name = "id_contactType", nullable = false)
    @JsonBackReference
    private ContactType contactType;

    public ContactType getContactType() {
        return contactType;
    }

    public void setContactType(ContactType contactType) {
        this.contactType = contactType;
    }


    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    @JsonBackReference
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column(name = "value", unique = true, nullable = true, length = 30)
    private String value;

    public java.lang.String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}

