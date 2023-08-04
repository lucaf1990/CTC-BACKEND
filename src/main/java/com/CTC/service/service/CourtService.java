package com.CTC.service.service;

import com.CTC.entity.Court;
import com.CTC.payload.CourtDTO;

public interface CourtService {

	
	public Court createCourt(CourtDTO courtPayload);
	public Court updateCourt(Long courtId, CourtDTO courtPayload);
	public void deleteCourt(Long courtId);
	
}
