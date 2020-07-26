package com.pm.backend.controller.v1;

import com.pm.backend.model.v1.question.QuestionModel;
import com.pm.backend.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.pm.backend.controller.v1.SchemaVersion.v;

import java.util.Optional;

@RestController
@RequestMapping("/v1/questions")
public class QuestionController {
    @Autowired
    private QuestionRepository questionRepository;

    @PostMapping
    public ResponseEntity addQuestion(@RequestBody QuestionModel question) {
        question.setSchemaVersion(v);
        questionRepository.save(question);

        return ResponseEntity.ok("Added question: " + question.toString());
    }

    @GetMapping("/{id}")
    public ResponseEntity getQuestion(@PathVariable int id) {
        Optional<QuestionModel> question = questionRepository.findById(id);
        if (question.isPresent()) {
            QuestionModel questionModel = question.get();
            return ResponseEntity.ok().body(new QuestionModel().setId(questionModel.getId())
                    .setQuestionDescription(questionModel.getQuestionDescription())
                    .setQuestionDifficulty(questionModel.getQuestionDifficulty())
                    .setQuestionTitle(questionModel.getQuestionTitle())
                    .setQuestionTopics(questionModel.getQuestionTopics())
                    .setQuestionUrl(questionModel.getQuestionUrl()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteQuestion(@PathVariable int id) {
        questionRepository.deleteById(id);
        return ResponseEntity.ok("Deleted question with id: " + id);
    }
}
