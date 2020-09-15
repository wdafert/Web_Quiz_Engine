package engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuizService implements QuizServiceInterface{

    @Autowired
    private QuizRepository quizRepository;

    @Override
    public Quiz saveQuiz(final Quiz quiz) {
        System.out.println("Answer: "+ quiz.getAnswer().toString());
        System.out.println("Options: "+ quiz.getOptions().toString());
        System.out.println("Text: "+ quiz.getText().toString());
        System.out.println("Title: "+ quiz.getTitle().toString());
        System.out.println("Id: "+ quiz.getId());
        quizRepository.save(quiz);
        return quiz;
    }
}
