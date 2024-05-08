
## nacos 配置
```yaml
aa: fir-gateway-plus
#第1步、加入JVM参数：
# B-Dreactor.netty.pool.leasingStrategy=lifo

spring:
  redis:
    host: localhost
    port: 6379
    password: 123456
    timeout: 6000
  cloud:
    gateway:
      httpclient:
        pool:
          # 根据需要调整
          maxIdleTime: 10000
      routes:
        # 路由的ID，没有固定规则但要求唯一，建议配合服务名
        - id: fir-config
          # 转发之前去掉第1层路径(此次访问下级路由时由原/get/value变成了/nacos/get/value)
          filters:
            - StripPrefix=1
          predicates:
            - name: Path
              # 断言，路径相匹配的进行路由
              args[pattern]: /api-one/**
            # 匹配连接nacos的服务作为后端服务的路由地址
          uri: lb://fir-config

        # 转发websocket
        - id: fir-config
          # 转发之前去掉第1层路径(此次访问下级路由时由原/get/value变成了/nacos/get/value)
          filters:
            - StripPrefix=1
          uri: lb:ws://fir-config
          predicates:
            - name: Path
              # 断言，路径相匹配的进行路由
              args[pattern]: /socket/**

        # 登录节点微服务
        - id: fir-node-two-dev
          # 转发之前去掉第1层路径(此次访问下级路由时由原/get/value变成了/nacos/get/value)
          filters:
            - StripPrefix=1
          predicates:
            - name: Path
              # 断言，路径相匹配的进行路由
              args[pattern]: /api-two/**
            # 匹配连接nacos的服务作为后端服务的路由地址
          uri: lb://fir-node-two-dev
leyou:
  jwt:
    pubKeyPath: C:\\tmp\\rsa\\rsa.pub # 公钥地址
    cookieName: LY_TOKEN
  filter:
    #需要进行过滤的白名单
    allowPaths:
      - /api/search
      - /api/auth
      - /api/user/register
      - /api/user/check
      - /api/user/code
      - /api/item
      - /ws
```