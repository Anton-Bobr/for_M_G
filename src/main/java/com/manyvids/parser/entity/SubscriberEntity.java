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
@Table(name = "SUBSCRIBER")
@AllArgsConstructor
@NoArgsConstructor
public class SubscriberEntity extends HasIdAndVersionAbstractEntity {

    @Column(name = "USER_NAME", nullable = false, length = 256)
    private String userName;

    @Column(name = "USWR_ID", nullable = false, length = 256)
    private String userId;

    @Column(name = "WE_SUBSCRIBD_AT", nullable = false)
    private LocalDateTime weSubscribedAt;

    @Column(name = "USER_SUBSCRIBED_AT")
    private LocalDateTime userSubscribedAt;

    @Column(name = "WE_UNSUBSCRIBED_AT")
    private LocalDateTime weUnsubscribedAt;
}
