package project.dto;

import project.model.Appointment;
import project.model.Comment;
import project.model.User;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class UserDTO {

    public UserDTO(User user) {
        this.id_user = user.getId();
        this.name = user.getName();
        this.lastName = user.getLastName();
        this.gender = user.getGender();
        this.email = user.getEmail();
        this.photo = user.getPhoto();
        this.login = user.getLogin();
        this.password = user.getPassword();
        this.age = user.getAge();
        this.roleId = user.getRole().getId_role();
        this.roleName = user.getRole().getName();
        //
        this.address = user.getAddress();
        this.phoneNum = user.getPhoneNum();
        this.policeNum = user.getPoliceNum();
        this.patronymic = user.getPatronymic();

        //

    }

    public UserDTO(String name, String lastName, Integer gender) {
        this.name = name;
        this.lastName = lastName;
        this.gender = gender;
    }

    private Long id_user;


    public Long getId() {
        return id_user;
    }

    public void setId(Long id_user) {
        this.id_user = id_user;
    }

    private String photo;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String lastName;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    private Integer gender;

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    private Integer age;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }


    private String login;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private List<ContactInfoDTO> contactInfoList = new LinkedList<>();

    public List<ContactInfoDTO> getContactInfoList() {
        return contactInfoList;
    }

    public void setContactInfoList(List<ContactInfoDTO> contactInfoList) {
        this.contactInfoList = contactInfoList;
    }

    private List<Comment> commentList;

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    private Integer roleId;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    private String roleName;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    //
    private String address;

    private String patronymic;

    private String policeNum;

    private String phoneNum;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getPoliceNum() {
        return policeNum;
    }

    public void setPoliceNum(String policeNum) {
        this.policeNum = policeNum;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    //

    public static Comparator<UserDTO> UserDTOComparatorByLastName = new Comparator<UserDTO>() {
        @Override
        public int compare(UserDTO lhs, UserDTO rhs) {
            String name1 = lhs.getLastName().toUpperCase();
            String name2 = rhs.getLastName().toUpperCase();
            return name1.compareTo(name2);
        }
    };
    public static Comparator<UserDTO> UserDTOComparatorByAge = new Comparator<UserDTO>() {
        @Override
        public int compare(UserDTO lhs, UserDTO rhs) {
            return lhs.getAge() - rhs.getAge();
        }

    };
    public static Comparator<UserDTO> UserDTOComparatorByGender = new Comparator<UserDTO>() {
        @Override
        public int compare(UserDTO lhs, UserDTO rhs) {
            return lhs.getGender() - rhs.getGender();
        }
    };
}
