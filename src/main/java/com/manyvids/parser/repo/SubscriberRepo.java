package com.manyvids.parser.repo;

import com.manyvids.parser.entity.SubscriberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriberRepo extends JpaRepository<SubscriberEntity, Long> {

    Optional<SubscriberEntity> findByUserName(final String username);
}
