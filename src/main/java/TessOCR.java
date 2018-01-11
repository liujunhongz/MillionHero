import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.LoadLibs;

import java.awt.*;
import java.io.*;

/**
 * Created by 618 on 2018/1/8.
 *
 * @author lingfengsan
 */
public class TessOCR {
    String getOCR(File imageFile) {
        ITesseract instance = new Tesseract();
        File tessDataFolder = LoadLibs.extractTessResources("tessdata");
        instance.setLanguage("chi_sim");
        //Set the tessdata path
        instance.setDatapath(tessDataFolder.getAbsolutePath());
        try {
            Rectangle rectangle = new Rectangle(100, 300, 900, 900);
            String ocr = instance.doOCR(imageFile, rectangle);
            return ocr.replace(" ", ".").replace(",", "");
        } catch (TesseractException | RuntimeException e) {
            System.err.println("提取文字失败：" + e.getMessage());
        }
        return null;
    }

    public static void main(String[] args) {
        File imageFile = new File("/Users/su/Desktop/20180111125453.png");
//        try {
//            Process exec = Runtime.getRuntime().exec("tesseract /Users/su/Desktop/20180111125453.png output -l chi_sim");
//            InputStream stream = exec.getInputStream();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
//            }
//            reader.close();
//            stream.close();
//            System.out.println("over");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        TessOCR tessOCR = new TessOCR();
        System.out.println(tessOCR.getOCR(imageFile));
    }
}
