package mg.itu.ticketing.repository;

import mg.itu.ticketing.model.PricingRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PricingRuleRepository extends JpaRepository<PricingRule, Long> {

    @Query("SELECT p FROM PricingRule p WHERE :age BETWEEN p.minAge AND p.maxAge")
    PricingRule findDiscountByAge(@Param("age") int age);
}
