public enum MessageType {
    MSG_USER_LOGGED_IN(1),
    MSG_USER_ANSWER_ANSWERED(2),
    MSG_USER_QUIZ_ABANDONED(3),
    MSG_USER_QUIZ_COMPLETED(4),
    MSG_SERVER_SENDS_QUESTION(5),
    MSG_HELLO(6);

    private final int id;
    private MessageType(int id) {
        this.id = id; // identyfikator
    }

    public int getTypeId() {
        return id;
    }
}
