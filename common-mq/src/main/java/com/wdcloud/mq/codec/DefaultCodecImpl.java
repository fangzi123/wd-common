package com.wdcloud.mq.codec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * 默认编码和解码
 *
 * @author Andy
 * @date 17/4/17
 */
public class DefaultCodecImpl implements ICodecFactory {

    private final Logger logger = LoggerFactory.getLogger(DefaultCodecImpl.class);

    @Override
    public byte[] serialize(Object obj) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        logger.info("[DefaultCodec] serialize object={}", obj.getClass());
        try {
            // 序列化
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            this.logger.error("[DefaultCodec] Failed to serialize.", e);
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                this.logger.error("[DefaultCodec] Failed to close stream.", e);
            }
        }
        return baos != null ? baos.toByteArray() : null;
    }

    @Override
    public Object deSerialize(byte[] in) {
        if (in == null) {
            return null;
        }

        logger.info("[DefaultCodec] deSerialize run");

        ByteArrayInputStream byteArrayInputStream = null;
        try {
            // 反序列化
            byteArrayInputStream = new ByteArrayInputStream(in);
            ObjectInputStream inputStream = new ObjectInputStream(byteArrayInputStream);
            return inputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            this.logger.error("[DefaultCodec] Failed to deSerialize.", e);

        } finally {
            try {
                if (byteArrayInputStream != null) {
                    byteArrayInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                this.logger.error("[DefaultCodec] Failed to close stream.", e);
            }
        }
        return null;
    }

}
