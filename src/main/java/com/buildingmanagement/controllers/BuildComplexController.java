package com.buildingmanagement.controllers;

import com.buildingmanagement.entities.*;
import com.buildingmanagement.repositories.*;
import com.buildingmanagement.services.BuildComplexService;
import com.buildingmanagement.services.impl.PDFGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
public class BuildComplexController {

    @Autowired
    private BuildComplexService buildComplexService;

    @Autowired
    private InfoRepo infoRepo;

    @Autowired
    private BuildComplexRepo buildComplexRepo;

    @Autowired
    private FloorRepo floorRepo;

    @Autowired
    private UnitRepo unitRepo;

    @Autowired
    private EquipmentRepo equipmentRepo;

    @Autowired
    private ExpenseRepo expenseRepo;

    @Autowired
    private ExpenseTypeRepo expenseTypeRepo;

    @Autowired
    private UnitTypeRepo unitTypeRepo;

    @Autowired
    private CoOwnerRepo coOwnerRepo;

    @Autowired
    private IncomeRepo incomeRepo;

    @Autowired
    private BuildingManagerRepo buildingManagerRepo;
    private Floor floor;


    @Autowired
    private UtilitiesPriceRepo utiliesPriceRepo;

    @Autowired
    private PDFGeneratorService pdfGeneratorService;
    private Integer coOwnerId;


    @ModelAttribute("buildComplex")
    public BuildingComplex buildComplex() {
        return new BuildingComplex();
    }

    @GetMapping("/manager/addBuildComplexForm")
    private String addBuildComplexForm(Model model, Principal principal) {
        String username = principal.getName();
        BuildingManager buildingManager = this.buildingManagerRepo.findByUsername(username);
        model.addAttribute("buildManagerId", buildingManager.getBuildManagerId());
        model.addAttribute("username", "User logged in: " + username);
        return "addBuildComplex";
    }


    @PostMapping("/manager/addBuildComplex/{buildManagerId}")
    public String registerUserAccount(@ModelAttribute("buildComplex") BuildingComplex buildComplex, @PathVariable Integer buildManagerId,
                                      Model model) {
        buildComplex.setBuildManager(this.buildingManagerRepo.findById(buildManagerId).get());
        this.buildComplexService.addBuildComplex(buildComplex);
        String username = buildComplex.getUsername();
        String password = buildComplex.getPassword();
        String userType = "CoOwner Representative";

//        Optional<BuildingComplex> buildingComplex = Optional.ofNullable(this.buildComplexRepo.findByUsername(username));
//        if (buildingComplex.isPresent()) {
//// ako je if uvjet zadovoljen. znači da postoji building complex s istim usernamom u bazi
//            model.addAttribute("error", "Username already exists");
//        } else
//        {
//            return "redirect:/pdf/generate/" + username + "/" + password + "/" + userType;
//        }
        return "redirect:/pdf/generate/" + username + "/" + password + "/" + userType;
    }

    @GetMapping("/manager/addFloorForm/{buildComplexId}")
    private String addFloorForm(Model model, Principal principal, @PathVariable Integer buildComplexId) {
        model.addAttribute("buildComplexId");
        BuildingComplex buildComplex = this.buildComplexRepo.findById(buildComplexId).get();
        model.addAttribute("address", buildComplex.getStreetName() + " " + buildComplex.getStreetNumber() + ", " + buildComplex.getCity());
        return "addFloor";
    }

    @PostMapping("/manager/addFloor/{buildComplexId}")
    public String addFloor(@RequestParam String floorName, @PathVariable Integer buildComplexId, Model model) {
        Floor floor = new Floor();
        BuildingComplex buildingComplex = this.buildComplexRepo.findById(buildComplexId).get();
        floor.setBuildingComplex(buildingComplex);
        floor.setFloorName(floorName);
        this.floorRepo.save(floor);
        model.addAttribute("address", floor.getBuildingComplex().getStreetName() + " " + floor.getBuildingComplex().getStreetNumber() + ", " + floor.getBuildingComplex().getCity());
        return "redirect:/manager/viewBuilding/" + buildComplexId;
    }

