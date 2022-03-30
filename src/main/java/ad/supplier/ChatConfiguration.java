package ad.supplier;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author natalija
 */
@Configuration
@EnableAsync
@ComponentScan("ad.supplier")
public class ChatConfiguration {

}
