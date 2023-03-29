package hu.webuni.hr.greg77.dto;

import java.util.ArrayList;
import java.util.List;

public class CompanyDto {
	private long id;
	private String companyIdNumber;
	private String name;
	private String address;
	private List<EmployeeDto> employees = new ArrayList<>();

	public CompanyDto() {
	}

	public CompanyDto(long id, String companyIdNumber, String name, String address) {
		this.id = id;
		this.companyIdNumber = companyIdNumber;
		this.name = name;
		this.address = address;
	}

	public CompanyDto(long id, String companyIdNumber, String name, String address, List<EmployeeDto> employees) {
		this.id = id;
		this.companyIdNumber = companyIdNumber;
		this.name = name;
		this.address = address;
		this.employees = employees;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCompanyIdNumber() {
		return companyIdNumber;
	}

	public void setCompanyIdNumber(String companyIdNumber) {
		this.companyIdNumber = companyIdNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<EmployeeDto> getEmployees() {
		return employees;
	}

	public void setEmployees(List<EmployeeDto> employees) {
		this.employees = employees;
	}

}