    @GetMapping("/manager/viewBuilding/{buildComplexId}")
    private String viewBuilding(Model model, Principal principal, @PathVariable Integer buildComplexId, String keyword) {
        BuildingComplex buildComplex = this.buildComplexRepo.findById(buildComplexId).get();
        model.addAttribute("buildComplex", buildComplex);
        List<Floor> floors = this.floorRepo.getAllFloorOfBuild(buildComplexId);
        model.addAttribute("floors", floors);
        List<Info> infos;
        if (keyword == null) {
            infos = this.infoRepo.getAllInfoOfBuild(buildComplexId);
        } else {
            infos = this.infoRepo.searchInfoByKeyword(buildComplexId, keyword);
        }
        model.addAttribute("infos", infos);

        model.addAttribute("utilitiesPrices", buildComplex.getUtilitiesPrices());
        model.addAttribute("address", buildComplex.getStreetName() + " " + buildComplex.getStreetNumber() + ", " + buildComplex.getCity());

        return "viewBuilding";
    }

    @GetMapping("/manager/listBuildingExpense/{buildComplexId}")
    private String listBuildingExpense(@PathVariable Integer buildComplexId, Model model) {
        List<Expense> expenses = this.expenseRepo.findByExpenseType_expenseTypeNameIn(Arrays.asList("EquipmentService", "Maintenance"));
        model.addAttribute("buildComplexId", buildComplexId);
        model.addAttribute("expenses", expenses);
        return "listBuildingExpense";
    }


    @GetMapping("/manager/viewFloor/{floorId}/{page}")
    private String viewFloor(@PathVariable Integer floorId, @PathVariable Integer page, Model model, String keyword, Integer unitTypeId) {
        Floor floor = this.floorRepo.findById(floorId).get();
        model.addAttribute("floor", floor);
        List<Equipment> equipments = this.equipmentRepo.getAllEquipmentOfFloor(floorId);
        model.addAttribute("equipments", equipments);
        model.addAttribute("buildComplexId", floor.getBuildingComplex().getBuildComplexId());
        List<UnitType> unitTypes = this.unitTypeRepo.findAll();
        model.addAttribute("unitTypes", unitTypes);
        Sort sort = Sort.by(Sort.Direction.ASC, "coOwner.coOwnerName");
        Pageable pageable = PageRequest.of(page, 5, sort);
        Page<Unit> units = null;
        if (keyword == null && unitTypeId == null) {
            units = this.unitRepo.findAllByFloor_floorId(floorId, pageable);
        } else if (keyword != null && unitTypeId == null) {
            units = this.unitRepo.searchUnitByKeyword(keyword, floorId, pageable);
            model.addAttribute("keyword", keyword);
        } else if (keyword == null && unitTypeId != null) {
            units = this.unitRepo.searchUnitByUnitType(unitTypeId, floorId, pageable);
            model.addAttribute("unitTypeId", unitTypeId);
        }
        model.addAttribute("units", units);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", units.getTotalPages());

        // za povuc adresu
        model.addAttribute("address", floor.getBuildingComplex().getStreetName() + " " + floor.getBuildingComplex().getStreetNumber() + ", " + floor.getBuildingComplex().getCity());

        return "viewFloor";
    }

