package com.vaadin.vaadin_archetype_application;

import java.util.Properties;

//import java.util.Properties;    
//import javax.mail.*;    
//import javax.mail.internet.*;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailNotifier {
	public static void sendCreationMail(String email, String meetingName, String meetingCode, String meetingPass) {    
		String text = "You have been invited to meeting '" + meetingName +"'\n"
				+ "Unique meeting code: " + meetingCode;
		if (meetingPass.equals(""))
			text += "\nThere is no meeting password. Leave this field empty.";
		else
			text += "Meeting password: " + meetingPass;
		Mailer.send("whenapp3130@gmail.com","csci3130",email,"WHEN Meeting Notification",text);
	} 
}

class Mailer {  
	
    public static void send(String from,String password,String recipient,String sub,String msg){  
          //Get properties object    
          Properties props = new Properties();    
          props.put("mail.smtp.host", "smtp.gmail.com");    
          props.put("mail.smtp.socketFactory.port", "465");    
          props.put("mail.smtp.socketFactory.class",    
                    "javax.net.ssl.SSLSocketFactory");    
          props.put("mail.smtp.auth", "true");    
          props.put("mail.smtp.port", "465");    
          //get Session   
          Session session = Session.getDefaultInstance(props,    
        		  new javax.mail.Authenticator() {    
        	  			protected PasswordAuthentication getPasswordAuthentication() {    
        	  					return new PasswordAuthentication(from,password);  
        	  	  }    
          });    
          
          //compose message    
          try {    
           MimeMessage message = new MimeMessage(session);    
           message.addRecipient(Message.RecipientType.TO,new InternetAddress(recipient));
           message.setSubject(sub);    
           message.setText(msg);    
           //send message  
           Transport.send(message);    
           System.out.println("message sent successfully");    
          } catch (MessagingException e) {throw new RuntimeException(e);}    
             
    }  
} 