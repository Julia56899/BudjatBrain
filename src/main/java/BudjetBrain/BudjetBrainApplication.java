package BudjetBrain;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "BudjetBrain")
public class BudjetBrainApplication {
    public static void main (String[] args) {
        SpringApplication.run(BudjetBrainApplication.class,args);
    }
}
