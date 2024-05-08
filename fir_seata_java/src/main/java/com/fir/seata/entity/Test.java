package com.fir.seata.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;


/**
 * <p>
 * Test对象
 * </p>
 *
 * @author dpe
 * @since 2023-04-17
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Test implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    private Integer num;

    private String name;

    private String result;


}
