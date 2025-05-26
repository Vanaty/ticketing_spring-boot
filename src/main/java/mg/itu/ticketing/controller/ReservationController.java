package mg.itu.ticketing.controller;

import mg.itu.ticketing.model.Reservation;
import mg.itu.ticketing.model.Status;
import mg.itu.ticketing.repository.ReservationRepository;
import mg.itu.ticketing.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/reservations")
public class ReservationController {
    
    private final ReservationRepository reservationRepository;
    
    @Autowired
    public ReservationController(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }
    
    @Autowired
    private ReservationService reservationPdfService;
    
    @GetMapping
    public String listReservations(@RequestParam(required = false) String status, Model model) {
        List<Reservation> reservations;
        
        if (status != null && !status.isEmpty()) {
            try {
                Status statusEnum = Status.valueOf(status.toUpperCase());
                reservations = reservationRepository.findByStatus(statusEnum);
                model.addAttribute("activeStatus", statusEnum);
            } catch (IllegalArgumentException e) {
                // If status parameter is invalid, show all reservations
                reservations = reservationRepository.findAll();
            }
        } else {
            reservations = reservationRepository.findAll();
        }
        
        model.addAttribute("reservations", reservations);
        model.addAttribute("statuses", Status.values());
        return "reservations";
    }
    
    @GetMapping("/{id}")
    public String viewReservation(@PathVariable("id") Long id, Model model) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Réservation non trouvée avec l'ID: " + id));
        model.addAttribute("reservation", reservation);
        return "reservation-detail";
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> exportReservationPdf(@PathVariable("id") Long id) throws IOException {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Réservation non trouvée avec l'ID: " + id));

        byte[] pdfBytes = reservationPdfService.generatePdf(reservation);

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=reservation-" + id + ".pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdfBytes);
    }
    
    /**
     * API REST pour exporter une réservation en PDF
     * Cette méthode est accessible sans authentification
     */
    @GetMapping(value = "/api/{id}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    @ResponseBody
    public ResponseEntity<byte[]> exportReservationPdfApi(@PathVariable("id") Long id) throws IOException {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Réservation non trouvée avec l'ID: " + id));

        byte[] pdfBytes = reservationPdfService.generatePdf(reservation);

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reservation-" + id + ".pdf")
            .header("Access-Control-Allow-Origin", "*")
            .header("Access-Control-Allow-Methods", "GET")
            .header("Access-Control-Allow-Headers", "Content-Type")
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdfBytes);
    }
}
