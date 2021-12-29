package es.davidenjuan.subscriptions.internalapi.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import es.davidenjuan.subscriptions.internalapi.domain.Subscription;

public interface SubscriptionRepository extends MongoRepository<Subscription, String> {

    public List<Subscription> findByUserId(String userId);

    public List<Subscription> findByUserIdAndNewsletterId(String userId, String newsletterId);
}
