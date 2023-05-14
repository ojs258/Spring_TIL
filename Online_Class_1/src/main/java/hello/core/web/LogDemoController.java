package hello.core.web;

import hello.core.common.MyLogger;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class LogDemoController {

    private  final LogDemoService logDemoService;
    private final /*ObjectProvider<MyLogger>*/MyLogger myLogger/*Provider*/;

    @RequestMapping("log-demo") // url파라미터와 매칭해주는 어노테이션
    @ResponseBody // view없이 바로 출력해주는 어노테이션
    public String logDemo(HttpServletRequest request) {
        String requestUrl = request.getRequestURL().toString();
        System.out.println("myLogger" + myLogger.getClass());
//        MyLogger myLogger = myLoggerProvider.getObject();
        myLogger.setRequestUrl(requestUrl);
        myLogger.log("controller log");
        logDemoService.logic("testId");
        return "OK";
    }
}
