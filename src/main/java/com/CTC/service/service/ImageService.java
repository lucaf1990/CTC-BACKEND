package com.CTC.service.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.CTC.entity.Booking;
import com.CTC.entity.Image;
import com.CTC.entity.Payment;
import com.CTC.entity.Receipt;
import com.CTC.entity.Reviews;
import com.CTC.entity.Role;
import com.CTC.entity.User;
import com.CTC.repository.repository.ImageRepository;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
@Getter
@Setter
@Builder
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