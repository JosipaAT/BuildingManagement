package com.buildingmanagement.services.impl;

import com.buildingmanagement.entities.*;
import com.buildingmanagement.repositories.BuildComplexRepo;
import com.buildingmanagement.repositories.ExpenseRepo;
import com.buildingmanagement.repositories.IncomeRepo;
import com.buildingmanagement.repositories.UnitRepo;
import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PDFGeneratorService {

    @Autowired
    private UnitRepo unitRepo;

    @Autowired
    private BuildComplexRepo buildComplexRepo;

    @Autowired
    private IncomeRepo incomeRepo;

    @Autowired
    private ExpenseRepo expenseRepo;

    public void export(HttpServletResponse response, String username, String password, String userType) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font fontTitle = FontFactory.getFont(FontFactory.TIMES_ROMAN, BaseFont.CP1250, 16);
        fontTitle.setSize(18);

        Paragraph paragraph = new Paragraph("Login Credentials for " + userType, fontTitle);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        Paragraph paragraph1 = new Paragraph(" ", fontTitle);
        paragraph1.setAlignment(Paragraph.ALIGN_CENTER);

        Font fontParagraph = FontFactory.getFont(FontFactory.TIMES_ROMAN, BaseFont.CP1250, 16);
        fontParagraph.setSize(12);

        Font fontParagraph2 = FontFactory.getFont(FontFactory.TIMES_ROMAN, BaseFont.CP1250, 16);
        fontParagraph2.setSize(14);

        Paragraph paragraph2 = new Paragraph("Username: " + username, fontParagraph2);
        Paragraph paragraph3 = new Paragraph("   ", fontParagraph);
        Paragraph paragraph4 = new Paragraph("Password: " + password, fontParagraph2);
        Paragraph paragraph5 = new Paragraph("   ", fontParagraph);
        paragraph2.setAlignment(Paragraph.ALIGN_LEFT);
        paragraph3.setAlignment(Paragraph.ALIGN_LEFT);
        paragraph4.setAlignment(Paragraph.ALIGN_LEFT);
        paragraph5.setAlignment(Paragraph.ALIGN_LEFT);

        document.add(paragraph);
        document.add(paragraph1);
        document.add(paragraph2);
        document.add(paragraph3);
        document.add(paragraph4);
        document.add(paragraph5);
        document.close();
    }

    public void exportReportForUnit(HttpServletResponse response, List<Expense> expenses, List<Income> incomes, Integer unitId) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font fontTitle = FontFactory.getFont(FontFactory.TIMES_ROMAN, BaseFont.CP1250, 16);
        fontTitle.setSize(18);

        Paragraph paragraph = new Paragraph("Report for UnitId: " + unitId, fontTitle);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);

        Font fontParagraph = FontFactory.getFont(FontFactory.TIMES_ROMAN, BaseFont.CP1250, 16);
        fontParagraph.setSize(12);

        Font fontParagraph2 = FontFactory.getFont(FontFactory.TIMES_ROMAN, BaseFont.CP1250, 16);
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
        Paragraph paragraph5 = new Paragraph("Unit Type: ", fontParagraph2);
        Paragraph paragraph6 = new Paragraph(String.valueOf(this.unitRepo.findById(unitId)
                .get()
                .getUnitType()), fontParagraph);
        Paragraph paragraph7 = new Paragraph("Total Expenses: ", fontParagraph2);
        Paragraph paragraph8 = new Paragraph(String.valueOf(totalExpense), fontParagraph);
        Paragraph paragraph9 = new Paragraph("Total Income:", fontParagraph2);
        Paragraph paragraph10 = new Paragraph(String.valueOf(totalIncome), fontParagraph);
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
        document.close();
    }

    public void exportReportForBuilding(HttpServletResponse response, int totalExpense, int totalIncome, Integer buildComplexId) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font fontTitle = FontFactory.getFont(FontFactory.TIMES_ROMAN, BaseFont.CP1250, 16);
        fontTitle.setSize(18);

