package es.davidenjuan.subscriptions.internalapi.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import es.davidenjuan.subscriptions.internalapi.domain.Email;
import es.davidenjuan.subscriptions.internalapi.domain.enumeration.EmailStatus;

public interface EmailRepository extends MongoRepository<Email, String> {

    public List<Email> findByStatus(EmailStatus status);
}
