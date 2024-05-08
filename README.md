<p align="center">
	<img alt="logo" src="https://foruda.gitee.com/avatar/1677189584093051772/9844924_dong-puen_1656601856.png!avatar200">
</p>
<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">Fir Cloud v1.0.0</h1>
<h4 align="center">基于Spring Cloud Alibaba微服务网关框架</h4>
<p align="center">
	<img src="https://img.shields.io/badge/Fir%20Cloud-v1.0.0-da282a"></a>
	<img src="https://img.shields.io/github/license/mashape/apistatus.svg"></a>
</p>


## 简述

本项目配合前端项目[fir_security_vue2](https://gitee.com/dong-puen/fir-cloud/tree/master/fir_security_vue2)可以运行。



## 🧭 功能说明



|          功能           | 实现 |
| :---------------------: | :--: |
|        令牌校验         |  ✔   |
|      请求整体解密       |  ✔   |
|      响应整体加密       |  ✔   |
|       防重放校验        |  ✔   |
|       完整性校验        |  ✔   |
|         xss校验         |  ✔   |
|      ip地址白名单       |  ✔   |
|     接口地址白名单      |  ✔   |
| Nacos配置、服务注册中心 |  ✔   |
|          Redis          |  ✔   |
|     访问日志记录器      |  ✔   |
|           盐            |  ✔   |
|       AES对称加密       |  ✔   |
|      RSA非对称加密      |  ✔   |

## 框架

| 框架                                                         | 说明                             | 版本           | 相关文档                                                     |
| ------------------------------------------------------------ | -------------------------------- | -------------- | ------------------------------------------------------------ |
| [Spring Cloud Alibaba](https://github.com/alibaba/spring-cloud-alibaba) | 阿里分布式应用服务一站式解决方案 | 2021.0.5.0     | [文档](https://github.com/alibaba/spring-cloud-alibaba/blob/2023.x/README-zh.md) |
| [Spring Boot](https://spring.io/projects/spring-boot)        | 应用开发框架                     | 2.6.13         | [文档](https://github.com/YunaiV/SpringBoot-Labs)            |
| [Spring Cloud Gateway](https://github.com/spring-cloud/spring-cloud-gateway) | Spring生态系统之上的API网关      | 2021.0.5.0     | [文档](https://springdoc.cn/spring-cloud-gateway/)           |
| [nacos](https://github.com/alibaba/nacos)                    | 配置管理和服务管理               | 2021.0.5.0     | [文档](https://nacos.io/zh-cn/docs/quick-start-spring.html)  |
| [loadbalancer](https://spring.io/guides/gs/spring-cloud-loadbalancer) | 负载均衡器                       | 2021.0.5.0     | [文档](https://springdoc.cn/spring-cloud-load-balancer/)     |
| [netflix-hystrix](https://github.com/Netflix/Hystrix)        | 熔断器                           | 2.2.10.RELEASE | [文档](https://github.com/Netflix/Hystrix/wiki/How-it-Works) |
| [feign](https://github.com/OpenFeign/feign)                  | 负载均衡和服务发现等功能         | 2021.0.5.0     | [文档](https://springdoc.cn/spring-cloud-openfeign/)         |
| [Redis](https://redis.io/)                                   | key-value 数据库                 | 5.0            | [文档](https://www.redis.net.cn/tutorial/3501.html)          |
| [hutool-all](https://github.com/dromara/hutool)              | 功能丰富且易用的Java工具库       | 5.8.20         | [文档](https://doc.hutool.cn/pages/index/)                   |
| [jsoup](https://jsoup.org/)                                  | Java 的HTML解析器                | 1.14.1         | [文档](https://jsoup.org/apidocs/)                           |
| [commons-lang3](https://github.com/apache/commons-lang)      | 类型转换工具库                   | 3.12.0         | [文档](https://commons.apache.org/proper/commons-lang/apidocs/) |
| [fastjson](https://github.com/alibaba/fastjson)              | JSON 工具库                      | 1.2.83         | [文档](https://github.com/alibaba/fastjson/wiki/Quick-Start-CN) |

## 版本选择

选择目前最新的Spring Boot 2.6.13作为基础。

| Spring Cloud Alibaba Version | Spring Cloud Version  | Spring Boot Version |
| ---------------------------- | --------------------- | ------------------- |
| 2021.0.5.0                   | Spring Cloud 2021.0.5 | 2.6.13              |

根据Spring Cloud的版本选择适合的中间件。

| Spring Cloud Alibaba Version | Sentinel Version | Nacos Version | RocketMQ Version | Dubbo Version | Seata Version |
| ---------------------------- | ---------------- | ------------- | ---------------- | ------------- | ------------- |
| 2021.0.5.0                   | 1.8.6            | 2.2.0         | 4.9.4            | ~             | 1.6.1         |

## 