//        Paragraph paragraph = new Paragraph("Report for buildingComplexId: " + buildComplexId, fontTitle);
        Paragraph paragraph = new Paragraph("Basic Building Info ", fontTitle);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);

        Font fontParagraph = FontFactory.getFont(FontFactory.TIMES_ROMAN, BaseFont.CP1250, 16);
        fontParagraph.setSize(12);

        Font fontParagraph2 = FontFactory.getFont(FontFactory.TIMES_ROMAN, BaseFont.CP1250, 16);
        fontParagraph2.setSize(14);

        Paragraph paragraph1 = new Paragraph("Building Manager Name", fontParagraph2);
        Paragraph paragraph2 = new Paragraph(this.buildComplexRepo.findById(buildComplexId)
                .get()
                .getBuildManager()
                .getBuildManagerName(), fontParagraph);
        Paragraph paragraph3 = new Paragraph("Building Username", fontParagraph2);
        Paragraph paragraph4 = new Paragraph(this.buildComplexRepo.findById(buildComplexId)
                .get()
                .getUsername(), fontParagraph);
        Paragraph paragraph5 = new Paragraph("Password", fontParagraph2);
        Paragraph paragraph6 = new Paragraph(this.buildComplexRepo.findById(buildComplexId)
                .get()
                .getPassword(), fontParagraph);
        Paragraph paragraph7 = new Paragraph("Building Address", fontParagraph2);
        Paragraph paragraph8 = new Paragraph(this.buildComplexRepo.findById(buildComplexId)
                .get()
                .getStreetName() + " " + this.buildComplexRepo.findById(buildComplexId)
                .get()
                .getStreetNumber() + " " + this.buildComplexRepo.findById(buildComplexId)
                .get()
                .getCity() + " " + this.buildComplexRepo.findById(buildComplexId)
                .get()
                .getPostalCode(), fontParagraph);
        Paragraph paragraph9 = new Paragraph("No Of Floors", fontParagraph2);
        Paragraph paragraph10 = new Paragraph(String.valueOf(this.buildComplexRepo.findById(buildComplexId)
                .get()
                .getFloors()
                .size()), fontParagraph);
        Paragraph paragraph11 = new Paragraph("No Of Units", fontParagraph2);
        Paragraph paragraph12 = new Paragraph(String.valueOf(this.buildComplexRepo.findById(buildComplexId)
                .get()
                .getUnits()
                .size()), fontParagraph);
        Paragraph paragraph13 = new Paragraph("Total Expenses Combined: ", fontParagraph2);
        Paragraph paragraph14 = new Paragraph(String.valueOf(totalExpense), fontParagraph);
        Paragraph paragraph15 = new Paragraph("Total Income Combined:", fontParagraph2);
        Paragraph paragraph16 = new Paragraph(String.valueOf(totalIncome), fontParagraph);
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
        paragraph15.setAlignment(Paragraph.ALIGN_LEFT);
        paragraph16.setAlignment(Paragraph.ALIGN_LEFT);

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
        document.add(paragraph15);
        document.add(paragraph16);
        document.close();
    }

    //report za sve troskove u razdoblju
    public void exportReportForExpenses(HttpServletResponse response, Unit unit, List<Expense> filteredExpenses, Date startDate, Date endDate) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font fontTitle = FontFactory.getFont(FontFactory.TIMES_ROMAN, BaseFont.CP1250, 16);
        fontTitle.setSize(18);

        Paragraph paragraph = new Paragraph("Report for unit " + unit.getUnitId() + " on floor " + unit.getFloor()
                .getFloorName(), fontTitle);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(paragraph);

        Font fontNormal = FontFactory.getFont(FontFactory.TIMES_ROMAN, BaseFont.CP1250, 12);
        fontNormal.setSize(12);

        Font fontBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, BaseFont.CP1250, 14);
        fontBold.setSize(14);

        if (unit.getCoOwner() != null) {
            Paragraph paragraph1 = new Paragraph("Building CoOwner Name", fontBold);
            Paragraph paragraph2 = new Paragraph(unit.getCoOwner()
                    .getCoOwnerName(), fontNormal);
            paragraph1.setAlignment(Paragraph.ALIGN_LEFT);
            paragraph2.setAlignment(Paragraph.ALIGN_LEFT);
            document.add(paragraph1);
            document.add(paragraph2);
        }
        Paragraph paragraph5 = new Paragraph("Building Address", fontBold);
        Paragraph paragraph6 = new Paragraph(unit.getBuildingComplex()
                .getStreetName() + " " + unit.getBuildingComplex()
                .getStreetNumber() + " " + unit.getBuildingComplex()
                .getCity() + " " + unit.getBuildingComplex()
                .getPostalCode(), fontNormal);

        paragraph5.setAlignment(Paragraph.ALIGN_LEFT);
        paragraph6.setAlignment(Paragraph.ALIGN_LEFT);

        document.add(paragraph5);
        document.add(paragraph6);

        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

        if (startDate != null) {
            Paragraph paragraph7 = new Paragraph("Period: " + formatter.format(startDate) + " - " + formatter.format(endDate), fontBold);
            paragraph7.setAlignment(Paragraph.ALIGN_LEFT);
            document.add(paragraph7);

        }

        Map<ExpenseType, List<Expense>> expenseTypeListMap = filteredExpenses.stream()
                .collect(Collectors.groupingBy(Expense::getExpenseType));


        for (Map.Entry<ExpenseType, List<Expense>> entry : expenseTypeListMap.entrySet()) {
            Paragraph expenseTypeParagraph = new Paragraph("Expense Type: " + entry.getKey()
                    .getExpenseTypeName());
            expenseTypeParagraph.setAlignment(Paragraph.ALIGN_LEFT);
            document.add(expenseTypeParagraph);


            PdfPTable tableExpenses = new PdfPTable(6);
            tableExpenses.setWidthPercentage(100);
            tableExpenses.setHeaderRows(1);
            tableExpenses.setSpacingBefore(15);
            tableExpenses.setSpacingAfter(15);

            PdfPCell numberCellHeader = new PdfPCell(new Phrase("No", fontBold));
            numberCellHeader.setPadding(10);
            tableExpenses.addCell(numberCellHeader);

            PdfPCell descriptionHeader = new PdfPCell(new Phrase("Description", fontBold));
            descriptionHeader.setPadding(10);
            tableExpenses.addCell(descriptionHeader);

            PdfPCell expenseTypeHeader = new PdfPCell(new Phrase("Expense Type", fontBold));
            expenseTypeHeader.setPadding(10);
            tableExpenses.addCell(expenseTypeHeader);

            PdfPCell dateCreatedHeader = new PdfPCell(new Phrase("Date Created", fontBold));
            dateCreatedHeader.setPadding(10);
            tableExpenses.addCell(dateCreatedHeader);

            PdfPCell dueDateHeader = new PdfPCell(new Phrase("Due Date", fontBold));
            dueDateHeader.setPadding(10);
            tableExpenses.addCell(dueDateHeader);

            PdfPCell amountHeader = new PdfPCell(new Phrase("Amount in KN", fontBold));
            amountHeader.setPadding(10);
            tableExpenses.addCell(amountHeader);


            tableExpenses.completeRow();

            int number = 1;
            Float totalAmount = Float.valueOf(0);

            for (Expense expense : entry.getValue()) {

                PdfPCell numberCell = new PdfPCell(new Phrase(number + "."));
                numberCell.setPadding(10);
                tableExpenses.addCell(numberCell);

                PdfPCell description = new PdfPCell(new Phrase(expense.getDescription()));
                description.setPadding(10);
                tableExpenses.addCell(description);

                PdfPCell expenseType = new PdfPCell(new Phrase(expense.getExpenseType()
                        .getExpenseTypeName()));
                expenseType.setPadding(10);
                tableExpenses.addCell(expenseType);

                PdfPCell dateOfReceipt = new PdfPCell(new Phrase(formatter.format(expense.getDateOfReceipt())));
                dateOfReceipt.setPadding(10);
                tableExpenses.addCell(dateOfReceipt);

                PdfPCell dueDate = new PdfPCell(new Phrase(formatter.format(expense.getDueDate())));
                dueDate.setPadding(10);
                tableExpenses.addCell(dueDate);

                PdfPCell amount = new PdfPCell(new Phrase(String.valueOf(expense.getAmount())));
                amount.setPadding(10);
                tableExpenses.addCell(amount);

                tableExpenses.completeRow();
                number++;

                totalAmount += expense.getAmount();
            }
            document.add(tableExpenses);

            // INCOME
            PdfPTable tableIncome = new PdfPTable(5);
            tableIncome.setWidthPercentage(100);
            tableIncome.setHeaderRows(1);
            tableIncome.setSpacingBefore(15);
            tableIncome.setSpacingAfter(15);

            tableIncome.addCell(numberCellHeader);
            tableIncome.addCell(descriptionHeader);
            tableIncome.addCell(expenseTypeHeader);
            tableIncome.addCell(dateCreatedHeader);
            tableIncome.addCell(amountHeader);


            tableIncome.completeRow();

            int numberIncome = 1;
            Float totalAmountIncome = Float.valueOf(0);

            List<Income> incomes = incomeRepo.findAllByExpenseType_expenseTypeNameAndBuildingComplex_buildComplexIdAndUnit_unitId(entry.getKey().getExpenseTypeName(), unit.getBuildingComplex().getBuildComplexId(), unit.getUnitId());


            for (Income income : incomes) {

                PdfPCell numberCell = new PdfPCell(new Phrase(numberIncome + "."));
                numberCell.setPadding(10);
                tableIncome.addCell(numberCell);

                PdfPCell description = new PdfPCell(new Phrase(income.getDescription()));
                description.setPadding(10);
                tableIncome.addCell(description);

                PdfPCell expenseType = new PdfPCell(new Phrase(income.getExpenseType().getExpenseTypeName()));
                expenseType.setPadding(10);
                tableIncome.addCell(expenseType);

                PdfPCell dateOfReceipt = new PdfPCell(new Phrase(formatter.format(income.getDateOfPayment())));
                dateOfReceipt.setPadding(10);
                tableIncome.addCell(dateOfReceipt);

                PdfPCell amount = new PdfPCell(new Phrase(String.valueOf(income.getAmount())));
                amount.setPadding(10);
                tableIncome.addCell(amount);

                tableIncome.completeRow();
                numberIncome++;

                totalAmountIncome += income.getAmount();
            }
            document.add(tableIncome);


            Paragraph totalAmountExpenseP = new Paragraph("Total Amount Expense: " + totalAmount + " KN", fontBold);
            totalAmountExpenseP.setAlignment(Paragraph.ALIGN_LEFT);
            document.add(totalAmountExpenseP);


            Paragraph totalAmountIncomeP = new Paragraph("Total Amount Income: " + totalAmountIncome + " KN", fontBold);
            totalAmountIncomeP.setAlignment(Paragraph.ALIGN_LEFT);
            document.add(totalAmountIncomeP);


            Paragraph totalAmountSubstitution = new Paragraph("Amount to pay: " + (totalAmount - totalAmountIncome) + " KN", fontBold);
            totalAmountSubstitution.setAlignment(Paragraph.ALIGN_LEFT);
            document.add(totalAmountSubstitution);


        }

        document.close();
    }

    public void exportReportForEquipment(HttpServletResponse response, BuildingComplex buildingComplex) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontTitle.setSize(18);

