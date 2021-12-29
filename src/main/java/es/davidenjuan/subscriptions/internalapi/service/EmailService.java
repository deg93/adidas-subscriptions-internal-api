package es.davidenjuan.subscriptions.internalapi.service;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import es.davidenjuan.subscriptions.internalapi.configuration.ApplicationProperties;
import es.davidenjuan.subscriptions.internalapi.domain.Email;
import es.davidenjuan.subscriptions.internalapi.domain.enumeration.EmailStatus;
import es.davidenjuan.subscriptions.internalapi.repository.EmailRepository;
import es.davidenjuan.subscriptions.internalapi.service.dto.EmailDTO;
import es.davidenjuan.subscriptions.internalapi.service.mapper.EmailMapper;

@Service
public class EmailService {

    private Logger log = LoggerFactory.getLogger(EmailService.class);

    private final EmailRepository emailRepository;

    private final EmailMapper emailMapper;

    private final String emailApiUri;

    private final String emailApiUser;

    private final String emailApiPassword;

    private final RestTemplate restTemplate;

    public EmailService(EmailRepository emailRepository, EmailMapper emailMapper, ApplicationProperties applicationProperties, RestTemplate restTemplate) {
        this.emailRepository = emailRepository;
        this.emailMapper = emailMapper;
        this.emailApiUri = applicationProperties.getEmailApi().getUri();
        this.emailApiUser = applicationProperties.getEmailApi().getUser();
        this.emailApiPassword = applicationProperties.getEmailApi().getPassword();
        this.restTemplate = restTemplate;
    }

    /**
     * Queues the given email. Once queued, the email will be sent in the near future.
     * @param emailDTO email to queue.
     * @return queued email.
     */
    public EmailDTO queueEmail(EmailDTO emailDTO) {
        log.info("Request to queue email");

        // Save email for sending later
        emailDTO.setStatus(EmailStatus.PENDING);
        Email email = emailMapper.toEntity(emailDTO);
        email = emailRepository.save(email);
        emailDTO = emailMapper.toDto(email);
        log.info("Email saved for sending later (ID: {})", emailDTO.getId());

        return emailDTO;
    }

    /**
     * Sends the queued emails.
     */
    @Scheduled(fixedDelay = 20000)
    public void sendEmails() {
        log.info("Sending emails");

        // Retrieve emails which are pending to send
        List<EmailDTO> pendingEmails = emailRepository.findByStatus(EmailStatus.PENDING).stream().map(emailMapper::toDto).collect(Collectors.toList());
        log.info("Found {} emails pending to send", pendingEmails.size());

        // Send each email
        pendingEmails.forEach(email -> {
            sendEmailRequest(email);
            email.setStatus(EmailStatus.SENT);
            emailRepository.save(emailMapper.toEntity(email));
        });
        log.info("All emails sent successfully");
    }

    /**
     * Sends the request for the email API to send the given email.
     * @param emailDTO email to send.
     */
    private void sendEmailRequest(EmailDTO emailDTO) {
        log.info("Sending email {}", emailDTO.getId());

        // Send request to email API
        String plainCredentials = emailApiUser + ":" + emailApiPassword;
        byte[] plainCredentialsBytes = plainCredentials.getBytes();
        byte[] base64CredentialsBytes = Base64.getEncoder().encode(plainCredentialsBytes);
        String base64Credentials = new String(base64CredentialsBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Credentials);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<EmailDTO> request = new HttpEntity<EmailDTO>(emailDTO, headers);
        restTemplate.exchange(emailApiUri, HttpMethod.POST, request, EmailDTO.class);
        log.info("Email {} sent successfully", emailDTO.getId());
    }
}