package cn.ttsales.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by 露青 on 2016/10/12.
 */
@Component
@ConfigurationProperties(prefix = "wx.api", locations = {"classpath:conf/weixin.properties"})
public class WxApiSetting {
    private String baseUrl;

    private String departmentList;

    private String code;

    private String userinfo;

    private String user;

    public WxApiSetting() {
    }

    public String getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(String userinfo) {
        this.userinfo = userinfo;
    }

    public String getDepartmentList() {
        return departmentList;
    }

    public void setDepartmentList(String departmentList) {
        this.departmentList = departmentList;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
