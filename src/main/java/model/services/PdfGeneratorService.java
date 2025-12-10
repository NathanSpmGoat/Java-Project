package model.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import model.entities.Reservation;
import model.entities.Utilisateur;
import model.entities.Vehicule;

import java.io.FileOutputStream;

public class PdfGeneratorService {

    public static void generateReservationPDF(Reservation r, Utilisateur u, Vehicule v) {
        try {
            Document doc = new Document();
            String fileName = "reservation_" + r.getId() + ".pdf";
            PdfWriter.getInstance(doc, new FileOutputStream(fileName));

            doc.open();

            // Titre
            Paragraph title = new Paragraph("Confirmation de réservation",
                    new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD));
            title.setAlignment(Element.ALIGN_CENTER);
            doc.add(title);

            doc.add(new Paragraph("\n"));

            // Infos utilisateur
            doc.add(new Paragraph("Client : " + u.getNom() + " " + u.getPrenom()));
            doc.add(new Paragraph("Email : " + u.getEmail()));

            doc.add(new Paragraph("\n"));

            // Infos véhicule
            doc.add(new Paragraph("Véhicule réservé :"));
            doc.add(new Paragraph(v.getMarque() + " " + v.getModele()));
            doc.add(new Paragraph("Type : " + v.getType()));
            doc.add(new Paragraph("Prix/jour : " + v.getPrixJournalier() + " €"));

            doc.add(new Paragraph("\n"));

            // Infos réservation
            doc.add(new Paragraph("Date début : " + r.getDateDebut()));
            doc.add(new Paragraph("Date fin : " + r.getDateFin()));
            doc.add(new Paragraph("Montant total : " + r.getMontantTotal() + " €"));

            doc.add(new Paragraph("\n\n"));
            doc.add(new Paragraph("Merci pour votre réservation !"));

            doc.close();

            System.out.println("PDF généré : " + fileName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
