package project.dto;

import project.model.ContactInfo;



public class ContactInfoDTO {


    ContactInfoDTO() {

    }


    public ContactInfoDTO(ContactInfo ci) {
        this.id_contactInfo = ci.getId_contactInfo();
        this.value = ci.getValue();
        this.contactTypeId = ci.getContactType().getId_contactType();
        this.contactTypeName = ci.getContactType().getType();
        this.id_contactInfo = id_contactInfo;
    }

    private Integer id_contactInfo;

    public Integer getId_contactInfo() {
        return id_contactInfo;
    }

    public void setId_contactInfo(Integer id_contactInfo) {
        this.id_contactInfo = id_contactInfo;
    }

    private Integer contactTypeId;

    public Integer getContactTypeId() {
        return contactTypeId;
    }

    public void setContactTypeId(Integer contactTypeId) {
        this.contactTypeId = contactTypeId;
    }


    private String contactTypeName;

    public String getContactTypeName() {
        return contactTypeName;
    }

    public void setContactTypeName(String contactTypeName) {
        this.contactTypeName = contactTypeName;
    }

    private UserDTO user;

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

