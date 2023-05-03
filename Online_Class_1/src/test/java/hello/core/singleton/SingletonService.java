package hello.core.singleton;

import javax.lang.model.SourceVersion;

public class SingletonService {

    public static final SingletonService instance = new SingletonService();

    public static SingletonService getInstance() {
        return instance;
    }

    private SingletonService(){

    }

    public void logic(){
        System.out.println("SingletonService.logic");
    }
}
