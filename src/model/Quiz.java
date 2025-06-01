package model;

public class Quiz {
    private int quizId;
    private int teacherId;
    private int subjectId;
    private String accessCode;
    private String subjectName;
    private int questionCount; // Make sure this field exists

    // Default constructor
    public Quiz() {
        // Initialize fields if necessary
    }

    // Constructor with all fields
    public Quiz(int quizId, int teacherId, int subjectId, String accessCode, String subjectName) {
        this.quizId = quizId;
        this.teacherId = teacherId;
        this.subjectId = subjectId;
        this.accessCode = accessCode;
        this.subjectName = subjectName;
        // questionCount is often set separately or derived, so not always in this constructor
    }

    // Getters
    public int getQuizId() {
        return quizId;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    // Setters
    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    // Corrected setQuestionCount method
    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }
}