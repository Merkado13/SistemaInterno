package sistema.interno;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;

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

import org.springframework.stereotype.Service;

@Service
public class GmailEmailService {

	private class Credentials{
		public String emailUser;
		public String password;
		public Credentials(String emailUser, String password) {
			this.emailUser = emailUser;
			this.password = password;
		}
		public String toString() {
			return "Email: " + emailUser + " Password: " + password;
		}
	}
	
	private Credentials credentials;
	private Properties props;
	private Session session;
	private final String CREDENTIALS_DIR = "src/main/resources/static/credentials.txt";
	
	@PostConstruct
	public void init() {
		props = getProperties();
		credentials = getCredentialsFromFile(CREDENTIALS_DIR);
		session = getSession();
	}
	
	private Properties getProperties() {   
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		return props;
	}
	
	private Credentials getCredentialsFromFile(String dir) {
		
		try{
			String[] credentials = new String[2];
			FileInputStream fis=new FileInputStream(dir);       
			Scanner sc=new Scanner(fis);
			int i = 0;
			while(sc.hasNextLine() && i < credentials.length) {  
				credentials[i] = sc.nextLine();
				i++;
			}
			sc.close();
			Credentials credential = new Credentials(credentials[0],credentials[1]);
			return credential;
		}  
		catch(IOException e){  
			e.printStackTrace();  
			return null;
		}  
	}
	
	private Session getSession() {
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(credentials.emailUser, credentials.password);
			}
		});
		
		return session;
	}
	
	public void sendEmail(String emailTo,String subject, String emailContent) throws AddressException, MessagingException, IOException {
			
		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(credentials.emailUser, false));
	
		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo));
		msg.setSubject(subject);
		msg.setContent(subject, "text/html");
		msg.setSentDate(new Date());
	
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(emailContent, "text/html");
	
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);
		msg.setContent(multipart);
		   
		Transport.send(msg);   
	}
	
	public String getNewBookEmailBodyMessage(String authorName, String title) {
		return authorName + " acaba de publicar su nuevo libro: " + title + 
				"\n" ;
	}
}
