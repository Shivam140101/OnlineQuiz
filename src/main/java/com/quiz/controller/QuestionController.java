package com.quiz.controller;

import com.quiz.model.Question;
import com.quiz.service.IQuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;


@RequiredArgsConstructor
@RestController
@RequestMapping("/quizzes")
public class QuestionController {

    @Autowired
    private final IQuestionService service;

    @PostMapping("/create-new-question")
    public ResponseEntity<Question> createQuestion(@Valid @RequestBody Question question) {
        Question createdQuestion = service.createQuestion(question);
        return ResponseEntity.status(CREATED).body(createdQuestion);
    }

    @GetMapping("/all-questions")
    public ResponseEntity<List<Question>> getAllQuestions() {
        List<Question> question = service.getAllQuestion();
        return ResponseEntity.ok(question);
    }

    @GetMapping("/question/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable Long id) throws NotFoundException {
        Optional<Question> theQuestion = service.getQuestionById(id);
        if (theQuestion.isPresent()) {
            return ResponseEntity.ok(theQuestion.get());
        } else {
            throw new NotFoundException();
        }
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<Question> updateQuestion(@PathVariable Long id , @RequestBody Question question) throws NotFoundException {
        Question updatesQuestion = service.updateQuestion(id, question);
        return ResponseEntity.ok(updatesQuestion);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id){
        service.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/subjects")
    public ResponseEntity<List<String>> getAllSubjects(){
        List<String> allSubjects = service.getAllSubjects();
        return ResponseEntity.ok(allSubjects);
    }

    @GetMapping("/fetch-questions-for-user")
    public ResponseEntity<List<Question>> getQuestionsForUser(@RequestParam Integer numOfQuestions,
                                                              @RequestParam String subjects){
        List<Question> allQuestions = service.getQuestionForUser(numOfQuestions, subjects);
        List<Question> mutableQuestions = new ArrayList<>(allQuestions);
        Collections.shuffle(mutableQuestions);

        int availableQuestions = Math.min(numOfQuestions,mutableQuestions.size());
        List<Question> randomQuestions = mutableQuestions.subList(0 , availableQuestions);
        return ResponseEntity.ok(randomQuestions);

    }

}
