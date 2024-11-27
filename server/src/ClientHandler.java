import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class ClientHandler implements Runnable {

    private Socket sock;
    private int questionNumber = 1;
    private int currentLine = 0;
//    private OutputStream outputStream;
//    private InputStream inputStream;
    private ConcurrentFileHandler concurrentFileHandler;
    private Question currentQuestion;

    public ClientHandler(Socket socket, ReentrantLock rwLock) throws FileNotFoundException {
        this.sock = socket;
        this.concurrentFileHandler = new ConcurrentFileHandler("src/bazaPytan.txt", rwLock);
    }

    @Override
    public void run() {
        try {

            PrintWriter socOut = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
            BufferedReader socIn = new BufferedReader(new InputStreamReader(sock.getInputStream()));

            Message msgHello = new Message(MessageType.MSG_HELLO, null);
            msgHello.SetPayload("dawaj kurwo weed");
            msgHello.Send(socOut);
            System.out.println("Hello sent!");

            Thread.sleep(2000);

            StringBuilder sb = new StringBuilder();
//            while (true) {

                // 1. Send question
            final Question nextQuestion = GetNextQuestion();
            if (nextQuestion != null) {
                this.currentQuestion = nextQuestion;
            } else {
                // end of questions?
            }
            String payload = nextQuestion.getQuestion();
            if (payload != null) {
                Message msgNextQuestion = new Message(MessageType.MSG_SERVER_SENDS_QUESTION, payload);
                System.out.println("Sending question...");
                msgNextQuestion.Send(socOut);
            }

            System.out.println("Question send, waiting for an answer...");
            String resp = GetResponseFromClient(socIn);

            System.out.println("Received response (answer): " + resp);

//                Message msgQuestion = new Message(MessageType.MSG_SERVER_SENDS_QUESTION, payload);
                // 2. Get Answer -> wait (block) on reading the socket...
                // 3. Write Answer to file
                // loop

//            }
            // while -> wait for response

            // Client closes the socket...
            // Listen to messages sent by the client

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("Ended task for " + sock.getInetAddress().getHostName());
        }
    }

    private Question GetNextQuestion() throws IOException {

        String[] lines = concurrentFileHandler.readNLines(2, currentLine);
        String questionString = lines[0];
        String answerString = lines[1];
        String correctAnswer = GetAnswerFromQuestionString(questionString);
        String question = GetQuestionFromQuestionString(questionString);

        if (correctAnswer == null) return null;

        String[] allPossibleAnswers = GetAlPossibleAnswers(answerString);

        for (String s : allPossibleAnswers) {
            System.out.println("Answer: " + s);
        }

        Question questionObj = new Question(question, correctAnswer, allPossibleAnswers);

        this.questionNumber++;
        currentLine += 2;
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
        Thread.sleep(2000);
        String buff = null;
        while ((buff = socIn.readLine()) == null);  // wait if there's nothing
        return buff;
    }
}
