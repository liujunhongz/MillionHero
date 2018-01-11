import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 618 on 2018/1/8.
 *
 * @author lingfengsan
 */
public class Search implements Callable {
    private static final String USER_AGENT = "Mozilla/5.0";
    private final String question;
    private final String[] answers;

    Search(String question, String... answers) {
        this.question = question;
        this.answers = answers;
    }

    @Override
    public Long call() {
        try {
            return search(question, answers);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    private static long search(String question, String... answers) throws IOException {
        String baiduPath = "http://www.baidu.com/s?tn=ichuner&lm=-1&word=" + URLEncoder.encode(question, "gb2312") + "&rn=20";
        String googlePath = "https://www.google.com.hk/search?q=" + URLEncoder.encode(question, "gb2312");
        URL url = new URL(googlePath);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        //默认值我GET
        con.setRequestMethod("GET");
        //添加请求头
        con.setRequestProperty("Content-Encoding", "UTF-8");
        con.setRequestProperty("Accept-Language", "zh-CN");
        con.setRequestProperty("Accept-Encoding", "UTF-8");
        con.setRequestProperty("Content-Type", "text/html; charset=UTF-8");
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();
        if (responseCode != 200) {
            return 0;
        }
        InputStream in = con.getInputStream();
        ByteArrayOutputStream response = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int read;
        while ((read = in.read(buffer, 0, buffer.length)) > 0) {
            response.write(buffer, 0, read);
        }
        response.flush();
        in.close();
        String content = response.toString("UTF-8");
        int result = 0;
        int resultCount = 0;
        if (answers != null) {
            for (int i = 0; i < answers.length; i++) {
                String answer = answers[i];
                int answerCount = indexAnswerCount(content, answer);
                if (answerCount > resultCount) {
                    result = i;
                    resultCount = answerCount;
                }
            }
            System.out.println();
            System.out.println("推荐答案：" + answers[result]);
            System.out.println();
        }
        System.out.println("===============================");
        return 0;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        long cur = System.currentTimeMillis();

//        String question = "“锅包肉″是下列哪个地区的名菜";
//        String[] answers = {"淮扬", "川渝", "东北"};

        String question = "我们会把农历的几月几日叫作″龙抬头″?";
        String[] answers = {"二月二", "三月三", "四月四"};
        search(question, answers);
        long now = System.currentTimeMillis();
        System.out.println("共花费时间：" + (now - cur) / 1000.0 + "s");
    }

    private static int findAnswerCount(String content, String answer) {
        int count = 0;
        if (content.length() > 0) {
            //得到第一个字符串比如"香蕉、"
            Pattern pattern = Pattern.compile(answer);
            Matcher matcher = pattern.matcher(content);
            while (matcher.find()) {
                count++;
            }
        }
        System.out.println(answer + ",出现次数为:" + count);
        return count;
    }

    private static int indexAnswerCount(String content, String answer) {
        long cur = System.currentTimeMillis();
        int count = 0;
        if (content.length() > 0) {
            int index = content.indexOf(answer);
            int length = answer.length();
            while (index > 0) {
                count++;
                index = content.indexOf(answer, index + length);
            }
        }
        System.out.println(answer + ",出现次数为:" + count);
        long now = System.currentTimeMillis();
        System.out.println("索引花费时间：" + (now - cur) / 1000.0 + "s");
        return count;
    }
}
