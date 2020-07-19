package com.pm.backend.controller.v1;


import com.pm.backend.model.v1.solution.SolutionModel;
import com.pm.backend.repository.SolutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/v1/solutions")
public class SolutionController {
    
    
    @Autowired
    private SolutionRepository solutionRepository;

    @PostMapping("/addSolution")
    public String addSolution(@RequestBody SolutionModel solution) {
        solutionRepository.save(solution);

        return "Added solution: " + solution.toString();
    }

    @GetMapping("/getSolution/{id}")
    public Optional<SolutionModel> getSolution(@PathVariable String id) {
        return solutionRepository.findById(id);
    }

    @DeleteMapping("/deleteSolution/{id}")
    public String deleteSolution(@PathVariable String id) {
        solutionRepository.deleteById(id);
        return "Deleted solution with id: " + id;
    }
}
