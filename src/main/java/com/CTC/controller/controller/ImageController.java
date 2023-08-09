package com.CTC.controller.controller;

import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.CTC.entity.Image;
import com.CTC.repository.repository.ImageRepository;
import com.CTC.service.service.ImageService;

import org.springframework.core.io.Resource;

@RestController
@RequestMapping("/api/images")
@CrossOrigin(origins = "*", maxAge = 6000000)
public class ImageController {
	 @Autowired
	  private ImageRepository imageRepository;
	 @Autowired
	 private ImageService iSer;
	 @GetMapping("/{id}")
	 
	 public ResponseEntity<String> trovaImmagine(@PathVariable Long id) throws UnsupportedEncodingException {
	     Optional<Image> image = imageRepository.findById(id);
	     if (image.isPresent()) {
	         String encodedUrl = URLEncoder.encode("http://localhost:8080/api/images/immagini/" + id, StandardCharsets.UTF_8.toString());
	         String imageUrl = "http://localhost:8080/api/images/visualizza?imageUrl=" + encodedUrl;
	         return ResponseEntity.ok(imageUrl);
	     } else {
	         return ResponseEntity.notFound().build();
	     }
	 }
	 @GetMapping("/db/{id}")
	    public ResponseEntity<Resource> retrieve(@PathVariable  Long id) {
	        var image = imageRepository.findById(id);
	        var body = new ByteArrayResource(image.get().getData());

	        return ResponseEntity.ok()
	                .header(HttpHeaders.CONTENT_TYPE, image.get().getType())
	                .cacheControl(CacheControl.maxAge(Duration.ofSeconds(60)).cachePrivate().mustRevalidate())
	                .body(body);
	    }

	  @PostMapping("/upload")
	  public ResponseEntity<Image> uploadImage(@RequestParam("file") MultipartFile file) {

		 return new ResponseEntity<Image>(iSer.saveImage(file),HttpStatus.OK);
	  }

}