    @GetMapping("/manager/viewUnit/{unitId}/{page1}/{page2}")
    private String viewUnit(Model model, @PathVariable Integer page1, @PathVariable Integer page2, @PathVariable Integer unitId,
                            @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate1,
                            @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate1,
                            @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate2,
                            @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate2) throws ParseException {
        Unit unit = this.unitRepo.findById(unitId).get();
        model.addAttribute("unit", unit);

        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Sort sort1 = Sort.by(Sort.Direction.ASC, "dateOfReceipt");
        Sort sort2 = Sort.by(Sort.Direction.ASC, "dateOfPayment");
        Pageable pageable1 = PageRequest.of(page1, 5, sort1);
        Pageable pageable2 = PageRequest.of(page2, 5, sort2);
        Page<Expense> expenses = null;
        Page<Income> incomes = null;
        if (startDate1 == null || endDate1 == null) {
            if (startDate2 == null || endDate2 == null) {
                expenses = this.expenseRepo.getAllExpenseOfUnit(unitId, pageable1);
                incomes = this.incomeRepo.getAllIncomeOfUnit(unitId, pageable2);
            } else if (startDate2 != null || endDate2 != null) {
                expenses = this.expenseRepo.getAllExpenseOfUnit(unitId, pageable1);
                incomes = this.incomeRepo.getIncomeOfPeriod(startDate2, endDate2, unitId, pageable2);
                model.addAttribute("startDate2", simpleDateFormat.format(startDate2));
                model.addAttribute("endDate2", simpleDateFormat.format(endDate2));
            }
        } else if (startDate2 == null || endDate2 == null) {
            if (startDate1 != null || endDate1 != null) {
                expenses = this.expenseRepo.getExpenseOfPeriod(startDate1, endDate1, unitId, pageable1);
                incomes = this.incomeRepo.getAllIncomeOfUnit(unitId, pageable2);
                model.addAttribute("startDate1", simpleDateFormat.format(startDate1));
                model.addAttribute("endDate1", simpleDateFormat.format(endDate1));
            }
        }
//        List<ExpenseType> expenseTypes = this.expenseTypeRepo.findAll();
        List<ExpenseType> expenseTypes = this.expenseTypeRepo.findByExpenseTypeNameNotIn(Arrays.asList("Maintenance", "EquipmentService"));

        model.addAttribute("expenseTypes", expenseTypes);
        model.addAttribute("currentPage1", page1);
        model.addAttribute("currentPage2", page2);
        model.addAttribute("totalPages1", expenses.getTotalPages());
        model.addAttribute("totalPages2", incomes.getTotalPages());
        model.addAttribute("expenses", expenses);
        model.addAttribute("incomes", incomes);
        String unitTypeName = unit.getUnitType().getUnitTypeName();
        model.addAttribute("unitTypeName", unitTypeName);
        Floor floor = this.floorRepo.findById(unit.getFloor().getFloorId()).get();
        model.addAttribute("buildComplexId", floor.getBuildingComplex().getBuildComplexId());
        CoOwner coOwner = this.unitRepo.findById(unitId).get().getCoOwner();
        model.addAttribute("coOwner", coOwner);

        model.addAttribute("kat", floor.getFloorName());
        // metoda prima coowner_repID za prikaz podatke koje zgrade pregledajemo
        model.addAttribute("address", floor.getFloorName() + " - " + floor.getBuildingComplex().getStreetName() + " " + floor.getBuildingComplex().getStreetNumber() + ", " + floor.getBuildingComplex().getCity());

        return "viewUnit";
    }

    @GetMapping("/manager/addUnitForm/{floorId}/{buildComplexId}")
    private String addUnitForm(Model model, @PathVariable Integer floorId, @PathVariable Integer buildComplexId) {
        model.addAttribute("floorId");
        model.addAttribute("buildComplexId");
        List<UnitType> unitTypes = this.unitTypeRepo.findAll();
        model.addAttribute("unitTypes", unitTypes);
        Floor floor = floorRepo.getById(floorId);
        BuildingComplex kat = this.buildComplexRepo.findById(buildComplexId).get();
        model.addAttribute("address", floor.getFloorName() + " - " + kat.getStreetName() + " " + kat.getStreetNumber() + ", " + kat.getCity());
        return "addUnit";
    }

    @PostMapping("/manager/addUnit/{floorId}/{buildComplexId}")
    public String addUnit(@RequestParam Integer areaSqrMeter, @RequestParam(required = false) Integer noOfTenants, @RequestParam Integer unitTypeId, @PathVariable Integer floorId, @PathVariable Integer buildComplexId, Model model) {
        Unit unit = new Unit();
        BuildingComplex buildingComplex = this.buildComplexRepo.findById(buildComplexId).get();
        Floor floor = this.floorRepo.findById(floorId).get();
        unit.setFloor(floor);
        unit.setAreaSqrMeter(areaSqrMeter);
        unit.setNoOfTenants(noOfTenants);
        UnitType unitType = this.unitTypeRepo.findById(unitTypeId).get();
        unit.setUnitType(unitType);
        unit.setBuildingComplex(buildingComplex);
        this.unitRepo.save(unit);
        model.addAttribute("address", buildComplex().getStreetName() + " " + buildComplex().getStreetNumber() + ", " + buildComplex().getCity());
        return "redirect:/manager/viewFloor/" + floorId + "/" + 0;
    }

    @GetMapping("/manager/addEquipmentForm/{floorId}/{buildComplexId}")
    private String addEquipmentForm(Model model, @PathVariable Integer floorId, @PathVariable Integer buildComplexId) {
        model.addAttribute("floorId");
        model.addAttribute("buildComplexId");
        model.addAttribute("address", floorRepo.getById(floorId).getFloorName() + " - " + buildComplexRepo.getById(buildComplexId).getStreetName() + " " + buildComplexRepo.getById(buildComplexId).getStreetNumber() + ", " + buildComplexRepo.getById(buildComplexId).getCity());
        return "addEquipment";
    }

