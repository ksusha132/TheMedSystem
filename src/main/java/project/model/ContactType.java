package project.model;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "ContactType")
public class ContactType implements Serializable {

    private static final long serialVersionUID = 1L;

    ContactType() {

    }

    ContactType(Integer id_contactType, String type) {
        this.id_contactType = id_contactType;
        this.type = type;
    }

    @Id
    @Column(name = "id_contactType", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_contactType;

    public Integer getId_contactType() {
        return id_contactType;
    }

    public void setId_contactType(Integer id_contactType) {
        this.id_contactType = id_contactType;
    }

    @Column(name = "type")
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
