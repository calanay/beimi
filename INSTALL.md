# 编译调试部署说明

### 1. 服务器编译

#### 安装调试的前提要求

必须安装了 maven，和 MySQL。

#### 修改数据库连接地址


修改 src/main/resources/application.properties 文件下面三个地方，改成自己的MySQL服务器的ip地址和帐号密码

spring.datasource.url=jdbc:mysql://localhost:3306/beimi?useUnicode=true&characterEncoding=UTF-8
spring.datasource.username=root
spring.datasource.password=


#### 用 maven 安装两个依赖包

这两个包已经在项目中，不需要下载，执行下面命令即可。

mvn install:install-file -Dfile=src/main/resources/WEB-INF/lib/jave-1.0.2.jar -DgroupId=lt.jave -DartifactId=jave -Dversion=1.0.2 -Dpackaging=jar

mvn install:install-file -Dfile=src/main/resources/WEB-INF/lib/ip2region-1.2.4.jar -DgroupId=org.lionsoul.ip2region -DartifactId=ip2region -Dversion=1.2.4 -Dpackaging=jar

#### 编译代码

mvn compile

#### 运行服务器端


mvn spring-boot:start

可以如下面这样，在运行时添加一些环境变量

mvn spring-boot:start -Drun.jvmArguments='-Dserver.port=8080'

#### 编译打包服务器端

mvn package

打包完成后会在项目根目录生成一个 target 目录，里面的 beimi-0.7.0.war 文件是打包完成的文件。

#### 运行编译后war

java -Xms1240m -Xmx1240m -Xmn450m -XX:PermSize=512M -XX:MaxPermSize=512m -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:+UseTLAB -XX:NewSize=128m -XX:MaxTenuringThreshold=0 -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=60 -XX:+PrintGCDetails -Xloggc:gc.log -jar target/beimi-0.7.0.war



### 2. 客户端编译流程

客户端代码位置在： client/version/chess

#### 打开客户端项目

用 CocosCreater 打开 client/version/chess 项目。


----用 CocosCreater 创建一个空白新项目 删除 assets和settings两个目录，将 client/version/chess 下的 assets 和 settings 目录cp不过去。----

#### 修改服务器ip地址

修改 client/version/chess/assets/resources/script/lib/HTTP.js 文件里的下面两句，改成自己的IP和端口号

baseURL:"http://localhost",
wsURL : "http://localhost:9081",

#### 启动 CocosCreater 调试

然后用 CocosCreater 运行。


### 3. 基于 docker 编译


#### 编译 docker 镜像前的准备

1. 数据库连接地址需要改成 beimi，这个名字是在 docker-compose.yml 里设置的一个 docker 别名。（在 application.properties 里改）

2. docker 启动后 mysql 数据库需要初始化数据结构和内容，初始化文件在 script/beimi.sql 。这个做一次即可，整个mysql容器的数据都存在 docker/mysql/data 目录下。只要这个目录不删除数据一直存在。可以反复重启或者删除docker容器数据都会存在。

3. 编译 docker 镜像之前必须使用 CocosCreater 发布一下项目，而且需要使用手机版发布。如果没有用手机版发布，需要到docker-compose.yml里，找到 beimi-client > volumes 标签，由 web-mobile 换成 desktop。


#### 执行编译镜像

mvn clean package docker:build

#### 启动docker容器（包括，mysql，beimi server,beimi client共三个容器）

docker-compose up -d

#### 关闭并删除所有容器

docker-compose down

注意：docker-compose down 并不会删除数据库内容，因为数据库内容存在目录里。