    @PostMapping("/manager/addEquipment/{floorId}/{buildComplexId}")
    public String addEquipment(@RequestParam String equipmentName, @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateOfService, @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date nextServiceDate, @PathVariable Integer floorId, @PathVariable Integer buildComplexId, Model model) {
        Equipment equipment = new Equipment();
        BuildingComplex buildingComplex = this.buildComplexRepo.findById(buildComplexId).get();
        Floor floor = this.floorRepo.findById(floorId).get();
        equipment.setFloor(floor);
        equipment.setEquipmentName(equipmentName);
        equipment.setDateOfService(dateOfService);
        equipment.setNextServiceDate(nextServiceDate);
        equipment.setBuildingComplex(buildingComplex);
        this.equipmentRepo.save(equipment);
        model.addAttribute("address", buildComplex().getStreetName() + " " + buildComplex().getStreetNumber() + ", " + buildComplex().getCity());
        return "redirect:/manager/viewFloor/" + floorId + "/" + 0;
    }


    @GetMapping("/manager/updateEquipmentForm/{equipmentId}")
    private String updateEquipmentForm(Model model, @PathVariable Integer equipmentId) {
        model.addAttribute("equipmentId");
        Equipment equipment = this.equipmentRepo.findById(equipmentId).get();
//        model.addAttribute("equipmentName", equipment.getEquipmentName());
        Integer floorId = equipment.getFloor().getFloorId();
//        model.addAttribute("floorId", floorId);

        model.addAttribute("floorId", this.equipmentRepo.findById(equipmentId).get().getFloor().getFloorId());
        model.addAttribute("equipmentName", this.equipmentRepo.findById(equipmentId).get().getEquipmentName());
        model.addAttribute("address", equipmentRepo.getById(equipmentId).getFloor().getFloorName() + " - " + equipmentRepo.getById(equipmentId).getBuildingComplex().getStreetName() + " " + equipmentRepo.getById(equipmentId).getBuildingComplex().getStreetNumber() + ", " + equipmentRepo.getById(equipmentId).getBuildingComplex().getCity());
        return "updateEquipment";
    }

    @PostMapping("/manager/updateEquipment/{equipmentId}")
    public String updateEquipment(@RequestParam(required = false) String equipmentName, @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateOfService, @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date nextServiceDate, @PathVariable Integer equipmentId, Model model) {
        Equipment equipment = this.equipmentRepo.findById(equipmentId).get();
        equipment.setEquipmentName(equipmentName);
        equipment.setDateOfService(dateOfService);
        equipment.setNextServiceDate(nextServiceDate);
        Integer floorId = equipment.getFloor().getFloorId();
        this.equipmentRepo.save(equipment);
        model.addAttribute("address", buildComplex().getStreetName() + " " + buildComplex().getStreetNumber() + ", " + buildComplex().getCity());
        return "redirect:/manager/viewFloor/" + floorId + "/" + 0;
    }

    @GetMapping("/manager/addExpenseForm/{unitId}/{buildComplexId}")
    private String addExpenseForm(Model model, @PathVariable Integer unitId, @PathVariable Integer buildComplexId, Unit unit) {
        model.addAttribute("unitId");
        model.addAttribute("buildComplexId");
        List<ExpenseType> expenseTypes = this.expenseTypeRepo.findAll();
        model.addAttribute("utilitiesPrices", utiliesPriceRepo.findAll());
        model.addAttribute("areaSqrMeter", this.unitRepo.findById(unitId).get().getAreaSqrMeter());

        model.addAttribute("address", unitRepo.getById(unitId).getUnitType().getUnitTypeName() + " - " + unitRepo.getById(unitId).getCoOwner().getCoOwnerName() + ", " + buildComplexRepo.findById(buildComplexId).get().getStreetName() + " " + buildComplexRepo.findById(buildComplexId).get().getStreetNumber() + ", " + buildComplexRepo.findById(buildComplexId).get().getCity());
        return "addExpense";
    }

