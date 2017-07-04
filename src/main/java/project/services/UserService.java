package project.services;

import java.util.List;



import project.dto.UserDTO;
import project.model.User;

public interface UserService {

	String validateVerificationToken(String token);
	User getUserByToken(String token);
	void createVerificationToken(User user, String token);
	UserDTO getUserDTO(Long id);
	UserDTO getUserDTO(String login);
	List<UserDTO> getAllUsers();
	List<String> updateUser(Long id, String name, String last_name, Integer age, Integer gender, Integer id_role, String address,
							String phoneNum, String policeNum,String patronymic);
}
