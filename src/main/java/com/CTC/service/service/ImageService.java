package com.CTC.service.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.CTC.entity.Image;
import com.CTC.repository.repository.ImageRepository;

@Service
public class ImageService {
	@Autowired
	ImageRepository imageRepo;

	public Image saveImage(MultipartFile file) {

		try {
			Image image = new Image(file.getOriginalFilename(), file.getContentType(), file.getBytes());
			Image iSaved = imageRepo.save(image);
			iSaved.setUrl("http://localhost:8080/api/images/db/" + image.getId_img());
			imageRepo.save(iSaved);

			return imageRepo.save(iSaved);
		} catch (Exception e) {

			return new Image();
		}

	}
}