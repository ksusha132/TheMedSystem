package project.repository;

import org.springframework.data.repository.CrudRepository;
import project.model.Role;


public interface RoleRepository extends CrudRepository <Role, Integer> {
	
	Role findByName(String name);
}
