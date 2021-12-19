package net.java.springboot.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.java.springboot.model.Depart;
import net.java.springboot.model.Employee;
import net.java.springboot.projectExceptions.ResourceNotFoundException;
import net.java.springboot.repositories.DepartRepository;
import net.java.springboot.repositories.EmployeeRepository;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/")
public class EmployeeController {
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private DepartRepository departRepository;
	
	//Get all employees
	@GetMapping("/getEmployees")
	public List<Employee> getAllEmployee() {
		return employeeRepository.findAll();
	}
	
	//Get employee by id
	@GetMapping("/getEmployee/{id}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable long id) {
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Employee not exist with id: " + id));
		return ResponseEntity.ok(employee);
	}

	//Add employee to repository
	@PostMapping("/addEmployee")
	public Employee createEmployee(@RequestBody Employee employee) {
		return employeeRepository.save(employee);
	}
	
	//Add employee to depart
	@PostMapping("/depart/{id}/addEmployee")
	public ResponseEntity<Employee> addEmployee(@PathVariable Long id, @RequestBody Employee employee) {
		Depart depart = departRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Depart not exist with id: " + id));	
		Employee employeeFound = employeeRepository.findById(employee.getId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Employee not exist with id: " + employee.getId()));
		if(depart.getBasicSalary() < employeeFound.getSalary())
			throw new ResourceNotFoundException("Employee's salary is more than basic depart's salary");
		
		depart.getEmployee().add(employeeFound);
		departRepository.save(depart);
		return ResponseEntity.ok(employeeFound);
	}
	
	//Update employee
	@PutMapping("/updateEmployee/{id}")
	public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employeeDetails) {
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id: " + id));
		
		employee.setFirstName(employeeDetails.getFirstName());
		employee.setLastName(employeeDetails.getLastName());
		employee.setBirthDay(employeeDetails.getBirthDay());
		employee.setEmail(employeeDetails.getBirthDay());
		employee.setGender(employeeDetails.getGender());
		employee.setPhone(employeeDetails.getPhone());
		employee.setRole(employeeDetails.getRole());
		employee.setSalary(employeeDetails.getSalary());
		
		Employee updatedEmployee = employeeRepository.save(employee);
		return ResponseEntity.ok(updatedEmployee);
	}
	
	@DeleteMapping("/deleteEmployee/{id}")
	public ResponseEntity<Map<String, Boolean>> deleteEmployee(@PathVariable Long id) {
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id: " + id));
		employeeRepository.delete(employee);
		
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted employee", Boolean.TRUE);
		return ResponseEntity.ok(response);
	}
}
