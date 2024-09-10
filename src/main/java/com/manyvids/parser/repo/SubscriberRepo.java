package com.manyvids.parser.repo;

import com.manyvids.parser.entity.SubscriberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriberRepo extends JpaRepository<SubscriberEntity, Long> {

    Optional<SubscriberEntity> findByUserName(final String username);

    @Query(value = "SELECT s "
                   + " FROM SubscriberEntity s "
                   + " where s.userSubscribedAt IS NULL "
                   + " and s.weUnsubscribedAt IS NULL "
                   + " and DATEDIFF(?1, s.weSubscribedAt) >= ?2")
    List<SubscriberEntity> getUsersWhoHaveNotSubscribedXdDays(LocalDateTime now,
                                                              int numberOfDaysForUnsubscribe);
}