    @PostMapping("/manager/addExpense/{unitId}/{buildComplexId}")
    public String addExpense(@RequestParam(required = false) Integer consumed, @RequestParam(required = false) Float rentAmount, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dueDate,
                             @RequestParam String description, @RequestParam Integer utilitiesPriceId, @RequestParam(required = false) Integer areaSqrMeter,
                             @PathVariable Integer unitId, @PathVariable Integer buildComplexId, Model model) throws ParseException {
        Expense expense = new Expense();
        BuildingComplex buildingComplex = this.buildComplexRepo.findById(buildComplexId).get();
        Unit unit = this.unitRepo.findById(unitId).get();
        expense.setUnit(unit);
        expense.setBuildingComplex(buildingComplex);
        UtilitiesPrice utilitiesPrice = this.utiliesPriceRepo.findById(utilitiesPriceId).get();
        expense.setExpenseType(utilitiesPrice.getExpenseType());

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();
        Date dateOfReceipt = formatter.parse(formatter.format(today));

        expense.setDateOfReceipt(dateOfReceipt);
        expense.setDueDate(dueDate);
        expense.setDescription(description);
        Float amount = Float.valueOf(0);
        if (consumed != null) {
            amount = Float.valueOf(utilitiesPrice.getPrice() * consumed);
        } else if (areaSqrMeter != null) {
            amount = Float.valueOf(utilitiesPrice.getPrice() * areaSqrMeter);
        } else if (rentAmount != null) {
            amount = rentAmount;
        }
        expense.setAmount(amount);
        this.expenseRepo.save(expense);
        //  model.addAttribute("address", buildComplex().getStreetName() + " " + buildComplex().getStreetNumber() + ", " + buildComplex().getCity());
        return "redirect:/manager/viewUnit/" + unitId + "/" + 0 + "/" + 0;
    }

    //    Building Expenses
    @GetMapping("/manager/addBuildingExpenseForm/{buildComplexId}")
    private String addBuildingExpenseForm(Model model, @PathVariable Integer buildComplexId) {
        model.addAttribute("buildComplexId");
        List<ExpenseType> expenseTypes = this.expenseTypeRepo.findByExpenseTypeNameIn(Arrays.asList("EquipmentService", "Maintenance"));
//    expenseTypeRepo.findByExpenseTypeNameIn(Arrays.asList("EquipmentService","Maintenance"));
        model.addAttribute("expenseTypes", expenseTypes);

        model.addAttribute("address", buildComplexRepo.findById(buildComplexId).get().getStreetName() + " " + buildComplexRepo.findById(buildComplexId).get().getStreetNumber() + ", " + buildComplexRepo.findById(buildComplexId).get().getCity());
        return "addBuildingExpense";
    }

    @PostMapping("/manager/addBuildingExpense/{buildComplexId}")
    public String addExpense(@RequestParam(required = false) Float amount, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dueDate,
                             @RequestParam String description, @RequestParam Integer expenseTypeId,
                             @PathVariable Integer buildComplexId, Model model) throws ParseException {
        Expense expense = new Expense();
        BuildingComplex buildingComplex = this.buildComplexRepo.findById(buildComplexId).get();

        expense.setBuildingComplex(buildingComplex);
        ExpenseType expenseType = this.expenseTypeRepo.findById(expenseTypeId).get();
        expense.setExpenseType(expenseType);

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();
        Date dateOfReceipt = formatter.parse(formatter.format(today));

        expense.setDateOfReceipt(dateOfReceipt);
        expense.setDueDate(dueDate);
        expense.setDescription(description);
        expense.setAmount(amount);
        this.expenseRepo.save(expense);
        //  model.addAttribute("address", buildComplex().getStreetName() + " " + buildComplex().getStreetNumber() + ", " + buildComplex().getCity());
        return "redirect:/manager/viewBuilding/" + buildComplexId;
    }


    @GetMapping("/manager/addIncomeForm/{unitId}/{buildComplexId}")
    private String addIncomeForm(Model model, @PathVariable Integer unitId, @PathVariable Integer buildComplexId) {
        model.addAttribute("unitId");
        model.addAttribute("buildComplexId");
        model.addAttribute("expenseTypes", expenseTypeRepo.findAll());
        model.addAttribute("address", unitRepo.getById(unitId).getCoOwner().getCoOwnerName() + ", " + buildComplexRepo.findById(buildComplexId).get().getStreetName() + " " + buildComplexRepo.findById(buildComplexId).get().getStreetNumber() + ", " + buildComplexRepo.findById(buildComplexId).get().getCity());
        return "addIncome";
    }

    @PostMapping("/manager/addIncome/{unitId}/{buildComplexId}")
    public String addIncome(@RequestParam Float amount, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateOfPayment,
                            @RequestParam String description, @PathVariable Integer unitId, @PathVariable Integer buildComplexId,
                            @RequestParam Integer expenseTypeId, Model model) {
        Income income = new Income();
        BuildingComplex buildingComplex = this.buildComplexRepo.findById(buildComplexId).get();
        Unit unit = this.unitRepo.findById(unitId).get();
        ExpenseType expenseType = expenseTypeRepo.getById(expenseTypeId);
        income.setUnit(unit);
        income.setBuildingComplex(buildingComplex);
        income.setDateOfPayment(dateOfPayment);
        income.setDescription(description);
        income.setAmount(amount);
        income.setExpenseType(expenseType);
        this.incomeRepo.save(income);
        model.addAttribute("address", unit.getCoOwner().getCoOwnerName() + ", " + unit.getBuildingComplex().getStreetName() + " " + unit.getBuildingComplex().getStreetNumber() + ", " + unit.getBuildingComplex().getCity());
        return "redirect:/manager/viewUnit/" + unitId + "/" + 0 + "/" + 0;
    }

