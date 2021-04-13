package com.ridko.module.reader.reflections;

import com.jeesite.common.utils.SpringUtils;
import com.ridko.common.anno.Producer;
import com.ridko.common.anno.Reader;
import com.ridko.common.anno.SqProcess;
import com.ridko.common.domain.BaseDevMsg;
import com.ridko.module.process.ReaderProcess;
import com.ridko.module.producer.factory.ProducerFactory;
import com.ridko.module.reader.AbstractReader;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 读写器bean对象管理容器
 * 其中包括ProducerFactory, readerProcess, readerClass
 * @author sijia3
 * @date 2021/1/9 15:05
 */
@Slf4j
@Component
public class ReaderBeanContext {

    private static Map<String, ProducerFactory> producerFactoryMap = new HashMap<>();      // 生产者管理容器

    private static Map<String, ReaderProcess> readerProcessMap = new HashMap<>();

    private static Map<String, Class<AbstractReader>> readerClassMap = new HashMap<>();

    static  {
        initProducerFactoryBeanMap();
        initSqProcessBeanMap();
//        initReaderMap();
        initReaderClassMap();
    }

    /**
     * 初始化ProducerFactoryBeanMap
     * step1: 筛选出spring管理的ProducerFactory
     * step2: 根据ProducerFactory上的注解value做key，producerFactory为value，维护在producerFactoryMap上
     */
    private static void initProducerFactoryBeanMap() {
        ApplicationContext ctx = SpringUtils.getApplicationContext();
        Map<String, ProducerFactory> factoryMap = ctx.getBeansOfType(ProducerFactory.class);
        Set<Map.Entry<String, ProducerFactory>> entries = factoryMap.entrySet();
        for (Map.Entry<String, ProducerFactory> entry : entries) {
            ProducerFactory producerFactory = entry.getValue();
            Class<? extends ProducerFactory> factoryClass = producerFactory.getClass();
            Producer processAnno = factoryClass.getAnnotation(Producer.class);
            if (Objects.nonNull(processAnno)) {
                String key = processAnno.value().getValue();
                producerFactoryMap.put(key, producerFactory);
            }
        }
    }

    /**
     * 初始化读写器处理器ProcessBeanMap
     * step1: 获取spring管理的ReaderProcess
     * step2: 筛选出有SqProcess注解管理的类，放到readerProcessMap中管理
     */
    private static void initSqProcessBeanMap() {
        ApplicationContext ctx = SpringUtils.getApplicationContext();
        Map<String, ReaderProcess> processMap = ctx.getBeansOfType(ReaderProcess.class);
        Set<Map.Entry<String, ReaderProcess>> entries = processMap.entrySet();
        for (Map.Entry<String, ReaderProcess> entry : entries) {
            ReaderProcess readerProcess = entry.getValue();
            Class<? extends ReaderProcess> readerProcessClass = readerProcess.getClass();
            SqProcess annotation = readerProcessClass.getAnnotation(SqProcess.class);
            if (Objects.nonNull(annotation)) {
                String key = annotation.value().getValue();
                readerProcessMap.put(key, readerProcess);
            }
        }

    }

    @Deprecated
    private static void initReaderMap() {
        ApplicationContext ctx = SpringUtils.getApplicationContext();
        Map<String, AbstractReader> readerMap1 = ctx.getBeansOfType(AbstractReader.class);
        Set<Map.Entry<String, AbstractReader>> entries = readerMap1.entrySet();
        for (Map.Entry<String, AbstractReader> entry : entries) {
            AbstractReader value = entry.getValue();
            Class<? extends AbstractReader> readerClass = value.getClass();
            Reader annotation = readerClass.getAnnotation(Reader.class);
            if (Objects.nonNull(annotation)) {
                String brand = annotation.brand().getValue();
                String functionType = annotation.functionType().getValue();
                String key = convert2Key(brand, functionType);
                readerClassMap.put(key, (Class<AbstractReader>) readerClass);
            }
        }
    }

    /**
     * 初始化Reader读写器管理对象Class
     * key: brand+functionType 组合
     * value: AbstractReader.class
     */
    private static void initReaderClassMap() {
        Reflections reflections = new Reflections("com.ridko.module");
        Set<Class<? extends AbstractReader>> subTypesOf = reflections.getSubTypesOf(AbstractReader.class);
        for (Class<? extends AbstractReader> aClass : subTypesOf) {
            Reader annotation = aClass.getAnnotation(Reader.class);
            if (Objects.nonNull(annotation)) {
                String brand = annotation.brand().getValue();
                String functionType = annotation.functionType().getValue();
                String key = convert2Key(brand, functionType);
                readerClassMap.put(key, (Class<AbstractReader>) aClass);
            }
        }

    }

    /**
     * 获取ProducerFacoty
     * @param processType
     * @return
     */
    public static ProducerFactory getProducerFactory(String processType) {
        return producerFactoryMap.get(processType);
    }

    /**
     * 获取ReaderProcessMap中维护的系统队列Process
     * @param processType
     * @return
     */
    public static ReaderProcess getReaderSqProcess(String processType) {
        return readerProcessMap.get(processType);
    }

    /**
     * 获取Reader读写器连接对象
     * 根据读写器实体的（品牌）brand+functionType（功能）获取对应的Reader对象
     * @param devMsg
     * @return
     */
    public static AbstractReader newReader(BaseDevMsg devMsg) {
        String brand = devMsg.getBrand();
        String functionType = devMsg.getReaderType();
        String key = convert2Key(brand, functionType);
        Class<AbstractReader> aClass = readerClassMap.get(key);
        AbstractReader bean = SpringUtils.getBean(aClass);
        bean.setDevMsg(devMsg);
        return bean;
    }


    /**
     * 转换Key规则，根据brand和functionType转换
     * @param brand
     * @param functionType
     * @return
     */
    private static String convert2Key(String brand, String functionType) {
        return brand + functionType;
    }


    public static void main(String[] args) {
        return;
    }

}
