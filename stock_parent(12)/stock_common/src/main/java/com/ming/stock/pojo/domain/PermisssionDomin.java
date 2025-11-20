package com.ming.stock.pojo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PermisssionDomin {
    private Long id;
    private Long pid;
    private String url;
    private String name;
    private String icon;
    private String perms;
    private String method;
    private String code;
    private Integer orderNum;
}
