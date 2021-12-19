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
import net.java.springboot.projectExceptions.ResourceNotFoundException;
import net.java.springboot.repositories.DepartRepository;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/")
public class DepartController {	
	@Autowired
	private DepartRepository departRepository;
	
	//Get all departs
	@GetMapping("/getDeparts")
	public List<Depart> getAllDepart() {
		return departRepository.findAll();
	}
	
	//Get Depart by ID
	@GetMapping("/depart/{id}")
	public ResponseEntity<Depart> getDepartById(@PathVariable long id) {
		Depart depart = departRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Depart not exist with id: " + id));
		return ResponseEntity.ok(depart);
	}
	
	//Add depart to repository
	@PostMapping("/addDepart")
	public Depart createDepart(@RequestBody Depart depart) {
		return departRepository.save(depart);
	}
	
	//Update depart
	@PutMapping("/updateDepart/id")
	public ResponseEntity<Depart> updateDepart(@PathVariable Long id, @RequestBody Depart departDetails) {
		Depart depart = departRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Depart not exist with id: " + id));
		
		depart.setBasicSalary(departDetails.getBasicSalary());
		depart.setEmployee(departDetails.getEmployee());
		depart.setName(departDetails.getName());
		
		Depart updatedDepart = departRepository.save(depart);
		return ResponseEntity.ok(updatedDepart);
	}
	
	//Delete Depart
	@DeleteMapping("/deleteDepart/{id}")
	public ResponseEntity<Map<String, Boolean>> deleteDepart(@PathVariable Long id) {
		Depart depart = departRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Depart not exist with id: " + id));
		
		departRepository.delete(depart);
		
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted depart", Boolean.TRUE);
		return ResponseEntity.ok(response);
	}
}
