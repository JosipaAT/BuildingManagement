package com.buildingmanagement.controllers;
import com.buildingmanagement.repositories.*;
import com.buildingmanagement.entities.CityList;
import com.buildingmanagement.services.CityListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CityListController {

    @Autowired
    private CityListRepo cityListRepo;

    @Autowired
    private CityListService cityListService;

    @GetMapping("/admin/addNewCityForm")
    private String addNewCityForm(Model model) {
        model.addAttribute("cityLists",this.cityListRepo.findAll());
        return "addNewCity";
    }

    @ModelAttribute("cityList")
    public CityList cityList() {
        return new CityList();
    }

    @PostMapping("/admin/addNewCity")
    public String addNewCity(@RequestParam String cityName, Model model) {
        CityList cityList = new CityList();
        cityList.setCityName(cityName);
        this.cityListRepo.save(cityList);
        return "redirect:/admin/addNewCityForm";
    }

    private void addNewCity(CityList cityList) {
    }

    @GetMapping("/admin/searchCity")
    public String searchCity(@RequestParam String keyword, Model model)
    {
        this.cityListRepo.searchByKeyword(keyword);
        List<CityList> cityLists = this.cityListRepo.findAll();
        model.addAttribute("cityLists",this.cityListRepo.searchByKeyword(keyword));
        return "addNewCity";

    }
}