//        Paragraph paragraph = new Paragraph("Report for buildingComplexId " + buildingComplex.getBuildComplexId(), fontTitle);
        Paragraph paragraph = new Paragraph("All Equipment Report ", fontTitle);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);

        Font fontNormal = FontFactory.getFont(FontFactory.HELVETICA);
        fontNormal.setSize(12);

        Font fontBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontBold.setSize(14);

        Paragraph paragraph1 = new Paragraph("Building Manager Name", fontBold);
        Paragraph paragraph2 = new Paragraph(buildingComplex.getBuildManager()
                .getBuildManagerName(), fontNormal);
        Paragraph paragraph3 = new Paragraph("Building Username", fontBold);
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
            table.setWidthPercentage(100);
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

    public void exportBuildingExpenseReport(HttpServletResponse response, Integer buildComplexId, Date startDate, Date endDate) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        BuildingComplex buildingComplex = buildComplexRepo.getById(buildComplexId);

        document.open();
        Font fontTitle = FontFactory.getFont(FontFactory.TIMES_ROMAN, BaseFont.CP1250, 16);
        fontTitle.setSize(18);

        Paragraph paragraph = new Paragraph("Report for building " + buildComplexRepo.getById(buildComplexId).getStreetName() + " " +
                buildComplexRepo.getById(buildComplexId).getStreetNumber() + ", " + buildComplexRepo.getById(buildComplexId).getCity()
                , fontTitle);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(paragraph);

        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        Paragraph paragraph22 = new Paragraph("for period: " + formatter.format(startDate) + " - " + formatter.format(endDate)   , fontTitle);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(paragraph22);

        Font fontNormal = FontFactory.getFont(FontFactory.TIMES_ROMAN, BaseFont.CP1250, 12);
        fontNormal.setSize(12);

        Font fontBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, BaseFont.CP1250, 14);
        fontBold.setSize(14);

        Paragraph paragraph5 = new Paragraph("Building Address", fontBold);
        Paragraph paragraph6 = new Paragraph(buildingComplex
                .getStreetName() + " " + buildingComplex
                .getStreetNumber() + " " + buildingComplex
                .getCity() + " " + buildingComplex
                .getPostalCode(), fontNormal);

        paragraph5.setAlignment(Paragraph.ALIGN_LEFT);
        paragraph6.setAlignment(Paragraph.ALIGN_LEFT);

        document.add(paragraph5);
        document.add(paragraph6);

        SimpleDateFormat formatter2 = new SimpleDateFormat("dd.MM.yyyy");


        LocalDate startOfTheYear = LocalDate.of(LocalDate.now().getYear(), 1, 1);

        List<Expense> expensesPreviousPeriod = expenseRepo.findByExpenseType_expenseTypeNameInAndDateOfReceiptAfterAndDateOfReceiptBefore(
                Arrays.asList("EquipmentService", "Maintenance"), convertLocalDateToDate(startOfTheYear), startDate);
        Double previousExpenseSum = expensesPreviousPeriod.stream().mapToDouble(Expense::getAmount).sum();


        List<Income> incomesPreviousPeriod = incomeRepo.findByExpenseType_expenseTypeNameInAndDateOfPaymentAfterAndDateOfPaymentBefore(
                Arrays.asList("UpKeep"), convertLocalDateToDate(startOfTheYear), startDate);
        Double previousIncomeSum = incomesPreviousPeriod.stream().mapToDouble(Income::getAmount).sum();

        Double previousTotal = previousIncomeSum - previousExpenseSum;

        Paragraph paragraph7 = new Paragraph("Saldo: " + previousTotal, fontBold);
        paragraph7.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(paragraph7);


        // current expenses
        List<Expense> expensesPeriod = expenseRepo.findByExpenseType_expenseTypeNameInAndDateOfReceiptAfterAndDateOfReceiptBefore(
                Arrays.asList("EquipmentService", "Maintenance"), startDate, endDate);
        Double expenseSum = expensesPeriod.stream().mapToDouble(Expense::getAmount).sum();


        List<Income> incomesPeriod = incomeRepo.findByExpenseType_expenseTypeNameInAndDateOfPaymentAfterAndDateOfPaymentBefore(
                Arrays.asList("UpKeep"), startDate, endDate);
        Double incomeSum = incomesPeriod.stream().mapToDouble(Income::getAmount).sum();


        Paragraph expenseTypeParagraph = new Paragraph("Expenses in the data range: ");
        expenseTypeParagraph.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(expenseTypeParagraph);


        PdfPTable tableExpenses = new PdfPTable(6);
        tableExpenses.setWidthPercentage(100);
        tableExpenses.setHeaderRows(1);
        tableExpenses.setSpacingBefore(15);
        tableExpenses.setSpacingAfter(15);

        PdfPCell numberCellHeader = new PdfPCell(new Phrase("No", fontBold));
        numberCellHeader.setPadding(10);
        tableExpenses.addCell(numberCellHeader);

        PdfPCell descriptionHeader = new PdfPCell(new Phrase("Description", fontBold));
        descriptionHeader.setPadding(10);
        tableExpenses.addCell(descriptionHeader);

        PdfPCell expenseTypeHeader = new PdfPCell(new Phrase("Expense Type", fontBold));
        expenseTypeHeader.setPadding(10);
        tableExpenses.addCell(expenseTypeHeader);

        PdfPCell dateCreatedHeader = new PdfPCell(new Phrase("Date Created", fontBold));
        dateCreatedHeader.setPadding(10);
        tableExpenses.addCell(dateCreatedHeader);

        PdfPCell dueDateHeader = new PdfPCell(new Phrase("Due Date", fontBold));
        dueDateHeader.setPadding(10);
        tableExpenses.addCell(dueDateHeader);

        PdfPCell amountHeader = new PdfPCell(new Phrase("Amount in KN", fontBold));
        amountHeader.setPadding(10);
        tableExpenses.addCell(amountHeader);


        tableExpenses.completeRow();

        int number = 1;
        Float totalAmount = Float.valueOf(0);

        for (Expense expense : expensesPeriod) {

            PdfPCell numberCell = new PdfPCell(new Phrase(number + "."));
            numberCell.setPadding(10);
            tableExpenses.addCell(numberCell);

            PdfPCell description = new PdfPCell(new Phrase(expense.getDescription()));
            description.setPadding(10);
            tableExpenses.addCell(description);

            PdfPCell expenseType = new PdfPCell(new Phrase(expense.getExpenseType()
                    .getExpenseTypeName()));
            expenseType.setPadding(10);
            tableExpenses.addCell(expenseType);

            PdfPCell dateOfReceipt = new PdfPCell(new Phrase(formatter2.format(expense.getDateOfReceipt())));
            dateOfReceipt.setPadding(10);
            tableExpenses.addCell(dateOfReceipt);

            PdfPCell dueDate = new PdfPCell(new Phrase(formatter2.format(expense.getDueDate())));
            dueDate.setPadding(10);
            tableExpenses.addCell(dueDate);

            PdfPCell amount = new PdfPCell(new Phrase(String.valueOf(expense.getAmount())));
            amount.setPadding(10);
            tableExpenses.addCell(amount);

            tableExpenses.completeRow();
            number++;

            totalAmount += expense.getAmount();
        }
        document.add(tableExpenses);

        // INCOME
        PdfPTable tableIncome = new PdfPTable(6);
        tableIncome.setWidthPercentage(100);
        tableIncome.setHeaderRows(1);
        tableIncome.setSpacingBefore(15);
        tableIncome.setSpacingAfter(15);


        Paragraph incomeParagraph = new Paragraph("Incomes in the data range: ");
        incomeParagraph.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(incomeParagraph);


        PdfPCell userHeader = new PdfPCell(new Phrase("Payed By", fontBold));
        userHeader.setPadding(10);
        tableExpenses.addCell(userHeader);

        tableIncome.addCell(numberCellHeader);
        tableIncome.addCell(userHeader);
        tableIncome.addCell(descriptionHeader);
        tableIncome.addCell(expenseTypeHeader);
        tableIncome.addCell(dateCreatedHeader);
        tableIncome.addCell(amountHeader);



        tableIncome.completeRow();

        int numberIncome = 1;
        Float totalAmountIncome = Float.valueOf(0);


