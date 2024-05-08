package com.fir.gateway.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fir
 * @date 2023/4/23 17:57
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="用户的登录接口信息")
public class LoginResultDTO {


    @ApiModelProperty("token令牌")
    private String token;
}
