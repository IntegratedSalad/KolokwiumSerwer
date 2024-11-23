public enum MessageType {
    MSG_USER_LOGGED_IN((byte)1),
    MSG_USER_ANSWER_ANSWERED((byte)2),
    MSG_USER_QUIZ_ABANDONED((byte)3),
    MSG_USER_QUIZ_COMPLETED((byte)4),
    MSG_SERVER_SENDS_QUESTION((byte)5),
    MSG_HELLO((byte)6);

    private final byte byteType;
    private MessageType(byte byteType) {
        this.byteType = byteType; // identyfikator
    }

    public byte getByteType() {
        return byteType;
    }
}
