package au.org.theark.core.util;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkSystemException;

/**
 * Implementation of JavaMail API, to allow sending of an email from within the application (requires smtp setup)
 * 
 * @author cellis
 * 
 */
@SuppressWarnings("restriction")
public class MailClient {
	private transient static Logger	log			= LoggerFactory.getLogger(MailClient.class);
	private String							mailServer	= "smtp.ivec.org"; // change this to your host
	private String							port			= "25";
	private String							from			= "admin@the-ark.org.au";
	protected String						to				= "someuser@somewhere.com";
	protected String						subject		= "Test Subject";
	protected StringBuffer				messageBody	= new StringBuffer("Testing email...");
	protected String[]					attachments	= new String[0];

	/**
	 * Send and email from within the application (requires smtp setup)
	 * @throws ArkSystemException 
	 */
	public void sendMail() throws ArkSystemException {
		// Setup mail server
		Properties props = System.getProperties();
		props.put("mail.smtp.host", mailServer);
		props.put("mail.smtp.port", port);

		// Get a mail session
		Session session = Session.getDefaultInstance(props, null);

		// Define a new mail message
		Message message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject(subject);

			// Create a message part to represent the body text
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText(messageBody.toString());

			// use a MimeMultipart as we need to handle the file attachments
			Multipart multipart = new MimeMultipart();

			// add the message body to the mime message
			multipart.addBodyPart(messageBodyPart);

			// add any file attachments to the message
			addAtachments(attachments, multipart);

			// Put all message parts in the message
			message.setContent(multipart);

			// Send the message
			Transport.send(message);
		}
		catch (AddressException e) {
			log.error(e.getMessage());
			throw new ArkSystemException();
		}
		catch (MessagingException e) {
			log.error(e.getMessage());
			throw new ArkSystemException();
		}
	}

	/**
	 * Add any attachments to the email
	 * @param attachments
	 * @param multipart
	 * @throws MessagingException
	 * @throws AddressException
	 */
	protected void addAtachments(String[] attachments, Multipart multipart) throws MessagingException, AddressException {
		for (int i = 0; i <= attachments.length - 1; i++) {
			String filename = attachments[i];
			MimeBodyPart attachmentBodyPart = new MimeBodyPart();

			// use a JAF FileDataSource as it does MIME type detection
			DataSource source = new FileDataSource(filename);
			attachmentBodyPart.setDataHandler(new DataHandler(source));

			// assume that the filename you want to send is the same as the
			// actual file name - could alter this to remove the file path
			attachmentBodyPart.setFileName(filename);

			// add the attachment
			multipart.addBodyPart(attachmentBodyPart);
		}
	}

	/**
	 * @return the to
	 */
	public String getTo() {
		return to;
	}

	/**
	 * @param to the to to set
	 */
	public void setTo(String to) {
		this.to = to;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the messageBody
	 */
	public String getMessageBody() {
		return messageBody.toString();
	}

	/**
	 * @param messageBody the messageBody to set
	 */
	public void setMessageBody(StringBuffer messageBody) {
		this.messageBody = messageBody;
	}

	/**
	 * @return the attachments
	 */
	public String[] getAttachments() {
		return attachments;
	}

	/**
	 * @param attachments the attachments to set
	 */
	public void setAttachments(String[] attachments) {
		this.attachments = attachments;
	}
}