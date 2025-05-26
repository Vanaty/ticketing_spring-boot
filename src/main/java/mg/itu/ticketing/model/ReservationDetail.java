package mg.itu.ticketing.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "reservationdetail")
public class ReservationDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_reservation")
    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name = "id_typesiege")
    private TypeSiege typeSiege;

    @ManyToOne
    @JoinColumn(name = "id_pricingrule")
    private PricingRule pricingRule;

    @Column(name = "prixnormale")
    private Double prixNormale;
    
    @Column(name = "pourcred")
    private Double pourcRed;

    @Column(name = "nbrpersonnes")
    private Integer nbrPersonnes;

    @Column(name = "nbrplaceenprom")
    private Integer nbrPlaceEnProm = 0;
    private Double prix;
}