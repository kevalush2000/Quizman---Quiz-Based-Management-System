package model;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private int id;
    private int quizId;
    private String text;
    private int order;
    private List<Answer> answers; // To hold answers associated with this question

    // Default constructor for utility or reflection
    public Question() {
        this.answers = new ArrayList<>();
    }

    public Question(int id, int quizId, String text, int order) {
        this.id = id;
        this.quizId = quizId;
        this.text = text;
        this.order = order;
        this.answers = new ArrayList<>();
    }

    // Getters and setters
    public int getId() { return id; }
    public int getQuizId() { return quizId; }
    public String getText() { return text; }
    public int getOrder() { return order; }
    public List<Answer> getAnswers() { return answers; }

    // Corrected setId method
    public void setId(int id) {
        this.id = id;
    }

    public void setQuizId(int quizId) { this.quizId = quizId; }
    public void setText(String text) { this.text = text; }
    public void setOrder(int order) { this.order = order; }
    public void setAnswers(List<Answer> answers) { this.answers = answers; }

    // Method to add an answer to the list
    public void addAnswer(Answer answer) {
        if (this.answers == null) {
            this.answers = new ArrayList<>();
        }
        this.answers.add(answer);
    }
}