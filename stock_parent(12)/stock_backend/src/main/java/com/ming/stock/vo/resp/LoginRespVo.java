package com.ming.stock.vo.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Ming
 * @Description TODO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRespVo {

    /*
    * 用户ID
    * 将Long类型进行json转化的时候转成String格式类型
    * */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /*
    * 电话
    * */
    private String phone;
    /*
    * 用户名
    * */
    private String username;
    /*
    * 昵称
    * */
    private String nickName;
}
