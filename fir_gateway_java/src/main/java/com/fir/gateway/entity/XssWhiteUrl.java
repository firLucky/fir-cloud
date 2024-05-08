package com.fir.gateway.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.NotEmpty;


/**
 * xss白名单配置实体
 */
@Data
@Validated
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class XssWhiteUrl {

    @NotEmpty
    private String url;

    @NotEmpty
    private String method;
}