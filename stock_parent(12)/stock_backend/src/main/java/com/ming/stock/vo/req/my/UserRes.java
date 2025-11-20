package com.ming.stock.vo.req.my;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRes {
    private Integer pageNum;
    private Integer pageSize;
    private String username;
    private String nickName;
    private String startTime;
    private String endTime;

}
