package com.content.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdviseRequest {
    private String profession;

    private String company;

    private String name;

    private String level;

    private String text;
}
