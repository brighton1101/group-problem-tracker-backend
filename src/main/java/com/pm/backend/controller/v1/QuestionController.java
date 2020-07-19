package com.pm.backend.controller.v1;

import com.pm.backend.model.v1.question.QuestionModel;
import com.pm.backend.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/v1/questions")
public class QuestionController {
    @Autowired
    private QuestionRepository questionRepository;

    @PostMapping("/addQuestion")
    public String addQuestion(@RequestBody QuestionModel question) {
        questionRepository.save(question);

        return "Added question: " + question.toString();
    }

    @GetMapping("/getQuestion/{id}")
    public Optional<QuestionModel> getQuestion(@PathVariable int id) {
        return questionRepository.findById(id);
    }

    @DeleteMapping("/deleteQuestion/{id}")
    public String deleteQuestion(@PathVariable int id) {
        questionRepository.deleteById(id);
        return "Deleted question with id: " + id;
    }
}
