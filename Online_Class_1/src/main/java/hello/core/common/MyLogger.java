package hello.core.common;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
// 프로토타입 형태의 클라이언트 리퀘스트에 따라 할당되는 옵션 value = "request"
// 프록시 설정을 해주는 proxyMode = ScopedProxyMode.TARGET_CLASS
// 프록시 대상이 인터페이스면 ScopedProxyMode.TARGET_INTERFACE로 바꿔주면 된다.
public class MyLogger {
    private String uuid;
    private String requestUrl;

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public void log(String message){
        System.out.println("[" + uuid + "]" + "[" + requestUrl + "] " +
                message);
    }

    @PostConstruct
    public void init(){
        uuid = UUID.randomUUID().toString();
        System.out.println("[" + uuid + "] request scope bean create" + this);
    }

    @PreDestroy
    public void close(){
        System.out.println("[" + uuid + "] request scope bean close" + this);

    }
}
