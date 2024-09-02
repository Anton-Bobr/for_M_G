package com.manyvids.parser.repo;

import com.manyvids.parser.entity.ParsingLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParsingLogRepo extends JpaRepository<ParsingLogEntity, Long> {
}
