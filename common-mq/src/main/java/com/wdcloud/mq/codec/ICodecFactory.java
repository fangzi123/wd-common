package com.wdcloud.mq.codec;

/**
 * 编码和解码工厂
 *
 * @author Andy
 * @date 17/4/17
 */
public interface ICodecFactory {

    byte[] serialize(Object obj);

    Object deSerialize(byte[] in);

}