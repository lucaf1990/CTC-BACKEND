package com.CTC.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginDto {
	  private String userName; // You can keep this field if you still want to support username login
	    private String email;    // Add this field for email login
	    private String password;
}

//Il client dovrà inviare un oggetto JSON nel body con questa forma
/*{
    "username": "francescaneri",
    "password": "qwerty"
}*/
