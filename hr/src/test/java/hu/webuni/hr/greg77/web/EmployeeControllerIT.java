package hu.webuni.hr.greg77.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.assertj.core.data.TemporalUnitWithinOffset;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

import hu.webuni.hr.greg77.dto.EmployeeDto;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class EmployeeControllerIT {

	private static final String BASE_URI = "/api/employees";

	@Autowired
	WebTestClient webTestClient;

	@Test
	void testThatCreatedEmployeeIsListed() throws Exception {
		List<EmployeeDto> employeesBefore = getAllEmployees();

		EmployeeDto newEmployeeDto = new EmployeeDto(0L, "Thomas Test", "tesztelő", 12_345,
				LocalDateTime.now().minusMonths(100));

		saveEmployee(newEmployeeDto)
			.expectStatus()
			.isOk();
		
//		createEmployee(newEmployeeDto);

		List<EmployeeDto> employeesAfter = getAllEmployees();

		/*
		assertThat(employeesAfter
			.subList(0, employeesBefore.size()))
			.usingRecursiveFieldByFieldElementComparator()
			.containsExactlyElementsOf(employeesBefore);

		assertThat(employeesAfter.get(employeesAfter.size() - 1))
			.usingRecursiveComparison()
			.isEqualTo(newEmployeeDto);
		*/
		
		assertThat(employeesAfter.size()).isEqualTo(employeesBefore.size() + 1);
		assertThat(employeesAfter.get(employeesAfter.size()-1))
			.usingRecursiveComparison()
			.ignoringFields("id","startDate")
			.isEqualTo(newEmployeeDto);
		assertThat(employeesAfter.get(employeesAfter.size()-1)
				.getStartDate())
				.isCloseTo(newEmployeeDto.getStartDate(), new TemporalUnitWithinOffset(1,ChronoUnit.SECONDS));
	}

	@Test
	void testThatNewInvalidEmployeeIsCreated() throws Exception {
		List<EmployeeDto> employeesBefore = getAllEmployees();

		EmployeeDto newInvalidEmployeeDto = new EmployeeDto(10L, "", null, 12_345,
				LocalDateTime.now().minusMonths(100));

		createInvalidEmployee(newInvalidEmployeeDto);

		List<EmployeeDto> employeesAfter = getAllEmployees();

		assertThat(employeesAfter)
			.usingRecursiveFieldByFieldElementComparator()
			.containsExactlyElementsOf(employeesBefore);
	}


	/*
	 * strategy: 1. create new 'employeeDto'
	 * 2.a) test_1: "before" and "after" lists are same size
	 * 2.b) test_2: "after" list contains 'employeeDto'
	 */
	@Test
	void testThatModifiedEmployeeHasUpdatedData() throws Exception {
		List<EmployeeDto> employeesBefore = getAllEmployees();

		EmployeeDto newEmployeeDto = new EmployeeDto(1L, "Thomas Test", "tesztelő", 12_345,
				LocalDateTime.now().minusMonths(100));

		updateEmployee(newEmployeeDto);

		List<EmployeeDto> employeesAfter = getAllEmployees();

		int index = findEmployeeIndex(employeesAfter, newEmployeeDto.getId());
		
		assertThat(employeesAfter.size()).isEqualTo(employeesBefore.size());
		assertThat(employeesAfter.get(index))
			.usingRecursiveComparison()
			.ignoringFields("startDate")
			.isEqualTo(newEmployeeDto);
		
		assertThat(employeesAfter.get(index).getStartDate())
				.isCloseTo(newEmployeeDto.getStartDate(), new TemporalUnitWithinOffset(1,ChronoUnit.SECONDS));
		
		/*
		System.out.println("EmployeesBefore list:");
		for (EmployeeDto employeeDto : employeesBefore) {
			System.out.println(employeeDto.toString());
		}		
		System.out.println("New employee:");
		System.out.println(newEmployeeDto.toString());
		System.out.println("EmployeesAfter list:");
		for (EmployeeDto employeeDto : employeesAfter) {
			System.out.println(employeeDto.toString());
		}
		System.out.println("findFirst() employee from EmployeesAfter list:");
		System.out.println(employeesAfter.get(index).toString());
		*/
	}

	@Test
	void testThatModifiedInvalidEmployeeHasUpdatedData() throws Exception {
		List<EmployeeDto> employeesBefore = getAllEmployees();

		EmployeeDto newInvalidEmployeeDto = new EmployeeDto(1L, "", null, 12_345,
				LocalDateTime.now().minusMonths(100));

		updateInvalidEmployee(newInvalidEmployeeDto);

		List<EmployeeDto> employeesAfter = getAllEmployees();

		assertThat(employeesAfter)
			.usingRecursiveFieldByFieldElementComparator()
			.containsExactlyElementsOf(employeesBefore);
	}
	
	private int findEmployeeIndex(List<EmployeeDto> employeesAfter, long id) {
		int index = 0;
		for (int i = 0; i < employeesAfter.size(); i++) {
            if (employeesAfter.get(i).getId() == id) {
                index = i;
                break;
            }
        }
		return index;
	}

	private void createEmployee(EmployeeDto newEmployeeDto) {
		webTestClient
			.post()
			.uri(BASE_URI)
			.bodyValue(newEmployeeDto)
			.exchange()
			.expectStatus()
			.isOk();
	}
	

	private void createInvalidEmployee(EmployeeDto newEmployeeDto) {
		webTestClient
			.put()
			.uri(BASE_URI + "/" + newEmployeeDto.getId())
			.bodyValue(newEmployeeDto)
			.exchange()
			.expectStatus()
			.isBadRequest();		
	}
	
	private void updateInvalidEmployee(EmployeeDto newEmployeeDto) {
		webTestClient
			.put()
			.uri(BASE_URI + "/" + newEmployeeDto.getId())
			.bodyValue(newEmployeeDto)
			.exchange()
			.expectStatus()
			.isBadRequest();
	}

	private void updateEmployee(EmployeeDto newEmployeeDto) {
		webTestClient
			.put()
			.uri(BASE_URI + "/" + newEmployeeDto.getId())
			.bodyValue(newEmployeeDto)
			.exchange()
			.expectStatus()
			.isOk();
	}
	
	private ResponseSpec saveEmployee(EmployeeDto newEmployee) {
		return webTestClient
				.post()
				.uri(BASE_URI)
				.bodyValue(newEmployee)
				.exchange();
	}
	
	private List<EmployeeDto> getAllEmployees() {
		List<EmployeeDto> responseList = webTestClient
				.get()
				.uri(BASE_URI)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBodyList(EmployeeDto.class)
				.returnResult()
				.getResponseBody();
//		Collections.sort(responseList, (a1, a2) -> Long.compare(a1.getId(), a2.getId()));
		Collections.sort(responseList, Comparator.comparing(EmployeeDto::getId));
		return responseList;
	}
}
