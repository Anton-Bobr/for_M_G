package com.manyvids.parser.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ContentTypeEnum {

    WOMEN_CREATORS("Women Creators"),
    MEN_CREATORS("Men Creators"),
    TRANS_CREATORS("Trans Creators"),
    PRODUCERS("Producers");

    private final String name;
}
