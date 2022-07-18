package com.buildingmanagement.controllers;

import com.buildingmanagement.entities.*;
import com.buildingmanagement.repositories.*;
import com.buildingmanagement.services.CoOwnerService;
import com.buildingmanagement.services.impl.PDFGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class CoOwnerController {

    @Autowired
    private CoOwnerService coOwnerService;

    @Autowired
    private BuildComplexRepo buildComplexRepo;

    @Autowired
    private UnitRepo unitRepo;

    @Autowired
    private UnitTypeRepo unitTypeRepo;

    @Autowired
    private EquipmentRepo equipmentRepo;

    @Autowired
    private ExpenseRepo expenseRepo;

    @Autowired
    private CoOwnerRepo coOwnerRepo;

    @Autowired
    private IncomeRepo incomeRepo;

    @Autowired
    private FloorRepo floorRepo;

    @GetMapping("/coowner_rep/addCoOwnerForm/{unitId}")
    private String addCoOwnerForm(Model model, @PathVariable Integer unitId){
        model.addAttribute("unitId",unitId);
        List<CoOwner> coOwners = this.coOwnerRepo.findAll();
        model.addAttribute("coOwners", coOwners);
        return "addCoOwner";
    }

    @ModelAttribute("coOwner")
    public CoOwner coOwner() {
        return new CoOwner();
    }

    @PostMapping("/coowner_rep/addCoOwner/{unitId}")
    public String registerUserAccount(@ModelAttribute("coOwner") CoOwner coOwner, @PathVariable Integer unitId, @RequestParam(required = false)  Integer coOwnerId) {
        if (coOwnerId!=null){
          this.coOwnerService.addCoOwner(null, coOwnerId, unitId);
          return "redirect:/coowner_rep/viewUnit/"+unitId+"/0/0";
        }
        this.coOwnerService.addCoOwner(coOwner,0, unitId);
        String username = coOwner.getUsername();
        String password =  coOwner.getPassword();
        String userType = "CoOwner";
        return "redirect:/pdf/generate/"+ username +"/"+ password +"/"+ userType;

    }

    @GetMapping("/coowner_rep/addFloorForm/{buildComplexId}")
    private String addFloorForm(Model model, Principal principal, @PathVariable Integer buildComplexId){
        model.addAttribute("buildComplexId");
        return "addFloor";
    }

    @PostMapping("/coowner_rep/addFloor/{buildComplexId}")
    public String addFloor(@RequestParam String floorName, @PathVariable Integer buildComplexId) {
        Floor floor = new Floor();
        BuildingComplex buildingComplex = this.buildComplexRepo.findById(buildComplexId).get();
        floor.setBuildingComplex(buildingComplex);
        floor.setFloorName(floorName);
        this.floorRepo.save(floor);
        return "redirect:/coowner_rep";
    }

    @GetMapping("/coowner_rep/viewFloor/{floorId}/{page}")
     private String viewFloor(@PathVariable Integer page, @PathVariable Integer floorId, Model model, String keyword, Integer unitTypeId){
            Floor floor = this.floorRepo.findById(floorId).get();
            model.addAttribute("floor",floor);
            List<Equipment> equipments = this.equipmentRepo.getAllEquipmentOfFloor(floorId);
            model.addAttribute("equipments",equipments);
            model.addAttribute("buildComplexId",floor.getBuildingComplex().getBuildComplexId());
            List<UnitType> unitTypes = this.unitTypeRepo.findAll();
            model.addAttribute("unitTypes",unitTypes);
            Sort sort = Sort.by(Sort.Direction.ASC, "coOwner.coOwnerName");
            Pageable pageable = PageRequest.of(page,5, sort);
            Page<Unit> units = null;
           if(keyword==null&&unitTypeId==null) {
              units = this.unitRepo.getAllUnitOfFloor(floorId, pageable);
           }
           else if(keyword!=null&&unitTypeId==null){
              units = this.unitRepo.searchUnitByKeyword(keyword, floorId, pageable);
              model.addAttribute("units", units);
              model.addAttribute("keyword", keyword);
           }
           else if(keyword==null&&unitTypeId!=null) {
              units = this.unitRepo.searchUnitByUnitType(unitTypeId, floorId, pageable);
           }
           model.addAttribute("units", units);
           model.addAttribute("currentPage",page);
           model.addAttribute("totalPages", units.getTotalPages());
           model.addAttribute("unitTypeId", unitTypeId );
        return "viewFloor";
    }

    @GetMapping("/coowner_rep/addUnitForm/{floorId}/{buildComplexId}")
    private String addUnitForm(Model model,@PathVariable Integer floorId, @PathVariable Integer buildComplexId){
        model.addAttribute("floorId");
        model.addAttribute("buildComplexId");
        List<UnitType> unitTypes = this.unitTypeRepo.findAll();
        model.addAttribute("unitTypes",unitTypes);
        return "addUnit";
    }

    @PostMapping("/coowner_rep/addUnit/{floorId}/{buildComplexId}")
    public String addUnit(@RequestParam Integer areaSqrMeter, @RequestParam(required = false) Integer noOfTenants, @RequestParam Integer unitTypeId, @PathVariable Integer floorId, @PathVariable Integer buildComplexId) {
        Unit unit = new Unit();
        BuildingComplex buildingComplex = this.buildComplexRepo.findById(buildComplexId).get();
        Floor floor = this.floorRepo.findById(floorId).get();
        unit.setFloor(floor);
        unit.setAreaSqrMeter(areaSqrMeter);
        unit.setNoOfTenants(noOfTenants);
        UnitType unitType = this.unitTypeRepo.findById(unitTypeId).get();
        unit.setUnitType(unitType);
        this.unitRepo.save(unit);
        return "redirect:/coowner_rep/viewFloor/"+ floorId +"/"+0;
    }

    @GetMapping("/coowner_rep/addEquipmentForm/{floorId}/{buildComplexId}")
    private String addEquipmentForm(Model model,@PathVariable Integer floorId, @PathVariable Integer buildComplexId){
        model.addAttribute("floorId");
        model.addAttribute("buildComplexId");
        return "addEquipment";
    }

    @PostMapping("/coowner_rep/addEquipment/{floorId}/{buildComplexId}")
    public String addEquipment(@RequestParam String equipmentName, @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateOfService, @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date nextServiceDate, @PathVariable Integer floorId, @PathVariable Integer buildComplexId) {
        Equipment equipment = new Equipment();
        BuildingComplex buildingComplex = this.buildComplexRepo.findById(buildComplexId).get();
        Floor floor = this.floorRepo.findById(floorId).get();
        equipment.setFloor(floor);
        equipment.setEquipmentName(equipmentName);
        equipment.setDateOfService(dateOfService);
        equipment.setNextServiceDate(nextServiceDate);
        this.equipmentRepo.save(equipment);
        return "redirect:/coowner_rep/viewFloor/"+ floorId +"/"+0;
    }

    @GetMapping("/coowner_rep/updateEquipmentForm/{equipmentId}")
    private String updateEquipmentForm(Model model,@PathVariable Integer equipmentId){
        model.addAttribute("equipmentId");
        model.addAttribute("floorId", this.equipmentRepo.findById(equipmentId).get().getFloor().getFloorId());
        model.addAttribute("equipmentName", this.equipmentRepo.findById(equipmentId).get().getEquipmentName());
        return "updateEquipment";
    }

    @PostMapping("/coowner_rep/updateEquipment/{equipmentId}")
    public String updateEquipment(@RequestParam(required = false) String equipmentName, @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateOfService, @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date nextServiceDate, @PathVariable Integer equipmentId) {
        Equipment equipment = this.equipmentRepo.findById(equipmentId).get();
        equipment.setEquipmentName(equipmentName);
        equipment.setDateOfService(dateOfService);
        equipment.setNextServiceDate(nextServiceDate);
        this.equipmentRepo.save(equipment);
        Integer floorId = equipment.getFloor().getFloorId();
        return "redirect:/coowner_rep/viewFloor/"+floorId+"/"+0;
    }


    @GetMapping("/coowner_rep/viewUnit/{unitId}/{page1}/{page2}")
    private String viewUnit(Model model, @PathVariable Integer page1, @PathVariable Integer page2, @PathVariable Integer unitId, @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate1,
                            @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate1,
                            @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate2,
                            @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate2
    ){
        Unit unit = this.unitRepo.findById(unitId).get();
        model.addAttribute("unit",unit);
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Sort sort1 = Sort.by(Sort.Direction.ASC, "dateOfReceipt");
        Sort sort2 = Sort.by(Sort.Direction.ASC, "dateOfPayment");
        Pageable pageable1 = PageRequest.of(page1, 5, sort1);
        Pageable pageable2 = PageRequest.of(page2, 5, sort2);
        Page<Expense> expenses = null;
        Page<Income> incomes = null;
        if(startDate1==null||endDate1==null) {
            if(startDate2==null||endDate2==null) {
                expenses = this.expenseRepo.getAllExpenseOfUnit(unitId, pageable1);
                incomes = this.incomeRepo.getAllIncomeOfUnit(unitId, pageable2);
            }
            else if(startDate2!=null||endDate2!=null){
                expenses = this.expenseRepo.getAllExpenseOfUnit(unitId, pageable1);
                incomes = this.incomeRepo.getIncomeOfPeriod(startDate2, endDate2, unitId, pageable2);
                model.addAttribute("startDate2",simpleDateFormat.format(startDate2));
                model.addAttribute("endDate2",simpleDateFormat.format(endDate2));
            }
        }
        else if(startDate2==null||endDate2==null){
            if(startDate1!=null||endDate1!=null) {
                expenses = this.expenseRepo.getExpenseOfPeriod(startDate1, endDate1, unitId, pageable1);
                incomes = this.incomeRepo.getAllIncomeOfUnit(unitId, pageable2);
                model.addAttribute("startDate1",simpleDateFormat.format(startDate1));
                model.addAttribute("endDate1",simpleDateFormat.format(endDate1));
            }
        }
        model.addAttribute("currentPage1",page1);
        model.addAttribute("currentPage2",page2);
        model.addAttribute("totalPages1", expenses.getTotalPages() );
        model.addAttribute("totalPages2", incomes.getTotalPages());
        model.addAttribute("expenses", expenses);
        model.addAttribute("incomes",incomes);
        String unitTypeName = unit.getUnitType().getUnitTypeName();
        model.addAttribute("unitTypeName",unitTypeName);
        Floor floor = this.floorRepo.findById(unit.getFloor().getFloorId()).get();
        model.addAttribute("buildComplexId",floor.getBuildingComplex().getBuildComplexId());
        CoOwner coOwner = this.unitRepo.findById(unitId).get().getCoOwner();
        model.addAttribute("coOwner",coOwner);
        return "viewUnit";
    }

    @GetMapping("/coowner_rep/updateUnit/{unitId}")
    public String updateUnitForm(@PathVariable Integer unitId, Model model){
        model.addAttribute("noOfTenants", unitRepo.findById(unitId).get().getNoOfTenants());
        model.addAttribute("floorId", unitRepo.findById(unitId).get().getFloor().getFloorId());
        return "updateUnit";
    }

    @PostMapping("/coowner_rep/updateUnit/{unitId}")
    public String updateUnit(@PathVariable Integer unitId, @RequestParam("noOfTenants") Integer noOfTenants){
        Unit unit = this.unitRepo.findById(unitId).get();
        unit.setNoOfTenants(noOfTenants);
        this.unitRepo.save(unit);
        return "redirect:/coowner_rep/viewFloor/"+unit.getFloor().getFloorId()+"/"+0;
    }

    @GetMapping("/coowner_rep/postBulletin/{buildComplexId}")
    public String postBulletinForm(Model model, @PathVariable Integer buildComplexId){
        model.addAttribute("buildComplexId",buildComplexId);
        return "postBulletin";
    }

    @PostMapping("/coowner_rep/postBulletin/{buildComplexId}")
    public String postBulletin(@PathVariable Integer buildComplexId, @RequestParam("bulletin") String bulletin){
        BuildingComplex buildingComplex = this.buildComplexRepo.findById(buildComplexId).get();
        buildingComplex.setBulletin(bulletin);
        this.buildComplexRepo.save(buildingComplex);
        return "redirect:/";
    }
}
