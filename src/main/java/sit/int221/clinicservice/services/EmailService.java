package sit.int221.clinicservice.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.time.LocalDate;
import java.util.Date;

import static java.lang.String.format;

@Slf4j
@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sent(String email, String name, String eventCategoryName, String date, String notes) {
        try {
            Context ctx = new Context();
            ctx.setVariable("email", email);
            ctx.setVariable("name", name);
            ctx.setVariable("date", date);
            ctx.setVariable("eventCategoryName", eventCategoryName);
            ctx.setVariable("notes", notes);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setSubject("[OASIP] " + eventCategoryName + " @ " + date);
            message.setTo(email);

            String content = templateEngine.process("email-template.html", ctx);
            message.setText(content, true);
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage(), e);
        }
    }
}