    @GetMapping("/manager/updateUnit/{unitId}")
    public String updateUnitForm(Model model, @PathVariable Integer unitId) {
        Unit unit = this.unitRepo.findById(unitId).get();
        Integer floorId = unit.getFloor().getFloorId();
        model.addAttribute("floorId", floorId);
        model.addAttribute("unitTypes", unitTypeRepo.findAll());
        model.addAttribute("coOwners", coOwnerRepo.findAll());
        model.addAttribute("noOfTenants", unit.getNoOfTenants());

//        model.addAttribute("vlasnik", unit.getCoOwner().getCoOwnerName());
//        model.addAttribute("kat", unit.getFloor().getFloorName());
        if (unitRepo.getById(unitId).getCoOwner() != null) {
            model.addAttribute("address", unitRepo.getById(unitId).getCoOwner().getCoOwnerName() + ", " + unitRepo.getById(unitId).getFloor().getFloorName() + " - " + unitRepo.getById(unitId).getBuildingComplex().getStreetName() + " " + unitRepo.getById(unitId).getBuildingComplex().getStreetNumber() + ", " + unitRepo.getById(unitId).getBuildingComplex().getCity());
        } else {
            model.addAttribute("address", unitRepo.getById(unitId).getFloor().getFloorName() + " - " + unitRepo.getById(unitId).getBuildingComplex().getStreetName() + " " + unitRepo.getById(unitId).getBuildingComplex().getStreetNumber() + ", " + unitRepo.getById(unitId).getBuildingComplex().getCity());
        }

        return "updateUnit";
    }


    @PostMapping("/manager/updateUnit/{unitId}")
    public String updateUnit(@PathVariable Integer unitId, @RequestParam("noOfTenants") Integer noOfTenants, Integer coOwnerId, Integer unitTypeId, Model model) {
        Unit unit = this.unitRepo.findById(unitId).get();
//        CoOwner coOwner = coOwnerRepo.findByCoOwnerId(coOwnerId);
//        UnitType unitType = unitTypeRepo.getById(unitId);
        unit.setNoOfTenants(noOfTenants);
        unit.setCoOwner(coOwnerRepo.getById(coOwnerId));
        unit.setUnitType(unitTypeRepo.getById(unitTypeId));
        Integer floorId = unit.getFloor().getFloorId();
        this.unitRepo.save(unit);
        model.addAttribute("address", buildComplex().getStreetName() + " " + buildComplex().getStreetNumber() + ", " + buildComplex().getCity());
        return "redirect:/manager/viewFloor/" + floorId + "/" + 0;


    }

    @ModelAttribute("info")
    public Info info() {
        return new Info();
    }

    @GetMapping("/manager/addInfoForm/{buildingComplexId}")
    public String addInfoForm(Model model, @PathVariable Integer buildingComplexId) {
        model.addAttribute("buildingComplexId", buildingComplexId);
        BuildingComplex buildComplex = this.buildComplexRepo.findById(buildingComplexId).get();
        model.addAttribute("address", buildComplex.getStreetName() + " " + buildComplex.getStreetNumber() + ", " + buildComplex.getCity());
        return "addInfoForm";
    }

    @PostMapping("/manager/addInfo/{buildingComplexId}")
    public String addInfo(@ModelAttribute("info") Info info, @PathVariable Integer buildingComplexId, Model model) {
        BuildingComplex buildComplex = this.buildComplexRepo.findById(buildingComplexId).get();
        info.setBuildingComplex(buildComplex);
        this.infoRepo.save(info);
        model.addAttribute("address", buildComplex.getStreetName() + " " + buildComplex.getStreetNumber() + ", " + buildComplex.getCity());
//        return "redirect:/manager" + "/" + 0;
        return "redirect:/manager" + "/viewBuilding/" + buildingComplexId;

    }

