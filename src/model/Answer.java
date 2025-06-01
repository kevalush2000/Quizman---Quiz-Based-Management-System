package model;

public class Answer {
    private int id;
    private int questionId;
    private String text;
    private boolean isCorrect;
    private int order;

    // Default constructor for utility or reflection
    public Answer() {}

    public Answer(int id, int questionId, String text, boolean isCorrect, int order) {
        this.id = id;
        this.questionId = questionId;
        this.text = text;
        this.isCorrect = isCorrect;
        this.order = order;
    }

    // Getters
    public int getId() { return id; }
    public int getQuestionId() { return questionId; }
    public String getText() { return text; }
    public boolean isCorrect() { return isCorrect; }
    public int getOrder() { return order; }

    // Setters (if needed, though constructor often suffices for immutable models)
    public void setId(int id) { this.id = id; }
    public void setQuestionId(int questionId) { this.questionId = questionId; }
    public void setText(String text) { this.text = text; }
    public void setCorrect(boolean correct) { isCorrect = correct; }
    public void setOrder(int order) { this.order = order; }
}