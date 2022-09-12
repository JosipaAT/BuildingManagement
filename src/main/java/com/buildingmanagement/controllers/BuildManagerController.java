package com.buildingmanagement.controllers;

import com.buildingmanagement.entities.*;
import com.buildingmanagement.repositories.BuildingManagerRepo;
import com.buildingmanagement.repositories.CityListRepo;
import com.buildingmanagement.services.BuildingManagerService;
import com.buildingmanagement.services.CityListService;
import com.buildingmanagement.services.UserService;
import com.buildingmanagement.services.impl.PDFGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
public class BuildManagerController {

    @Autowired
    private PDFGeneratorService pdfGeneratorService;

    @Autowired
    private  UserService userService;

    @Autowired
    private BuildingManagerRepo buildingManagerRepo;

    @Autowired
    private BuildingManagerService buildingManagerService;

    @Autowired
    private CityListRepo cityListRepo;

    @GetMapping("/admin/addBuildManagerForm")
    private String addBuildManagerForm(Model model){
        model.addAttribute("cityLists",this.cityListRepo.findAll());
        return "addBuildManager";
    }

    @ModelAttribute("buildingManager")
    public BuildingManager buildingManager() {
        return new BuildingManager();
    }

    @PostMapping("/admin/addBuildManager")
    public String registerUserAccount(@ModelAttribute("buildingManager")  BuildingManager buildingManager, Model model,
                                    @RequestParam(required = false) Integer cityId) {
        this.buildingManagerService.addBuildManager(buildingManager);
        String username = buildingManager.getUsername();
        String password =  buildingManager.getPassword();
        String userType = "Building Manager";
        buildingManager.setCityList(cityListRepo.getById(cityId));
        return "redirect:/pdf/generate/"+ username +"/"+ password +"/"+ userType;
    }

    @GetMapping("/admin/updateBuildManager/{buildManagerId}")
    private String updateBuildManagerForm(@PathVariable Integer buildManagerId,Model model){
        model.addAttribute("buildManagerId");
        model.addAttribute("username", this.buildingManagerRepo.findById(buildManagerId).get().getUsername());
        model.addAttribute("password", this.buildingManagerRepo.findById(buildManagerId).get().getPassword());
        model.addAttribute("userType", "Building Manager");
        model.addAttribute("buildManagerName",this.buildingManagerRepo.findById(buildManagerId).get().getBuildManagerName());
        model.addAttribute("streetName",this.buildingManagerRepo.findById(buildManagerId).get().getStreetName());
        model.addAttribute("streetNumber",this.buildingManagerRepo.findById(buildManagerId).get().getStreetNumber());
        model.addAttribute("cityLists",this.cityListRepo.findAll());
        model.addAttribute("postalCode",this.buildingManagerRepo.findById(buildManagerId).get().getPostalCode());
        model.addAttribute("contact",this.buildingManagerRepo.findById(buildManagerId).get().getContact());
        return "updateBuildManager";
    }

    @PostMapping("/admin/updateBuildManager/{buildManagerId}")
    public String updateBuildManager(@PathVariable Integer buildManagerId, @RequestParam(required = false) String buildManagerName,
                                     @RequestParam(required = false) Integer contact, @RequestParam(required = false) String streetName,
                                     @RequestParam(required = false) Integer streetNumber, @RequestParam(required = false) Integer cityId,
                                     @RequestParam(required = false) Integer postalCode, @RequestParam(required = false) String username) {
     BuildingManager buildingManager = this.buildingManagerRepo.findById(buildManagerId).get();
     buildingManager.setBuildManagerName(buildManagerName);
     buildingManager.setUsername(username);
     buildingManager.setStreetName(streetName);
     buildingManager.setStreetNumber(streetNumber);
     buildingManager.setCityList(cityListRepo.getById(cityId));
     buildingManager.setPostalCode(postalCode);
     buildingManager.setContact(contact);
     this.buildingManagerService.update(buildManagerId, buildingManager);
     return "redirect:/";
    }

}
