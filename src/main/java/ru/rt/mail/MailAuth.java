package ru.rt.mail;

/**
 *
 * @author Maksim.Filatov
 */
public class MailAuth extends javax.mail.Authenticator {
    private final char[] user;
    private final char[] password;
    
    public MailAuth(String user, String password) {
	this.user = user.toCharArray();
	this.password = password.toCharArray();
    }
    
    @Override
    protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
	return new javax.mail.PasswordAuthentication(String.valueOf(user), String.valueOf(password));
    }
}
