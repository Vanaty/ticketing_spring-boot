package mg.itu.ticketing.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PricingRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String libelle;
    private int minAge;
    private int maxAge;
    private double discountPercentage; // Pourcentage de réduction (ex: 30% pour les enfants)
}
