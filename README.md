## 介绍

------

##### mirai-drp-spring-boot-starter基于mirai的为java开发人员量身打造的快速开发业务相关机器人的扩展框架,旨在加快Java开发QQ机器人的效率,适配Java技术人员常用的springboot框架,快速搭建!

##### DRP: dispatch 指令分发 receive 指令接收 process 指令处理  对于消息指令 三个核心流程

#### 基于注解编程!操作非常简单!目标是用最简单的使用方式搭建能够处理各种复杂指令的QQ机器人!

##### 相关项目:

##### mirai:https://github.com/mamoe/mirai

##### 对应版本:

##### mirai:2.7.0

##### 刚做完第一个简单的版本,文档还未来得及更新完善,只有快速开始.

##### "只做扩展,不做修改"你仍可以使用mirai的其他功能

##### 如有其他更复杂的需求,请参考mirai文档:https://docs.mirai.mamoe.net/

------

## 声明:

------

### 一切开发旨在学习，请勿用于非法用途

- ##### 目前版本:1.0  最初版本,作者也是第一次写这样的东西

- ##### 纯个人使用中突发奇想制作,可能存在许多bug,欢迎反馈,后续会继续更新

- ##### 交流QQ群:937621780 欢迎交流提出意见

------

## 许可证:

Copyright (C) <2021-2022>  <JinHuang>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.
    
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.
    
    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

------

本项目 采用 AGPLv3 协议开源。如果您有更好的想法一起更新,欢迎提交PR

------

## 特性

------

- 两个注解就可直接接收到指定需要处理的消息内容
- 简易的配置,不需要过多复杂操作
- 可直接引入springboot项目 即开即用 
- 提供了一些可扩展开发的内容

------

## 快速开始

------

推荐环境:

JDK 8 +

SpringBoot版本:2.5.0

请确认已安装了maven并配置了环境变量

#### 1.克隆本仓库

```java
git clone https://github.com/JinHuang0923/mirai-drp.git
```

#### 2.安装jar包到maven本地仓库

```html
找到其中的mirai-drp-spring-boot-starter-1.0.jar包
执行maven指令(CMD 或 idea控制台都可)
mvn install:install-file 
    -Dfile=E:\mygit\mirai\mirai-drp-spring-boot-starter-1.0.jar #jar包位置(请使用你本机的路径)
    -DgroupId=xyz.sinsong  # 配置生成 jar 包对应的 groupId
    -DartifactId=mirai-drp-spring-boot-starter # 配置生成 jar 包对应的 artifactId
    -Dversion=1.0 #配置版本号
    -Dpackaging=jar # 配置文件的打包方式
```

#### 3.新建一个Springboot工程 引入springboot父工程与本jar包依赖

![image-20211101200338923](https://gitee.com/jinhuang0923/simbotQQ-drp/raw/main/docImg/image-20211101200338923.png)

![image-20211101200559556](https://gitee.com/JinHuang0923/simbotQQ-drp/raw/main/docImg/image-20211101200559556.png)

pom文件,引入依赖,修改父工程版本为2.5.0最好

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.5.0</version> <!--父工程修改为2.5.0最好-->
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.example</groupId>
    <artifactId>testspringboot</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>testspringboot</name>
    <description>testspringboot</description>
    <properties>
        <java.version>1.8</java.version>
    </properties>
    <dependencies>
        
    <!--引入依赖-->
         <dependency>
            <groupId>xyz.sinsong</groupId>
            <artifactId>mirai-drp-spring-boot-starter</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>

```

#### 4.创建一个接收器包存放接收器类(包名随便你自己起)

#### 5.创建一个最简单的接收器类(打上@Receiver注解即可)

```java
@Receiver //表明这是一个接收器
public class MyTestReceiver {


}
```

#### 6.编写接收处理方法(使用@Receive 注解)

```java
@Receiver
public class MyTestReceiver {

    @Autowired
    private ServiceTest serviceTest;


    @Receive(value = "测试",isPrivate = true) //这是一个处理私聊指令的方法
    public Message testContent(CommandRequest request){
        System.out.println(serviceTest.test());
        return MessageBuilderUtis.buildMessage("私聊测试成功");
    }
    @Receive("测试") //这是一个处理群聊指令的方法
    public Message testContent2(CommandRequest request){
        System.out.println(request);
        return MessageBuilderUtis.buildMessage("群聊测试成功");
    }


}
```

#### 7.添加配置文件mirai-drp.properties在 resource目录下

展示一些可选配置 心跳策略与工作目录有默认值,qq账号与密码以及扫描包路径必填

```yaml
#心跳策略
mirai-drp.heartbeatStrategy=STAT_HB
#工作目录
mirai-drp.workspace=/miraiDrpTemp
#机器人qq账号
mirai-drp.bot.qq=xxxxxxx
#机器人qq密码
mirai-drp.bot.password=xxxxxxxx
#接收器的全路径包名
mirai-drp.scanPackage=xyz.sinsong.bot.receiver
```

#### 7.创建启动类 普通的springboot注解就可(可忽略数据源配置)

##### //目前还没有做开启机器人的注解,所以直接启动就自动开启登录机器人了

```java
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class MyTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyTestApplication.class,args);
    }
}

