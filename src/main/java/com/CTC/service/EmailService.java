package com.CTC.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

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
                        + "<h1>CTC Conferma il tuo indirizzo Email </h1>"
                        + "<p>Ciao " + name +",</p>"
                        + "<p>Clicca il seguente link per verificare la tua email: <a href='" + confirmationLink + "'>Conferma Email</a></p>"
                        + "<p>Grazie,</p>"
                        + "<p>Il CTC Team</p>"
                        +"<p>Se non hai richiesto questa email, ti consigliamo di ignorarla o eliminarla.</p>"
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
}
