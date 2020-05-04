package sistema.interno;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {
	
	@Autowired
	GmailEmailService emailSender;
	
	private final String SELF_EMAIL = "misbuenoslibritos2@gmail.com";
	private final String prueba = "Esto es un email de prueba :3";
	private final String welcome = "Bienvenid@ a 'Mis buenos libritos', una web donde podrás organizar tus lecturas y encontrar tu próximo libro preferido\n";
	
	public static final String TEST_SUBJECT = "Email Test";
	public static final String WELCOME_SUBJECT = "Welcome";
	public static final String NEW_BOOK_SUBJECT = "Nueva Publicacion!";
	
	private final int NUM_THREADS = 20;
	private ExecutorService pool = Executors.newFixedThreadPool(NUM_THREADS);
	
	@RequestMapping(value = "/sendemail")
	public String sendEmail() throws AddressException, MessagingException, IOException {
	   emailSender.sendEmail(SELF_EMAIL, TEST_SUBJECT, prueba);
	   return "Email sent successfully";
	}
	
	@RequestMapping(value = "/welcomeemail")
	public String welcomeEmail(@RequestBody String email) {
		
	   pool.execute(() -> welcomedEmail(email));
	   return "Sending welcome email...";
	}
	
	
	@RequestMapping(value = "/newbookemail")	
	public String welcomeEmail(@RequestBody Map<String, String> emailData) {
		
		pool.execute(() -> newBookEmail(emailData));
		return "Sending emails...";
	}
	
	private void welcomedEmail(String email) {
		try {
			emailSender.sendEmail(email, WELCOME_SUBJECT, welcome);
		} catch (MessagingException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void newBookEmail(Map<String, String> emailData) {
		String bodyMessage = emailSender.getNewBookEmailBodyMessage(emailData.get("authorName"), emailData.get("title"));
		
		String [] userEmails = emailData.get("userEmails").split(";");
		for(String email : userEmails) {
			System.out.println("Enviando email a: "+ email);
			try {
				emailSender.sendEmail(email, NEW_BOOK_SUBJECT, bodyMessage);
			} catch (MessagingException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/*@RequestMapping(value = "/newbookemail")
	public String welcomeEmail(@RequestBody NewBookEmailData emailData) throws AddressException, MessagingException, IOException {
		System.out.println("Hola soy el servidor");
		String bodyMessage = emailSender.getNewBookEmailBodyMessage(emailData.getAuthorName(), emailData.getTitle());
		
		List<String> userEmails = emailData.getUserEmails();
		for(String email : userEmails) {
			emailSender.sendEmail(email, NEW_BOOK_SUBJECT, bodyMessage);
		}
		
		return "Email sent successfully";
	}*/
	
}
