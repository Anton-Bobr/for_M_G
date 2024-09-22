package com.manyvids.parser.service;

import com.manyvids.parser.entity.ParsingLogEntity;
import com.manyvids.parser.repo.ParsingLogRepo;
import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class ParsingLogService {
    @Autowired
    private ParsingLogRepo parsingLogRepo;
    @Autowired
    private ObjectMapper objectMapper;

    @SneakyThrows
    public void saveUnsubscribingLog(final int numberOfDaysForUnsubscribe,
                                     final int numberOfUserForUnsubscribing,
                                     final int numberOfUserRealUnsubscribed) {

        final ParsingLogEntity entity = createLogEntity("Unsubscribing");

        final Map<String, Object> data = new HashMap();
        data.put("numberOfDaysForUnsubscribe", numberOfDaysForUnsubscribe);
        data.put("numberOfUserForUnsubscribing", numberOfUserForUnsubscribing);
        data.put("numberOfUserRealUnsubscribed", numberOfUserRealUnsubscribed);

        entity.setData(objectMapper.writeValueAsString(data));
        parsingLogRepo.saveAndFlush(entity);
    }

    @SneakyThrows
    public void saveComparisonFollowersLog(final int numberOfFollowers,
                                           final int numberOfFollowing,
                                           final int subscribedUsers) {

        final ParsingLogEntity entity = createLogEntity("ComparisonFollowers");

        final Map<String, Object> data = new HashMap();
        data.put("numberOfFollowers", numberOfFollowers);
        data.put("numberOfFollowing", numberOfFollowing);
        data.put("subscribedUsers", subscribedUsers);

        entity.setData(objectMapper.writeValueAsString(data));
        parsingLogRepo.saveAndFlush(entity);
    }

    private ParsingLogEntity createLogEntity(final String type) {
        final ParsingLogEntity entity = new ParsingLogEntity();
        entity.setLogType(type);
        entity.setCreateAt(LocalDateTime.now());
        return entity;
    }
}
