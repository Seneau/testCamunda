package com.seneau.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.List;

//@Component
public class EmailServiceImpl{

    // @Autowired
  //  private JavaMailSender emailSender;

    public void sendEmail(){
        System.out.println("Sending Email...");
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("ndeyesalydione@gmail.com","abdoulaye.ndiaye@seneau.sn");
        msg.setSubject("FIMMMMMP");
        msg.setText("Hello from \n MYRH send Email");

        //emailSender.send(msg);
    }
}
