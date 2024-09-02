package com.manyvids.parser.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "PARSING_LOG")
@AllArgsConstructor
@NoArgsConstructor
public class ParsingLogEntity extends HasIdAndVersionAbstractEntity {

    @Column(name = "TYPE", nullable = false, length = 256)
    private String logType;

    @Column(name = "DATA", nullable = false, length = 256)
    private String data;

    @Column(name = "CREATE_AT", nullable = false)
    private LocalDateTime createAt;
}
