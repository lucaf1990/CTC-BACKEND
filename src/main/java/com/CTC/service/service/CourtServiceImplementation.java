package com.CTC.service.service;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.CTC.entity.Booking;
import com.CTC.entity.Court;
import com.CTC.entity.Payment;
import com.CTC.entity.Receipt;
import com.CTC.entity.Reviews;
import com.CTC.entity.Role;
import com.CTC.entity.User;
import com.CTC.enums.CourtType;
import com.CTC.enums.TypeField;
import com.CTC.exception.InvalidCourtDataException;
import com.CTC.exception.MyAPIException;
import com.CTC.payload.CourtDTO;
import com.CTC.repository.repository.CourtRepository;
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
public class CourtServiceImplementation {

	



	    @Autowired
	    private CourtRepository courtRepository;

	    public String changeCourtHoursPrice(Long id, Double price) {
	    	Optional<Court> court2= courtRepository.findById(id);
	    	if(court2.isPresent()) {
	    		court2.get().setPrice(price);
	    		
	   
	    		
	    		
	    		
	    		
	    		courtRepository.save(court2);
	    	} else {
	    		return "Il campo non esiste";
	    	}
	    	return "Prezzi aggiornati correttamente";
	    	
	    }
	    public Court createCourt(CourtDTO courtPayload) {
	    
	        TypeField typeField = courtPayload.getTypeField();
	        if (typeField == null || !EnumSet.of(TypeField.CALCETTO, TypeField.TENNIS, TypeField.BEACHVOLLEY).contains(typeField)) {
	            throw new InvalidCourtDataException("Invalid typeField value. Use CALCETTO, TENNIS, or BEACHVOLLEY.");
	        }

	     
	        CourtType courtType = courtPayload.getCourtType();
	        if (courtType == null || !EnumSet.of(CourtType.INDOOR, CourtType.OUTDOOR).contains(courtType)) {
	            throw new InvalidCourtDataException("Invalid courtType value. Use INDOOR or OUTDOOR.");
	        }

	 
	        Court court = new Court();
	        court.setTypeField(typeField);
	        court.setPrice(courtPayload.getPrice());
	        court.setPriceSocio(courtPayload.getPriceSocio());
	        court.setCourtType(courtType);
	        court.setCapacity(courtPayload.getCapacity());
court.setIsActive(true);
	        courtRepository.save(court);

	        return court;
	    }
	  
	    public Court courtMaintaince(Long id) {
	   Court c1= courtRepository.findById(id).get();
	   c1.setIsActive(!c1.getIsActive());
	   courtRepository.save(c1);
		return c1;
	    }
	  
	  public Court updateCourt(Long courtId, Court updatedCourt) {
		    if (courtRepository.existsById(courtId)) {
		        Court existingCourt = courtRepository.findById(courtId).orElseThrow();
		       
		        existingCourt.setTypeField(updatedCourt.getTypeField());
		        existingCourt.setPrice(updatedCourt.getPrice());
		        existingCourt.setCourtType(updatedCourt.getCourtType());
		        existingCourt.setCapacity(updatedCourt.getCapacity());
		        existingCourt.setPriceSocio(updatedCourt.getPriceSocio());

		        courtRepository.save(existingCourt);
		        return existingCourt;
		    } else {
		        throw new MyAPIException(HttpStatus.NOT_FOUND, "Field was not found");
		    }
        
    }
	 public void deleteCourt(Long courtId) {

	        Court existingCourt = courtRepository.findById(courtId)
	                .orElseThrow(() -> new IllegalArgumentException("Court not found with ID: " + courtId));

	  
	        courtRepository.delete(existingCourt);
	    }
	public Court getSpecificField(Long id) {
		Optional<Court> c1= courtRepository.findById(id);
				if(c1.isPresent()) {
					return c1.get();
				} else {
					throw new MyAPIException(HttpStatus.NOT_FOUND, "Field was not found ");
				}
	}
	
	public List<Court> getAllField() {
		List<Court> c1= courtRepository.findAll();
				
					return c1;
				
	}
	
	public Court changePrice(Long courtId,double price, double priceSocio) {
		 if (courtRepository.existsById(courtId)) {
		        Court existingCourt = courtRepository.findById(courtId).orElseThrow();
		       
		     
		        existingCourt.setPrice(price);
		        existingCourt.setPriceSocio(priceSocio);
		      

		        courtRepository.save(existingCourt);
		        return existingCourt;
		    } else {
		        throw new MyAPIException(HttpStatus.NOT_FOUND, "Field was not found");
		    }
     
	}
	
}
