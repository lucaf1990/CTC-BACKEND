package com.CTC.service.service;

import java.util.EnumSet;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.CTC.entity.Court;
import com.CTC.enums.CourtType;
import com.CTC.enums.TypeField;
import com.CTC.exception.InvalidCourtDataException;
import com.CTC.exception.MyAPIException;
import com.CTC.payload.CourtDTO;
import com.CTC.repository.repository.CourtRepository;



@Service
public class CourtServiceImplementation {

	



	    @Autowired
	    private CourtRepository courtRepository;

	    public String changeCourtHoursPrice(Long id, Double price) {
	    	Optional<Court> court2= courtRepository.findById(id);
	    	if(court2.isPresent()) {
	    		court2.get().setPrice(price);
	    		
	    		//ismEMBER O NO
	    		
	    		
	    		
	    		
	    		courtRepository.save(court2);
	    	} else {
	    		return "Il campo non esiste";
	    	}
	    	return "Prezzi aggiornati correttamente";
	    	
	    }
	    public Court createCourt(CourtDTO courtPayload) {
	        // Validate typeField
	        TypeField typeField = courtPayload.getTypeField();
	        if (typeField == null || !EnumSet.of(TypeField.CALCETTO, TypeField.TENNIS, TypeField.BEACHVOLLEY).contains(typeField)) {
	            throw new InvalidCourtDataException("Invalid typeField value. Use CALCETTO, TENNIS, or BEACHVOLLEY.");
	        }

	        // Validate courtType
	        CourtType courtType = courtPayload.getCourt();
	        if (courtType == null || !EnumSet.of(CourtType.INDOOR, CourtType.OUTDOOR).contains(courtType)) {
	            throw new InvalidCourtDataException("Invalid courtType value. Use INDOOR or OUTDOOR.");
	        }

	        // Create and save the court
	        Court court = new Court();
	        court.setTypeField(typeField);
	        court.setPrice(courtPayload.getPrice());
	        court.setPriceSocio(courtPayload.getPriceSocio());
	        court.setCourtType(courtType);
	        court.setCapacity(courtPayload.getCapacity());

	        courtRepository.save(court);

	        return court;
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
