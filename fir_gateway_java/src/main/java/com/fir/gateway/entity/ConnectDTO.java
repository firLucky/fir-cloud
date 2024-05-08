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
@ApiModel(value="用户登录密钥信息")
public class ConnectDTO implements Serializable {

    private static final long serialVersionUID = -1;

    @ApiModelProperty("整体加密密钥")
    private String secretKey;

    @ApiModelProperty("会话id")
    private String sessionId;

    @ApiModelProperty("盐")
    private String salt;

    @ApiModelProperty("服务端通信公钥")
    private String publicKey;

    @ApiModelProperty("服务端通信私钥")
    private String privateKey;

    @ApiModelProperty("客户端端通信公钥")
    private String clientPublicKey;
}
