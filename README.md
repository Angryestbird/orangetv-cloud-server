# 橘子云TV服务端🍊

## 项目简介

这个项目可以快速的搭建起一整套视频服务器集群，支持Api网关，统一认证，全文搜索。
支持自动扫描指定路径的视频，能够自动生成视频封面图。支持使用Docker部署。

## 快速启动

1. 配置docker环境，如果使用Windows可以安装Docker Desktop
2. 在项目根目录创建.env文件，并添加下面的内容

       OUTER_PORT=8080(默认的端口)
       OUTER_HOST={计算机名称（不是IP）}
       VIDEO_ROOT={存放视频的绝对路径}

       # user specific config
       ORANGE_TV_USERNAME={管理员用户名}
       ORANGE_TV_PASSWORD={管理员密码}

* 注：配套的前端项目默认发布到8080端口

3. 执行 docker-compose up -d

## 服务构成

项目基于微服务的架构搭建，整合了Spring Cloud Gateway，
Spring Security Oauth2等开源框架， 视频元数据使用MySql存储，
视频封面图基于Mongo DB grid FS存储，服务之间可以通过Http restful
接口(基于Open Feign 或 Web Client)或kafka进行通信。

### 图片存储服务：album

接受kafka消息队列的视频元数据信息，使用javacv库提取视频的关键帧，供展示使用。

### 统一认证服务：auth-oidc

基于Spring security Authentication Server项目，完美支持Oauth2 OIDC协议。

### 服务注册与发现中心：discovery

基于Spring Cloud Eureka项目，使用单点的方式部署，支持扩展到Eureka集群。

### 服务API网关：gateway

基于Spring Boot Gateway，能够支持动态的注册和管理路由。

### 视频元数据服务：video

视频元数据存储服务，支持全文的视频标题搜索。

## 启动顺序

1. 数据库和中间件：MySQL,MongoDB,ZooKeeper,Kafka,Nginx(视频服务器)
2. 微服务: discovery -> auth -> oidc -> gateway -> video -> album

## 横向扩展

本项目支持横向扩展到多个视频节点，方案如下：

1. 部署视频服务器，可以使用nginx。
2. 部署视频元数据服务，配置文件扫描路径到视频根目录。
3. 配置视频元数据服务服务发现Url,指向Eureka服务器。
3. 配置视频元数据服务令牌发现Uri,指向统一认证服务。

这样，视频元数据服务会自动提取视频元数据，且把路由注册到Api网关。  
一个集群可以部署多个视频服务器和视频元数据服务，突破了单节点的限制。

## 消息流

1. 视频应用扫描文件夹生成视频元数据，保存到Mysql。
2. 视频应用推送每一条视频元数据到kafka的消息队列里面。
3. 相册应用接收kafka中的视频元数据，生成截图，保存的gridFS。
4. 相册应用每次生成截图，通过openFeign更新视频的元数据。
