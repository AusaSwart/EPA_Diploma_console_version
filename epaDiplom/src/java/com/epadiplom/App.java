package java.com.epadiplom;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.com.epadiplom.config.SpringConfig;

public class App {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);



    }
}