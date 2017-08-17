package com.example.demo.repositories;

import org.springframework.data.repository.CrudRepository;
import com.example.demo.domain.DocumentMetaData;
	 
public interface DocumentMetaDataRepository extends CrudRepository<DocumentMetaData, Integer>{

    public DocumentMetaData findByAuthor(String author);
}
