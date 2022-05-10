package sk.tuke.gamestudio.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class EmailSender {

    @Autowired
    JavaMailSender javaMailSender;

    public String sendEmail(String email) throws EmailException {
        try {
            SimpleMailMessage message = new SimpleMailMessage();

            int min = 1000000;
            int max = 9999999;
            int verification_number = (int) Math.floor(Math.random() * (max - min + 1) + min);

            message.setFrom("game1024service@gmail.com");
            message.setTo(email);
            message.setSubject("Verification code.");
            message.setText("Welcome to game1024.\r\rYour verification code is: " + verification_number + ".\rPlease enter it on the site to successfully complete the registration.\r ");

            javaMailSender.send(message);
            return Integer.toString(verification_number);
        } catch (EmailException e) {
            throw new EmailException("problem sending email", e);
        }
    }
}
