package sk.tuke.gamestudio.server.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.service.email.EmailSender;

@RestController
@RequestMapping("/api/send-email")
public class EmailServiceRest {

    @Autowired
    private EmailSender emailSender;

    @GetMapping("/{email}")
    public String sendMail(@PathVariable String email) {
        return emailSender.sendEmail(email);
    }


}
