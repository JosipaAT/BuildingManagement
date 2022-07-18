package com.buildingmanagement.services.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buildingmanagement.entities.BuildingComplex;
import com.buildingmanagement.entities.Equipment;
import com.buildingmanagement.entities.Expense;
import com.buildingmanagement.entities.Income;
import com.buildingmanagement.repositories.BuildComplexRepo;
import com.buildingmanagement.repositories.UnitRepo;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class PDFGeneratorService {

    @Autowired
    private UnitRepo unitRepo;

    @Autowired
    private BuildComplexRepo buildComplexRepo;

    public void export(HttpServletResponse response, String username, String password, String userType) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontTitle.setSize(18);

        Paragraph paragraph = new Paragraph("Login Credentials for " + userType, fontTitle);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);

        Font fontParagraph = FontFactory.getFont(FontFactory.HELVETICA);
        fontParagraph.setSize(12);

        Font fontParagraph2 = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontParagraph2.setSize(14);

        Paragraph paragraph1 = new Paragraph("Username", fontParagraph2);
        Paragraph paragraph2 = new Paragraph(username, fontParagraph);
        Paragraph paragraph3 = new Paragraph("Password", fontParagraph2);
        Paragraph paragraph4 = new Paragraph(password, fontParagraph);
        paragraph1.setAlignment(Paragraph.ALIGN_LEFT);
        paragraph2.setAlignment(Paragraph.ALIGN_LEFT);
        paragraph3.setAlignment(Paragraph.ALIGN_LEFT);
        paragraph4.setAlignment(Paragraph.ALIGN_LEFT);

        document.add(paragraph);
        document.add(paragraph1);
        document.add(paragraph2);
        document.add(paragraph3);
        document.add(paragraph4);
        document.close();
    }

    public void exportReportForUnit(HttpServletResponse response, List<Expense> expenses, List<Income> incomes, Integer unitId) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontTitle.setSize(18);

        Paragraph paragraph = new Paragraph("Report for UnitId: " + unitId, fontTitle);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);

        Font fontParagraph = FontFactory.getFont(FontFactory.HELVETICA);
        fontParagraph.setSize(12);

        Font fontParagraph2 = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontParagraph2.setSize(14);

        int totalExpense = 0;
        int totalIncome = 0;
        for (int i = 0; i < expenses.size(); i++) {
            totalExpense += expenses.get(i)
                    .getAmount();
        }

        for (int i = 0; i < incomes.size(); i++) {
            totalIncome += incomes.get(i)
                    .getAmount();
        }

        Paragraph paragraph1 = new Paragraph("Co-Owner Name", fontParagraph2);
        Paragraph paragraph2 = new Paragraph(this.unitRepo.findById(unitId)
                .get()
                .getCoOwner()
                .getCoOwnerName(), fontParagraph);
        Paragraph paragraph3 = new Paragraph("Floor Name", fontParagraph2);
        Paragraph paragraph4 = new Paragraph(this.unitRepo.findById(unitId)
                .get()
                .getFloor()
                .getFloorName(), fontParagraph);
        Paragraph paragraph5 = new Paragraph("Total Expenses: ", fontParagraph2);
        Paragraph paragraph6 = new Paragraph(String.valueOf(totalExpense), fontParagraph);
        Paragraph paragraph7 = new Paragraph("Total Income:", fontParagraph2);
        Paragraph paragraph8 = new Paragraph(String.valueOf(totalIncome), fontParagraph);
        paragraph1.setAlignment(Paragraph.ALIGN_LEFT);
        paragraph2.setAlignment(Paragraph.ALIGN_LEFT);
        paragraph3.setAlignment(Paragraph.ALIGN_LEFT);
        paragraph4.setAlignment(Paragraph.ALIGN_LEFT);
        paragraph5.setAlignment(Paragraph.ALIGN_LEFT);
        paragraph6.setAlignment(Paragraph.ALIGN_LEFT);
        paragraph7.setAlignment(Paragraph.ALIGN_LEFT);
        paragraph8.setAlignment(Paragraph.ALIGN_LEFT);

        document.add(paragraph);
        document.add(paragraph1);
        document.add(paragraph2);
        document.add(paragraph3);
        document.add(paragraph4);
        document.add(paragraph5);
        document.add(paragraph6);
        document.add(paragraph7);
        document.add(paragraph8);
        document.close();
    }

    public void exportReportForBuilding(HttpServletResponse response, int totalExpense, int totalIncome, Integer buildComplexId) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontTitle.setSize(18);

        Paragraph paragraph = new Paragraph("Report for buildingComplexId: " + buildComplexId, fontTitle);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);

        Font fontParagraph = FontFactory.getFont(FontFactory.HELVETICA);
        fontParagraph.setSize(12);

        Font fontParagraph2 = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontParagraph2.setSize(14);

        Paragraph paragraph1 = new Paragraph("Building Manager Name", fontParagraph2);
        Paragraph paragraph2 = new Paragraph(this.buildComplexRepo.findById(buildComplexId)
                .get()
                .getBuildManager()
                .getBuildManagerName(), fontParagraph);
        Paragraph paragraph3 = new Paragraph("Co-Owner-Rep Name", fontParagraph2);
        Paragraph paragraph4 = new Paragraph(this.buildComplexRepo.findById(buildComplexId)
                .get()
                .getUsername(), fontParagraph);
        Paragraph paragraph5 = new Paragraph("Building Address", fontParagraph2);
        Paragraph paragraph6 = new Paragraph(this.buildComplexRepo.findById(buildComplexId)
                .get()
                .getStreetName() + " " + this.buildComplexRepo.findById(buildComplexId)
                .get()
                .getStreetNumber() + " " + this.buildComplexRepo.findById(buildComplexId)
                .get()
                .getCity() + " " + this.buildComplexRepo.findById(buildComplexId)
                .get()
                .getPostalCode(), fontParagraph);
        Paragraph paragraph7 = new Paragraph("No Of Floors", fontParagraph2);
        Paragraph paragraph8 = new Paragraph(String.valueOf(this.buildComplexRepo.findById(buildComplexId)
                .get()
                .getFloors()
                .size()), fontParagraph);
        Paragraph paragraph9 = new Paragraph("No Of Units", fontParagraph2);
        Paragraph paragraph10 = new Paragraph(String.valueOf(this.buildComplexRepo.findById(buildComplexId)
                .get()
                .getUnits()
                .size()), fontParagraph);
        Paragraph paragraph11 = new Paragraph("Total Expenses: ", fontParagraph2);
        Paragraph paragraph12 = new Paragraph(String.valueOf(totalExpense), fontParagraph);
        Paragraph paragraph13 = new Paragraph("Total Income:", fontParagraph2);
        Paragraph paragraph14 = new Paragraph(String.valueOf(totalIncome), fontParagraph);
        paragraph1.setAlignment(Paragraph.ALIGN_LEFT);
        paragraph2.setAlignment(Paragraph.ALIGN_LEFT);
        paragraph3.setAlignment(Paragraph.ALIGN_LEFT);
        paragraph4.setAlignment(Paragraph.ALIGN_LEFT);
        paragraph5.setAlignment(Paragraph.ALIGN_LEFT);
        paragraph6.setAlignment(Paragraph.ALIGN_LEFT);
        paragraph7.setAlignment(Paragraph.ALIGN_LEFT);
        paragraph8.setAlignment(Paragraph.ALIGN_LEFT);
        paragraph9.setAlignment(Paragraph.ALIGN_LEFT);
        paragraph10.setAlignment(Paragraph.ALIGN_LEFT);
        paragraph11.setAlignment(Paragraph.ALIGN_LEFT);
        paragraph12.setAlignment(Paragraph.ALIGN_LEFT);
        paragraph13.setAlignment(Paragraph.ALIGN_LEFT);
        paragraph14.setAlignment(Paragraph.ALIGN_LEFT);

        document.add(paragraph);
        document.add(paragraph1);
        document.add(paragraph2);
        document.add(paragraph3);
        document.add(paragraph4);
        document.add(paragraph5);
        document.add(paragraph6);
        document.add(paragraph7);
        document.add(paragraph8);
        document.add(paragraph9);
        document.add(paragraph10);
        document.add(paragraph11);
        document.add(paragraph12);
        document.add(paragraph13);
        document.add(paragraph14);
        document.close();
    }

    public void exportReportForEquipment(HttpServletResponse response, BuildingComplex buildingComplex) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontTitle.setSize(18);

        Paragraph paragraph = new Paragraph("Report for buildingComplexId " + buildingComplex.getBuildComplexId(), fontTitle);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);

        Font fontNormal = FontFactory.getFont(FontFactory.HELVETICA);
        fontNormal.setSize(12);

        Font fontBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontBold.setSize(14);

        Paragraph paragraph1 = new Paragraph("Building Manager Name", fontBold);
        Paragraph paragraph2 = new Paragraph(buildingComplex.getBuildManager()
                .getBuildManagerName(), fontNormal);
        Paragraph paragraph3 = new Paragraph("Co-Owner-Rep Name", fontBold);
        Paragraph paragraph4 = new Paragraph(buildingComplex.getUsername(), fontNormal);
        Paragraph paragraph5 = new Paragraph("Building Address", fontBold);
        Paragraph paragraph6 = new Paragraph(buildingComplex.getStreetName() + " " + buildingComplex.getStreetNumber() + " " + buildingComplex.getCity() + " " + buildingComplex.getPostalCode(), fontNormal);
        Paragraph paragraph7 = new Paragraph("No. Of Floors: " + buildingComplex.getFloors()
                .size(), fontBold);

        paragraph1.setAlignment(Paragraph.ALIGN_LEFT);
        paragraph2.setAlignment(Paragraph.ALIGN_LEFT);
        paragraph3.setAlignment(Paragraph.ALIGN_LEFT);
        paragraph4.setAlignment(Paragraph.ALIGN_LEFT);
        paragraph5.setAlignment(Paragraph.ALIGN_LEFT);
        paragraph6.setAlignment(Paragraph.ALIGN_LEFT);
        paragraph7.setAlignment(Paragraph.ALIGN_LEFT);

        document.add(paragraph);
        document.add(paragraph1);
        document.add(paragraph2);
        document.add(paragraph3);
        document.add(paragraph4);
        document.add(paragraph5);
        document.add(paragraph6);
        document.add(paragraph7);

        Map<String, List<Equipment>> equipmentByFloor = buildingComplex.getEquipments()
                .stream()
                .collect(Collectors.groupingBy(equipment -> equipment.getFloor()
                        .getFloorName()));

        for (Map.Entry<String, List<Equipment>> entry : equipmentByFloor.entrySet()) {

            Paragraph paragraphFloorName = new Paragraph(entry.getKey(), fontBold);
            paragraphFloorName.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(paragraphFloorName);

            PdfPTable table = new PdfPTable(4);
            table.setHeaderRows(1);
            table.setSpacingBefore(15);
            table.setSpacingAfter(15);

            PdfPCell numberCellHeader = new PdfPCell(new Phrase("No", fontBold));
            numberCellHeader.setPadding(10);
            table.addCell(numberCellHeader);

            PdfPCell equipmentNameHeader = new PdfPCell(new Phrase("Equipment", fontBold));
            equipmentNameHeader.setPadding(10);
            table.addCell(equipmentNameHeader);

            PdfPCell previousServiceDateHeader = new PdfPCell(new Phrase("Previous service date", fontBold));
            previousServiceDateHeader.setPadding(10);
            table.addCell(previousServiceDateHeader);

            PdfPCell nextServiceDateHeader = new PdfPCell(new Phrase("Next service date", fontBold));
            nextServiceDateHeader.setPadding(10);
            table.addCell(nextServiceDateHeader);

            table.completeRow();

            int number = 1;

            for (Equipment e : entry.getValue()) {

                PdfPCell numberCell = new PdfPCell(new Phrase(number + "."));
                numberCell.setPadding(10);
                table.addCell(numberCell);

                PdfPCell equipmentName = new PdfPCell(new Phrase(e.getEquipmentName()));
                equipmentName.setPadding(10);
                table.addCell(equipmentName);

                PdfPCell previousServiceDate = new PdfPCell(new Phrase(new SimpleDateFormat("dd-MM-yyyy").format(e.getDateOfService())));
                previousServiceDate.setPadding(10);
                table.addCell(previousServiceDate);

                PdfPCell nextServiceDate = new PdfPCell(new Phrase(new SimpleDateFormat("dd-MM-yyyy").format(e.getNextServiceDate())));
                nextServiceDate.setPadding(10);
                table.addCell(nextServiceDate);

                table.completeRow();
                number++;
            }

            document.add(table);
        }

        document.close();
    }
}

