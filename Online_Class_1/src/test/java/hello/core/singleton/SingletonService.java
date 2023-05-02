package hello.core.singleton;

public class SingletonService {

    public static final SingletonService instance = new SingletonService();

    public static SingletonService getInstance() {
        return instance;
    }
}
