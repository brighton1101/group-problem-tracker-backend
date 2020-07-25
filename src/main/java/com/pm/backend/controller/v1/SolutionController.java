package com.pm.backend.controller.v1;


import com.pm.backend.controller.v1.request.AddSolutionRequest;
import com.pm.backend.model.v1.solution.SolutionModel;
import com.pm.backend.repository.SolutionRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.pm.backend.controller.v1.SchemaVersion.v;

@RestController
@RequestMapping("/v1/solutions")
public class SolutionController {
    
    
    @Autowired
    private SolutionRepository solutionRepository;

    @PostMapping
    public ResponseEntity addSolution(@NotNull @RequestBody AddSolutionRequest addSolutionRequest) {
        //solution.setSchemaVersion(v);
        SolutionModel solution = new SolutionModel().setUserId(addSolutionRequest.getUserId())
                                        .setGroupId(addSolutionRequest.getGroupId())
                                        .setQuestionId(addSolutionRequest.getQuestionId())
                                        .setSolutionCode(addSolutionRequest.getSolutionCode())
                                        .setSolutionLanguage(addSolutionRequest.getSolutionLanguage())
                                        .setSchemaVersion(v);
        solutionRepository.save(solution);

        return ResponseEntity.ok("Added solution: " + solution.getId());
    }

    @GetMapping("/{id}")
    public Optional<SolutionModel> getSolution(@PathVariable String id) {
        return solutionRepository.findById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteSolution(@PathVariable String id) {
        solutionRepository.deleteById(id);
        return ResponseEntity.ok("Deleted solution with id: " + id);
    }
}
