package project.repository;

import org.springframework.data.repository.CrudRepository;

import project.model.ContactType;

public interface ContactTypeRepository extends CrudRepository <ContactType, Integer>{

}
