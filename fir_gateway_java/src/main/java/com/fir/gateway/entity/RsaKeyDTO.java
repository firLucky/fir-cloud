package com.fir.gateway.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * @author fir
 * @date 2023/4/23 17:57
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="RSA公私钥对象")
public class RsaKeyDTO implements Serializable {

    @ApiModelProperty("通信公钥")
    private String publicKey;

    @ApiModelProperty("通信私钥")
    private String privateKey;
}
