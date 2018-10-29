import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.MyProps;
import config.limitApp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @ClassName: PropTest
 * @Description:
 * @Author zm
 * @Date 2018/10/26 16:09
 **/
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = limitApp.class)
//@EnableConfigurationProperties(MyProps.class)
public class PropTest {
    @Autowired
    private MyProps myProps;

    @Test
    public void propsTest() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println("simpleProp: " + myProps.getSimpleProp());
        System.out.println("arrayProps: " + objectMapper.writeValueAsString(myProps.getArrayProps()));
        System.out.println("listProp1: " + objectMapper.writeValueAsString(myProps.getListProp1()));
        System.out.println("listProp2: " + objectMapper.writeValueAsString(myProps.getListProp2()));
        System.out.println("mapProps: " + objectMapper.writeValueAsString(myProps.getMapProps()));
    }

}
