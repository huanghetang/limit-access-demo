package config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: AccessConfig
 * @Description:
 * @Author zm
 * @Date 2018/10/26 18:17
 **/
@ConfigurationProperties(prefix = "access")
public class AccessConfig {
    private List<String> urlTimeAndUser = new ArrayList<>(); //接收prop2里面的属性值

    public List<String> getUrlTimeAndUser(){
        return this.urlTimeAndUser;
    }
}

