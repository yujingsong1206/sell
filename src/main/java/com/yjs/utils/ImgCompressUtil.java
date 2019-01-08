package com.yjs.utils;

import java.io.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import com.sun.image.codec.jpeg.*;
import lombok.extern.slf4j.Slf4j;

/**
 * 图片压缩
 */
@Slf4j
public class ImgCompressUtil {

    private Image img;
    private int width;
    private int height;
    private String targetPath;

    public static void compress(String sourcePath, Integer width , Integer height, String targetPath) throws IOException {
        log.info("原文件 sourcePath:{}", sourcePath);
        ImgCompressUtil imgCom = new ImgCompressUtil(sourcePath, targetPath);
        imgCom.resizeFix(width, height);
    }

    /**
     * 构造函数
     */
    private ImgCompressUtil(String fileName, String targetPath) throws IOException {
        this.targetPath = targetPath;
        File file = new File(fileName);// 读入文件
        img = ImageIO.read(file);      // 构造Image对象
        width = img.getWidth(null);    // 得到源图宽
        height = img.getHeight(null);  // 得到源图长
    }
    /**
     * 按照宽度还是高度进行压缩
     * @param w int 最大宽度
     * @param h int 最大高度
     */
    private void resizeFix(int w, int h) throws IOException {
        if (width / height > w / h) {
            resizeByWidth(w);
        } else {
            resizeByHeight(h);
        }
    }
    /**
     * 以宽度为基准，等比例放缩图片
     * @param w int 新宽度
     */
    private void resizeByWidth(int w) throws IOException {
        int h = (int) (height * w / width);
        resize(w, h);
    }
    /**
     * 以高度为基准，等比例缩放图片
     * @param h int 新高度
     */
    private void resizeByHeight(int h) throws IOException {
        int w = (int) (width * h / height);
        resize(w, h);
    }
    /**
     * 强制压缩/放大图片到固定的大小
     * @param w int 新宽度
     * @param h int 新高度
     */
    private void resize(int w, int h) throws IOException {
        BufferedImage image = new BufferedImage(w, h,BufferedImage.TYPE_INT_RGB );
        image.getGraphics().drawImage(img, 0, 0, w, h, null); // 绘制缩小后的图
        File destFile = new File(targetPath);
        FileOutputStream out = new FileOutputStream(destFile); // 输出到文件流
        // 可以正常实现bmp、png、gif转jpg
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
        encoder.encode(image); // JPEG编码
        out.close();
    }

}
