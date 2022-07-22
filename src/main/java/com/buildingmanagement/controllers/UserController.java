package com.buildingmanagement.controllers;

import com.buildingmanagement.entities.*;
import com.buildingmanagement.repositories.*;
import com.buildingmanagement.services.BuildComplexService;
import com.buildingmanagement.services.UserService;
import com.buildingmanagement.services.impl.PDFGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.net.ssl.HandshakeCompletedEvent;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class UserController {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private BuildComplexService buildComplexService;

	@Autowired
	private BuildComplexRepo buildComplexRepo;

	@Autowired
	PDFGeneratorService pdfGeneratorService;

	@Autowired
	private FloorRepo floorRepo;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CoOwnerRepo coOwnerRepo;

	@Autowired
	private ExpenseRepo expenseRepo;

	@Autowired
	private BuildingManagerRepo buildingManagerRepo;

	@Autowired
	private IncomeRepo incomeRepo;

	@Autowired
	private UnitRepo unitRepo;

	@Autowired
	private UserService userService;

	@Autowired
	private InfoRepo infoRepo;

	@GetMapping("/login")
	public String login() {
		return "login";
	}
	@GetMapping("/")
	public String home(Principal principal) {
		String email = principal.getName();
		UserDetails details = userService.loadUserByUsername(email);
		if (details != null && details.getAuthorities()
				.stream()
				.anyMatch(a -> a.getAuthority()
						.equals("ADMIN"))) {
			return "redirect:/admin/0";

		}
		if (details != null && details.getAuthorities()
				.stream()
				.anyMatch(a -> a.getAuthority()
						.equals("BUILDMANAGER"))) {
			return "redirect:/manager/0";

		}
		if (details != null && details.getAuthorities()
				.stream()
				.anyMatch(a -> a.getAuthority()
						.equals("COOWNERREP"))) {
			return "redirect:/coowner_rep";

		}

		return "redirect:/coowner";
	}

	@GetMapping("/admin/{page}")
	public String homeAdmin(@PathVariable Integer page, Model model, String keyword) {
		Sort sort = Sort.by(Sort.Direction.ASC, "buildManagerName");
		Pageable pageable = PageRequest.of(page,5, sort);
		Page<BuildingManager> managers;
		if(keyword==null) {
			managers = this.buildingManagerRepo.findAll(pageable);
		}
		else{
			managers = this.buildingManagerRepo.searchByKeyword(keyword, pageable);
			model.addAttribute("keyword",keyword);
		}
		//managers.sort(Comparator.comparing(BuildingManager::getBuildManagerName));
		model.addAttribute("managers",managers);
		model.addAttribute("currentPage",page);
		model.addAttribute("totalPages", managers.getTotalPages());
		return "admin";
	}

	@GetMapping("/manager/{page}")
	public String homeManager(@PathVariable Integer page, Model model, String keyword, Principal principal, Integer build) {
		String username = principal.getName();
		BuildingManager buildingManager = this.buildingManagerRepo.findByUsername(username);
		Sort sort = Sort.by(Sort.Direction.ASC, "username");
		Pageable pageable = PageRequest.of(page,5, sort);
		Page<BuildingComplex> buildComplexes;
		if(keyword==null) {
			buildComplexes = this.buildComplexRepo.getAllBuildComplexOfManager(buildingManager.getBuildManagerId(), pageable);
		}
		else{
			buildComplexes = this.buildComplexRepo.searchByKeyword(buildingManager.getBuildManagerId(), keyword, pageable);
			model.addAttribute("keyword",keyword);
		}
		model.addAttribute("buildComplexes",buildComplexes);
		model.addAttribute("currentPage",page);
		model.addAttribute("totalPages", buildComplexes.getTotalPages());
		return "manager";
	}

	@GetMapping("/coowner_rep")
	public String homeCoOwnerRep(Model model, Principal principal, String keyword) {
		String username = principal.getName();
		BuildingComplex buildComplex = this.buildComplexRepo.findByUsername(username);
		List<Floor> floors = this.floorRepo.getAllFloorOfBuild(buildComplex.getBuildComplexId());
		model.addAttribute("buildComplex",buildComplex);
		model.addAttribute("floors",floors);
		model.addAttribute("bulletin", buildComplex.getBulletin());

		List<Info> infos;
		if(keyword==null) {
			infos = this.infoRepo.getAllInfoOfBuild(buildComplex.getBuildComplexId());
		}
		else{
			infos = this.infoRepo.searchInfoByKeyword(buildComplex.getBuildComplexId(), keyword);
		}
		model.addAttribute("infos", infos);
		return "coowner_rep";
	}

	@GetMapping("/coowner")
	public String homeCoOwner(Model model, Principal principal, String keyword) {
		String username = principal.getName();
		CoOwner coOwner = this.coOwnerRepo.findByUsername(username);
		Integer buildComplexId = coOwner.getUnits().get(0).getBuildingComplex().getBuildComplexId();
		List<Unit> units = this.unitRepo.getAllUnitOfCoOwner(coOwner.getCoOwnerId());
		coOwner.setUnits(units);
		model.addAttribute("units",units);
		BuildingComplex buildingComplex = this.buildComplexRepo.findById(units.get(0).getFloor().getBuildingComplex().getBuildComplexId()).get();
		model.addAttribute("bulletin", buildingComplex.getBulletin());


		List<Info> infos;
		if(keyword==null) {
			infos = this.infoRepo.getAllInfoOfBuild(buildComplexId);
		}
		else{
			infos = this.infoRepo.searchInfoByKeyword(buildComplexId, keyword);
		}
		model.addAttribute("infos", infos);
		return "coowner";
	}

	@GetMapping("/coOwner/viewCoOwnerUnit/{unitId}")
	public String homeCoOwner(Model model, Principal principal, @PathVariable Integer unitId) {
		String username = principal.getName();
		UserDetails details = userService.loadUserByUsername(username);
		Unit unit = this.unitRepo.findById(unitId).get();
		List<Expense> expenses = this.expenseRepo.getAllExpenseOfUnit(unitId);
		List<Income> incomes = this.incomeRepo.getAllIncomeOfUnit(unitId);
		model.addAttribute("expenses",expenses);
		model.addAttribute("incomes",incomes);
		model.addAttribute("unit",unit);
		return "viewCoOwnerUnit";
	}

	@GetMapping("/changePass")
	public String changePassForm(){
		return "changePass";
	}

	@PostMapping("/changePass")
	public String changePass(Principal principal, @RequestParam ("oldPass") String oldPass, @RequestParam ("newPass") String newPass ){
		String username =principal.getName();
		User curUser = this.userRepository.findByEmail(username);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication.getAuthorities().stream()
				.anyMatch(r -> r.getAuthority().equals("BUILDMANAGER"))){
			if(this.bCryptPasswordEncoder.matches(oldPass,curUser.getPassword())){
				curUser.setPassword(bCryptPasswordEncoder.encode(newPass));
				this.userRepository.save(curUser);
				BuildingManager buildingManager = this.buildingManagerRepo.findByUsername(curUser.getEmail());
				buildingManager.setPassword(newPass);
				this.buildingManagerRepo.save(buildingManager);
				return "redirect:/";

			}
			else{
				return "Passwords didn't match";
			}

		}

		if(authentication.getAuthorities().stream()
				.anyMatch(r -> r.getAuthority().equals("COOWNERREP"))){
			if(this.bCryptPasswordEncoder.matches(oldPass,curUser.getPassword())){
				curUser.setPassword(bCryptPasswordEncoder.encode(newPass));
				this.userRepository.save(curUser);
				BuildingComplex buildingComplex = this.buildComplexRepo.findByUsername(curUser.getEmail());
				buildingComplex.setPassword(newPass);
				this.buildComplexRepo.save(buildingComplex);
				return "redirect:/";

			}
			else{
				return "Passwords didn't match";
			}

		}

		if(authentication.getAuthorities().stream()
				.anyMatch(r -> r.getAuthority().equals("COOWNER"))){
			if(this.bCryptPasswordEncoder.matches(oldPass,curUser.getPassword())){
				curUser.setPassword(bCryptPasswordEncoder.encode(newPass));
				this.userRepository.save(curUser);
				CoOwner coOwner = this.coOwnerRepo.findByUsername(curUser.getEmail());
				coOwner.setPassword(newPass);
				this.coOwnerRepo.save(coOwner);
				return "redirect:/";

			}
			else{
				return "Passwords didn't match";
			}

		}

		return "redirect:/";
	}

	@GetMapping("/pdf/generate/{username}/{password}/{userType}")
	public void generatePDF(HttpServletResponse response, @PathVariable String username,
							@PathVariable String password, @PathVariable String userType) throws IOException {
		response.setContentType("application/pdf");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
		response.setHeader(headerKey, headerValue);
		this.pdfGeneratorService.export(response, username, password, userType);
	}
	@ModelAttribute("expenses")
	public List<Expense> expenses() {
		return new ArrayList<>();
	}

	@ModelAttribute("incomes")
	public List<Income> incomes() {
		return new ArrayList<>();
	}

	/*@PostMapping("/manager/unitReport/{unitId}")
	public void unitReport(HttpServletResponse response, @PathVariable Integer unitId) throws IOException {
		response.setContentType("application/pdf");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
		response.setHeader(headerKey, headerValue);

		List<Expense> expenses = this.expenseRepo.getAllExpenseOfUnit(unitId);
		List<Income> incomes = this.incomeRepo.getAllIncomeOfUnit(unitId);
		this.pdfGeneratorService.exportReportForUnit(response,expenses, incomes, unitId);
	}*/

	@PostMapping("/manager/unitReport/{unitId}")
	public void getExpenseReport(HttpServletResponse response, @PathVariable("unitId") Integer unitId,
		@DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
						  @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate, Integer expenseTypeId) throws IOException {

		Unit unit = unitRepo.findById(unitId).get();

		List<Expense> filteredExpenses = new ArrayList<>();
		for(Expense expense : unit.getExpenses()){
			if(expenseTypeId == null || expense.getExpenseType().getExpenseTypeId().equals(expenseTypeId)){
				if(startDate != null && expense.getDateOfReceipt().after(startDate)){
					if(endDate != null && expense.getDateOfReceipt().before(endDate)){
						filteredExpenses.add(expense);
					}else{
						filteredExpenses.add(expense);
					}

				}else{
					filteredExpenses.add(expense);
				}
			}
		}

		List<Income> filteredIncomes = new ArrayList<>();
		for(Income income : unit.getIncomes()){
			if(expenseTypeId == null || income.getExpenseType().getExpenseTypeId().equals(expenseTypeId)){
				if(startDate != null && income.getDateOfPayment().after(startDate)){
					if(endDate != null && income.getDateOfPayment().before(endDate)){
						filteredIncomes.add(income);
					}else{
						filteredIncomes.add(income);
					}

				}else{
					filteredIncomes.add(income);
				}
			}
		}

		response.setContentType("application/pdf");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
		response.setHeader(headerKey, headerValue);
		pdfGeneratorService.exportReportForExpenses(response, unit, filteredExpenses, filteredIncomes, startDate, endDate);
	}

	@PostMapping("/manager/buildReport/{buildComplexId}")
	public void buildReport(HttpServletResponse response, @PathVariable Integer buildComplexId) throws IOException {
		response.setContentType("application/pdf");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
		response.setHeader(headerKey, headerValue);

		BuildingComplex buildingComplex = this.buildComplexRepo.findById(buildComplexId).get();

		int totalExpense = 0;
		int totalIncome = 0;

		for(int i=0; i<buildingComplex.getUnits().size(); i++){
			for(int j=0; j<buildingComplex.getUnits().get(i).getExpenses().size(); j++ ) {
				totalExpense += buildingComplex.getUnits().get(i).getExpenses().get(j).getAmount();
			}
		}

		for(int i=0; i<buildingComplex.getUnits().size(); i++){
			for(int j=0; j<buildingComplex.getUnits().get(i).getIncomes().size(); j++ ) {
				totalIncome += buildingComplex.getUnits().get(i).getIncomes().get(j).getAmount();
			}
		}

		this.pdfGeneratorService.exportReportForBuilding(response,totalExpense, totalIncome, buildComplexId);
	}

	@PostMapping("/manager/build-equipment-report/{buildComplexId}")
	public void buildReportEquipment(HttpServletResponse response, @PathVariable Integer buildComplexId) throws IOException {
		response.setContentType("application/pdf");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
		response.setHeader(headerKey, headerValue);

		BuildingComplex buildingComplex = this.buildComplexRepo.findById(buildComplexId).get();
		this.pdfGeneratorService.exportReportForEquipment(response, buildingComplex);
	}

	@PostMapping("/coowner_rep/unitReport/{unitId}")
	public void unitReportCoownerRep(HttpServletResponse response, @PathVariable Integer unitId) throws IOException {
		response.setContentType("application/pdf");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
		response.setHeader(headerKey, headerValue);

		List<Expense> expenses = this.expenseRepo.getAllExpenseOfUnit(unitId);
		List<Income> incomes = this.incomeRepo.getAllIncomeOfUnit(unitId);
		this.pdfGeneratorService.exportReportForUnit(response,expenses, incomes, unitId);
	}

	@PostMapping("/coowner_rep/buildReport/{buildComplexId}")
	public void buildReportCoownerRep(HttpServletResponse response, @PathVariable Integer buildComplexId) throws IOException {
		response.setContentType("application/pdf");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
		response.setHeader(headerKey, headerValue);

		BuildingComplex buildingComplex = this.buildComplexRepo.findById(buildComplexId).get();

		int totalExpense = 0;
		int totalIncome = 0;

		for(int i=0; i<buildingComplex.getUnits().size(); i++){
			for(int j=0; j<buildingComplex.getUnits().get(i).getExpenses().size(); j++ ) {
				totalExpense += buildingComplex.getUnits().get(i).getExpenses().get(j).getAmount();
			}
		}

		for(int i=0; i<buildingComplex.getUnits().size(); i++){
			for(int j=0; j<buildingComplex.getUnits().get(i).getIncomes().size(); j++ ) {
				totalIncome += buildingComplex.getUnits().get(i).getIncomes().get(j).getAmount();
			}
		}

		this.pdfGeneratorService.exportReportForBuilding(response,totalExpense, totalIncome, buildComplexId);
	}




}
