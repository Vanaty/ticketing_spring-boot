package mg.itu.ticketing.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Vol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne
    @JoinColumn(name = "id_avion")
    Avion avion;
    
    @ManyToOne
    @JoinColumn(name = "id_ville_dep")
    VilleDesservie villeDepart;

    @ManyToOne
    @JoinColumn(name = "id_ville_arr")
    VilleDesservie villeArrive;

    @Column(name = "datedepart")
    LocalDate dtDepart;

    @Column(name = "heuredepart")
    LocalTime heureDepart;

    LocalTime duree;

    @OneToMany(mappedBy = "vol", cascade = CascadeType.ALL)
    List<PrixVol> prixVols = new ArrayList<>();

    @OneToMany(mappedBy = "vol", cascade = CascadeType.ALL)
    List<Promotion> promotions = new ArrayList<>();
}
