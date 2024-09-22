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

    @SneakyThrows
    public void saveUnsubscribingLog(final int numberOfDaysForUnsubscribe,
                                     final int numberOfUserForUnsubscribing,
                                     final int numberOfUserRealUnsubscribed) {

        final ParsingLogEntity entity = new ParsingLogEntity();
        entity.setLogType("Unsubscribing");

        final Map<String, Object> data = new HashMap();
        data.put("numberOfDaysForUnsubscribe", numberOfDaysForUnsubscribe);
        data.put("numberOfUserForUnsubscribing", numberOfUserForUnsubscribing);
        data.put("numberOfUserRealUnsubscribed", numberOfUserRealUnsubscribed);
        final ObjectMapper objectMapper = new ObjectMapper();
        final String jacksonData = objectMapper.writeValueAsString(data);
        entity.setData(jacksonData);
        entity.setCreateAt(LocalDateTime.now());
        parsingLogRepo.saveAndFlush(entity);
    }
}
