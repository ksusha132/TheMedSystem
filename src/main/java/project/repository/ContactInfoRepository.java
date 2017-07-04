package project.repository;

import org.springframework.data.repository.CrudRepository;

import project.model.ContactInfo;

public interface ContactInfoRepository extends CrudRepository<ContactInfo, Integer>{

}
