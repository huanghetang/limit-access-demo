package config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: TestController
 * @Description:
 * @Author zm
 * @Date 2018/10/26 10:31
 **/
@Controller
public class TestController {

    @Autowired
    private MyProps myProps;

    @GetMapping("test")
    @ResponseBody
    public String test(@RequestParam(required = false) Long id){
        return "hello test user id:"+id;
    }

    @GetMapping("limit")
    @ResponseBody
    public String limit(@RequestParam(required = false) Long id){
        return "hello limit user id:"+id;
    }

    @GetMapping("noLimit")
    @ResponseBody
    public String noLimit(@RequestParam(required = false) Long id) throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        System.out.println("simpleProp: " + myProps.getSimpleProp());
//        System.out.println("arrayProps: " + objectMapper.writeValueAsString(myProps.getArrayProps()));
//        System.out.println("listProp1: " + objectMapper.writeValueAsString(myProps.getListProp1()));
//        System.out.println("listProp2: " + objectMapper.writeValueAsString(myProps.getListProp2()));
//        System.out.println("mapProps: " + objectMapper.writeValueAsString(myProps.getMapProps()));
////        return "hello noLimit user id:"+id;
        return "htmlError2";
    }
}
