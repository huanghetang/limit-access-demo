package config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * @ClassName: RegisterNewInterceptor
 * @Description:
 * @Author zm
 * @Date 2018/10/26 9:45
 **/
@Configuration
public class RegisterNewInterceptor implements WebMvcConfigurer {

    @Autowired
    private UserFilterConfig userFilterConfig;
    @Bean
    public NewInterceptor getNewInterceptor(){
        return new NewInterceptor(userFilterConfig);
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.getNewInterceptor()).addPathPatterns("/**");
    }
}
