package com.cwteams.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;

public class EmailUtils{
	public static void sendEmail(String destination, String new_pass) throws MessagingException{
        
        Properties props = new Properties();
        
        props.put("mail.smtp.host","smtp.gmail.com");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.transport.protocol","smtp");
        props.setProperty("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587");//465
        /*props.put("mail.debug", "true");        
	    props.put("mail.smtp.socketFactory.port", "465");  
	    props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");  
	    props.put("mail.smtp.socketFactory.fallback", "false");*/
        
        String emailT="",passwordT="";
        
        try {
			ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();		
			String path = servletContext.getRealPath("") + File.separatorChar
					+ "settings" + File.separatorChar + "Settings.properties";
			
			Properties propiedades = new Properties();
			propiedades.load(new FileInputStream(path));
			
			emailT=propiedades.getProperty("emailMR");
			passwordT=propiedades.getProperty("passwordMR");			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        final String email = emailT;
    	final String password = passwordT;
        
        Session session = Session.getInstance(props,
			new Authenticator() {
  				protected PasswordAuthentication getPasswordAuthentication() {
  					return new PasswordAuthentication(email, password);
			}
	  	});
        
        MimeMessage message = new MimeMessage(session);

        message.setFrom(new InternetAddress(email));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destination));
		message.setSubject("Recuperacion de Contraseña");
        message.setText("Nueva Contraseña: "+new_pass);
            
        Transport.send(message);   
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", "Recordatorio de contraseña enviado a " + destination + "!"));
    }  
}