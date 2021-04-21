package com.gm.mail;

import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import java.util.Properties;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Named(value = "email")
@RequestScoped
public class email implements Serializable {

    private String to;
    private String from;
    private String subject;
    private String descr;
    private String password;
    private String smtp;
    private int port;
    private int respuesta = 1;

    public email() {
        this.to = "andres121958@hotmail.com" + ";" + "infernous2018@gmail.com" + ";" + "infernous2017@gmail.com" + ";" + "comentariodnasport@gmail.com";
        this.from = "comentariodnasport@gmail.com";
        this.subject = null;
        this.descr = null;
        this.password = "adsi123456";
        this.smtp = "smtp.gmail.com";
        this.port = 587;
        this.descr = "";
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSmtp() {
        return smtp;
    }

    public void setSmtp(String smtp) {
        this.smtp = smtp;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void validateEmail(FacesContext context, UIComponent toValidate, Object value) {
        String message = "";
        String email = (String) value;
        if ((email == null) || (email.equals(""))) {
            ((UIInput) toValidate).setValid(false);
            message = "Email Requerido";
            context.addMessage(toValidate.getClientId(context), new FacesMessage(message));
        } else if ((!email.contains("@")) || (!email.contains("."))) {
            ((UIInput) toValidate).setValid(false);
            message = "Email Invalido";
            context.addMessage(toValidate.getClientId(context), new FacesMessage(message));
        }
    }

    public void validarComentario(FacesContext context, UIComponent toValidate, Object value) {
        String message = "";
        String com = (String) value;
        if ((com == null) || (com.equals(""))) {
            ((UIInput) toValidate).setValid(false);
            message = "Comentario Requerido";
            context.addMessage(toValidate.getClientId(context), new FacesMessage(message));
        }
    }

    public void submitEmail() {
        Properties props = null;
        Session session = null;
        MimeMessage message = null;
        Address fromAddress = null;
        Address toAddress = null;

        props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtp);
        props.put("mail.smtp.port", port);

        session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });
        message = new MimeMessage(session);
        try {
            message.setContent(getDescr(), "text/plain");
            message.setSubject(getSubject() + " - " + "Comentario");
            fromAddress = new InternetAddress(getFrom());
            message.setFrom(fromAddress);
            String correos = getTo();
            String[] correos_destinos = correos.split(";");
            Address[] receptores = new Address[correos_destinos.length];
            int j = 0;
            while (j < correos_destinos.length) {
                receptores[j] = new InternetAddress(correos_destinos[j]);
                j++;
            }
            message.setRecipients(Message.RecipientType.TO, receptores);
            message.saveChanges();
            Transport transport = session.getTransport("smtp");
            transport.connect(this.smtp, this.port, this.from, this.password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            respuesta = 2;
        } catch (Exception e) {
            respuesta = 3;
        }
    }

    public int getRespuesta() {
        return respuesta;
    }

    public int isRespuesta() {
        return respuesta;
    }

    public void setRespuesta(int respuesta) {
        this.respuesta = respuesta;
    }
}
