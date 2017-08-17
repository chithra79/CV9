package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.example.demo.domain.DocumentMetaData;
import com.example.demo.repositories.DocumentMetaDataRepository;

/*
 * Controller that accepts HTML file upload. The uploaded file has 3 meta fields - author, description, createDate
 * The uploaded file is saved to the destination folder.
 * Also meta fields extracted from HTML file and saved to H2 DB table DOC_META_DATA 
 */

@Controller
public class HTMLFileUploadController {
	
    private static String SRC_FOLDER =  "C://chithra//interview//GIT//source//";
    private static String DEST_FOLDER =  "C://chithra//interview//GIT//destination//";
    
    private static final Logger logger = LoggerFactory.getLogger(HTMLFileUploadController.class);
    
    private DocumentMetaDataRepository docRepository; //Extends CrudRepository
    
    @Autowired
    public void setProductRepository(DocumentMetaDataRepository docRepository) {
        this.docRepository = docRepository;
    }

    @GetMapping("/")
    public String index() {
    	logger.debug("Calling GET Method / ");
        return "uploadHtml";
    }

    @PostMapping("/upload") 
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

    	
    	if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:uploadStatus";
        }
    	logger.debug("Calling POST Method /upload to process the HTML file");
        try {

            byte[] bytes = file.getBytes();
            
            //Save html file to destination folder 
            
            Path path = Paths.get( DEST_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);

            File convFile = new File( SRC_FOLDER + file.getOriginalFilename());
            file.transferTo(convFile);
            
            //Parse html file to extract meta tags and save the meta fields to H2 DB.
            
            Document doc = Jsoup.parse(convFile, "UTF-8", "");
            String description = doc.select("meta[name=description]").first().attr("content");  
                       
            String author = doc.select("meta[name=author]").first().attr("content");             
            
            String createDateString = doc.select("meta[name=createDate]").first().attr("content");
            
            logger.debug("Meta data fields - " + "Author =" + author + ", Description =" + description + ", CreateDate =" + createDateString);
            
            try {
            	DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy"); 
            	Date createDate = (Date)formatter.parse(createDateString); 
                      
            	DocumentMetaData model = new DocumentMetaData();
            	model.setDescription(description);
            	model.setAuthor(author);
            	model.setCreateDate(createDate);
            	docRepository.save(model); //save to H2 DB
            	
            } catch (ParseException e) {        	
                logger.error("Error writing to DB table" + e);
            }           
            
            //Redirect to Success page.
            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + file.getOriginalFilename() + "'");

        } catch (IOException e) {
            logger.error("Error IOException" + e);
        }

        return "redirect:/uploadStatus";
    }

    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }

}