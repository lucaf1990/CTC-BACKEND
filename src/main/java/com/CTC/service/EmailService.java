package com.CTC.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.CTC.entity.Booking;
import com.CTC.entity.Payment;
import com.CTC.entity.Receipt;
import com.CTC.entity.Reviews;
import com.CTC.entity.Role;
import com.CTC.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.mail.internet.MimeMessage;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
@Getter
@Setter
@Builder
@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendConfirmationEmail(String to, String confirmationLink, String name, String expectedLink) {
        MimeMessage message = emailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject("CTC - Richiesta conferma la tua email");
            
            String emailContent;
            if (confirmationLink.equals(expectedLink)) {
                emailContent = "<html><body>"
                        + "<h1>CTC  </h1>"
                		+"<h5>Conferma il tuo indirizzo Email</h5>"
                        +""
                        + "<p>Ciao " + name +",</p>"
                        +"<p> Ti confermiamo che la registrazione è avvenuta con successo </p>"
                        
                        +"<p> Per iniziare ad utilizzare i nostri servizi verifica il tuo indirizzo Email </p>"
                        + "<p>Clicca il seguente link per verificare la tua email: <a href='" + confirmationLink + "'>Conferma Email</a></p>"
                        + "<p>Grazie,</p>"
                        + "<p>Il CTC Team</p>"
                        +"<p>Se non hai richiesto questa email, ti consigliamo di ignorarla o eliminarla.</p>"
                        +"<p>Se hai bisogno di ulteriore assistenza, ti preghiamo di contattarci immediatamente a questo indirizzo.</p>"
                        + "</body></html>";
            } else {

                emailContent = "<html><body>"
                        + "<h1>CTC Conferma il tuo indirizzo Email </h1>"
                        + "<p>Ciao " + name +",</p>"
                        + "<p>Abbiamo rilevato un tentativo di accesso non autorizzato al link di conferma dell'email.</p>"
                        + "<p>Se non hai richiesto questa email, ti consigliamo di ignorarla o eliminarla.</p>"
                        + "<p>Grazie,</p>"
                        + "<p>Il CTC Team</p>"
                        + "</body></html>";
            }

            helper.setText(emailContent, true); 
            emailSender.send(message);
        } catch (Exception e) {
           
        }
    }
    public void sendReceiptToUser(Payment receipt) {
    	MimeMessage message = emailSender.createMimeMessage();
    	try {
    		MimeMessageHelper helper = new MimeMessageHelper(message, true);

        	helper.setTo(receipt.getUser().getEmail());
        	helper.setSubject("CTC - Payment Receipt");

        
        	String emailContent = "<html><body>"
        	        + "<h1>CTC - Payment Receipt</h1>"
        	        + "<p>Dear " + receipt.getUser().getName() + ",</p>"
        	        + "<p>Thank you for your payment!</p>"
        	        + "<p>Booking ID: " + receipt.getBooking().getId() + "</p>"
        	        + "<p>Payment Amount: " + receipt.getAmount() + "</p>"
        	       
        	        + "</body></html>";

        	helper.setText(emailContent, true);
        	emailSender.send(message);

		} catch (Exception e) {
			
		}
    	

    	// Send the email
    	
    }
}
