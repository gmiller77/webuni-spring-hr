package hu.webuni.hr.greg77.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import hu.webuni.hr.greg77.dto.CompanyDto;
import hu.webuni.hr.greg77.dto.EmployeeDto;
import hu.webuni.hr.greg77.mapper.CompanyMapper;
import hu.webuni.hr.greg77.model.AverageSalaryByPosition;
import hu.webuni.hr.greg77.model.Company;
import hu.webuni.hr.greg77.service.CompanyService;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

	@Autowired
	CompanyService companyService;

	@Autowired
	CompanyMapper companyMapper;
	
//	@Autowired
//	CompanyRepository companyRepository;

	@GetMapping
	public List<CompanyDto> getAll(@RequestParam Optional<Boolean> full) {

		List<Company> companies = companyService.findAll();
		return mapCompanies(companies, full);
	}

	private List<CompanyDto> mapCompanies(List<Company> companies, Optional<Boolean> full) {
		if (full.orElse(false))
			return companyMapper.companiesToDtos(companies);
		else
			return companyMapper.companiesToSummaryDtos(companies);
	}
	

	@GetMapping("/{id}")
	public CompanyDto getById(@PathVariable long id, @RequestParam Optional<Boolean> full) {
		Company company = companyService.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		if (full.orElse(false)) {
			return companyMapper.companyToDto(company);
		} else {
			return companyMapper.companyToSummaryDto(company);
		}
	}

	@PostMapping
	public CompanyDto createCompany(@RequestBody CompanyDto companyDto) {
		return companyMapper.companyToDto(companyService.save(companyMapper.dtoToCompany(companyDto)));
	}

	@PutMapping("/{id}")
	public CompanyDto modifyCompany(@PathVariable long id, @RequestBody CompanyDto companyDto) {
		companyDto.setId(id);
		Company updatedCompany = companyService.update(companyMapper.dtoToCompany(companyDto));
		if (updatedCompany == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		return companyMapper.companyToDto(updatedCompany);
	}

	@DeleteMapping("/{id}")
	public void deleteCompany(@PathVariable long id) {
		companyService.delete(id);
	}

	@PostMapping("/{id}/employees")
	public CompanyDto addNewEmployee(@PathVariable long id, @RequestBody EmployeeDto employeeDto) {
		Company company = companyService.addEmployee(id, companyMapper.dtoToEmployee(employeeDto));
		return companyMapper.companyToDto(company);
	}

	@DeleteMapping("/{id}/employees/{employeeId}")
	public CompanyDto deleteEmployee(@PathVariable long id, @PathVariable long employeeId) {
		Company company = companyService.deleteEmployee(id, employeeId);
		return companyMapper.companyToDto(company);
	}

	@PutMapping("/{id}/employees")
	public CompanyDto replaceAllEmployees(@PathVariable long id, @RequestBody List<EmployeeDto> newEmployees) {
		Company company = companyService.replaceEmployees(id, companyMapper.dtosToEmployees(newEmployees));
		return companyMapper.companyToDto(company);
	}

	@GetMapping(params = "aboveSalary")
	public List<CompanyDto> getCompaniesAboveSalary(@RequestParam int aboveSalary,
			@RequestParam Optional<Boolean> full) {
		List<Company> filteredCompanies = companyService.findCompaniesWithHighSalaryEmployees(aboveSalary);
		return mapCompanies(filteredCompanies, full);
	}

	@GetMapping(params = "aboveEmployeeCount")
	public List<CompanyDto> getCompaniesAboveEmployeeCount(@RequestParam int aboveEmployeeCount,
			@RequestParam Optional<Boolean> full) {
		List<Company> filteredCompanies = companyService.findCompaniesWithEmployeeCountHigherThan(aboveEmployeeCount);
		return mapCompanies(filteredCompanies, full);
	}

	@GetMapping("/{id}/salaryStats")
	public List<AverageSalaryByPosition> getSalaryStatsById(@PathVariable long id) {
		return companyService.findAverageSalariesByPosition(id);
	}
	

}
