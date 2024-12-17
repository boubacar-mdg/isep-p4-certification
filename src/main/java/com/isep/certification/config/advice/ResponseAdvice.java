package com.isep.certification.config.advice;


import org.threeten.bp.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseAdvice {
    public LocalDateTime timestamp;
/*     public int statusCode;
    public String status; */
    public Object data;
}
