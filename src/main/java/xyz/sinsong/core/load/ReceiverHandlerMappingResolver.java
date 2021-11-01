package xyz.sinsong.core.load;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;
import xyz.sinsong.core.anotation.Receiver;

import java.io.IOException;

/**
 * @author Jin Huang
 * @date 2021/10/30 19:23
 * 扫描包 将带有 @Receiver 注解的类实例化放入工厂
 * 建立 k:指令  v:执行指令的方法 的map 工厂中添加的
 * 带有@Receive 注解的 表明是执行指令的方法 也是工厂中处理的
 */
@Slf4j
public class ReceiverHandlerMappingResolver {
    //要扫描的包路径
    private static final String RESOURCE_PATTERN = "/**/*.class";

    //待扫描的包名 数组
    private String[] packagesToScan;



    //用于解析资源文件的策略接口
    //可扫描后返回指定扫描路径下的所有资源对象
    //也叫资源加载器ResourceLoader
    private ResourcePatternResolver resourcePatternResolver;


    //类型过滤器 通过类上的注解过滤出需要的类 匹配match()方法 需要一个元数据读取器对象 与元数据读取器工厂
    //创建的过滤器是注解类型过滤器(匹配打了Receiver注解的类)
    private static final TypeFilter[] ENTITY_TYPE_FILTERS = new TypeFilter[] {
            new AnnotationTypeFilter(Receiver.class, false)};



    //初始化 构造对象时就加载资源加载器
    public ReceiverHandlerMappingResolver(){
        //创建一个资源扫描器(通过资源路径扫描的)
        //也叫资源加载器ResourceLoader
        //是一个Ant模式通配符的Resource查找器，可以用来查找类路径下或者文件系统中的资源 加载进来
        this.resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(new PathMatchingResourcePatternResolver());
    }


    /**
     * 主要扫描包的方法 返回他本身
     * @param packagesToScan
     * @return ServletHandlerMappingResolver
     */
    public ReceiverHandlerMappingResolver scanPackages(String[] packagesToScan){
//        log.info("扫描所有接收器资源...");
        try {
            //遍历所有的待扫描的包名 路径
            for (String pkg : packagesToScan) {

                //ClassUtils.convertClassNameToResourcePath(pkg) : 把包名的 . 换成 /
                //  示例 : classpath*: xyz/sinsong/**/*.class
                //classpath*:xyz/sinsong/bot/receiver/**/*.class
                String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                        ClassUtils.convertClassNameToResourcePath(pkg) + RESOURCE_PATTERN;

                //Resource对象是资源文件的统一接口，通过applicationContext.getResource方法可以将某个资源文件转化为Resource对象使用
                //存储扫描出来的 .class文件 转换成的 Resource对象 数组
                Resource[] resources;

                //调用资源加载器的方法 通过包路径 扫描出包下的所有 .class文件 加载为 resource对象
                resources = this.resourcePatternResolver.getResources(pattern);

                //创建一个 获取元数据读取器的 工厂 元数据读取器(MetadataReader)的实现并未都public暴露出来，所以我们若想得到它的实例，就只能通过此工厂。
                //指定使用的资源加载器为 PathMatchingResourcePatternResolver
                MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(this.resourcePatternResolver);

                //遍历这个包下的所有加载进来的资源对象
                for (Resource resource : resources) {

                    //如果资源对象可读
                    if (resource.isReadable()) {

                        //工厂通过元数据读取器 放入资源 得到 元数据读取器(存储读取的各种元数据:类元数据 方法元数据 注解元数据等)
                        MetadataReader reader = readerFactory.getMetadataReader(resource);

                        //通过读取器中的类元数据 获取类名
                        String className = reader.getClassMetadata().getClassName();

                        //通过元数据 与 过滤器 匹配是否是需要的类型
                        if (matchesFilter(reader, readerFactory)) {
                            //是需要返回的类型 加入 该类型 mapping 工厂 MappingFactory
                            //资源加载器获取类加载器 通过类名 加载这个符合类型的类 把这个类 加入这个工厂
                            ReceiverHandlerMappingFactory.addClassMapping(this.resourcePatternResolver.getClassLoader().loadClass(className));
                        }

                    }

                }


            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return this;
    }

    public String[] getPackagesToScan() {
        return packagesToScan;
    }

    /**
     * 调用加载的方法入口
     * @param packagesToScan 待扫描的包名数组
     */
    public void setPackagesToScan(String[] packagesToScan) {
        this.packagesToScan = packagesToScan;
        this.scanPackages(packagesToScan);
    }


    /**
     *
     * 匹配过滤器 通过 元数据(类元数据 方法元数据 注解元数据都有)
     * 遍历类型过滤器 是否是匹配的类型 如果是 返回true
     * @param reader
     * @param readerFactory
     * @return
     * @throws IOException
     */
    private boolean matchesFilter(MetadataReader reader, MetadataReaderFactory readerFactory) throws IOException {
        //遍历所有类型过滤器(这里只有个注解过滤器 )
        for (TypeFilter filter : ENTITY_TYPE_FILTERS) {

            //调用过滤器的匹配方法 通过元数据比较 是否是匹配类型 是的话返回true
            if (filter.match(reader, readerFactory)) {
                return true;
            }

        }
        return false;
    }

}
