package mg.itu.ticketing.config;

import mg.itu.ticketing.model.PricingRule;
import mg.itu.ticketing.repository.PricingRuleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner loadData(PricingRuleRepository pricingRuleRepository) {
        return args -> {
            if (pricingRuleRepository.count() == 0) {
                pricingRuleRepository.save(new PricingRule(null,"Enfant", 0, 12, 30.0));
                pricingRuleRepository.save(new PricingRule(null,"Agee", 60, 120, 20.0));
            }
        };
    }
}
