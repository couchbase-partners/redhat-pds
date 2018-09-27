package hello;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Component
public class BackgroundThread implements DisposableBean, Runnable {

    private Thread thread;

    @Value("${person.name}")
    private String cmdTest;

    public BackgroundThread() {
        System.out.println("TEST");
        this.thread = new Thread(this);
        this.thread.start();
    }

    @Override
    @PostConstruct //postconstruct ensures that nothing is loaded until values are loaded
    public void run() {
        System.out.println("Starting background thread!");
        while (true) {
            System.out.println("Hello from background thread");
            System.out.println("person.name = " + cmdTest);
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    @Override
    public void destroy() throws Exception {
    }
}
