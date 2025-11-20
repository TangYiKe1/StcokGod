package com.ming.stock.pojo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor@NoArgsConstructor
@Data
@Builder
public class RolesDomin {
    private String id;
    private String label;
    private List<RolesDomin> children = new ArrayList<>();

    // 可选: 用于前端的额外字段
    private String permission;
    private String url;
    private String method;
}
