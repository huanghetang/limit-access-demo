package config;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * @ClassName: NewInterceptor
 * @Description:
 * @Author zm
 * @Date 2018/10/25 16:43
 **/

public class NewInterceptor extends HandlerInterceptorAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(NewInterceptor.class);

    private UserFilterConfig userFilterConfig;//用户访问频率配置类

    public NewInterceptor() {
    }

    public NewInterceptor(UserFilterConfig userFilterConfig) {
        this.userFilterConfig = userFilterConfig;
    }

    @Autowired
    private StringRedisTemplate redisTemplate;//redis记录用户访问信息

    private ThreadLocal<String> threadLocalKey = new ThreadLocal<String>();//记录保存在redis中的key

    private ThreadLocal<UserConfig> threadLocalUserConfig = new ThreadLocal<UserConfig>();//记录与当前url匹配的配置

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            List<UserConfig> userConfigList = userFilterConfig.getUserConfigList();
            if (CollectionUtils.isEmpty(userConfigList)) {
                return true;
            }
            //获取用户信息
            User user = getUser(request);

            if (user == null) {
                return true; //用户没有登录放行
            }
            boolean isLimit = false; //当前的访问的url是否需要拦截

            UserConfig limitUserConfig = null; //当前访问的url匹配到的拦截配置对象

            for (UserConfig userConfig : userConfigList) {
                if (userConfig.getNoRepeatTime() == -1) {//没有配置不重复时间直接放行
                    continue;
                }
                if (isLimitUrl(request, userConfig)) { //当前的url吻合配置的url
                    isLimit = true;
                    limitUserConfig = userConfig;
                    break;
                }

            }
            if (isLimit == false && limitUserConfig == null) {
                return true;
            }

            threadLocalKey.set(generateKeyFromConfig(limitUserConfig, user, request));//生成key并放入线程域中

            threadLocalUserConfig.set(limitUserConfig);

            if (limitUserConfig.getNoRepeatTime() != -1) { //设置了不重复时间

                String s = redisTemplate.opsForValue().get(threadLocalKey.get());

                if (s != null && !"-2".equals(s)) { //有key,没有过期，之前已有访问
                    // todo 如果返回值为json 则返回数据
                    String contentType = response.getContentType();

                    System.out.println("contentType = " + contentType);

                    response.sendRedirect(request.getContextPath() + "/htmlError.html");//拦截后转到错误页面

                    return false;
                }
            }
        } catch (Exception e) {
            LOGGER.error("用户频率拦截器发生错误",e);
        }

        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        try {
            String contentType = response.getContentType();

            System.out.println("contentType = " + contentType);

            if (threadLocalUserConfig.get() == null) {
                super.afterCompletion(request, response, handler, ex);
                return;
            }
            UserConfig currentUserConfig = threadLocalUserConfig.get();

            if (currentUserConfig.getNoRepeatTime() != -1 && threadLocalKey.get() != null) {
                //请求成功后设置该用户和路径的过期时间

                redisTemplate.opsForValue().set(threadLocalKey.get(), "1", currentUserConfig.getNoRepeatTime(), TimeUnit.MILLISECONDS);
            }

            threadLocalKey.remove();//key清空

            threadLocalUserConfig.remove();

        } catch (Exception e) {
            LOGGER.error("用户频率拦截器发生错误",e);
        }
        super.afterCompletion(request, response, handler, ex);
    }

    private User getUser(HttpServletRequest request) {
        String testUserId = request.getParameter("id");
        User user = new User();
        user.setId(testUserId);
        user.setName("景甜");
        return user;
    }


    //判断这个url是否匹配到需要限制访问的url
    private boolean isLimitUrl(HttpServletRequest request, UserConfig userConfig) {
        String actionUrl = userConfig.getActionUrl();
        if (StringUtils.isBlank(actionUrl)) {
            return false;
        }
        if (userConfig.getNoRepeatTime() == -1) {//没有配置不重复时间
            return false;
        }

        if (actionUrl.startsWith("/*")) {
            return false;
        }
        if (!actionUrl.startsWith("/*") && actionUrl.contains("/*")) {// "/report/*"=>"/report"
            actionUrl = actionUrl.substring(0, actionUrl.indexOf("/*"));
        }
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith(actionUrl)) {//以这个uri开始
            return true;
        }
        return false;
    }


    //根据配置生成一个redisKey
    private String generateKeyFromConfig(UserConfig userConfig, User user, HttpServletRequest request) {

        String key = "";
        //根据配置设置key和过期时间
        if (userConfig.getDiffUser() == 0L) { // 区分用户
            key += user.getId() + ":";
        }
        if (StringUtils.isNotBlank(userConfig.getActionUrl())) {
            //除去host（域名或者ip）部分的路径
            String actionUri = request.getRequestURI();
            if (StringUtils.isNotBlank(actionUri)) {
                key += actionUri;
            }
        }
        return key;
    }

    public static void main(String[] args) {
        boolean matches = Pattern.matches("/report/*", "/report/action");
        String a = "/report/*";
        String substring = a.substring(0, a.indexOf("/*"));
        System.out.println("substring = " + substring);
    }
}