    @GetMapping("/manager/updateInfoForm/{infoId}")
    public String updateInfoForm(Model model,
                                 @PathVariable Integer infoId) {
        model.addAttribute("infoId", infoId);
        Info info = this.infoRepo.findById(infoId).get();
        model.addAttribute("serviceName", info.getServiceName());
        model.addAttribute("serviceDescription", info.getServiceDescription());
        model.addAttribute("streetName", info.getStreetName());
        model.addAttribute("streetNumber", info.getStreetNumber());
        model.addAttribute("city", info.getCity());
        model.addAttribute("postalCode", info.getPostalCode());
        model.addAttribute("contact", info.getContact());
        model.addAttribute("iban", info.getIban());
        model.addAttribute("mail", info.getMail());
        model.addAttribute("address", info.getBuildingComplex().getStreetName() + " " + info.getBuildingComplex().getStreetNumber() + ", " + info.getBuildingComplex().getCity());
        return "updateInfoForm";
    }

    @PostMapping("/manager/updateInfo/{infoId}")
    public String updateInfo(@PathVariable Integer infoId,
                             @RequestParam String serviceName, @RequestParam String serviceDescription,
                             @RequestParam String streetName, @RequestParam Integer streetNumber,
                             @RequestParam String city, @RequestParam Integer postalCode,
                             @RequestParam Integer contact, @RequestParam String mail, @RequestParam String iban, Model model
    ) {
        Info info = this.infoRepo.findById(infoId).get();
        info.setServiceName(serviceName);
        info.setServiceDescription(serviceDescription);
        info.setStreetName(streetName);
        info.setStreetNumber(streetNumber);
        info.setPostalCode(postalCode);
        info.setCity(city);
        info.setIban(iban);
        info.setContact(contact);
        info.setMail(mail);
        this.infoRepo.save(info);
        model.addAttribute("address", buildComplex().getStreetName() + " " + buildComplex().getStreetNumber() + ", " + buildComplex().getCity());
        Integer buildingComplexId = info.getBuildingComplex().getBuildComplexId();
        return "redirect:/manager" + "/viewBuilding/" + buildingComplexId;
    }

    @GetMapping("/manager/updateBuilding/{buildComplexId}")
    private String updateBuildingForm(@PathVariable Integer buildComplexId, Model model) {
        model.addAttribute("buildComplexId");
        BuildingComplex updatebuild = this.buildComplexRepo.findById(buildComplexId).get();
        model.addAttribute("username", updatebuild.getUsername());
        model.addAttribute("password", updatebuild.getPassword());
        model.addAttribute("userType", "Building Complex");
        model.addAttribute("streetName", updatebuild.getStreetName());
        model.addAttribute("streetNumber", updatebuild.getStreetNumber());
        model.addAttribute("city", updatebuild.getCity());
        model.addAttribute("postalCode", updatebuild.getPostalCode());
        model.addAttribute("address", updatebuild.getStreetName() + " " + updatebuild.getStreetNumber() + "," + updatebuild.getCity());
        return "updateBuilding";
    }

    @PostMapping("/manager/updateBuilding/{buildComplexId}")
    public String updateBuilding(@PathVariable Integer buildComplexId,
                                 @RequestParam(required = false) String streetName,
                                 @RequestParam(required = false) Integer streetNumber, @RequestParam(required = false) String city,
                                 @RequestParam(required = false) Integer postalCode, @RequestParam(required = false) String coOwnerUsername) {
        BuildingComplex buildComplex = this.buildComplexRepo.findById(buildComplexId).get();
        buildComplex.setcoOwnerUsername(coOwnerUsername);
        buildComplex.setStreetName(streetName);
        buildComplex.setStreetNumber(streetNumber);
        buildComplex.setCity(city);
        buildComplex.setPostalCode(postalCode);
        this.buildComplexService.update(buildComplexId, buildComplex);
        this.buildComplexRepo.save(buildComplex);
        return "redirect:/manager/viewBuilding/" + buildComplexId;
    }


//utilities price

    @ModelAttribute("utilitiesPrice")
    public UtilitiesPrice utilitiesPrice() {
        return new UtilitiesPrice();
    }

    @GetMapping("/manager/addUtilitiesPriceForm/{buildingComplexId}")
    public String addUtilitiesPriceForm(Model model, @PathVariable Integer buildingComplexId) {
        model.addAttribute("buildingComplexId", buildingComplexId);
        List<ExpenseType> expenseTypes = this.expenseTypeRepo.findByExpenseTypeNameNotIn(Arrays.asList("Maintenance", "EquipmentService"));
        model.addAttribute("expenseTypes", expenseTypes);
        List<UnitType> unitTypes = this.unitTypeRepo.findAll();
        model.addAttribute("unitTypes", unitTypes);
        BuildingComplex buildComplex = this.buildComplexRepo.findById(buildingComplexId).get();
        model.addAttribute("address", buildComplex.getStreetName() + " " + buildComplex.getStreetNumber() + ", " + buildComplex.getCity());
        return "addUtilitiesPriceForm";
    }

