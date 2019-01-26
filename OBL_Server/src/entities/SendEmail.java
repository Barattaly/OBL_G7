package entities;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class SendEmail 
{
	public void sendEmail(String emailRecipient, String emailSubject, String emailMessage) {
		final String username = "ortbraudelibrary.utility@gmail.com";
		final String password = "b205469851";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("ortbraudelibrary.utility@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(emailRecipient));
			message.setSubject(emailSubject);
			message.setText(emailMessage);

			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}
