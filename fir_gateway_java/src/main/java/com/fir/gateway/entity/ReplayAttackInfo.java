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
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="重放攻击信息签名对象")
public class ReplayAttackInfo implements Serializable {


    @ApiModelProperty("请求时间戳")
    private String t;
}
