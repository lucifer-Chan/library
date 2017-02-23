package cn.ttsales.config;

import net.sf.json.JSONObject;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by 露青 on 2016/10/12.
 * 企业号的一些信息
 */
@Component
@ConfigurationProperties(prefix = "wx.corp", locations = {"classpath:conf/weixin.properties"})
public class CorpSetting {
    private String id;
    private String name;
    private String secret;
    private String broker;
    private String token;
    private String aesKey;
    private String smileAgentId;

    public CorpSetting() {
    }

    public String getSmileAgentId() {
        return smileAgentId;
    }

    public void setSmileAgentId(String smileAgentId) {
        this.smileAgentId = smileAgentId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAesKey() {
        return aesKey;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }

    @Override
    public String toString() {
        return JSONObject.fromObject(this).toString();
    }
}
