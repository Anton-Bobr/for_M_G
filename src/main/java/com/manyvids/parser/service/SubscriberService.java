package com.manyvids.parser.service;

import com.manyvids.parser.entity.SubscriberEntity;
import com.manyvids.parser.repo.SubscriberRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriberService {
    @Autowired
    private SubscriberRepo subscriberRepo;

    public int compareFollowersAnsSaveNew(final List<String> followers,
                                           final List<String> following) {
        final List<String> commonNamesList = following.stream().filter(followers::contains).toList();
        final List<SubscriberEntity> subscriberEntityForUpdate = new ArrayList<>();

        commonNamesList.forEach(name -> {
            final Optional<SubscriberEntity> optional = subscriberRepo.findByUserName(name);
            if (optional.isPresent() && optional.get().getUserSubscribedAt() != null) {
                subscriberEntityForUpdate.add(optional.get());
            }
        });

        if (!subscriberEntityForUpdate.isEmpty()) {
            subscriberEntityForUpdate
                .forEach(subscriberEntity -> subscriberEntity.setUserSubscribedAt(LocalDateTime.now()));
            subscriberRepo.saveAllAndFlush(subscriberEntityForUpdate);
        }
        return subscriberEntityForUpdate.size();
    }

    public boolean isFollowerExist(final String userName) {
        return subscriberRepo.findByUserName(userName).isPresent();
    }

    public void createAndSaveNewSubscriber(final String followerName,
                                           final String userId) {
        final SubscriberEntity se = new SubscriberEntity();
        se.setUserName(followerName);
        se.setUserId(userId);
        se.setWeSubscribedAt(LocalDateTime.now());
        subscriberRepo.saveAndFlush(se);
    }

    public List<SubscriberEntity> getUsersWhoHaveNotSubscribedXdDays(final int numberOfDaysForUnsubscribe) {
        return subscriberRepo.getUsersWhoHaveNotSubscribedXdDays(LocalDateTime.now(), numberOfDaysForUnsubscribe);
    }

    public void updateUserAsUnsubscribed(final SubscriberEntity user) {
        user.setWeUnsubscribedAt(LocalDateTime.now());
        subscriberRepo.saveAndFlush(user);
    }
}