    @PostMapping("/manager/addUtilitiesPrice/{buildingComplexId}")
    public String addUtilitiesPrice(@ModelAttribute("utilitiesPrice") UtilitiesPrice utilitiesPrice, @PathVariable Integer buildingComplexId, Model model,
                                    @RequestParam Integer expenseTypeId, @RequestParam Integer unitTypeId) {
        BuildingComplex buildComplex = this.buildComplexRepo.findById(buildingComplexId).get();
        utilitiesPrice.setBuildingComplex(buildComplex);


        ExpenseType expenseType = this.expenseTypeRepo.findById(expenseTypeId).get();
        utilitiesPrice.setExpenseType(expenseType);

        UnitType unitType = this.unitTypeRepo.findById(unitTypeId).get();
        utilitiesPrice.setUnitType(unitType);

        this.utiliesPriceRepo.save(utilitiesPrice);

        model.addAttribute("address", buildComplex.getStreetName() + " " + buildComplex.getStreetNumber() + ", " + buildComplex.getCity());
        return "redirect:/manager/viewBuilding/" + buildingComplexId;
    }

    @GetMapping("/manager/updateUtilitiesPriceForm/{utilitiesPriceId}/{buildingComplexId}")
    public String updateUtilitiesPriceForm(Model model,
                                           @PathVariable Integer utilitiesPriceId, @PathVariable Integer buildingComplexId) {
        model.addAttribute("utilitiesPriceId", utilitiesPriceId);
        model.addAttribute("buildingComplexId", buildingComplexId);
        model.addAttribute("unitOfMeasurement", this.utiliesPriceRepo.getById(utilitiesPriceId).getUnitOfMeasurement());
        model.addAttribute("price", this.utiliesPriceRepo.findById(utilitiesPriceId).get().getPrice());
        List<ExpenseType> expenseTypes = this.expenseTypeRepo.findByExpenseTypeNameNotIn(Arrays.asList("Maintenance", "EquipmentService"));
        model.addAttribute("expenseTypes", expenseTypes);
        model.addAttribute("unitTypes", this.unitTypeRepo.findAll());

        model.addAttribute("address", utiliesPriceRepo.getById(utilitiesPriceId).getBuildingComplex().getStreetName() + " " + utiliesPriceRepo.getById(utilitiesPriceId).getBuildingComplex().getStreetNumber() + ", " + utiliesPriceRepo.getById(utilitiesPriceId).getBuildingComplex().getCity());
        return "updateUtilitiesPriceForm";
    }

    @PostMapping("/manager/updateUtilitiesPrice/{utilitiesPriceId}")
    public String updateUtilitiesPrice(@PathVariable Integer utilitiesPriceId,
                                       @RequestParam(required = false) Integer expenseTypeId,
                                       @RequestParam(required = false) String unitOfMeasurement, Model model, Float price, Integer unitTypeId
    ) {
        UtilitiesPrice utilitiesPrice;
        utilitiesPrice = this.utiliesPriceRepo.findById(utilitiesPriceId).get();
        utilitiesPrice.setUtilitiesPriceId(utilitiesPriceId);
        utilitiesPrice.setPrice(price);
        utilitiesPrice.setUnitOfMeasurement(unitOfMeasurement);
        utilitiesPrice.setUnitType(unitTypeRepo.getById(unitTypeId));
        utilitiesPrice.setExpenseType(expenseTypeRepo.getById(expenseTypeId));
        ExpenseType expenseType = this.expenseTypeRepo.findById(expenseTypeId).get();

        model.addAttribute("address", buildComplex().getStreetName() + " " + buildComplex().getStreetNumber() + ", " + buildComplex().getCity());
        Integer buildingComplexId = utilitiesPrice.getBuildingComplex().getBuildComplexId();
        this.utiliesPriceRepo.save(utilitiesPrice);
        return "redirect:/manager" + "/viewBuilding/" + buildingComplexId;
    }


    //print report za upkeep - servis bla bla
    @PostMapping("/manager/buildingExpenseReport/{buildComplexId}")
    public void buildingExpenseReport(HttpServletResponse response, @PathVariable Integer buildComplexId,
                                      @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                      @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        this.pdfGeneratorService.exportBuildingExpenseReport(response, buildComplexId, startDate, endDate);
    }
}
