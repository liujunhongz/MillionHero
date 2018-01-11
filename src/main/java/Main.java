import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Created by 618 on 2018/1/8.
 *
 * @author lingfengsan
 */
public class Main {
    private static final int NUM_OF_ANSWERS = 3;
    private static final String QUESTION_FLAG = "?";

    public static void main(String[] args) throws IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            String str = bf.readLine();
            System.out.println("开始执行");
            try {
                if (str.length() == 0) {
                    run();
                }
            } catch (Exception e) {
                System.out.println("error");
            }

        }
    }

    private static void run() throws InterruptedException {
//       记录开始时间
        long startTime;
//       记录结束时间
        long endTime;
        startTime = System.currentTimeMillis();
        //获取图片
        File image = new Phone().getImage();
        System.out.println("获取图片成功" + image.getAbsolutePath());
        //图像识别
        long beginOfDectect = System.currentTimeMillis();
        String questionAndAnswers = new TessOCR().getOCR(image);
        System.out.println("识别成功：" + (System.currentTimeMillis() - beginOfDectect) / 1000.0 + "s");
        if (!questionAndAnswers.contains(QUESTION_FLAG)) {
            return;
        }
        //获取问题和答案
        Information information = new Information(questionAndAnswers);
        String question = information.getQuestion();
        String[] answers = information.getAns();
        System.out.println("问题:" + question);
        //搜索
        new Search(question, answers).call();
        endTime = System.currentTimeMillis();
        System.out.println("共花费时间：" + (endTime - startTime) / 1000.0 + "s");
    }
}
