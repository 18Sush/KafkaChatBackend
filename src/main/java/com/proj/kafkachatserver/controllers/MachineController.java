package com.proj.kafkachatserver.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.proj.kafkachatserver.models.Machine;
import com.proj.kafkachatserver.services.MachineService;

import java.util.List;

@RestController
@RequestMapping("/api/machines")
public class MachineController {
    
	@Autowired
    private MachineService machineService;

    
	@GetMapping
    public List<Machine> getAllMachines() {
        return machineService.getAllMachinesWithStatus();
    }

      
	@PostMapping
	public ResponseEntity<?> registerMachine(@RequestBody Machine machine) {
	    try {
	        Machine registeredMachine = machineService.registerMachine(machine);
	        // Create a simple success message response
	        String successMessage = "Machine registered successfully.";
	        return ResponseEntity.status(HttpStatus.CREATED).body(successMessage);
	    } catch (IllegalArgumentException e) {
	        // Return an error message response
	        return ResponseEntity.badRequest().body(e.getMessage());
	    }
	}



    @PostMapping("/{id}/status")
    public ResponseEntity<?> updateMachineStatus(@PathVariable Long id, @RequestParam("online") boolean online) {
        try {
            machineService.updateMachineStatus(id, online);
            return ResponseEntity.ok("Machine status updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/online")
    public ResponseEntity<List<Machine>> getOnlineMachines() {
        List<Machine> onlineMachines = machineService.getOnlineMachines();
        return ResponseEntity.ok(onlineMachines);
    }
}
    
    



