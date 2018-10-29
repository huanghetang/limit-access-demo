package config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: UserFilterConfig
 * @Description:
 * @Author zm
 * @Date 2018/10/25 17:03
 **/

@Component
@EnableConfigurationProperties(AccessConfig.class)
public class UserFilterConfig {

    @Autowired
    private AccessConfig accessConfig;

    private List<UserConfig> userConfigList;

    public List<UserConfig> getUserConfigList() {
        List<String> urlTimeAndUserList = accessConfig.getUrlTimeAndUser();
        if (CollectionUtils.isEmpty(urlTimeAndUserList)){
            return null;
        }
        //     /limit/*=10000,0
        userConfigList = new ArrayList<UserConfig>();
        for (String urlTimeAndUser : urlTimeAndUserList) {
            String[] threeProperties = urlTimeAndUser.split(",");
            Byte diffUser = Byte.parseByte(threeProperties[1]);
            String twoPropertiesStr = threeProperties[0];
            String[] twoProperties = twoPropertiesStr.split("=");
            String actionUrl = twoProperties[0];
            Long noRepeatTime = Long.parseLong(twoProperties[1]);
            UserConfig userConfig = new UserConfig();
            userConfig.setDiffUser(diffUser);
            userConfig.setActionUrl(actionUrl);
            userConfig.setNoRepeatTime(noRepeatTime);
            userConfigList.add(userConfig);
        }
        return userConfigList;
    }

    //构造方法注入
//    public UserFilterConfig(AccessConfig accessConfig ){
//        this.accessConfig = accessConfig;
//        System.out.println("初始化accessConfig = " + accessConfig);
//    }




}
