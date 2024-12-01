public class Question {
    private String question;
    private String rightAnswer;
    private String[] possibleAnswers;

    public Question(String question, String rightAnswer, String[] possibleAnswers) {
        this.question = question;
        this.rightAnswer = rightAnswer;
        this.possibleAnswers = possibleAnswers;
    }

    public String getQuestion() {
        return question;
    }

    public String getRightAnswer() {
        return rightAnswer;
    }

    public String[] getPossibleAnswers() {
        return possibleAnswers;
    }



}
