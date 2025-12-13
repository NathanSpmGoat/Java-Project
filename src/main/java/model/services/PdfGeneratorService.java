package model.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import model.entities.Reservation;
import model.entities.Utilisateur;
import model.entities.Vehicule;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class PdfGeneratorService {

    private static final String DOWNLOAD_DIR = "src/main/download/";

    // Crée le dossier s'il n'existe pas
    static {
        File dir = new File(DOWNLOAD_DIR);
        if (!dir.exists()) dir.mkdirs();
    }

    /**
     * Génère un PDF pour une seule réservation
     */
    public static void generateReservationPDF(Reservation r, Utilisateur u, Vehicule v) {
        try {
            String safeName = u.getNom().replaceAll("[^a-zA-Z0-9]", "_").toLowerCase();
            String fileName = DOWNLOAD_DIR + "reservation_" + safeName + "_" + r.getId() + ".pdf";

            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(fileName));
            doc.open();

            Paragraph title = new Paragraph("Confirmation de réservation",
                    new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD));
            title.setAlignment(Element.ALIGN_CENTER);
            doc.add(title);
            doc.add(new Paragraph("\n"));

            doc.add(new Paragraph("Client : " + u.getNom() + " " + u.getPrenom()));
            doc.add(new Paragraph("Email : " + u.getEmail()));
            doc.add(new Paragraph("\n"));

            doc.add(new Paragraph("Véhicule réservé :"));
            doc.add(new Paragraph(v.getMarque() + " " + v.getModele()));
            doc.add(new Paragraph("Type : " + v.getType()));
            doc.add(new Paragraph("Prix/jour : " + v.getPrixJournalier() + " €"));
            doc.add(new Paragraph("\n"));

            doc.add(new Paragraph("Date début : " + r.getDateDebut()));
            doc.add(new Paragraph("Date fin : " + r.getDateFin()));
            doc.add(new Paragraph("Montant total : " + r.getMontantTotal() + " €"));

            doc.add(new Paragraph("\n\nMerci pour votre réservation !"));

            doc.close();

            System.out.println("PDF généré : " + fileName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Génère un PDF pour toutes les réservations
     */
    public static void generateAllReservationPDFs(List<Reservation> reservations) {
        for (Reservation r : reservations) {
            generateReservationPDF(r, r.getUtilisateur(), r.getVehicule());
        }
    }
}
