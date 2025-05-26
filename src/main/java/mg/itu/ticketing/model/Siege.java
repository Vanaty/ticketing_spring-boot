package mg.itu.ticketing.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "siege")
public class Siege {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String libelle;

    @ManyToOne
    @JoinColumn(name = "id_avion")
    Avion avion;

    @ManyToOne    
    @JoinColumn(name = "id_typesiege")
    TypeSiege typeSiege;
    
    Integer nbr;

}