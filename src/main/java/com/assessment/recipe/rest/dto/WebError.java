package com.assessment.recipe.rest.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class WebError {
    private int statusCode;
    private String message;
    private String path;
    private Date timestamp;
}
