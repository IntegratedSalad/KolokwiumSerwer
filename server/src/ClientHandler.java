import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;

public class ClientHandler implements Runnable {

    private Socket sock;
    private int questionNumber = 0;
    private int currentLine = 0;
    private ConcurrentFileHandler concurrentFileHandler;
    private Question currentQuestion;

    private String clientName;

    private int score = 0;

    public ClientHandler(Socket socket, ReentrantLock rwLock) throws FileNotFoundException, SQLException {
        this.sock = socket;
        this.concurrentFileHandler = new ConcurrentFileHandler(rwLock);
    }

    @Override
    public void run() {
        try {

            PrintWriter socOut = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
            BufferedReader socIn = new BufferedReader(new InputStreamReader(sock.getInputStream()));

            // wait for name
            System.out.println("Waiting for name...");
            clientName = GetResponseFromClient(socIn); // TODO: Decode message
            System.out.println("Client " + clientName + " has been connected to " + sock.getRemoteSocketAddress());

            Message msgHello = new Message(MessageType.MSG_HELLO, null);
            msgHello.SetPayload("Połączono");
            msgHello.Send(socOut);
            System.out.println("Hello sent!");

            StringBuilder sb = new StringBuilder();
            while (true) {

                // 1. Send question
                final Question nextQuestion = GetNextQuestion();
                if (nextQuestion != null) {
                    this.currentQuestion = nextQuestion;
                    String payload = nextQuestion.getQuestion();
                    payload += " ANS:";
                    for (String answer : nextQuestion.getPossibleAnswers()) {
                        payload += answer + " ";
                    }
                    System.out.println("Question: " + payload);
                    Message msgNextQuestion = new Message(MessageType.MSG_SERVER_SENDS_QUESTION, payload);
                    System.out.println("Sending question...");
                    msgNextQuestion.Send(socOut);
                    System.out.println("Question send, waiting for an answer...");
                    String resp = GetResponseFromClient(socIn);
                    resp = resp.toLowerCase();

                    System.out.println("Received response (answer): " + resp);

                    if (resp.equalsIgnoreCase(this.currentQuestion.getRightAnswer())) {
                        this.score++;
                    }
                    this.concurrentFileHandler.writeLine(clientName + " " + this.questionNumber + ": " + resp);
                } else {
                    System.out.println("End of questions, terminating session...");
                    final String scoreString = this.score + "/" + this.questionNumber;
                    System.out.println("Score: " + scoreString);

                    Message msgQuizCompleted = new Message(MessageType.MSG_USER_QUIZ_COMPLETED, scoreString);
                    Thread.sleep(2000);
                    msgQuizCompleted.Send(socOut);
                    msgQuizCompleted.Send(socOut); // nie mam pojecia dlaczego trzeba dwa razy
                    return;
                }
            }
            // Client closes the socket... TODO: Server closes socket upon timer...

        } catch (IOException | InterruptedException e) {
            if (e instanceof SocketException) {
                System.out.println("Disconnecting, probably client had a timeout.");
            }
        } finally {
            System.out.println("Ended task for " + sock.getInetAddress().getHostName());
        }
    }

    private Question GetNextQuestion() throws IOException {

        String[] lines = concurrentFileHandler.readNLines(2, this.currentLine);
        String questionString = lines[0];
        if (questionString == null) return null;
        String answerString = lines[1];
        String correctAnswer = GetAnswerFromQuestionString(questionString);
        String question = GetQuestionFromQuestionString(questionString);

        String[] allPossibleAnswers = GetAlPossibleAnswers(answerString);

        for (String s : allPossibleAnswers) {
            System.out.println("Answer: " + s);
        }

        Question questionObj = new Question(question, correctAnswer, allPossibleAnswers);

        this.questionNumber++;
        this.currentLine += 2;
        return questionObj;
    }

    private String GetQuestionFromQuestionString(final String qs) {
        final int indexOfAns = qs.indexOf("ANS");
        return qs.substring(0, indexOfAns);
    }

    private String GetAnswerFromQuestionString(final String qs) {
        String answer = null;

        System.out.println("Question String: " + qs);

        final int indexOfAns = qs.indexOf("ANS");
        if (indexOfAns != -1) {
            System.out.println("Ans index = " + indexOfAns);
            answer = qs.substring(indexOfAns + 5);
            System.out.println("Correct answer: " + answer);

        } else {
            System.out.println("Couldn't find ANS!");
        }
        return answer;
    }

    private String[] GetAlPossibleAnswers(final String as) {
        return as.split(" ");
    }

    private String GetResponseFromClient(BufferedReader socIn) throws IOException, InterruptedException {
        String buff = null;
        while ((buff = socIn.readLine()) == null);  // wait if there's nothing
        return buff;
    }
}