//        Double saldoEnd = Double.valueOf(0);



        for (Income income : incomesPeriod) {

            PdfPCell numberCell = new PdfPCell(new Phrase(numberIncome + "."));
            numberCell.setPadding(10);
            tableIncome.addCell(numberCell);

            PdfPCell user = new PdfPCell(new Phrase(income.getUnit().getCoOwner().getCoOwnerName()));
            user.setPadding(10);
            tableIncome.addCell(user);

            PdfPCell description = new PdfPCell(new Phrase(income.getDescription()));
            description.setPadding(10);
            tableIncome.addCell(description);

            PdfPCell expenseType = new PdfPCell(new Phrase(income.getExpenseType().getExpenseTypeName()));
            expenseType.setPadding(10);
            tableIncome.addCell(expenseType);

            PdfPCell dateOfReceipt = new PdfPCell(new Phrase(formatter2.format(income.getDateOfPayment())));
            dateOfReceipt.setPadding(10);
            tableIncome.addCell(dateOfReceipt);

            PdfPCell amount = new PdfPCell(new Phrase(String.valueOf(income.getAmount())));
            amount.setPadding(10);
            tableIncome.addCell(amount);

            tableIncome.completeRow();
            numberIncome++;

            totalAmountIncome += income.getAmount();

//            saldoEnd = previousTotal + totalAmountIncome - totalAmount;

        }
        document.add(tableIncome);


        Paragraph totalAmountExpenseP = new Paragraph("Total Amount Expense: " + totalAmount + " KN", fontBold);
        totalAmountExpenseP.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(totalAmountExpenseP);


        Paragraph totalAmountIncomeP = new Paragraph("Total Amount Income: " + totalAmountIncome + " KN", fontBold);
        totalAmountIncomeP.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(totalAmountIncomeP);



//        Paragraph saldoEnd = new Paragraph("Saldo at the end of date range: " + saldoEnd + " KN", fontBold);
//        saldoEnd.setAlignment(Paragraph.ALIGN_LEFT);
//        document.add(totalAmountIncomeP);

        document.close();
    }


    private Date convertLocalDateToDate(LocalDate date){
        return Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

}

