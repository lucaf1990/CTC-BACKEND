package com.CTC.payload;





import com.CTC.enums.CourtType;
import com.CTC.enums.TypeField;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Getter
@Setter
public class CourtDTO {
	  private TypeField typeField;
	    private Long capacity;  
	    private double price;
	    private CourtType court;
private 	    Long courtId;
}
