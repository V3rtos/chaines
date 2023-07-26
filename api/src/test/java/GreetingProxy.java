import me.moonways.bridgenet.api.intercept.MethodHandler;
import me.moonways.bridgenet.api.intercept.MethodInterceptor;
import me.moonways.bridgenet.api.intercept.ProxiedMethod;

@MethodInterceptor
public class GreetingProxy {

    @MethodHandler(target = Greeting.class)
    public Object handle(ProxiedMethod method, Object[] args) {
        System.out.println("Hello world!");

        return method.call(args);
    }
}
