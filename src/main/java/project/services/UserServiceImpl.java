package project.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.dto.ContactInfoDTO;
import project.dto.UserDTO;
import project.model.ContactInfo;
import project.model.User;
import project.model.VerificationToken;
import project.repository.RoleRepository;
import project.repository.UserRepository;
import project.repository.VerificationTokenRepository;
import project.util.SecurityUtil;

@Service
public class UserServiceImpl implements UserService {

	public static final String TOKEN_INVALID = "invalid";
    public static final String TOKEN_EXPIRED = "expired";
    public static final String TOKEN_VALID = "valid";
    
    @Autowired
    private SessionFactory sessionFactory;


    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private VerificationTokenRepository tokenRepository;
    
    @Autowired
   	private RoleRepository roleRepository;
    
	@Override
	public String validateVerificationToken(String token) {
		
		final VerificationToken verificationToken = tokenRepository.findByToken(token);
        
		if (verificationToken == null) {
            return TOKEN_INVALID;
        }

        final User user = verificationToken.getUser();
        
        if (verificationToken.getExpiryDate().before(new Date())) {
        	tokenRepository.delete(verificationToken);
            return TOKEN_EXPIRED;
        }       

        user.setEnabled(true);
        
        // tokenRepository.delete(verificationToken);
        
        userRepository.saveAndFlush(user);
        
        return TOKEN_VALID;
	}

	@Override
	public User getUserByToken(final String verificationToken) {
		VerificationToken token = tokenRepository.findByToken(verificationToken);
		if (token == null) return null;
		return token.getUser();
	}

	@Override
	public void createVerificationToken(User user, String token) {
		VerificationToken vtoken = tokenRepository.findByUser(user);
		if ( vtoken != null) {
			vtoken.setToken(token);
			vtoken.setExpiryDate(DateUtils.addMinutes(new Date(), VerificationToken.TOKEN_EXPIRATION_MIN));
		} else {
			vtoken = new VerificationToken(token, user);
		}
        tokenRepository.saveAndFlush(vtoken);
	}



	@Override
	public UserDTO getUserDTO(Long id) {
		Session session = getSessionFactory().getCurrentSession();
        String s = "from User where id_user = " + id;
        Query query = session.createQuery(s);
        Object result = query.uniqueResult();
        User user = (User) result;
 
        project.dto.UserDTO userDTO = new project.dto.UserDTO(user);
        for (ContactInfo ci : user.getContactInfoList()) {
           ContactInfoDTO ciDTO = new ContactInfoDTO(ci);
           userDTO.getContactInfoList().add(ciDTO);
        }
        
        return userDTO;
	}

	@Override
	public UserDTO getUserDTO(String login) {
		Session session = getSessionFactory().getCurrentSession();
        User getuser = userRepository.findByLogin(login);
        Long id_user = getuser.getId();
        String s = "from User where id_user = " + id_user;
        Query query = session.createQuery(s);
        Object result = query.uniqueResult();
        User user = (User) result;

        project.dto.UserDTO userDTO = new project.dto.UserDTO(user);
        for (ContactInfo ci : user.getContactInfoList()) {
            ContactInfoDTO ciDTO = new ContactInfoDTO(ci);
            userDTO.getContactInfoList().add(ciDTO);
        }
        return userDTO;
	}


	@Override
	public List<String> updateUser(Long id, String name, String last_name, Integer age,
			Integer gender, Integer id_role, String address, String phoneNum, String policeNum, String patronymic) {
		
		User user = userRepository.findOne(id);

		List<String> messages = new ArrayList<>();

		if (!user.getName().equals(name)) {
			user.setName(name);
			messages.add("Name has been successfully updated");
		}

		if (!user.getLastName().equals(last_name)) {
			user.setLastName(last_name);
			messages.add("Lastname has been successfully updated");
		}
		if (user.getAge() != age) {
			user.setAge(age);
			messages.add("Age has been successfully updated");
		}
		if (user.getGender() != gender) {
			user.setGender(gender);
			messages.add("Gender has been successfully updated");
		}

		if (user.getAddress() != address) {
			user.setAddress(address);
			messages.add("Address has been successfully updated");
		}

		if (user.getPhoneNum() != phoneNum) {
			user.setPhoneNum(phoneNum);
			messages.add("Phone number has been successfully updated");
		}

		if (user.getPoliceNum() != policeNum) {
			user.setPoliceNum(policeNum);
			messages.add("Police number number has been successfully updated");
		}

		if (user.getPatronymic() != patronymic) {
			user.setPatronymic(patronymic);
			messages.add("Patronymic number number has been successfully updated");
		}

		if (id_role != null && id_role != user.getRole().getId_role()) {
			SecurityUtil.expireUserSessions(user.getLogin());
			user.setRole(roleRepository.findOne(id_role));
			messages.add("Role has been successfully updated");
		}
		userRepository.save(user);

		return messages;
	}

	@Override
	public List<UserDTO> getAllUsers() {
		Session session = getSessionFactory().getCurrentSession();
        String s = "from User";
        Query query = session.createQuery(s);
        List<User> list = query.list();

        List<UserDTO> usersDTO = new ArrayList<>();
        for (User u : list) {
            UserDTO userDTO = new UserDTO(u);
            for (ContactInfo ci : u.getContactInfoList()) {
                ContactInfoDTO ciDTO = new ContactInfoDTO(ci);
                userDTO.getContactInfoList().add(ciDTO);
            }
            usersDTO.add(userDTO);
        }
        
        Collections.sort(usersDTO, UserDTO.UserDTOComparatorByLastName);
        return usersDTO;
	}
	
}
