package es.davidenjuan.subscriptions.internalapi.service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import es.davidenjuan.subscriptions.internalapi.configuration.ApplicationProperties;
import es.davidenjuan.subscriptions.internalapi.domain.Subscription;
import es.davidenjuan.subscriptions.internalapi.exception.UserAlreadySubscribedException;
import es.davidenjuan.subscriptions.internalapi.repository.SubscriptionRepository;
import es.davidenjuan.subscriptions.internalapi.service.dto.EmailDTO;
import es.davidenjuan.subscriptions.internalapi.service.dto.SubscriptionDTO;
import es.davidenjuan.subscriptions.internalapi.service.mapper.SubscriptionMapper;

@Service
public class SubscriptionService {

    private Logger log = LoggerFactory.getLogger(SubscriptionService.class);

    private final SubscriptionRepository subscriptionRepository;

    private final SubscriptionMapper subscriptionMapper;

    private final EmailService emailService;

    private final MessageSource messageSource;

    private final TemplateEngine templateEngine;

    private final String frontEndUri;

    public SubscriptionService(
        SubscriptionRepository subscriptionRepository,
        SubscriptionMapper subscriptionMapper,
        EmailService emailService,
        MessageSource messageSource,
        TemplateEngine templateEngine,
        ApplicationProperties applicationProperties
    ) {
        this.subscriptionRepository = subscriptionRepository;
        this.subscriptionMapper = subscriptionMapper;
        this.emailService = emailService;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
        this.frontEndUri = applicationProperties.getFrontEndUri();
    }

    /**
     * Creates a new subscription.
     * @param subscriptionDTO subscription to create.
     * @return created subscription.
     */
    public SubscriptionDTO create(SubscriptionDTO subscriptionDTO) {
        log.info("Request to create subscription");

        // Assert user is not subscribed yet
        boolean userAlreadySubscribed = !subscriptionRepository.findByUserIdAndNewsletterId(subscriptionDTO.getUserId(), subscriptionDTO.getNewsletterId()).isEmpty();
        if (userAlreadySubscribed) {
            log.warn("User {} is already subscribed to newsletter {}", subscriptionDTO.getUserId(), subscriptionDTO.getNewsletterId());
            throw new UserAlreadySubscribedException("User " + subscriptionDTO.getUserId() + " is already subscribed to newsletter " + subscriptionDTO.getNewsletterId());
        }

        // Establish subscription ID and save
        subscriptionDTO.setId(UUID.randomUUID().toString());
        Subscription subscription = subscriptionMapper.toEntity(subscriptionDTO);
        subscription = subscriptionRepository.save(subscription);
        subscriptionDTO = subscriptionMapper.toDto(subscription);
        log.info("Subscription {} created", subscriptionDTO.getId());

        // Send subscription created email
        EmailDTO emailDTO = buildSubscriptionCreatedEmail(subscriptionDTO);
        emailService.queueEmail(emailDTO);
        log.info("Subscription created email queued");

        return subscriptionDTO;
    }

    /**
     * Returns all the subscriptions.
     * @return all the subscriptions.
     */
    public List<SubscriptionDTO> findAll() {
        log.info("Request to get all subscriptions");
        return subscriptionRepository.findAll().stream().map(subscriptionMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Returns the list of subscriptions for the given user.
     * @param userId user ID for which subscriptions will be returned.
     * @return subscriptions of the specified user.
     */
    public List<SubscriptionDTO> findByUserId(String userId) {
        log.info("Request to get subscriptions for user {}", userId);
        return subscriptionRepository.findByUserId(userId).stream().map(subscriptionMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Returns the subscription with the specified ID.
     * @param id ID of the subscription to return.
     * @return found subscription.
     */
    public Optional<SubscriptionDTO> findOne(String id) {
        log.info("Request to get subscription with ID {}", id);
        return subscriptionRepository.findById(id).map(subscriptionMapper::toDto);
    }

    /**
     * Cancels the subscription with the specified ID.
     * @param id ID of the subscription to cancel.
     */
    public void cancel(String id) {
        log.info("Request to cancel subscription with ID {}", id);
        subscriptionRepository.deleteById(id);
    }

    /**
     * Builds an instance of {@link EmailDTO} which represents an email informing of
     * subscription creation.
     * @param subscriptionDTO subscription for which email will be created
     * @return new {@link EmailDTO} instance
     */
    private EmailDTO buildSubscriptionCreatedEmail(SubscriptionDTO subscriptionDTO) {
        // Prepare email data
        Locale locale = Locale.forLanguageTag("en");
        Context context = new Context(locale);
        context.setVariable("subscription", subscriptionDTO);
        context.setVariable("manageSubscriptionsFrontEndUri", frontEndUri + "/users/" + subscriptionDTO.getUserAccessId());
        context.setVariable("cancelSubscriptionFrontEndUri", frontEndUri + "/users/" + subscriptionDTO.getUserAccessId() + "?cancel=" + subscriptionDTO.getId());
        String subject = messageSource.getMessage("email.subscription-created-email.subject", null, locale);
        String body = templateEngine.process("subscription-created-email_en", context);

        // Build email instance
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setFrom("Subscriptions Notifications <no-reply@davidenjuan.es>");
        emailDTO.setTo(subscriptionDTO.getEmail());
        emailDTO.setSubject(subject);
        emailDTO.setBody(body);
        emailDTO.setBodyIsHtml(true);
        return emailDTO;
    }
}
