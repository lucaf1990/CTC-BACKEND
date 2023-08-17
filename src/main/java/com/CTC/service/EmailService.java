package com.CTC.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.CTC.entity.Payment;
import com.CTC.entity.Receipt;

import jakarta.mail.internet.MimeMessage;

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
                // Define your HTML content for a valid confirmation link
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
                // Define your HTML content for an invalid confirmation link
                emailContent = "<html><body>"
                        + "<h1>CTC Conferma il tuo indirizzo Email </h1>"
                        + "<p>Ciao " + name +",</p>"
                        + "<p>Abbiamo rilevato un tentativo di accesso non autorizzato al link di conferma dell'email.</p>"
                        + "<p>Se non hai richiesto questa email, ti consigliamo di ignorarla o eliminarla.</p>"
                        + "<p>Grazie,</p>"
                        + "<p>Il CTC Team</p>"
                        + "</body></html>";
            }

            helper.setText(emailContent, true); // true indicates HTML content
            emailSender.send(message);
        } catch (Exception e) {
            // Handle exception
        }
    }
    public void sendReceiptToUser(Payment receipt) {
    	MimeMessage message = emailSender.createMimeMessage();
    	try {
    		MimeMessageHelper helper = new MimeMessageHelper(message, true);

        	helper.setTo(receipt.getUser().getEmail());
        	helper.setSubject("CTC - Payment Receipt");

        	// Construct the email content with receipt details
        	String emailContent = "<html><body>"
        	        + "<h1>CTC - Payment Receipt</h1>"
        	        + "<p>Dear " + receipt.getUser().getName() + ",</p>"
        	        + "<p>Thank you for your payment!</p>"
        	        + "<p>Booking ID: " + receipt.getBooking().getId() + "</p>"
        	        + "<p>Payment Amount: " + receipt.getAmount() + "</p>"
        	        // Include other receipt details
        	        + "</body></html>";

        	helper.setText(emailContent, true);
        	emailSender.send(message);

		} catch (Exception e) {
			// TODO: handle exception
		}
    	

    	// Send the email
    	
    }
}
