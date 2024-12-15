package com.zhi.pin.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.logging.log4j.util.Base64Util;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class zxingTest {

    @Test
    public void test() throws Exception {
        //1.二维码中的信息
        String content = "https://www.baidu.com";
        //2.通过zxing生成二维码（保存到本地图片，支持以dataurl的形式体现）

        //创建QRCodeWriter对象
        QRCodeWriter writer = new QRCodeWriter();
        //基本配置
        /**
         * 1.content:二维码中的信息
         * 2.图片类型
         * 3.图片宽度
         * 4.图片高度
         */
        BitMatrix encode = writer.encode(content, BarcodeFormat.QR_CODE, 300, 300);
        //拿到流信息
        //创建ByteArrayOutputStream
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        //将二维码数据以byte数组的形式保存到ByteArrayOutputStream
        /**
         * 1.image对象
         * 2.图片类型
         * 3.输出流对象
         */
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(encode);
        ImageIO.write(bufferedImage, "png", os);
        //对byte数组进行base64处理
        String encode1 = Base64Util.encode(Arrays.toString(os.toByteArray()));
        System.out.println("data:image/png;base64," + encode1);
        //保存二维码到本地
//        MatrixToImageWriter.writeToPath(encode, "png", java.nio.file.Paths.get("D:/test.png"));
    }


    @Test
    public void test1() throws Exception {
        // 1. 二维码中的信息
        String content = "https://www.baidu.com";

        // 2. 通过zxing生成二维码（保存到本地图片，支持以dataurl的形式体现）
        QRCodeWriter writer = new QRCodeWriter();

        // 基本配置
        Map<EncodeHintType, ErrorCorrectionLevel> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

        BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 300, 300, hints);

        // 拿到流信息
        // 创建ByteArrayOutputStream
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        // 将二维码数据以byte数组的形式保存到ByteArrayOutputStream
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        ImageIO.write(bufferedImage, "png", os);

        // 对byte数组进行base64处理
        String base64Image = Base64.getEncoder().encodeToString(os.toByteArray());
        System.out.println("data:image/png;base64," + base64Image);
    }
}




















