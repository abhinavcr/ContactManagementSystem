package com.sts;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Properties;
import org.springframework.stereotype.*;
import javax.websocket.Session;

import org.springframework.boot.rsocket.server.RSocketServer.Transport;
import org.springframework.stereotype.Service;
import org.springframework.ws.mime.MimeMessage;

@Service
public class EmailService {
	
	public boolean emailservice(String subject,String message,String to)
	{
		//rest of code
		boolean f=false;
		String from="jhaabhinav736@gmail.com";
		
		//variable for gmail
		String host="smtp.gmail.com";
		
		//get the system properties
		Properties pro=System.getProperties();
		System.out.println("Properties:"+pro);
		
		//setting important information to properties
		//host set
		pro.put("mail.smtp.host", host);
		pro.put("mail.smtp.port", "465");
		pro.put("mail.smtp.ssl.enable", "true");
		pro.put("mail.smtp.auth", "true");
		
		//step1: To get the session object
		Session sess=Session.getInstance(pro, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("jhaabhinav736@gmail.com","Abhi8287@");
			}
		});
		sess.setDebug(true);
		
		//
		MimeMessage m=new MimeMessage(sess);
		
		try {
			//from email
			m.setFrom(from);
			//adding recipient to message
			m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			//adding subject to message
			m.addSubject(subject);
			//adding text to message
			//m.setText(message);
			m.setContent(message,"text/html");
			
			//send
			//step3: send the message using transport class
			Transport.send(m);
			System.out.println("Send to email..............");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return f;
	}
}
