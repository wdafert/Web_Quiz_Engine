package engine;


import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class QuizController {

    private List<Quiz> quizzes = new ArrayList<>();

    public QuizController(){
    }

    @Autowired
    private QuizService quizService;

    @PostMapping(value = "/api/quizzes", consumes = "application/json")
    public Quiz addQuiz(@Valid @RequestBody Quiz quiz){

        int newId =quizzes.size();
        if (quiz.getAnswer()==null){

            Quiz toSave = new Quiz(quiz.getTitle(), quiz.getText(), quiz.getOptions(), new int[0], newId);
            quizzes.add(toSave);
            Quiz savedQuiz = quizService.saveQuiz(toSave);
        }else{
            Quiz toSave = new Quiz(quiz.getTitle(), quiz.getText(), quiz.getOptions(), quiz.getAnswer(), newId);
            quizzes.add(toSave);
            Quiz savedQuiz = quizService.saveQuiz(toSave);
        }
        return quizzes.get(newId);
    }

    @PostMapping(value = "/api/quizzes/{id}/solve", consumes = "application/json; charset=utf-8")
    public QuizAnswer solveQuizJson(@PathVariable int id, @RequestBody Guess guess){
        int[] answer = guess.getAnswer();
        if(quizzes.size()<=id){
            throw new QuizNotFoundException("Invalid id: " + id);
        }

        Quiz quiz= quizzes.get(id);

        if(answer.length==0&&quiz.getAnswer().length==0){
            return new QuizAnswer(true,"Congratulations, you're right!");
        }

        if(answer.length!=quiz.getAnswer().length){
            return new QuizAnswer(false,"Wrong answer! Please, try again");
        }

        for(int i = 0;i<answer.length;i++){
            if(answer[i]!=quiz.getAnswer()[i]){
                return new QuizAnswer(false,"Wrong answer! Please, try again");
            }
        }
        return new QuizAnswer(true,"Congratulations, you're right!");


    }

    @PostMapping(value = "/api/quizzes/{id}/solve", consumes = "application/x-www-form-urlencoded")
    public QuizAnswer solveQuizUrl(@PathVariable int id, int[] answer){
        if(quizzes.size()<=id){
            throw new QuizNotFoundException("Invalid id: " + id);
        }

        Quiz quiz= quizzes.get(id);
        int answerLength = answer.length;
        int counter = 0;
        if(answer.length==0&&quiz.getAnswer().length==0){
            return new QuizAnswer(true,"Congratulations, you're right!");
        }
        if(answer.length!=quiz.getAnswer().length){
            return new QuizAnswer(false,"Wrong answer! Please, try again");
        }

        for(int i = 0;i<answer.length;i++){
            if(answer[i]!=quiz.getAnswer()[i]){
                return new QuizAnswer(false,"Wrong answer! Please, try again");
            }
        }
        return new QuizAnswer(true,"Congratulations, you're right!");

    }


    @GetMapping("/api/quizzes/{id}")
    public Quiz getQuizById( @PathVariable(value="id",required = true) int id) {
        if(quizzes.size()<=id){
            throw new QuizNotFoundException("Invalid id: " + id);
        }
        Quiz quiz = quizzes.get(id);
        return quiz;

    }


    @GetMapping("/api/quizzes")
    public List getAllQuizzes() {
        return quizzes;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    class QuizNotFoundException extends RuntimeException {

        public QuizNotFoundException() {

        }

        public QuizNotFoundException(String message) {
            super(message);
        }

    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

}