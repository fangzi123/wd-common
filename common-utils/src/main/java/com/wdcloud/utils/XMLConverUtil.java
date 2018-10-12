package com.wdcloud.utils;

import com.sun.xml.bind.marshaller.CharacterEscapeHandler;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * XML 数据接收对象转换工具类
 */
public class XMLConverUtil {

    private static final ThreadLocal<Map<Class<?>, Marshaller>> mMapLocal = new ThreadLocal<Map<Class<?>, Marshaller>>() {
        @Override
        protected Map<Class<?>, Marshaller> initialValue() {
            return new HashMap<>();
        }
    };

    private static final ThreadLocal<Map<Class<?>, Unmarshaller>> uMapLocal = new ThreadLocal<Map<Class<?>, Unmarshaller>>() {
        @Override
        protected Map<Class<?>, Unmarshaller> initialValue() {
            return new HashMap<>();
        }
    };

    /**
     * XML to Object
     *
     * @param <T>
     * @param clazz
     * @param xml 内容utf-8编码
     * @return
     */
    public static <T> T convertToObject(Class<T> clazz, String xml) {
        return convertToObject(clazz, new StringReader(xml));
    }

    /**
     * XML to Object
     *
     * @param <T>
     * @param clazz
     * @param inputStream　内容utf-8编码
     * @return
     */
    public static <T> T convertToObject(Class<T> clazz, InputStream inputStream) {
        return convertToObject(clazz, new InputStreamReader(inputStream));
    }

    /**
     * XML to Object
     *
     * @param <T>
     * @param clazz
     * @param reader　内容utf-8编码
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T convertToObject(Class<T> clazz, Reader reader) {
        try {
            XMLStreamReader xmlStreamReader = preventXxeReader(reader);
            Map<Class<?>, Unmarshaller> uMap = uMapLocal.get();
            if (!uMap.containsKey(clazz)) {
                JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                uMap.put(clazz, unmarshaller);
            }
            return (T) uMap.get(clazz).unmarshal(xmlStreamReader);
        } catch (JAXBException | XMLStreamException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Prevent XXE attack
     *
     * @param reader
     * @return
     * @throws XMLStreamException
     */
    private static XMLStreamReader preventXxeReader(Reader reader) throws XMLStreamException {
        XMLInputFactory xif = XMLInputFactory.newFactory();
        xif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        xif.setProperty(XMLInputFactory.SUPPORT_DTD, true);
        return xif.createXMLStreamReader(reader);
    }

    /**
     * Object to XML
     *
     * @param object
     * @return
     */
    public static String convertToXML(Object object) {
        try {
            Map<Class<?>, Marshaller> mMap = mMapLocal.get();
            if (!mMap.containsKey(object.getClass())) {
                JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());
                Marshaller marshaller = jaxbContext.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                marshaller.setProperty(CharacterEscapeHandler.class.getName(), new CharacterEscapeHandler() {
                    public void escape(char[] ac, int i, int j, boolean flag, Writer writer) throws IOException {
                        writer.write(ac, i, j);
                    }
                });
                mMap.put(object.getClass(), marshaller);
            }
            StringWriter stringWriter = new StringWriter();
            mMap.get(object.getClass()).marshal(object, stringWriter);
            return stringWriter.getBuffer().toString();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }
}
