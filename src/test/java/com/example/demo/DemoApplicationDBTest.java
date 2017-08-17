package com.example.demo;

import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.domain.DocumentMetaData;
import com.example.demo.repositories.DocumentMetaDataRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
@SpringBootTest
public class DemoApplicationDBTest {

	@Autowired
    private DocumentMetaDataRepository docRepository;
		
	@Test
	public void contextLoads() {
	}
	
	
	@Test
    public void testfindByAuthor() {

		String author = "Test Author";
		String description = "Test Description";
		
		DocumentMetaData model = new DocumentMetaData();
    	model.setDescription(description);
    	model.setAuthor(author);
    	model.setCreateDate(new Date());
    	docRepository.save(model);
    	DocumentMetaData dbResult =  docRepository.findByAuthor(author);
    	
        assertTrue(dbResult.getAuthor().equals(model.getAuthor()));
    }

}
