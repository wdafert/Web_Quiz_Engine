package engine;

public class QuizAnswer {


    private final boolean success ;
    private final String feedback;


    public QuizAnswer(boolean success, String feedback) {
        this.success = success;
        this.feedback = feedback;

    }

    public String getFeedback() {
        return feedback;
    }

    public boolean isSuccess() {
        return success;
    }
}