package sistema.interno;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {
	
	@Autowired
	GmailEmailService emailSender;
	
	private final String SELF_EMAIL = "misbuenoslibritos@gmail.com";
	private final String prueba = "Esto es un email de prueba :3";
	private final String welcome = "Bienvenid@ a 'Mis buenos libritos', una web donde podrás organizar tus lecturas y encontrar tu próximo libro preferido\n";
	
	@RequestMapping(value = "/sendemail")
	public String sendEmail() throws AddressException, MessagingException, IOException {
	   emailSender.sendEmail(SELF_EMAIL, prueba);
	   return "Email sent successfully";
	}
	
	@RequestMapping(value = "/welcomeemail")
	public boolean welcomeEmail(@RequestBody String email) throws AddressException, MessagingException, IOException {
		
	   emailSender.sendEmail(email, welcome);
	   return true;
	}
	
}
