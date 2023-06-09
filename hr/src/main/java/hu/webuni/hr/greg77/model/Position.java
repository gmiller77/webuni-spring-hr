package hu.webuni.hr.greg77.model;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Position {

	@Id
	@GeneratedValue
	private int id;
	private String name;
	private Qualification qualification;
//	private int minSalary;
	
	@OneToMany(mappedBy = "position")
	private List<Employee> employees;
	
	public Position() {
	}
	
	public Position(String name, Qualification qualification) {
		super();
		this.name = name;
		this.qualification = qualification;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Qualification getQualification() {
		return qualification;
	}
	public void setQualification(Qualification qualification) {
		this.qualification = qualification;
	}

//	public int getMinSalary() {
//		return minSalary;
//	}
//
//	public void setMinSalary(int minSalary) {
//		this.minSalary = minSalary;
//	}
	
}
