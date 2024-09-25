package com.manyvids.parser.repo;

import com.manyvids.parser.entity.ParsingLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ParsingLogRepo extends JpaRepository<ParsingLogEntity, Long> {

    @Query(value = "SELECT e FROM ParsingLogEntity e WHERE e.logType = ?1 AND DATE(e.createAt) =?2")
    Optional<ParsingLogEntity> findByLogTypeAndCreateAt_Date(String logType,
                                                             LocalDate createAt);
}
