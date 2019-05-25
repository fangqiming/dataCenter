package com.gougu.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件读写工具
 */
@Service
public class FileHelpService {

    /**
     * 创建输出流
     *
     * @param path 文件路径
     * @return
     */
    public OutputStreamWriter create(String path) {
        try {
            File file = new File(path);
            FileOutputStream fos = null;
            if (!file.exists()) {
                file.createNewFile();
                fos = new FileOutputStream(file);
            } else {
                fos = new FileOutputStream(file, true);
            }

            return new OutputStreamWriter(fos, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 关闭指定的输出流
     *
     * @param osw 需要关闭的输出流
     */
    public void close(OutputStreamWriter osw) {
        try {
            osw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 向指定的输出流中写入一行数据
     *
     * @param osw
     * @param line
     */
    public void write(OutputStreamWriter osw, String line) {
        try {
            osw.write(line);
            osw.write("\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将多个文件片段合并按行合并成同一个文件
     *
     * @param destination 合成后的目标文件 如 "D:/cash"
     * @param source      片段文件共同部分 如需要将D:/cash0 ... D:/cash15 合并则其传入值为"D:/cash"
     * @param start       第一个片段文件的标记值 如上则为 0
     * @param end         最后一个片段文件的标记值 如上则为 16
     * @param spec        可选如果该行中包含该指定的字符串，则忽略该行。为null则全部写入
     */
    public void mergeFile(String destination, String source, int start, int end, String spec) {
        OutputStreamWriter balance = create(destination);
        for (int i = start; i < end; i++) {
            String pathname = source;
            try (FileReader reader = new FileReader(pathname + i);
                 BufferedReader br = new BufferedReader(reader)
            ) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (StringUtils.isEmpty(spec) || !line.contains(spec)) {
                        write(balance, line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        close(balance);
    }

    /**
     * 将指定的List平均分割成n份，此函数放在此处不合适。
     *
     * @param source 需要切割的List
     * @param share  分割的份数
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> cutList(List<T> source, int share) {
        List<List<T>> result = new ArrayList<>();
        int remaider = source.size() % share;
        int number = source.size() / share;
        int offset = 0;
        for (int i = 0; i < share; i++) {
            List<T> value;
            if (remaider > 0) {
                value = source.subList(i * number + offset, (i + 1) * number + offset + 1);
                remaider--;
                offset++;
            } else {
                value = source.subList(i * number + offset, (i + 1) * number + offset);
            }
            result.add(value);
        }
        return result;
    }

    /**
     * 防止频率过高，被和谐，此函数放在此处也不合适
     *
     * @param mill 毫秒数
     */
    public static void sleep(Long mill) {
        try {
            Thread.sleep(mill);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