```

#### 8.main方法运行开始测试

![image-20211101202556866](https://gitee.com/JinHuang0923/simbotQQ-drp/raw/main/docImg/image-20211101202556866.png)

##### 看到此界面就是运行成功,可对他发送具体的私聊或群聊消息测试指令 测试是否成功

如果运行报错 qq登录连接超时,等待或重试几次就行

如果需要验证 按控制台提示操作即可

#### 至此 一个简单的demo测试完毕,接下来讲下基础操作,一定要看!

------

## 正式开始

------

### 核心注解(其实就两个!):

------

##### @Receiver 通过springIOC容器自动加载接收器类 标注为处理指令的类 可在其中写处理指令的方法

使用方式:直接标注在类上就好(已经替代了@Component),无需再添加@Component注解

##### @Receive 标注处理指令的方法 有两个属性可指定

##### value:指令名

##### isPrivate:是否是处理私聊消息的 默认false

以测试的类为参考:

```java
@Receiver //标注为接收器
public class TestReceiver {

    @Receive("测试")//指令 发送群聊qq消息:测试  就会执行下面的方法,并回复"分发指令测试成功"
    public Message testContent(CommandRequest request){
        System.out.println(request);
        return MessageBuilderUtis.buildMessage("分发指令测试成功");
//MessageBuilderUtis 是一个消息构建工具类 你也可以选择使用mirai提供的实现发送图片等功能
    }
    @Receive("参数测试")// 示例:发送群聊qq消息:参数测试 1 2 则会回复:参数测试12
    public Message testContent2(CommandRequest request){
        System.out.println(request);
        return MessageBuilderUtis.buildMessage("参数测试:" + request.getParameter(1) + request.getParameter(2));
    }
    @Receive(value = "混搭私聊测试",isPrivate = true)
    //这是标注接收私聊指令的 私聊机器人发送混搭私聊测试 则会收到回复:"混搭私聊测试成功!"
    public Message testContent3(CommandRequest request){
        System.out.println(request);
        return MessageBuilderUtis.buildMessage("混搭私聊测试成功!");
    }


}
```

使用方式:标注在有@Receiver注解的类的方法上即可 可指定处理什么样的指令

##### 注意:方法的返回值必须为Message(可以返回null)且入参中 必须有一个 CommandRequest request对象,我们马上会讲这个CommandRequest 类;

------

### 指令请求对象CommandRequest 

------

每个标注了@Receive 注解的方法 都可通过入参 获取到一个指令请求对象

里面包含了指令的参数,发送人的信息,群的信息等,还有mirai原生的联系人对象(可主动发送消息)可通过getXXX()获取

例:创建这样一个指令处理方法

```java
 @Receive("签到")
    public Message testContent4(CommandRequest request){
        System.out.println(request);
        String name = request.getParameter(1);
        return MessageBuiderUtils.buildMessage("签到成功!" + name +"!");
    }
```

在有机器人的群里发送qq消息: 签到 罪歌歌

机器人则会回复消息:签到成功!罪歌歌!

```java
 request.getParameter(下标); 可获取指令后面空格隔开的不同参数 例 签到 罪歌歌 早上
 @Receive("签到")
    public Message testContent4(CommandRequest request){
        System.out.println(request.getParameter(1));
        System.out.println(request.getParameter(2));
        return MessageBuiderUtils.buildMessage("");
    }
打印结果为:罪歌歌 早上
```

##### 这只是一个简单的应用,你可以自行探索,实现更多其他复杂的业务处理

------

### 消息返回

------

本组件建议,返回消息,统一使用MessageBuiderUtils 工具类即可.

通过buildMessage()方法可以构建一个返回需要的MessageContent对象(仅仅文字的),支持char序列的各种类(String StringBuffer等等)

如果你有发送图片等复杂需求,可选择使用mirai提供的消息构建类构建Message,具体请见mirai的文档 在复杂需求场景,使用更加灵活.

------

### 其他说明

------

##### 目前,此版本仅支持单Q登录,后续会有支持多Q登录的更新

------

##### 至此,第一版最简短的入门讲解,就到这里,刚开始写,后续文档更新会给大家详细说明核心特性是如何实现的以及如何实现更多自定义的功能

##### 如果你有更好的想法,欢迎提交pr,或与我共同开发

#### 交流QQ群:937621780

#### 欢迎大佬们一起开发!交流



















