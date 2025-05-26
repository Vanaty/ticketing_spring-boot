package mg.itu.ticketing.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import mg.itu.ticketing.model.Reservation;
import mg.itu.ticketing.model.Status;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

@Service
public class ReservationService {
    public byte[] generatePdf(Reservation reservation) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        try {
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            document.open();
            
            // Ajout d'une bordure bleue en haut de page
            Rectangle rect = new Rectangle(document.getPageSize());
            rect.setBorder(Rectangle.BOX);
            rect.setBorderWidth(2);
            rect.setBorderColor(new BaseColor(66, 133, 244)); // Bleu Google
            document.add(rect);
            
            // Titre principal avec style
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, new BaseColor(33, 33, 33));
            Paragraph title = new Paragraph("Réservation #" + reservation.getId(), titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);
            
            // Informations générales avec style
            Font sectionFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, new BaseColor(66, 133, 244));
            Paragraph infoSection = new Paragraph("Informations Générales", sectionFont);
            infoSection.setSpacingBefore(10);
            infoSection.setSpacingAfter(10);
            document.add(infoSection);
            
            // Tableau pour les informations générales
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setSpacingAfter(20);
            
            // Style des cellules
            Font labelFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.DARK_GRAY);
            Font valueFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);
            
            // Formatage des dates
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm");
            
            // Statut avec couleur selon le statut
            addTableRow(infoTable, "Statut:", getStatusText(reservation.getStatus()), labelFont, valueFont);
            
            // Autres informations
            addTableRow(infoTable, "Date de réservation:", 
                    reservation.getDaty() != null ? dateFormat.format(java.sql.Timestamp.valueOf(reservation.getDaty())) : "N/A", 
                    labelFont, valueFont);
            addTableRow(infoTable, "Prix total:", 
                    String.format("%,.2f Ar", reservation.getPrixTotal()), 
                    labelFont, valueFont);
            addTableRow(infoTable, "Nombre de places:", 
                    String.valueOf(reservation.getNbrPlaces()), 
                    labelFont, valueFont);
            
            if (reservation.getNbrPlacesAnnuller() > 0) {
                addTableRow(infoTable, "Places annulées:", 
                        String.valueOf(reservation.getNbrPlacesAnnuller()), 
                        labelFont, new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.RED));
            }
            
            document.add(infoTable);
            
            // Section Vol
            if (reservation.getVol() != null) {
                Paragraph volSection = new Paragraph("Détails du Vol", sectionFont);
                volSection.setSpacingBefore(10);
                volSection.setSpacingAfter(10);
                document.add(volSection);
                
                PdfPTable volTable = new PdfPTable(2);
                volTable.setWidthPercentage(100);
                volTable.setSpacingAfter(20);
                
                String trajet = (reservation.getVol().getVilleDepart() != null ? 
                        reservation.getVol().getVilleDepart().getVille() : "N/A") + 
                        " → " + 
                        (reservation.getVol().getVilleArrive() != null ? 
                        reservation.getVol().getVilleArrive().getVille() : "N/A");
                
                addTableRow(volTable, "Trajet:", trajet, labelFont, valueFont);
                
                SimpleDateFormat dateOnlyFormat = new SimpleDateFormat("dd MMMM yyyy");
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                
                if (reservation.getVol().getDtDepart() != null) {
                    addTableRow(volTable, "Date de départ:", 
                            dateOnlyFormat.format(java.sql.Date.valueOf(reservation.getVol().getDtDepart())), 
                            labelFont, valueFont);
                }
                
                if (reservation.getVol().getHeureDepart() != null) {
                    addTableRow(volTable, "Heure de départ:", 
                            timeFormat.format(java.sql.Time.valueOf(reservation.getVol().getHeureDepart())), 
                            labelFont, valueFont);
                }
                
                document.add(volTable);
            }
            
            // Section Détails des Places
            if (reservation.getDetails() != null && !reservation.getDetails().isEmpty()) {
                Paragraph detailsSection = new Paragraph("Détails des Places", sectionFont);
                detailsSection.setSpacingBefore(10);
                detailsSection.setSpacingAfter(10);
                document.add(detailsSection);
                
                PdfPTable detailsTable = new PdfPTable(4);
                detailsTable.setWidths(new float[]{3, 2, 3, 2});
                detailsTable.setWidthPercentage(100);
                
                // En-têtes du tableau
                Font headerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);
                PdfPCell headerCell;
                
                headerCell = new PdfPCell(new Phrase("Type de siège", headerFont));
                headerCell.setBackgroundColor(new BaseColor(66, 133, 244));
                headerCell.setPadding(8);
                detailsTable.addCell(headerCell);
                
                headerCell = new PdfPCell(new Phrase("Personnes", headerFont));
                headerCell.setBackgroundColor(new BaseColor(66, 133, 244));
                headerCell.setPadding(8);
                detailsTable.addCell(headerCell);
                
                headerCell = new PdfPCell(new Phrase("Réduction appliquée", headerFont));
                headerCell.setBackgroundColor(new BaseColor(66, 133, 244));
                headerCell.setPadding(8);
                detailsTable.addCell(headerCell);
                
                headerCell = new PdfPCell(new Phrase("Prix", headerFont));
                headerCell.setBackgroundColor(new BaseColor(66, 133, 244));
                headerCell.setPadding(8);
                detailsTable.addCell(headerCell);
                
                // Lignes de données
                for (var detail : reservation.getDetails()) {
                    String typeSiege = detail.getTypeSiege() != null ? detail.getTypeSiege().getLibelle() : "N/A";
                    String personnes = detail.getNbrPersonnes() + " personne(s)";
                    String pricingRule = detail.getPricingRule() != null ? 
                            detail.getPricingRule().getLibelle() + " (" + detail.getPricingRule().getDiscountPercentage() + "%)" : 
                            "Aucune réduction";
                    String prix = String.format("%,.2f Ar", detail.getPrix());
                    
                    PdfPCell cell;
                    Font cellFont = new Font(Font.FontFamily.HELVETICA, 10);
                    
                    cell = new PdfPCell(new Phrase(typeSiege, cellFont));
                    cell.setPadding(6);
                    detailsTable.addCell(cell);
                    
                    cell = new PdfPCell(new Phrase(personnes, cellFont));
                    cell.setPadding(6);
                    detailsTable.addCell(cell);
                    
                    cell = new PdfPCell(new Phrase(pricingRule, cellFont));
                    cell.setPadding(6);
                    detailsTable.addCell(cell);
                    
                    cell = new PdfPCell(new Phrase(prix, cellFont));
                    cell.setPadding(6);
                    detailsTable.addCell(cell);
                }
                
                document.add(detailsTable);
            }
            
            // Pied de page
            Paragraph footer = new Paragraph("Document généré automatiquement. Réservation émise le " + 
                    (reservation.getDaty() != null ? new SimpleDateFormat("dd/MM/yyyy").format(java.sql.Timestamp.valueOf(reservation.getDaty())) : "N/A"),
                    new Font(Font.FontFamily.HELVETICA, 8, Font.ITALIC, BaseColor.GRAY));
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setSpacingBefore(30);
            document.add(footer);
            
        } catch (DocumentException e) {
            throw new IOException("Erreur lors de la génération du PDF", e);
        } finally {
            document.close();
        }
        return baos.toByteArray();
    }
    
    private void addTableRow(PdfPTable table, String label, String value, Font labelFont, Font valueFont) {
        PdfPCell cell = new PdfPCell(new Phrase(label, labelFont));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(5);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase(value, valueFont));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(5);
        table.addCell(cell);
    }
    
    private String getStatusText(Status status) {
        if (status == null) return "Inconnu";
        switch (status) {
            case PENDING: return "En attente";
            case CONFIRMED: return "Confirmée";
            case CANCELLED: return "Annulée";
            default: return status.toString();
        }
    }
}
