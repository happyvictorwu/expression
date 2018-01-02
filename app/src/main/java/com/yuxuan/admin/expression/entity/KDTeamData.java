package com.yuxuan.admin.expression.entity;
/*
 * 项目名:   expression
 * 包名:     com.yuxuan.admin.expression.entity
 * 文件名:   KDTeamData
 * 创建者:   YUXUAN
 * 创建时间: 2018/1/2 15:55
 * 描述:     代取团队的实体
 */

public class KDTeamData {
    private String team_head;
    private String team_name;
    private String team_property;

    public String getTeam_name() {
        return team_name;
    }

    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }

    public String getTeam_property() {
        return team_property;
    }

    public void setTeam_property(String team_property) {
        this.team_property = team_property;
    }

    public String getTeam_head() {
        return team_head;
    }

    public void setTeam_head(String team_head) {
        this.team_head = team_head;
    }
}
