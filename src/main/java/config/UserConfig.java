package config;

/**
 * @ClassName: UserConfig
 * @Description:
 * @Author zm
 * @Date 2018/10/26 18:21
 **/
public class UserConfig {

    private Byte diffUser;//0区分用户(默认),1不区分用户
    private String actionUrl; //访问路径，默认所有url不控制访问,支持通配符
    private Long noRepeatTime;//此url在多少毫秒内不能重复访问,-1表示不限制(默认)

    public Byte getDiffUser() {
        return diffUser == null ? 0 : diffUser;
    }

    public void setDiffUser(Byte diffUser) {
        this.diffUser = diffUser;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public Long getNoRepeatTime() {
        return noRepeatTime == null?-1:noRepeatTime;
    }

    public void setNoRepeatTime(Long noRepeatTime) {
        this.noRepeatTime = noRepeatTime;
    }
}
