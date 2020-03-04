package sistema.interno;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.stereotype.Component;

@Component
public class GmailEmailSender {

	
	private class Credentials{
		public String emailUser;
		public String password;
		public Credentials(String emailUser, String password) {
			this.emailUser = emailUser;
			this.password = password;
		}
	}
	
	private Credentials credentials;
	
	@PostConstruct
	public void init() {
		credentials = getCredentials();
	}
	
	private Credentials getCredentials() {
		Credentials credential = new Credentials("","");
		return credential;
	}
	
	public void sendEmail(String emailTo) throws AddressException, MessagingException, IOException {
		   
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
	   
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("misbuenoslibritos@gmail.com", "distribuidos123");
			}
		});
		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress("misbuenoslibritos@gmail.com", false));
	
		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo));
		msg.setSubject("Email Test");
		msg.setContent("Email Test", "text/html");
		msg.setSentDate(new Date());
	
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent("Esto es un email de prueba :3", "text/html");
	
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);
		msg.setContent(multipart);
		   
		Transport.send(msg);   
	}
}
