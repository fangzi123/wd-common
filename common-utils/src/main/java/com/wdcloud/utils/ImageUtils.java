package com.wdcloud.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.*;
import java.util.Hashtable;
import java.util.Iterator;

public class ImageUtils {
    /**
     * zxing创建QR bytes
     *
     * @return
     * @throws WriterException
     */
    public static byte[] createQRCodeImg(String body, int width, int height) {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.MARGIN, 0);
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(new String(body.getBytes("GBK")), BarcodeFormat.QR_CODE, width,
                    height, hints);
        } catch (WriterException | UnsupportedEncodingException e) {
            throw new RuntimeException("生成二维码出错" + e);
        }
        BufferedImage qrCodeImg = bitMatrixToBufferedImage(bitMatrix);
        byte[] imgBytes;
        try {
            imgBytes = bufferedImageConverBytes(qrCodeImg);
        } catch (IOException e) {
            throw new RuntimeException("生成二维码出错" + e);
        }
        return imgBytes;
    }

    /**
     * bitMatrix 转换为 BufferedImage
     *
     * @param bitMatrix
     * @return
     */
    private static BufferedImage bitMatrixToBufferedImage(BitMatrix bitMatrix) {
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (bitMatrix.get(x, y)) {
                    image.setRGB(x, y, 0xff000000);
                } else {
                    image.setRGB(x, y, 0xFFFFFFFF);
                }
            }
        }
        return image;
    }

    /**
     * bufferedImage 转换为 bytes
     *
     * @param bufferedImage
     * @return
     * @throws IOException
     */
    public static byte[] bufferedImageConverBytes(BufferedImage bufferedImage) throws IOException {
        if (bufferedImage == null) {
            throw new IllegalArgumentException("bufferedImageConverBytes bufferedImage can't be empty !");
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();// 新建流。
        ImageIO.write(bufferedImage, "bmp", os);// 利用ImageIO类提供的write方法，将bi以bmp图片的数据模式写入流。
        return os.toByteArray();// 从流中获取数据数组。
    }

    /**
     *
     * src：图片位置，dest：图片保存位置
     *
     */
    /**
     * 图片裁剪通用接口
     * @param src 图片位置
     * @param dest 图片保存位置(若要覆盖原图片，只需src == dest即可)
     * @param x
     * @param y (x,y)新图片左上角坐标
     * @param width 新图片宽度
     * @param height 新图片高度
     * @throws IOException
     */
    public static void cutImage(String src,String dest,int x,int y,int width,int height) throws IOException{

        File srcImg =new File(src);
        //获取后缀名
        String suffix = srcImg.getName().substring(srcImg.getName().lastIndexOf(".") + 1);
        //根据不同的后缀获取图片读取器
        Iterator iterator = ImageIO.getImageReadersBySuffix(suffix);
        ImageReader reader = (ImageReader)iterator.next();

        InputStream in=new FileInputStream(src);
        ImageInputStream iis = ImageIO.createImageInputStream(in);

        reader.setInput(iis, true);
        ImageReadParam param = reader.getDefaultReadParam();

        //设置裁剪位置
        Rectangle rect = new Rectangle(x, y, width,height);
        param.setSourceRegion(rect);

        //保存裁剪后的图片
        BufferedImage bi = reader.read(0,param);
        boolean b = ImageIO.write(bi, suffix, new File(dest));
    }


    /**
     * 将图片处理成 RGB565 格式
     * @param filePath 源文件
     * @param saveFileName 处理后的目标文件
     * 若要处理后覆盖原图，让两个参数相等即可
     */
    public static void image2RGB565Bmp(String filePath, String saveFileName) {
        try {
            BufferedImage sourceImg = ImageIO.read(new File(filePath));
            int h = sourceImg.getHeight(), w = sourceImg.getWidth();
            int[] pixel = new int[w * h];
            PixelGrabber pixelGrabber = new PixelGrabber(sourceImg, 0, 0, w, h, pixel, 0, w);
            pixelGrabber.grabPixels();

            MemoryImageSource m = new MemoryImageSource(w, h, pixel, 0, w);
            Image image = Toolkit.getDefaultToolkit().createImage(m);
            BufferedImage buff = new BufferedImage(w, h, BufferedImage.TYPE_USHORT_565_RGB);
            buff.createGraphics().drawImage(image, 0, 0 ,null);
            ImageIO.write(buff, "bmp", new File(saveFileName));
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }


}
