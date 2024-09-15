
## 注解
<details>
<summary> Swagger </summary>

| 注解               | 说明                                               |
|-------------------|--------------------------------------------------|
| @Api              | 用在类上，表示对类的说明，如 Controller，通过 tags 参数进行描述         |
| @ApiModel         | 用在类上，表示对类的说明，如 Entity、DTO、VO，通过 description 参数描述 |
| @ApiModelProperty | 用在属性上，描述属性信息，通过 value 参数描述                       |
| @ApiOperation     | 用在方法上，说明方法的用途、作用，如 Controller 的方法，通过 value 参数描述  |

1. 接口区分
- 在 WebMvcConfiguration 中配置 Swagger 的多个 Docket 对象
- 通过 groupName 进行区分

</details>

<details>

<summary> @Result </summary>

解决 数据库表字段名 和 Java类变量名 对应关系
如：
```java
@Results({
    @Result(property = "id", column = "employee_id"),
    @Result(property = "name", column = "employee_name"),
    @Result(property = "email", column = "email_address")
})
```

也可以使用 MyBatis 的自动映射功能（Auto Mapping）

也可以在 xml 中显示映射
```xml
<resultMap id="employeeMap" type="Employee">
    <result property="id" column="employee_id"/>
    <result property="name" column="employee_name"/>
    <result property="email" column="email_address"/>
</resultMap>
```

</details>

## Redis

<details>

<summary> 1. 启动服务 </summary>

1. 启动服务端：redis-server.exe redis.windows.conf
   - 默认端口：6379
2. 启动客户端：redis-cli.exe
   - -h：指定ip
   - -p：指定端口
   - -a：密码
3. 修改配置文件：redis.windows.conf
   - requirepass：设置密码

</details>

<details>

<summary> 2. 字符串操作命令 String </summary>

| 命令                      | 解释                                    |
|-------------------------|---------------------------------------|
| SET key value           | 设置指定 key 的值                           |
| GET key                 | 获取指定 key 的值                           |
| SETEX key seconds value | 设置指定 key 的值，并将 key 的过期时间设置为 seconds 秒 |
| SETNX key value         | 只有 key 不存在时设置 key 的值                  |

</details>

<details>

<summary> 3. 哈希操作命令 Hash </summary>

fieId 和 value 都是字符串

| 命令                   | 解释                              |
|----------------------|---------------------------------|
| HSET key fieId value | 将哈希表 key 中的字段 field 的值设置为 value |
| HGET key fieId       | 获取哈希表 key 中 field 字段的值          |
| HDEL key fieId       | 删除哈希表 key 中的 field 字段           |
| HKEYS key            | 获取哈希表 key 的所有字段（field）          |
| HVALS key            | 获取哈希表 key 的所有值                  |

</details>

<details>

<summary> 4. 列表操作命令 List </summary>

简单的字符串列表，按插入顺序排序

| 命令                        | 解释              |
|---------------------------|-----------------|
| LPUSH key value1 [value2] | 将一个/多个值插入到列表 头部 |
| LRANGE key start stop     | 获取列表指定范围内的元素    |
| RPOP key                  | 从列表 尾部 移除元素     |
| LLEN key                  | 获取列表长度          |

</details>

<details>

<summary> 5. 集合操作命令 Set </summary>

集合内的元素也是字符串

| 命令                         | 解释             |
|----------------------------|----------------|
| SADD key member1 [member2] | 向集合 key 添加元素   |
| SMEMBERS key               | 返回集合 key 的所有元素 |
| SCARD key                  | 返回集合 key 的元素数  |
| SINTER key1 [key2]         | 返回给定所有集合的 交集   |
| SUNION key1 [key2]         | 返回给定所有集合的 并集   |
| SREM key member1 [member2] | 删除集合 key 中的元素  |

</details>

<details>

<summary> 6. 有序集合操作命令 Zset </summary>

集合内的元素也是字符串，有序，每个元素管理一个 double 类型的分数

| 命令                                       | 解释                                      |
|------------------------------------------|-----------------------------------------|
| ZADD key score1 member1 [score2 member2] | 向有序集合 key 添加元素                          |
| ZRANGE key start stop [WITHSCORES]       | 获得有序集合指定范围的元素，也可以通过 WITHSCORES 将分数一起返回  |
| ZINCRBY key increment member             | 对有序集合 key 中的成员 member 的分数增量加上 increment |
| SREM key member1 [member2]               | 删除有序集合 key 中的元素                         |

</details>

<details>

<summary> 7. 通用操作命令 </summary>

不分数据类型，都可以使用的命令

| 命令           | 解释                                            |
|--------------|-----------------------------------------------|
| KEYS pattern | 查找所有符合给定模式 pattern 的 key，如 KEYS * / KEYS set* |
| EXISTS key   | 判断 key 是否存在                                   |
| TYPE key     | 获得 key 所存储的值的类型                               |
| DEL key      | 在 key 存在时删除 key，可以删除多个                        |

</details>

<details>

<summary> 8. 在 Java 中使用 Redis：Spring Data Redis </summary>

1. 操作步骤
- 导入 Spring Data Redis 的 Maven 坐标
  - sky-server 的 pom 文件
- 配置 Redis 数据源
  - application.yml 和 application-dev.yml
- 编写配置类，创建 RedisTemplate 对象
  - com.sky.config 中创建 RedisConfiguration
  - 设置 Redis 的连接工厂，获取 Redis 连接，与 Redis 服务器建立通讯
  - 设置 Redis key 的序列化器，将 Redis 中的二进制数据转换为字符串
- 通过 RedisTemplate 对象操作 Redis

2. 单元测试
- 在 test/java 文件夹下，创建 SpringDataRedisTest 类
- 使用不同对象操作不同数据类型
```
  ValueOperations valueOperations = redisTemplate.opsForValue();
  HashOperations hashOperations = redisTemplate.opsForHash();
  ListOperations listOperations = redisTemplate.opsForList();
  SetOperations setOperations = redisTemplate.opsForSet();
  ZSetOperations zSetOperations = redisTemplate.opsForZSet();
```
- 操作通用类型命令时，使用 RedisTemplate 对象

</details>

## HttpClient
用于构造、发送 Http 请求，在 Java 中以代码方式发送 Http 请求

<details>

<summary> 1. 使用方法 </summary>

- 案例在 sky-server 的 test/HttpClientTest 中
- 已经封装在了 sky-common 中的 HttpClientUtils 中
- 将 Httpclient 坐标导入到 Maven 中
  - 使用的阿里云 OSS 包，已经包含了 HttpClient，不用再导入了
- 发送请求的步骤
  - 创建 HttpClient 对象
    - 使用 HttpClients 进行创建，返回 CloseableHttpClient 对象
  - 创建 Http 请求对象
    - 创建 HttpGet 对象，传入请求地址
    - 可选：构造 StringEntity 对象传参，参数为 json 格式的字符串，并制定实体的编码方式（urf-8）和数据格式（json）
    - 使用 JSONObject 对象构造 json 格式的字符串
  - 调用 HttpClient 的 execute 方法发送请求
    - 返回 CloseableHttpResponse 对象
- 解析请求
  - 获得状态码
    - 使用 response.getStatusLine().getStatusCode()
  - 获得返回数据
    - 获取实体：response.getEntity()
    - 获取 String 类型的数据：EntityUtils.toString(entity);
- 关闭资源
  - 关闭 CloseableHttpResponse 对象 response
  - 关闭 CloseableHttpClient 对象

</details>


## Spring Cache
<details>

<summary> 1. 使用方法 </summary>

Spring Cache 是一个框架，实现了基于 注解 的缓存功能，只需要添加注解即可实现缓存功能

提供了一层抽象，底层可以切换不同的缓存实现
- EHCache
- Caffeine
- Redis

具体使用
- 导入 Maven 坐标
- 使用 Redis 就导入 Redis 的坐标，其他同理
</details>

<details>

<summary> 2. 常用注解 </summary>

| 注解             | 说明                                 |
|----------------|------------------------------------|
| @EnableCaching | 开启缓存注解功能，加在启动类上                    |
| @Cacheable     | 在方法执行前先查询缓存，有就返回；没有就执行方法，并将返回值放到缓存 |
| @CachePut      | 将方法的返回值放到缓存                        |
| @CacheEvict    | 将一条或多条数据从缓存中删除                     |

- @CachePut：加到 Controller 的方法上
  - 使用方法：@CachePut(cacheNames = "userCache", key = "#user.id")
  - 生成 key 的规则：userCache::1，即 cacheNames + :: + key 这种格式
  - key 用形参赋值：# + 形参名.属性
  - key 用形参赋值：# + p0.属性，或者 # + a0.属性，或者 # + root.args[0].属性（表示第一个参数，下标从 0 开始）
  - key 用返回值赋值：# + result.属性
- @Cacheable：加到 Controller 的方法上
  - 使用方法：@Cacheable(cacheNames = "userCache", key = "#id")
- @CacheEvict：加到 Controller 的方法上
  - 使用方法：@CacheEvict(cacheNames = "userCache", key = "#id")
  - 删除所有：@CacheEvict(cacheNames = "userCache", allEntries = true)
  - allEntries：是否清空所有缓存，默认为 false
  - beforeInvocation：是否在方法执行前清空缓存，默认为 false

</details>

## Spring Task
Spring Task 是 Spring 框架提供的任务调度工具，可以按照约定的时间自动执行某个代码逻辑
- 定位：定时任务框架
- 作用：定时自动执行某段 Java 代码

<details>

<summary> 1. 应用场景 </summary>

- 信用卡每月还款提醒
- 银行贷款每月还款提醒
- 火车票售票系统处理未支付订单
- 入职纪念日为用户发送通知
- 等等

</details>

<details>
<summary> 2. Cron 表达式 </summary>

Cron 表达式其实就是一个字符串，通过 Cron 表达式可以定义任务触发的时间

规则构成：分为 6 或 7 个域，由空格分开，每个域代表一个含义
- 秒、分钟、小时、日、月、周、年（可选）
- 周：星期几
- 日 和 周往往不能同时出现，例如 每个月的 5 号不一定都是星期 3，其中一个写成 ?
- 使用 Cron 表达式在线生成器生成：https://cron.qqe2.com

| 秒 | 分钟 | 小时 | 日 | 月  | 周 | 年    | 备注                     | 表达式                |
|---|----|----|---|----|---|------|------------------------|--------------------|
| 0 | 0  | 9  | 12 | 10 | ? | 2022 | 2022 年 10 月 12 日上午 9点整 | 0 0 9 12 10 ？ 2022 |

</details>

## WebSocket
WebSocket 是基于 TCP 的一种新的网络协议，实现了浏览器与服务器的全双工通信
- 浏览器和服务器只需完成一次握手，两者之间可以创建 持久性 的连接，并进行 双向 数据传输

HTTP 与 WebSocket的区别
- HTTP 是短连接，WebSocket 是长连接
- HTTP 通信是单向的，基于请求响应模式
- WebSocket 支持双向通信
- 二者的底层都是 TCP 连接

<details>

<summary> 1. 使用场景 </summary>

服务器主动推送数据给服务端
- 视频弹幕
- 网页聊天
- 体育实况更新
- 股票基金报价实时更新

</details>

<details>

<summary> 2. 入门案例 </summary>

- 直接使用 websocket.html 页面作为 WebSocket 客户端
  - 建立连接、许多回调方法、关闭连接、内容显示、发送消息给服务端
  - 建立连接，需要指定路径：ws://localhost:8080/ws/clientId
- 服务端导入 WebSocket 的 Maven 坐标
- 导入 WebSocket 服务端组件 WebSocketServer，用于和客户端通信
  - 在 sky-server 下创建包 websocket，导入 WebSocketServer
  - 类使用注解：@Component
  - 类使用注解：@ServerEndpoint，提供连接路径 ws/{clientId}
  - 定义回调方法（通过 id 区分不同客户端）：建立连接、接收消息、关闭连接、群发方法
- 导入配置类 WebSocketConfiguration，注册 WebSocket 的服务端组件
  - 类使用注解：@Configuration
  - 方法使用注解：@Bean
- 导入定时任务类 WebSocketTask，用于定时向客户端发送消息
  - 放到 task 包下
  - 注入 WebSocketServer

</details>

## Apache ECharts
Apache ECharts 是一款基于 JS 的数据可视化图表库，提供直观、生动、可交互、可个性化定制的数据可视化图表，官方网址：https://echarts.apache.org/zh/index.html

<details>

<summary> 1. 入门案例 </summary>

- 获取 echarts 的 js 文件：https://www.jsdelivr.com/package/npm/echarts?tab=files&path=dist
- 下载 dist/echarts.js
- 在 html 中引入 Apache ECharts：\<script src="echarts.js">\</script>
- 使用 div 标签创建一个图表容器，在里面编写代码
- 可以参考官方示例：https://echarts.apache.org/handbook/zh/get-started/#%E5%BC%95%E5%85%A5-apache-echarts

</details>

## 需求分析
<details>
<summary>1. JWT令牌</summary>
令牌：用于合法用户发送请求，后端只相应合法令牌对应的请求

流程如下：
1. 用户登录（给后端发送账号密码），后端验证用户信息，生成令牌
2. 后端生成 JWT Token 将令牌返回给前端
3. 前端将令牌存储在本地，每次请求时将令牌放在请求头中，向后端发送请求
4. 后端进行拦截，验证令牌，如果合法则执行业务逻辑并返回数据，否则返回错误信息
</details>

<details>
<summary>2. 添加员工</summary>

1. 需求描述

添加员工信息

| 字段   | 需求        |
|------|-----------|
| 账号   | 唯一        |
| 密码   | 默认123456  |
| 姓名   |           |
| 手机号  | 11位合法手机号  |
| 性别   | 男/女 二选一   |
| 身份证号 | 18位合法身份证号 |

2. 接口信息

（1）基本信息
- path：/admin/employee
- method：POST

（2）请求参数
- Headers

| 名称           | 参数值              | 是否必须 | 示例 | 备注 |
|--------------|------------------|------|----| --- |
| Content-Type | application/json | 必须   |    |      |
- Body

| 名称      | 类型    | 是否必须  | 默认值 | 备注   | 其他信息 |
|----------|---------|-------|-----|------|------|
| id       | integer | 非必须   |     | 员工id |      |
| idNumber | string  | 必须    |     | 身份证  |   |
| name     | string  | 必须    |     | 姓名   |      |
| phone    | string  | 必须    |     | 手机号  |      |
| sex      | string  | 必须    |     | 性别   |      |
| username | string  | 必须    |     | 用户名  |      |

（3）返回数据

| 名称   | 类型      | 是否必须  | 默认值 | 备注   | 其他信息 |
|------|---------|-------|-----|------|------|
| code | integer | 必须   |     | 状态码  |      |
| msg  | string  | 非必须    |     | 错误信息 |      |
| data | object  | 非必须    |     | 返回数据 | 不需要  |

3. 错误处理

（1）插入重复字段

提取重复字段，构造msg，进行返回

（2）动态获取添加者的id：使用 ThreadLocal

ThreadLocal：为每个线程单独提供一份存储空间，每个线程都可以独立修改自己的副本，而不会影响其他线程的副本

因此，在拦截器拦截并判断令牌时，可以将用户id存储在ThreadLocal中，方便后续使用

常用方法：
- public void set (T value)：设置当前线程的线程局部变量的值 -- 加
- public T get ()：返回当前线程的线程局部变量的值  -- 获得
- public void remove ()：移除当前线程的线程局部变量的值  -- 删
</details>

<details>
<summary> 3. 员工分页查询 </summary>

1. 需求描述
- 根据页码展示员工信息
- 每页展示 10 条数据
- 分页查询时可以根据需要，输入员工姓名进行查询

2. 接口信息

（1）基本信息
- path：/admin/employee/page
- method：GET

（2）请求参数
- Query

| 名称       | 类型      | 是否必须  | 默认值 | 备注    | 其他信息 |
|----------|---------|-------|-----|-------|------|
| name     | string  | 非必须   |     | 员工姓名  |      |
| page     | integer | 必须    |     | 页码    |   |
| pageSize | integer | 必须    |     | 每页记录数 |      |

（3）返回数据

| 名称                                | 类型       | 是否必须  | 默认值 | 备注         | 其他信息 |
|-----------------------------------|----------|-------|-----|------------|------|
| code                              | integer  | 必须   |     | 状态码        |      |
| msg                               | string   | 非必须    |     | 错误信息       |   |
| data                              | object   | 必须    |     | 返回数据         |      |
| &emsp;\|-- total                  | integer  | 必须    |     | 总页数        |      |
| &emsp;\|-- records                | object[] | 必须    |     | 当前页的所有员工数据 |      |
| &emsp;&emsp;&emsp;\|-- id         | integer  | 必须    |     | 员工 id      |      |
| &emsp;&emsp;&emsp;\|-- username   | string   | 必须    |     | 用户名        |      |
| &emsp;&emsp;&emsp;\|-- name       | string   | 必须    |     | 姓名         |      |
| &emsp;&emsp;&emsp;\|-- password   | string   | 必须    |     | 密码         |      |
| &emsp;&emsp;&emsp;\|-- phone      | string   | 必须    |     | 电话号        |      |
| &emsp;&emsp;&emsp;\|-- sex        | string   | 必须    |     | 性别         |      |
| &emsp;&emsp;&emsp;\|-- idNumber   | string   | 必须    |     | 身份证号       |      |
| &emsp;&emsp;&emsp;\|-- status     | integer  | 必须    |     | 账号状态       |      |
| &emsp;&emsp;&emsp;\|-- createTime | string   | 必须    |     | 创建时间       |      |
| &emsp;&emsp;&emsp;\|-- updateTime | string   | 必须    |     | 更新时间       |      |
| &emsp;&emsp;&emsp;\|-- createUser | integer   | 必须    |     | 创建者的 id    |      |
| &emsp;&emsp;&emsp;\|-- updateUser | integer   | 必须    |     | 修改者的 id    |      |

3. 错误处理

日期返回格式错误
- 使用 @JsonFormat 注解格式化日期变量
- 在 WebMvcConfiguration 中扩展 Spring MVC 的消息转换器，统一对日期类型进行格式化处理
  - 需要自己定一个 日期转换 的消息转换器，将其加入到系统的消息转换器列表中

</details>

<details>

<summary> 4. 启用 & 禁用员工账号 </summary>

1. 需求描述
- 启用员工账号：对禁用的账号，将状态设置为启用
- 禁用员工账号：对启用的账号，将状态设置为禁用
- 禁用状态的账号不能登陆系统

2. 接口信息

（1）基本信息
- path：/admin/employee/status/{status}
- method：POST

（2）请求参数
- Headers

| 名称         | 参数值    | 是否必须 | 示例 | 备注    |
|------------|--------|------|----|-------|
| Content-Type | application/json | 必须   |    |   |

- 路径参数

| 名称     | 示例 | 备注           |
|--------|----|--------------|
| status | 1  | 状态，1为启用，0为禁用 |

- Query

| 名称 | 类型      | 是否必须 | 默认值 | 备注    | 其他信息 |
|----|---------|------|-----|-------|------|
| id | integer | 必须   |     | 员工 id |      |

（3）返回数据

| 名称                                | 类型       | 是否必须  | 默认值 | 备注         | 其他信息 |
|-----------------------------------|----------|-------|-----|------------|------|
| code                              | integer  | 必须   |     | 状态码        |      |
| msg                               | string   | 非必须    |     | 错误信息      |   |
| data                              | object   | 非必须    |     | 返回数据       |      |

</details>

<details>

<summary> 5. 编辑员工账号 </summary>

1. 需求描述
- 根据 id 查询员工信息，用于前端显示
- 对员工信息进行修改
  - 账号 username
  - 姓名 name
  - 手机号 phone
  - 性别 sex
  - 身份证号 idNumber

2. 接口信息

<details>

<summary style="margin-left: 20px;"> 2.1 查询 </summary>>

（1）基本信息
- path：/admin/employee/{id}
- method：GET

（2）请求参数
- 路径参数

| 名称 | 示例 | 备注   |
|----|----|------|
| id | 3  | 员工id |

（3）返回数据

| 名称                   | 类型       | 是否必须  | 默认值 | 备注         | 其他信息 |
|-----------------------|----------|-------|-----|------------|------|
| code                  | integer  | 必须   |     | 状态码        |      |
| msg                   | string   | 非必须    |     | 错误信息       |   |
| data                  | object   | 必须    |     | 返回数据         |      |
| &emsp;\|-- id         | integer  | 必须    |     | 员工 id      |      |
| &emsp;\|-- username   | string   | 必须    |     | 用户名        |      |
| &emsp;\|-- name       | string   | 必须    |     | 姓名         |      |
| &emsp;\|-- password   | string   | 必须    |     | 密码         |      |
| &emsp;\|-- phone      | string   | 必须    |     | 电话号        |      |
| &emsp;\|-- sex        | string   | 必须    |     | 性别         |      |
| &emsp;\|-- idNumber   | string   | 必须    |     | 身份证号       |      |
| &emsp;\|-- status     | integer  | 必须    |     | 账号状态       |      |
| &emsp;\|-- createTime | string   | 必须    |     | 创建时间       |      |
| &emsp;\|-- updateTime | string   | 必须    |     | 更新时间       |      |
| &emsp;\|-- createUser | integer   | 必须    |     | 创建者的 id    |      |
| &emsp;\|-- updateUser | integer   | 必须    |     | 修改者的 id    |      |

</details>

<details>

<summary style="margin-left: 20px;"> 2.2 编辑员工信息 </summary>

（1）基本信息
- path：/admin/employee
- method：PUT

（2）请求参数
- Headers

| 名称         | 参数值              | 是否必须 | 示例 | 备注    |
|------------|------------------|------|----|-------|
| Content-Type | application/json | 必须   |    |   |

- Body

| 名称                   | 类型       | 是否必须  | 默认值 | 备注         | 其他信息 |
|-----------------------|----------|-------|-----|------------|------|
| id         | integer  | 必须    |     | 员工 id      |      |
| username   | string   | 必须    |     | 用户名        |      |
| name       | string   | 必须    |     | 姓名         |      |
| phone      | string   | 必须    |     | 电话号        |      |
| sex        | string   | 必须    |     | 性别         |      |
| idNumber   | string   | 必须    |     | 身份证号       |      |

（3）返回数据

| 名称                   | 类型       | 是否必须  | 默认值 | 备注         | 其他信息 |
|-----------------------|----------|-------|-----|------------|------|
| code                  | integer  | 必须   |     | 状态码        |      |
| msg                   | string   | 非必须    |     | 错误信息       |   |
| data                  | object   | 非必须    |     | 返回数据         |      |

</details>

</details>

<details>

<summary> 6. 菜品分类 </summary>

1. 需求描述
- 分类名称必须 唯一
- 分类类型为：菜品分类 和 套餐分类
- 新添加的分类状态默认为 禁用（新分类无菜品，禁用 不让在应用端展示）

2. 接口信息
- 新增分类
  - path：/admin/category
  - method：POST
- 分类分页查询
  - path：/admin/category/page
  - method：GET
- 根据 id 删除分类
  - path：/admin/category
  - method：DELETE
- 修改分类
  - path：/admin/category
  - method：PUT
- 启用 & 禁用分类
  - path：/admin/category/status/{status}
  - method：POST
- 根据类型查询
  - path：/admin/category/list
  - method：GET

</details>

<details>

<summary> 7. 公共字段自动填充 </summary>

1. 需求描述

业务表中有许多公共（重复）的字段

| 名称         | 类型       | 含义   | 操作类型          |
|---------------------|----------|------|---------------|
| create_time | datetime | 创建时间 | insert        |
| create_user | bigint   | 创建者  | insert        |
| update_time | datetime | 修改时间 | insert、update |
| update_user | bigint   | 修改者  | insert、update  |


这种重复的字段导致业务代码需要重复编写，十分冗余，不利于后期维护

因此需要一种手段来进行统一的操作

2. 实现方式
- 使用 切面 拦截 Mapper，统一对公共字段进行赋值
- 自定义注解 AutoFill，用于标识需要进行公共字段自动填充的方法
- 自定义切面类 AutoFillAspect，统一拦截加入了 AutoFill 注解的方法，通过反射为公共字段赋值
- 在 Mapper 的方法上加入 AutoFill 注解
- 技术点：枚举、注解、AOP、反射

3. 具体实现

（1）创建 AutoFill 注解
- com.sky.annotation/Autofill

（2）创建 AutoFillAspect 切面类
- com.sky.aspect/AutoFillAspect
- 切入点
  - 定义切入点 Pointcut：@Pointcut("execution(* com.sky.mapper.*.*(..))")
  - 拦截 Mapper 包下所有类的所有方法（.*.*），参数是任意参数（..），返回值是任意返回值（*）
  - 此外：该包下的查询 & 删除不需要拦截，因此需要设定只拦截带有 @AutoFill 注解的方法
  - 需要加入：@annotation(com.sky.annotation.AutoFill)
  - 整体：@Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
- 通知-参数
  - 需要 前缀通知，在插入 & 更新前进行赋值
  - 指定切入点：即切入点的函数名
  - @Before("autoFillPointcut()")
  - 参数：JoinPoint，被拦截方法的信息
- 通知-实现
  - 获取拦截到的数据块操作类型：insert or update
    - 更新操作不用改变 创建时间 和 创建者
  - 获得拦截到的方法的参数，即实体对象，这样才能对它赋值
    - 默认方法的第一个参数为所需的实体对象
  - 准备公共字段赋值的数据
  - 根据操作类型，通过 反射 赋值
    - 先获得实体的 set 方法，定义了常量字段
    - 然后调用 set 方法进行赋值

</details>

<details>

<summary> 8. 新增菜品 </summary>

1. 需求描述

添加菜品信息

| 字段     | 需求       |
|--------|----------|
| 菜品名称   | 唯一       |
| 菜品分类   |        |
| 菜品价格   |  |
| 口味做法配置 |    |
| 菜品图片   |        |
| 菜品描述   |    |

业务规则：
- 菜品名唯一
- 菜品必须属于某个分类，不能单独存在
- 新增菜品时可以根据情况选择菜品的口味
- 每个菜品必须对应一张图片

接口设计：
- 查询菜品分类
- 文件上传（图片）
- 新增菜品

2. 接口信息

<details>

<summary style="margin-left: 20px;"> 2.1 根据类型查询分类 </summary>
  - 在 6. 菜品分类中已经实现

</details>

<details>

<summary style="margin-left: 20px;"> 2.2文件上传 </summary>

（1）基本信息
- path：/admin/common/upload
- method：POST

（2）请求参数
- Headers

| 名称         | 参数值                 | 是否必须 | 示例 | 备注    |
|------------|---------------------|------|----|-------|
| Content-Type | multipart/form-data | 必须   |    |   |

- Body

| 名称   | 类型   | 是否必须  | 默认值 | 备注 | 其他信息 |
|------|------|-------|-----|----|------|
| file | file | 必须    |     | 文件 |      |


（3）返回数据

| 名称                   | 类型       | 是否必须  | 默认值 | 备注     | 其他信息 |
|-----------------------|----------|-------|-----|--------|------|
| code                  | integer  | 必须   |     | 状态码    |      |
| msg                   | string   | 非必须    |     | 错误信息   |   |
| data                  | object   | 必须    |     | 文件上传路径 |      |

</details>

<details>

<summary style="margin-left: 20px;"> 2.3 新增菜品 </summary>

（1）基本信息
- path：/admin/dish
- method：POST

（2）请求参数
- Headers

| 名称         | 参数值              | 是否必须 | 示例 | 备注    |
|------------|------------------|------|----|-------|
| Content-Type | application/json | 必须   |    |   |

- Body

| 名称                | 类型         | 是否必须 | 默认值 | 备注             | 其他信息 |
|-------------------|------------|------|-----|----------------|------|
| categoryid        | integer    | 必须   |     | 分类 id          |      |
| description       | string     | 非必须  |     | 菜品描述           |      |
| flavors           | object[]   | 非必须   |     | 口味             |      |
| &emsp;\|-- dishid | integer    | 非必须   |     | 菜品 id          |      |
| &emsp;\|-- id     | integer    | 非必须   |     | 口味 id          |      |
| &emsp;\|-- name   | string     | 必须   |     | 口味名称           |      |
| &emsp;\|-- value  | string     | 必须   |     | 口味值            |      |
| id                | integer    | 非必须   |     | 菜品 id          |      |
| image             | string     | 必须   |     | 菜品图片路径         |      |
| name              | string     | 必须   |     | 菜品名称           |      |
| price             | bigdecimal | 必须   |     | 菜品价格           |      |
| status            | integer    | 非必须   |     | 菜品状态：1为起售，0为停售 |      |

（3）返回数据

| 名称                   | 类型       | 是否必须  | 默认值 | 备注    | 其他信息 |
|-----------------------|----------|-------|-----|-------|------|
| code                  | integer  | 必须   |     | 状态码   |      |
| msg                   | string   | 非必须    |     | 错误信息  |   |
| data                  | object   | 非必须    |     | 返回数据  |      |

</details>

3. 具体实现

（1）文件上传
- 新增 CommonController 
- 配置阿里云OSS服务 application.yml/application-dev.yml
- 创建配置类，配置服务信息 OssConfiguration 
  - 在程序运行的时候创建一个配置类对象：使用 @Bean 和 @ConditionalOnMissingBean
- 在 CommonController 中使用 @Autowired 来获取这个对象
- 调用 AliOssUtils 中的上传方法，进行上传，并获得路径

（2）新增菜品
- 新增 DishController
- 新增菜品相关服务 DishService，以及其实现类 DishServiceImpl
- 在 菜品表 中添加菜品时，还需要在口味表中进行添加，调用 DishMapper
  - 因此创建添加方法 addDishAndFlavor
  - 开启 事务 保证一致性：@Transactional
  - 需要在 SkyApplication 中开启注解方式的事务管理
- 在 DishMapper 中新增 添加菜品 函数
  - 使用公共字段填充 @AutoFill
- 新增 DishMapper.xml 文件，实现上述函数
  - 需要获得该菜品自动生成的 id：使用 useGeneratedKeys="true"
  - 将该 id 赋值给 实体对象 Dish 中的 id 字段：使用 keyProperty="id"
- 新增 DishFlavorMapper 对口味表进行操作，添加 @Mapper 注解
  - 定义批量添加口味方法：addBatchFlavor
- 新增 DishFlavorMapper.xml 文件，实现上述函数
  - 使用 foreach 进行遍历列表

</details>

<details>

<summary> 9. 菜品分配查询 </summary>

1. 需求分析
- 根据页码展示菜品信息
- 每页展示 10 条数据
- 分页查询时，可以输入菜品名称、菜品分类、菜品状态进行查询

2. 接口信息

（1）基本信息
- path：/admin/dish/page
- method：GET

（2）请求参数
- Query

| 名称         | 类型      | 是否必须 | 默认值 | 备注     | 其他信息 |
|------------|---------|------|-----|--------|------|
| page       | integer | 必须   |     | 页码     |   |
| pageSize   | integer | 必须   |     | 每页记录数  |      |
| name       | string  | 非必须  |     | 菜品名称   |      |
| categoryId | integer | 非必须   |     | 分类 id  |      |
| status     | integer | 非必须   |     | 菜品售卖状态 |      |

（3）返回数据

| 名称                                  | 类型         | 是否必须 | 默认值 | 备注         | 其他信息 |
|-------------------------------------|------------|------|-----|------------|------|
| code                                | integer    | 必须   |     | 状态码        |      |
| msg                                 | string     | 非必须  |     | 错误信息       |   |
| data                                | object     | 必须   |     | 返回数据       |      |
| &emsp;\|-- total                    | integer    | 必须   |     | 总页数        |      |
| &emsp;\|-- records                  | object[]   | 非必须  |     | 当前页的所有员工数据 |      |
| &emsp;&emsp;&emsp;\|-- id           | integer    | 必须   |     | 菜品 id      |      |
| &emsp;&emsp;&emsp;\|-- name         | string     | 必须   |     | 菜品名称       |      |
| &emsp;&emsp;&emsp;\|-- price        | bigdecimal | 必须   |     | 价格         |      |
| &emsp;&emsp;&emsp;\|-- image        | string     | 必须   |     | 图片路径       |      |
| &emsp;&emsp;&emsp;\|-- description  | string     | 必须   |     | 菜品描述       |      |
| &emsp;&emsp;&emsp;\|-- status       | integer    | 必须   |     | 菜品售卖状态     |      |
| &emsp;&emsp;&emsp;\|-- updateTime   | string     | 必须   |     | 更新时间       |      |
| &emsp;&emsp;&emsp;\|-- categoryName | string     | 必须   |     | 分类名称       |      |

3. 具体实现
- 在 xml 文件中写的查询语句，需要连表查询，字段名需要一一对应，因此在 sql 语句中需要给字段起别名

</details>

<details>

<summary> 10. 删除菜品 </summary>

1. 需求分析
- 可以删除一个菜品，批量删除多个菜品
- 起售中的菜品不能删除
- 被套餐关联的菜品不能删除
- 删除菜品后，关联的口味数据也需要删除掉

2. 接口信息

（1）基本信息
- path：/admin/dish
- method：DELETE

（2）请求参数
- Query

| 名称  | 类型     | 是否必须 | 默认值 | 备注        | 其他信息  |
|-----|--------|------|-----|-----------|-------|
| ids | string | 必须   |     | 用逗号分隔的字符串 | 1,2,3 |

（3）返回数据

| 名称                            | 类型         | 是否必须 | 默认值 | 备注         | 其他信息 |
|-------------------------------|------------|------|-----|------------|------|
| code                          | integer    | 必须   |     | 状态码        |      |
| msg                           | string     | 非必须  |     | 错误信息       |   |
| data                          | object     | 非必须   |     | 返回数据       |      |

3. 具体实现
- 涉及的表
  - 菜品表 dish
  - 菜品口味表 dish_flavor
  - 套餐菜品关系表 setmeal_dish
- 新增 SetmealDishMapper 套餐菜品关系表的 Mapper
  - 查询菜品是否和套餐有关联
- 新增 SetmealDishMapper.xml 并编写动态 sql 语句
  - 使用 foreach 进行遍历，使用 open 和 close 在遍历前后加上括号
- 使用事务注解保证一致性：@Transactional

</details>

<details>

<summary> 11. 修改菜品 </summary>

1. 需求分析
- 菜品信息的回显
- 菜品分类的查询（已实现）
- 文件上传（已实现）
- 修改菜品

2. 接口信息

<details>

<summary style="margin-left: 20px;"> 2.1 根据 id 查询数据 </summary>
（1）基本信息
- path：/admin/dish/{id}
- method：GET

（2）请求参数
- 路径参数

| 名称 | 类型      | 是否必须 | 默认值 | 备注    | 其他信息  |
|----|---------|------|-----|-------|-------|
| id | integer | 必须   |     | 菜品 id |  |

（3）返回数据

| 名称                            | 类型         | 是否必须 | 默认值 | 备注         | 其他信息 |
|-------------------------------|------------|------|-----|------------|------|
| code                          | integer    | 必须   |     | 状态码        |      |
| msg                           | string     | 非必须  |     | 错误信息       |   |
| data                          | object     | 非必须   |     | 返回数据       |      |
| &emsp;\|-- categoryId         | integer    | 非必须   |     | 菜品类别 id    |      |
| &emsp;\|-- description        | string     | 非必须  |     | 菜品描述       |      |
| &emsp;\|-- flavors            | object[]   | 非必须  |     | 当前页的所有员工数据 |      |
| &emsp;&emsp;&emsp;\|-- dishId | integer    | 非必须   |     | 菜品 id      |      |
| &emsp;&emsp;&emsp;\|-- id     | integer    | 非必须   |     | 菜品名称       |      |
| &emsp;&emsp;&emsp;\|-- name   | string     | 非必须   |     | 价格         |      |
| &emsp;&emsp;&emsp;\|-- value  | string     | 非必须   |     | 图片路径       |      |
| &emsp;\|-- id                 | integer    | 非必须  |     | 菜品 id      |      |
| &emsp;\|-- image              | string     | 非必须  |     | 菜品图像       |      |
| &emsp;\|-- name               | string     | 非必须  |     | 菜品名        |      |
| &emsp;\|-- price              | bigdecimal | 非必须  |     | 菜品价格       |      |

</details>

<details>

<summary style="margin-left: 20px;"> 2.2 修改菜品 </summary>>

（1）基本信息
- path：/admin/dish
- method：PUT

（2）请求参数
- Headers

| 名称           | 类型               | 是否必须 | 默认值 | 备注    | 其他信息  |
|--------------|------------------|------|-----|-------|-------|
| Content-Type | applocation/json | 必须   |     |  |  |

- Body

| 名称                | 类型         | 是否必须 | 默认值 | 备注         | 其他信息 |
|-------------------|------------|----|-----|------------|------|
| categoryId        | integer    | 必须 |     | 菜品类别 id    |      |
| description       | string     | 非必须 |     | 菜品描述       |      |
| flavors           | object[]   | 非必须 |     | 当前页的所有员工数据 |      |
| &emsp;\|-- dishId | integer    | 必须 |     | 菜品 id      |      |
| &emsp;\|-- id     | integer    | 必须 |     | 菜品名称       |      |
| &emsp;\|-- name   | string     | 必须 |     | 价格         |      |
| &emsp;\|-- value  | string     | 必须 |     | 图片路径       |      |
| id                | integer    | 必须 |     | 菜品 id      |      |
| image             | string     | 必须 |     | 菜品图像       |      |
| name              | string     | 必须 |     | 菜品名称       |      |
| price             | bigdecimal | 必须 |     | 菜品价格       |      |
| status            | integer    | 必须 |     | 菜品状态       |      |

（3）返回数据

| 名称                            | 类型         | 是否必须 | 默认值 | 备注         | 其他信息 |
|-------------------------------|------------|------|-----|------------|------|
| code                          | integer    | 必须   |     | 状态码        |      |
| msg                           | string     | 非必须  |     | 错误信息       |   |
| data                          | object     | 非必须   |     | 返回数据       |      |

</details>

</details>

<details>

<summary> 12. 套餐相关 </summary>

1. 需求分析
- 新增套餐
- 套餐分页查询
- 删除套餐
- 修改套餐
- 起售停售套餐

2. 接口设计

<details>
<summary style="margin-left: 20px;"> 2.1 新增套餐 </summary>

（1）根据分类 id 查询菜品
- path：/admin/dish/list
- method：GET

（2）新增套餐
- path：/admin/setmeal
- method：POST

</details>

<details>
<summary style="margin-left: 20px;"> 2.2 套餐分页查询 </summary>

- path：/admin/setmeal/page
- method：GET

</details>

<details>
<summary style="margin-left: 20px;"> 2.3 删除套餐 </summary>

- path：/admin/setmeal
- method：DELETE

</details>

<details>
<summary style="margin-left: 20px;"> 2.4 修改套餐 </summary>

（1）根据 id 查询套餐
- path：/admin/setmeal/{id}
- method：GET

（2）修改套餐
- path：/admin/setmeal
- method：PUT

</details>

<details>
<summary style="margin-left: 20px;"> 2.5 起售停售套餐 </summary>

- path：/admin/setmeal/status/{status}
- method：POST

</details>

</details>

<details>

<summary> 13. 店铺营业状态设置 </summary>

1. 需求分析
- 设置营业状态
- 管理端查询营业状态
- 用户端查询营业状态

2. 接口信息

<details>

<summary style="margin-left: 20px;"> 2.1 设置营业状态 </summary>

（1）基本信息
- path：/admin/shop/{status}
- method：PUT

（2）请求参数
- Headers

| 名称           | 类型               | 是否必须 | 描述 |
|--------------|------------------|------|-----|
| Content-Type | application/json | 必须   |     |

- 路径参数

| 名称 | 类型      | 是否必须 | 默认值 | 备注                 | 其他信息  |
|----|---------|------|-----|--------------------|-------|
| status | integer | 必须   |     | 店铺营业状态：1 为营业，2 为打烊 |  |

（3）返回数据

| 名称                            | 类型         | 是否必须 | 默认值 | 备注         | 其他信息 |
|-------------------------------|------------|------|-----|------------|------|
| code                          | integer    | 必须   |     | 状态码        |      |
| msg                           | string     | 非必须  |     | 错误信息       |   |
| data                          | object     | 非必须   |     | 返回数据       |      |

</details>

<details>

<summary style="margin-left: 20px;"> 2.2 管理端查询营业状态 </summary>

（1）基本信息
- path：/admin/shop/status
- method：GET

（2）请求参数 

无

（3）返回数据

| 名称                            | 类型         | 是否必须 | 默认值 | 备注             | 其他信息 |
|-------------------------------|------------|------|-----|----------------|------|
| code                          | integer    | 必须   |     | 状态码            |      |
| msg                           | string     | 非必须  |     | 错误信息           |   |
| data                          | object     | 必须   |     | 返回数据，1为营业，2为打烊 |      |

</details>

<details>

<summary style="margin-left: 20px;"> 2.3 用户端查询营业状态 </summary>

（1）基本信息
- path：/user/shop/status
- method：GET

（2）请求参数

无

（3）返回数据

| 名称                            | 类型         | 是否必须 | 默认值 | 备注             | 其他信息 |
|-------------------------------|------------|------|-----|----------------|------|
| code                          | integer    | 必须   |     | 状态码            |      |
| msg                           | string     | 非必须  |     | 错误信息           |   |
| data                          | object     | 必须   |     | 返回数据，1为营业，2为打烊 |      |

</details>

3. 具体实现
- 使用 Redis 进行存储
  - 1为营业，0为打烊

</details>

<details>

<summary> 14. 微信登录 </summary>

1. 需求分析
- 基于微信登录实现小程序的登录功能
- 如果是新用户需要自动完成注册

2. 接口信息

（1）基本信息
- path：/user/user/login
- method：POST

（2）请求参数
- Headers

| 名称           | 类型               | 是否必须 | 描述 |
|--------------|------------------|------|-----|
| Content-Type | application/json | 必须   |     |

- 路径参数

| 名称   | 类型     | 是否必须 | 默认值 | 备注       | 其他信息  |
|------|--------|------|-----|----------|-------|
| code | string | 必须   |     | 微信用户的授权码 |  |

（3）返回数据

| 名称                | 类型         | 是否必须 | 默认值 | 备注        | 其他信息 |
|-------------------|------------|------|-----|-----------|------|
| code              | integer    | 必须   |     | 状态码       |      |
| msg               | string     | 非必须  |     | 错误信息      |   |
| data              | object     | 必须   |     | 返回数据      |      |
| &emsp;\|-- id     | integer    | 必须 |     | 用户 id     |      |
| &emsp;\|-- openid | string     | 必须 |     | 微信 openid |      |
| &emsp;\|-- token  | string     | 必须 |     | jwt 令牌    |      |

3. 具体实现
- 配置微信登录所需的配置项
  - 在 applocation.yml 和 applocation-dev.yml 新增代码
  - 填入 appid 和 secret
- 为用户端配置 jwt 令牌相关信息
  - 在 applocation.yml 新增代码
  - 填入 jwt 的 secret、过期时间、token 名称
- 流程
  - 获取用户的 openid
    - 使用 http 向微信请求
    - 参数：自己的 appid、secret，用户登录的 code，授权类型 grant_type（固定值）
  - 判断 openid 是否为空
  - 判断当前用户是否已经注册
    - 根据 openid 查询用户表
  - 新用户，自动完成注册 
    - 用户信息插入用户表，并返回该记录的用户 id
  - 返回用户对象
- 生成 JWT 令牌加入到用户信息中返回
- 创建拦截器校验用户带来的 JWT 令牌
  - 在 sky-server 的 interceptor 中创建拦截器
- 注册该拦截器
  - 在 sky-server 的 WebMvcConfiguration 中注册拦截器

</details>

<details>

<summary> 15. 导入商品浏览功能 </summary>

1. 需求分析
- 查询分类
- 根据分类 id 查询菜品
- 根据分类 id 查询套餐
- 根据套餐 id 查询包含的菜品

2. 接口信息

<details>

<summary style="margin-left: 20px;"> 2.1 查询分类 </summary>

（1）基本信息
- path：/user/category/list
- method：GET

（2）请求参数
- Query

| 名称   | 类型      | 是否必须 | 默认值 | 备注                          | 其他信息  |
|------|---------|------|-----|-----------------------------|-------|
| type | integer | 非必须  |     | 分类类型：1为菜品分类，2为套餐分类，不传值为全部分类 |  |

（3）返回数据

| 名称                    | 类型      | 是否必须 | 默认值 | 备注               | 其他信息 |
|-----------------------|---------|------|-----|------------------|------|
| code                  | integer | 必须   |     | 状态码              |      |
| msg                   | string  | 非必须  |     | 错误信息             |   |
| data                  | object  | 必须   |     | 返回数据             |      |
| &emsp;\|-- id         | integer | 必须   |     | 分类 id            |      |
| &emsp;\|-- name       | string  | 必须   |     | 分类名称             |      |
| &emsp;\|-- sort       | integer | 非必须   |     | 排序               |      |
| &emsp;\|-- status     | integer | 非必须   |     | 分类状态，0为禁用，1为启用   |      |
| &emsp;\|-- type       | integer | 非必须   |     | 类型，1为菜品分类，2为套餐分类 |      |
| &emsp;\|-- createTime | string  | 必须   |     | 创建时间             |      |
| &emsp;\|-- createUser | integer | 非必须  |     | 创建者              |      |
| &emsp;\|-- updateTime | string  | 必须   |     | 修改时间             |      |
| &emsp;\|-- updateUser | integer | 非必须   |     | 修改者              |      |

</details>

<details>

<summary style="margin-left: 20px;"> 2.2 根据分类 id 查询菜品 </summary>

（1）基本信息
- path：/user/dish/list
- method：GET

（2）请求参数
- Query

| 名称         | 类型      | 是否必须 | 默认值 | 备注    | 其他信息  |
|------------|---------|------|-----|-------|-------|
| categoryId | integer | 必须   |     | 分类 id |  |

（3）返回数据

| 名称                            | 类型         | 是否必须 | 默认值 | 备注             | 其他信息 |
|-------------------------------|------------|------|-----|----------------|------|
| code                          | integer    | 必须   |     | 状态码            |      |
| msg                           | string     | 非必须  |     | 错误信息           |   |
| data                          | object     | 必须   |     | 返回数据           |      |
| &emsp;\|-- categoryId         | integer    | 必须   |     | 分类 id          |      |
| &emsp;\|-- categoryName       | integer    | 非必须  |     | 分类名称           |      |
| &emsp;\|-- description        | string     | 非必须  |     | 菜品描述           |      |
| &emsp;\|-- flavors            | object[]   | 非必须  |     | 口味             |      |
| &emsp;&emsp;&emsp;\|-- dishId | integer    | 非必须  |     | 菜品 id          |      |
| &emsp;&emsp;&emsp;\|-- id     | integer    | 非必须  |     | 口味 id          |      |
| &emsp;&emsp;&emsp;\|-- name   | integer    | 非必须  |     | 口味名称           |      |
| &emsp;&emsp;&emsp;\|-- value  | integer    | 非必须  |     | 口味数据           |      |
| &emsp;\|-- id                 | integer    | 必须   |     | 菜品 id          |      |
| &emsp;\|-- name               | string     | 必须   |     | 菜品名称           |      |
| &emsp;\|-- image              | string     | 必须   |     | 菜品图片           |      |
| &emsp;\|-- price              | bigdecimal | 必须   |     | 菜品价格           |      |
| &emsp;\|-- status             | integer    | 非必须  |     | 售卖状态，0为停售，1为起售 |      |
| &emsp;\|-- updateTime         | string     | 非必须  |     | 修改时间           |      |

</details>

<details>

<summary style="margin-left: 20px;"> 2.3 根据分类 id 查询套餐 </summary>

（1）基本信息
- path：/user/setmeal/list
- method：GET

（2）请求参数
- Query

| 名称         | 类型      | 是否必须 | 默认值 | 备注    | 其他信息  |
|------------|---------|------|-----|-------|-------|
| categoryId | integer | 必须   |     | 分类 id |  |

（3）返回数据

| 名称                            | 类型         | 是否必须 | 默认值 | 备注    | 其他信息 |
|-------------------------------|------------|------|-----|-------|------|
| code                          | integer    | 必须   |     | 状态码   |      |
| msg                           | string     | 非必须  |     | 错误信息  |   |
| data                          | object     | 必须   |     | 返回数据  |      |
| &emsp;\|-- categoryId         | integer    | 必须   |     | 分类 id |      |
| &emsp;\|-- description        | string     | 非必须   |     | 套餐描述  |      |
| &emsp;\|-- id                 | integer    | 必须   |     | 套餐 id |      |
| &emsp;\|-- name               | string     | 必须   |     | 套餐名称  |      |
| &emsp;\|-- image              | string     | 必须   |     | 套餐图片  |      |
| &emsp;\|-- price              | bigdecimal | 必须   |     | 套餐价格           |      |
| &emsp;\|-- status             | integer    | 非必须   |     | 售卖状态，0为停售，1为起售 |      |
| &emsp;\|-- createTime | string  | 非必须   |     | 创建时间  |      |
| &emsp;\|-- createUser | integer | 非必须  |     | 创建者   |      |
| &emsp;\|-- updateTime | string  | 非必须   |     | 修改时间  |      |
| &emsp;\|-- updateUser | integer | 非必须   |     | 修改者   |      |

</details>

<details>

<summary style="margin-left: 20px;"> 2.4 根据套餐 id 查询包含的菜品 </summary>

（1）基本信息
- path：/user/setmeal/dish/{id}
- method：GET

（2）请求参数
- 路径参数

| 名称 | 类型      | 是否必须 | 默认值 | 备注    | 其他信息  |
|----|---------|------|-----|-------|-------|
| id | integer | 必须   |     | 套餐 id |  |

（3）返回数据

| 名称                     | 类型       | 是否必须 | 默认值 | 备注   | 其他信息 |
|------------------------|----------|------|-----|------|------|
| code                   | integer  | 必须   |     | 状态码  |      |
| msg                    | string   | 非必须  |     | 错误信息 |   |
| data                   | object[] | 非必须  |     | 返回数据 |      |
| &emsp;\|-- copies      | integer  | 必须   |     | 份数   |      |
| &emsp;\|-- description | string   | 必须   |     | 菜品描述 |      |
| &emsp;\|-- image       | string   | 必须   |     | 菜品图片 |      |
| &emsp;\|-- name        | string   | 必须   |     | 菜品名称 |      |

</details>

</details>

<details>

<summary> 16. 缓存菜品信息 </summary>

1. 需求分析
- 每个分类下的菜品保存一份缓存数据，用户端进行请求
  - key：dish_ + 使用菜品分类 id，例如：dish_3
  - value：使用 string 存储菜品信息集合
- 数据库中菜品数据有变更时，清理缓存数据，管理端
  - 修改菜品信息
  - 删除菜品信息
  - 停售/起售菜品
  - 新增菜品信息

2. 具体实现
- 缓存菜品数据
  - 在 Redis 中查询该分类的 id
  - 若存在，直接返回结果
  - 若不存在，查询 db，将查询到的结果写入 Redis

</details>

<details>
<summary> 17. 缓存套餐信息（使用 Spring Cache） </summary>

1. 具体实现
- 导入 Spring Cache 和 Redis 相关的 Maven 坐标
- 在启动类上加入 @EnableCaching 注解，开启缓存注解功能
- 用户端接口 SetmealController 的 list 方法加入 @Cacheable 注解
- 管理端接口 SetmealController 的 add、update、delete、startOrStop 方法加入 @CacheEvict 注解
  - 新增、修改、删除、起售停售状态改变

</details>

<details>
<summary> 18. 购物车 </summary>

1. 需求分析
- 没有口味选择的商品直接加入购物车
- 有口味的商品，需要选择口味后加入购物车
- 相同商品，只需要数量相加，不需要重复展示

2. 接口设计

（1）基本信息
- path：/user/shoppingCart/add
- method：POST
- 请求参数：套餐 id、菜品 id、口味
  - 对于某一次提交，要么是套餐 id，要么是菜品 id

（2）请求参数
- Headers

| 名称           | 类型               | 是否必须 | 描述 |
|--------------|------------------|------|-----|
| Content-Type | application/json | 必须   |     |

- Body

| 名称         | 类型         | 是否必须 | 默认值 | 备注    | 其他信息 |
|------------|------------|----|-----|-------|------|
| dishFlavor | string    | 非必须 |     | 菜品口味  |      |
| dishId     | integer     | 非必须 |     | 菜品 id |      |
| setmealId  | integer   | 非必须 |     | 套餐 id |      |


（3）返回数据

| 名称                     | 类型       | 是否必须 | 默认值 | 备注   | 其他信息 |
|------------------------|----------|------|-----|------|------|
| code                   | integer  | 必须   |     | 状态码  |      |
| msg                    | string   | 非必须  |     | 错误信息 |   |
| data                   | object[] | 非必须  |     | 返回数据 |      |

3. 数据库设计
- 作用：暂时存放所选商品的地方
- 选的什么商品（套餐还是菜品），菜品有没有口味
- 每个商品买了几个，不同口味的商品不能叠加
- 不同用户的购物车需要分开
- 冗余字段：提高查询速度，避免连表查询

| 名称          | 类型            | 说明    | 备注   |
|-------------|---------------|-------|------|
| id          | bigint        | 主键    |      |
| name        | varchar(32)   | 商品名   | 冗余字段     |
| image       | varchar(255)  | 商品图片  | 冗余字段     |
| user_id     | bigint        | 用户 id |      |
| dish_id     | bigint        | 菜品 id |      |
| setmeal_id  | bigint        | 套餐 id |      |
| dish_flavor | varchar(50)   | 菜品口味  |      |
| number      | int           | 商品数量  |      |
| amount      | decimal(10,2) | 商品单价  | 冗余字段 |
| create_time | datetime      | 创建时间  |      |

4. 具体实现
- 在 user 中新增 ShoppingCartController
- 创建 ShoppingCartService
- 创建 ShoppingCartMapper
  - 实现查询、添加、修改

</details>

<details>
<summary> 19. 查看购物车 </summary>

1. 需求分析
- 查询用户的购物车数据

2. 接口设计

（1）基本信息
- path：/user/shoppingCart/list
- method：GET
- 请求参数：不用参数，可以通过 ThreadLocal 拿id

（2）返回数据

| 名称                    | 类型        | 是否必须 | 默认值 | 备注    | 其他信息 |
|-----------------------|-----------|-----|-----|-------|------|
| code                  | integer   | 必须  |     | 状态码   |      |
| msg                   | string    | 非必须 |     | 错误信息  |   |
| data                  | object[]  | 必须  |     | 返回数据  |      |
| &emsp;\|-- id         | integer   | 必须   |     | 唯一标识  |      |
| &emsp;\|-- name       | string    | 必须   |     | 商品名称  |      |
| &emsp;\|-- userId     | integer   | 必须   |     | 用户 id |      |
| &emsp;\|-- dishId     | integer   | 必须   |     | 菜品 id |      |
| &emsp;\|-- setmealId  | integer   | 必须   |     | 套餐 id |      |
| &emsp;\|-- dishFlavor | string    | 必须   |     | 菜品口味  |      |
| &emsp;\|-- number     | integer   | 必须   |     | 数量    |      |
| &emsp;\|-- amount     | decimal   | 必须   |     | 价格    |      |
| &emsp;\|-- image      | string    | 必须   |     | 图片地址  |      |
| &emsp;\|-- createTime | string    | 必须   |     | 创建时间  |      |

</details>

<details>
<summary> 20. 清空购物车 </summary>

1. 需求分析
- 删除用户的购物车数据

2. 接口设计

（1）基本信息
- path：/user/shoppingCart/clean
- method：DELETE
- 请求参数：不用参数，可以通过 ThreadLocal 拿id

（2）返回数据

| 名称                    | 类型      | 是否必须 | 默认值 | 备注    | 其他信息 |
|-----------------------|---------|-----|-----|-------|------|
| code                  | integer | 必须  |     | 状态码   |      |
| msg                   | string  | 非必须 |     | 错误信息  |   |
| data                  | object  | 非必须  |     | 返回数据  |      |

</details>

<details>
<summary> 21. 导入地址簿 </summary>

1. 需求分析
- 用户可以有多个地址，但只有 1 个默认地址
- 查询地址列表
- 设置默认地址
- 查询默认地址
- 新增、修改、删除地址
 
2. 接口设计

<details>

<summary style="margin-left: 20px;"> 2.1 新增地址 </summary>

（1）基本信息
- path：/user/addressBook
- method：POST

（2）请求参数
- Headers

| 名称           | 类型               | 是否必须 | 描述 |
|--------------|------------------|------|-----|
| Content-Type | application/json | 必须   |     |

- Body

| 名称           | 类型       | 是否必须 | 默认值 | 备注              | 其他信息 |
|--------------|----------|----|-----|-----------------|------|
| cityCode     | string   | 非必须 |     | 市级编号            |      |
| cityName     | string   | 非必须 |     | 市级名称            |      |
| consignee    | string   | 非必须 |     | 收货人             |      |
| detail       | string   | 必须 |     | 详细地址            |      |
| districtCode | string   | 非必须 |     | 区级编号            |      |
| districtName | string   | 非必须 |     | 区级名称            |      |
| id           | integer  | 非必须 |     | 唯一标识            |      |
| isDefault    | integer  | 非必须 |     | 是否默认地址，0为不是，1为是 |      |
| label        | string   | 非必须 |     | 标签，家，学校等        |      |
| phone        | string   | 必须 |     | 手机号             |      |
| provinceCode | string   | 非必须 |     | 省级编号            |      |
| provinceName | string   | 非必须 |     | 省级名称            |      |
| sex          | string   | 必须 |     | 性别              |      |
| userId       | integer  | 非必须 |     | 用户 id           |      |

（3）返回数据

| 名称                    | 类型      | 是否必须 | 默认值 | 备注    | 其他信息 |
|-----------------------|---------|-----|-----|-------|------|
| code                  | integer | 必须  |     | 状态码   |      |
| msg                   | string  | 非必须 |     | 错误信息  |   |
| data                  | object  | 非必须  |     | 返回数据  |      |

</details>

<details>

<summary style="margin-left: 20px;"> 2.2 查询用户所有地址 </summary>

（1）基本信息
- path：/user/addressBook/list
- method：GET

（2）返回数据

| 名称                    | 类型       | 是否必须 | 默认值 | 备注    | 其他信息 |
|-----------------------|----------|----|-----|-------|------|
| code                  | integer  | 必须 |     | 状态码   |      |
| msg                   | string   | 非必须 |     | 错误信息  |   |
| data                  | object[] | 必须 |     | 返回数据  |      |
| &emsp;\|-- id           | integer  | 必须 |     | 唯一标识            |      |
| &emsp;\|-- userId       | integer  | 必须 |     | 用户 id           |      |
| &emsp;\|-- consignee    | string   | 必须 |     | 收货人             |      |
| &emsp;\|-- phone        | string   | 必须 |     | 手机号             |      |
| &emsp;\|-- sex          | string   | 必须 |     | 性别              |      |
| &emsp;\|-- provinceCode | string   | 必须 |     | 省级编号            |      |
| &emsp;\|-- provinceName | string   | 必须 |     | 省级名称            |      |
| &emsp;\|-- cityCode     | string   | 必须 |     | 市级编号            |      |
| &emsp;\|-- cityName     | string   | 必须 |     | 市级名称            |      |
| &emsp;\|-- districtCode | string   | 必须 |     | 区级编号            |      |
| &emsp;\|-- districtName | string   | 必须 |     | 区级名称            |      |
| &emsp;\|-- detail       | string   | 必须 |     | 详细地址            |      |
| &emsp;\|-- label        | string   | 必须 |     | 标签，家，学校等        |      |
| &emsp;\|-- isDefault    | integer  | 必须 |     | 是否默认地址，0为不是，1为是 |      |

</details>

<details>

<summary style="margin-left: 20px;"> 2.3 查询默认地址 </summary>>

（1）基本信息
- path：/user/addressBook/default
- method：GET

（2）返回数据

| 名称                    | 类型     | 是否必须 | 默认值 | 备注    | 其他信息 |
|-----------------------|--------|------|-----|-------|------|
| code                  | integer | 必须   |     | 状态码   |      |
| msg                   | string | 非必须  |     | 错误信息  |   |
| data                  | object | 非必须  |     | 返回数据  |      |
| &emsp;\|-- id           | integer | 非必须  |     | 唯一标识            |      |
| &emsp;\|-- userId       | integer | 非必须  |     | 用户 id           |      |
| &emsp;\|-- consignee    | string | 非必须  |     | 收货人             |      |
| &emsp;\|-- phone        | string | 非必须  |     | 手机号             |      |
| &emsp;\|-- sex          | string | 非必须  |     | 性别              |      |
| &emsp;\|-- provinceCode | string | 非必须  |     | 省级编号            |      |
| &emsp;\|-- provinceName | string | 非必须  |     | 省级名称            |      |
| &emsp;\|-- cityCode     | string | 非必须  |     | 市级编号            |      |
| &emsp;\|-- cityName     | string | 非必须  |     | 市级名称            |      |
| &emsp;\|-- districtCode | string | 非必须  |     | 区级编号            |      |
| &emsp;\|-- districtName | string | 非必须  |     | 区级名称            |      |
| &emsp;\|-- detail       | string | 非必须  |     | 详细地址            |      |
| &emsp;\|-- label        | string | 非必须  |     | 标签，家，学校等        |      |
| &emsp;\|-- isDefault    | integer | 非必须  |     | 是否默认地址，0为不是，1为是 |      |

</details>

<details>

<summary style="margin-left: 20px;"> 2.4 修改地址 </summary>>

（1）基本信息
- path：/user/addressBook
- method：PUT

（2）请求参数
- Headers

| 名称           | 类型               | 是否必须 | 描述 |
|--------------|------------------|------|-----|
| Content-Type | application/json | 必须   |     |

- Body

| 名称           | 类型       | 是否必须 | 默认值 | 备注              | 其他信息 |
|--------------|----------|------|-----|-----------------|------|
| cityCode     | string   | 非必须  |     | 市级编号            |      |
| cityName     | string   | 非必须  |     | 市级名称            |      |
| consignee    | string   | 非必须  |     | 收货人             |      |
| detail       | string   | 必须   |     | 详细地址            |      |
| districtCode | string   | 非必须  |     | 区级编号            |      |
| districtName | string   | 非必须  |     | 区级名称            |      |
| id           | integer  | 必须   |     | 唯一标识            |      |
| isDefault    | integer  | 非必须  |     | 是否默认地址，0为不是，1为是 |      |
| label        | string   | 非必须  |     | 标签，家，学校等        |      |
| phone        | string   | 必须   |     | 手机号             |      |
| provinceCode | string   | 非必须  |     | 省级编号            |      |
| provinceName | string   | 非必须  |     | 省级名称            |      |
| sex          | string   | 必须   |     | 性别              |      |
| userId       | integer  | 非必须  |     | 用户 id           |      |

（3）返回数据

| 名称                    | 类型      | 是否必须 | 默认值 | 备注    | 其他信息 |
|-----------------------|---------|-----|-----|-------|------|
| code                  | integer | 必须  |     | 状态码   |      |
| msg                   | string  | 非必须 |     | 错误信息  |   |
| data                  | object  | 非必须  |     | 返回数据  |      |

</details>

<details>

<summary style="margin-left: 20px;"> 2.5 根据 id 删除地址 </summary>

（1）基本信息
- path：/user/addressBook
- method：DELETE

（2）请求参数
- Query

| 名称 | 类型      | 是否必须 | 默认值 | 备注    | 其他信息 |
|----|---------|-----|-----|-------|------|
| id | integer | 必须  |     | 地址 id |      |

（3）返回数据

| 名称                    | 类型      | 是否必须 | 默认值 | 备注    | 其他信息 |
|-----------------------|---------|-----|-----|-------|------|
| code                  | integer | 必须  |     | 状态码   |      |
| msg                   | string  | 非必须 |     | 错误信息  |   |
| data                  | object  | 非必须  |     | 返回数据  |      |

</details>

<details>

<summary style="margin-left: 20px;"> 2.6 根据 id 查询地址 </summary>

（1）基本信息
- path：/user/addressBook/{id}
- method：GET

（2）请求参数
- 路径参数

| 名称 | 类型      | 是否必须 | 默认值 | 备注    | 其他信息 |
|----|---------|-----|-----|-------|------|
| id | integer | 必须  |     | 地址 id |      |

（3）返回数据

| 名称                    | 类型      | 是否必须 | 默认值 | 备注    | 其他信息 |
|-----------------------|---------|-----|-----|-------|------|
| code                  | integer | 必须  |     | 状态码   |      |
| msg                   | string  | 非必须 |     | 错误信息  |   |
| data                  | object  | 必须  |     | 返回数据  |      |
| &emsp;\|-- id           | integer | 非必须 |     | 唯一标识            |      |
| &emsp;\|-- userId       | integer | 非必须 |     | 用户 id           |      |
| &emsp;\|-- consignee    | string | 非必须 |     | 收货人             |      |
| &emsp;\|-- phone        | string | 非必须 |     | 手机号             |      |
| &emsp;\|-- sex          | string | 非必须 |     | 性别              |      |
| &emsp;\|-- provinceCode | string | 非必须 |     | 省级编号            |      |
| &emsp;\|-- provinceName | string | 非必须 |     | 省级名称            |      |
| &emsp;\|-- cityCode     | string | 非必须 |     | 市级编号            |      |
| &emsp;\|-- cityName     | string | 非必须 |     | 市级名称            |      |
| &emsp;\|-- districtCode | string | 非必须 |     | 区级编号            |      |
| &emsp;\|-- districtName | string | 非必须 |     | 区级名称            |      |
| &emsp;\|-- detail       | string | 非必须 |     | 详细地址            |      |
| &emsp;\|-- label        | string | 非必须 |     | 标签，家，学校等        |      |
| &emsp;\|-- isDefault    | integer | 非必须 |     | 是否默认地址，0为不是，1为是 |      |

</details>

<details>

<summary style="margin-left: 20px;"> 2.7 设置默认地址 </summary>

（1）基本信息
- path：/user/addressBook/defaultc
- method：PUT

（2）请求参数
- Headers

| 名称           | 类型               | 是否必须 | 描述 |
|--------------|------------------|------|-----|
| Content-Type | application/json | 必须   |     |

- Body

| 名称 | 类型      | 是否必须 | 默认值 | 备注    | 其他信息 |
|----|---------|-----|-----|-------|------|
| id | integer | 必须  |     | 地址 id |      |

（3）返回数据

| 名称                    | 类型      | 是否必须 | 默认值 | 备注    | 其他信息 |
|-----------------------|---------|-----|-----|-------|------|
| code                  | integer | 必须  |     | 状态码   |      |
| msg                   | string  | 非必须 |     | 错误信息  |   |
| data                  | object  | 必须  |     | 返回数据  |      |

</details>

</details>

<details>
<summary> 22. 用户下单 </summary>

1. 需求分析
- 地址簿 id
- 配送状态（立即送出、选择送出时间）
- 打包费、总金额（配送费固定为6块）
- 备注
- 餐具数量

2. 接口设计

（1）基本信息
- path：/user/order/submit
- method：POST

（2）请求参数
- Headers

| 名称           | 类型               | 是否必须 | 描述 |
|--------------|------------------|------|-----|
| Content-Type | application/json | 必须   |     |

- Body

| 名称                    | 类型      | 是否必须 | 默认值 | 备注                      | 其他信息 |
|-----------------------|---------|-----|-----|-------------------------|------|
| addressBookId         | integer | 必须  |     | 地址簿 id                  |      |
| amount                | decimal | 必须  |     | 总金额                     |      |
| deliveryStatus        | integer | 必须  |     | 配送状态：1为立即送出，0为选择具体时间    |      |
| estimatedDeliveryTime | string  | 必须  |     | 预计送达时间                  |      |
| packAmount            | decimal | 必须  |     | 打包费                     |      |
| payMethod             | integer | 必须  |     | 付款方式                    |      |
| remark                | string  | 必须  |     | 备注                      |      |
| tablewareNumber       | integer | 必须  |     | 餐具数量                    |      |
| tablewareStatus       | integer | 必须  |     | 餐具数量状态，1为按餐量提供，0为选择具体数量 |      |

（3）返回数据

| 名称                     | 类型      | 是否必须 | 默认值 | 备注    | 其他信息 |
|------------------------|---------|----|-----|-------|------|
| code                   | integer | 必须 |     | 状态码   |      |
| msg                    | string  | 非必须 |     | 错误信息  |   |
| data                   | object  | 必须 |     | 返回数据  |      |
| &emsp;\|-- id          | integer | 必须 |     | 订单 id |      |
| &emsp;\|-- orderAmount | integer | 必须 |     | 订单总金额 |      |
| &emsp;\|-- orderNumber | string | 必须 |     | 订单号   |      |
| &emsp;\|-- orderTime   | string | 必须 |     | 下单时间  |      |


3. 数据库设计

见数据库
- 订单表
- 订单明细表

4. 具体实现
- 新增 OrderController
- 新增 OrderService
- 新增 OrderServiceImpl
- 新增 OrderMapper
- 新增 OrderDetailMapper
- 新增 OrderMapper.xml
- 新增 OrderDetailMapper.xml

</details>

<details>
<summary> 23. 微信订单支付（微信小程序支付） </summary>

1. 需求分析
- 微信小程序订单支付（涉及用户端、服务端、微信后台）
  - 用户下单，服务端返回订单号等信息
  - 用户申请微信支付，服务端调用微信下单接口（预下单，查微信支付接口，JSAPI下单）
  - 微信返回预支付交易标识
  - 服务端组合数据再次签名，返回支付参数给用户
  - 用户确认支付，直接调用微信支付接口（wx.requestPayment）
  - 微信返回支付结果给用户，显示支付结果
  - 微信回调服务端接口，推送支付结果，服务端更新订单状态

2. 存在问题

（1）调用过程中的数据安全：签名、加密解密
- 需要商户私钥文件：apiclient_key.pem
- 需要微信支付平台整数：wechatpay_xxxxxx.pem

（2）微信后台回调，怎么向服务端发送请求（服务端是局域网）：需要公网 ip，内网穿透
- 使用 cpolar：dashboard.cpolar.com
- 使用令牌 Authtoken，该令牌在验证中，复制
  - 在 C:\Program Files\cpolar 中使用 cmd：输入以下代码
  - cpolar.exe authtoken xxxxxxxx
  - 生成配置文件：C:\Users\NoCai\.cpolar\cpolar.yml
- 获取临时公网 ip
  - 在 cmd 中输入代码：cpolar.exe http 8080
  - 8080 为服务端的端口号
  - 得到公网 ip 地址，在 Forwarding 中显示
- 使用公网 ip

3. 具体操作
- 配置微信支付相关配置项，application.yml
- 导入代码
- 由于没有证书，无法测试相关功能 

</details>


<details>

<summary> 24. 用户端历史订单模块 </summary>

1. 需求分析
- 查询历史订单
- 查询订单详情
- 取消订单
- 再来一单

2. 接口设计

<details>
<summary style="margin-left: 20px;"> 2.1 查询历史订单 </summary>

- path：/user/order/historyOrders
- GET

</details>

<details>
<summary style="margin-left: 20px;"> 2.2 查询订单详情 </summary>

- path：/user/order/orderDetail/{id}
- GET

</details>

<details>
<summary style="margin-left: 20px;"> 2.3 取消订单 </summary>

- path：/user/order/cancel/{id}
- PUT

</details>

<details>
<summary style="margin-left: 20px;"> 2.4 再来一单 </summary>

- path：/user/order/repetition/{id}
- POST

</details>

</details>

<details>

<summary> 25. 商家端订单管理模块 </summary>

1. 需求分析
- 订单搜索
- 统计各状态订单数量
- 查询订单详情
- 接单
- 拒单
- 取消订单
- 派送订单
- 完成订单

2. 接口设计

<details>
<summary style="margin-left: 20px;"> 2.1 订单搜索 </summary>

- path：/admin/order/conditionSearch
- GET

</details>

<details>
<summary style="margin-left: 20px;"> 2.2 统计各状态订单数量 </summary>

- path：/admin/order/statistics
- GET

</details>

<details>
<summary style="margin-left: 20px;"> 2.3 查询订单详情 </summary>

- path：/admin/order/details/{id}
- GET

</details>

<details>
<summary style="margin-left: 20px;"> 2.4 接单 </summary>

- path：/admin/order/confirm
- PUT

</details>

<details>
<summary style="margin-left: 20px;"> 2.5 拒单 </summary>

- path：/admin/order/rejection
- PUT

</details>

<details>
<summary style="margin-left: 20px;"> 2.6 取消订单 </summary>

- path：/admin/order/cancel
- PUT

</details>

<details>
<summary style="margin-left: 20px;"> 2.7 派送订单 </summary>

- path：/admin/order/delivery/{id}
- PUT

</details>

<details>
<summary style="margin-left: 20px;"> 2.8 完成订单 </summary>

- path：/admin/order/complete/{id}
- PUT

</details>

</details>

<details>

<summary> 26. 校验收货地址是否超出配送范围 </summary>

1. 需求分析
- 判断用户地址和商家地址的距离

2. 具体实现
- 使用百度地图开放平台：https://lbsyun.baidu.com/
- 创建服务端应用，获取 AK
- 在 application.yml 中配置商家地址和百度地图 AK
- 在 OrderServiceImpl 中，注入商家地址和 AK，使用 @Value("${sky.shop.address}") 和 @Value("${sky.baidu.ak}")
- 在 OrderServiceImpl 中，调用 API，提供距离校验方法
- 在 OrderServiceImpl 中的 submitOrder 方法中，调用距离校验方法

</details>

<details>

<summary> 27. 订单状态定时处理 </summary>

1. 需求分析
- 用户下单但未支付，订单一直处于"待支付"的状态，超时需要取消订单
  - 每分钟检查一次是否存在支付超时订单（下单超过 15 分钟仍未支付），改为"已取消"
- 用户收货后，管理端未点击完成按钮，订单一直处于"派送中"状态，需要完成订单
  - 每天凌晨 1 点检查一次是否存在"派送中"的订单，存在则改为"已完成"

2. 具体实现
- 在 sky-server 中，创建 task 包，创建 OrderTask 类
- 添加处理超时订单方法 processTimeoutOrder 
  - 每分钟查询一次，下单未支付 且 下单时间距离现在大于 15 min
  - 将这些订单的状态设置为"已取消"，设置取消原因和取消时间
  - 更新数据库
- 添加处理一直处于派送中的订单 processDeliveryOrder
  - 每天凌晨 1 点查询一次，派送中 且 下单时间距离现在大于 1 小时（昨天的订单）
  - 将这些订单的状态设置为"已完成"
  - 更新数据库

</details>

<details>

<summary> 28. 来单提醒 </summary>

1. 需求分析

用户下单后需要在网页上提醒商家，两种形式
- 语音播报
- 弹出提示框

2. 具体实现
- 通过 WebSocket 实现管理端页面和服务端的长连接
- 客户支付后，调用 WebSocket 的相关 API 实现服务端想管理端推送消息
  - 支付成功后，微信后台回调服务端的 paySuccessNotify 方法（在 controller 包下的 PayNotifyController 中）
  - 在该方法中，调用 orderService 的 paySuccess 进行订单处理
  - 在 paySuccess 使用 WebSocket 通知管理端
- 管理端浏览器解析消息，判断来单提醒还是客户催单，进行相应的提示和语音播报
- 服务端给管理端的数据格式：JSON
  - type：消息类型，1为来单提醒，2为客户催单
  - orderId：订单 id
  - content：消息内容

</details>

<details>

<summary> 29. 客户催单 </summary>

1. 需求分析

用户在小程序点击催单后需要在网页上提醒商家，两种形式
- 语音播报
- 弹出提示框
- 注意：用户支付后，商家未接单，才能催单
- 可以手动修改数据库进行测试

2. 接口设计

- 用户点击按钮后，向服务端发消息

（1）基本信息
- path：/user/order/reminder/{id}
- method：GET

（2）请求参数
- 路径参数

| 名称 | 类型      | 是否必须 | 默认值 | 备注    | 其他信息 |
|----|---------|-----|-----|-------|------|
| id | integer | 必须  |     | 订单 id |      |

（3）返回数据

| 名称                    | 类型      | 是否必须 | 默认值 | 备注    | 其他信息 |
|-----------------------|---------|-----|-----|-------|------|
| code                  | integer | 必须  |     | 状态码   |      |
| msg                   | string  | 非必须 |     | 错误信息  |   |
| data                  | object  | 非必须  |     | 返回数据  |      |

3. 具体实现
- 通过 WebSocket 实现管理端页面和服务端的长连接
- 客户点击催单按钮后，调用 WebSocket 的相关 API 实现服务端想管理端推送消息
  - 定义催单接口，小程序向服务端发送催单消息
  - 服务端使用 WebSocket 向商家端发送消息
- 管理端浏览器解析消息，判断来单提醒还是客户催单，进行相应的提示和语音播报
- 服务端给管理端的数据格式：JSON
  - type：消息类型，1为来单提醒，2为客户催单
  - orderId：订单 id
  - content：消息内容

</details>

<details>

<summary> 30. 营业额统计 </summary>

1. 需求分析

- 营业额：订单状态为已完成的订单金额合计
- X轴：日期 
- Y轴：营业额
- 根据时间选择器，展示昨天、近 7 日、近 30 日、本周、本月的数据
- 对于每天，展示营业额

2. 接口设计

（1）基本信息
- path：/admin/report/turnoverStatistics
- method：GET

（2）请求参数
- Query

| 名称    | 类型      | 是否必须 | 默认值 | 备注   | 其他信息 |
|-------|---------|-----|-----|------|------|
| begin | string | 必须  |     | 开始日期 |      |
| end   | string | 必须  |     | 结束日期 |      |

（3）返回数据

| 名称                      | 类型      | 是否必须 | 默认值 | 备注              | 其他信息 |
|-------------------------|---------|-----|-----|-----------------|------|
| code                    | integer | 必须  |     | 状态码             |      |
| msg                     | string  | 非必须 |     | 错误信息            |   |
| data                    | object  | 非必须  |     | 返回数据            |      |
| &emsp;\|-- dateList     | string | 必须 |     | 日期列表，以逗号分隔的字符串  |      |
| &emsp;\|-- turnoverList | string | 必须 |     | 营业额列表，以逗号分隔的字符串 |      |

3. 具体实现
- 在 admin 下新增 ReportController
- 新增 ReportService 和 对应的 ReportServiceImpl
  - 可以使用 @RequestParam("begin") 指定 query 参数对应的值
- 若这一天没有订单，查询数据库返回的是空值，需要进行特殊处理

</details>

<details>

<summary> 31. 用户量统计 </summary>

1. 需求分析

- X轴：日期
- Y轴：用户数量
- 根据时间选择器，展示昨天、近 7 日、近 30 日、本周、本月的数据
- 针对每天，展示用户总量和新增用户数量

2. 接口设计

（1）基本信息
- path：/admin/report/userStatistics
- method：GET

（2）请求参数
- Query

| 名称    | 类型      | 是否必须 | 默认值 | 备注   | 其他信息 |
|-------|---------|-----|-----|------|------|
| begin | string | 必须  |     | 开始日期 |      |
| end   | string | 必须  |     | 结束日期 |      |

（3）返回数据

| 名称                       | 类型      | 是否必须 | 默认值 | 备注               | 其他信息 |
|--------------------------|---------|-----|-----|------------------|------|
| code                     | integer | 必须  |     | 状态码              |      |
| msg                      | string  | 非必须 |     | 错误信息             |   |
| data                     | object  | 非必须  |     | 返回数据             |      |
| &emsp;\|-- dateList      | string | 必须 |     | 日期列表，以逗号分隔的字符串   |      |
| &emsp;\|-- newUserList   | string | 必须 |     | 新增用户数量列表，以逗号分隔的字符串 |      |
| &emsp;\|-- totalUserList | string | 必须 |     | 用户总量列表，以逗号分隔的字符串 |      |

</details>

<details>

<summary> 32. 订单统计 </summary>

1. 需求分析

- 有效订单：订单状态为已完成的订单
- X轴：日期
- Y轴：订单数量
- 根据时间选择器，展示昨天、近 7 日、近 30 日、本周、本月的数据
- 针对每天，展示订单总量和有效订单总数
- 展示该时间区间内，有效订单总数、订单总数、订单完成率

2. 接口设计

（1）基本信息
- path：/admin/report/ordersStatistics
- method：GET

（2）请求参数
- Query

| 名称    | 类型      | 是否必须 | 默认值 | 备注   | 其他信息 |
|-------|---------|-----|-----|------|------|
| begin | string | 必须  |     | 开始日期 |      |
| end   | string | 必须  |     | 结束日期 |      |

（3）返回数据

| 名称                             | 类型         | 是否必须 | 默认值 | 备注                   | 其他信息 |
|--------------------------------|------------|-----|-----|----------------------|------|
| code                           | integer    | 必须  |     | 状态码                  |      |
| msg                            | string     | 非必须 |     | 错误信息                 |   |
| data                           | object     | 非必须  |     | 返回数据                 |      |
| &emsp;\|-- dateList            | string     | 必须 |     | 日期列表，以逗号分隔的字符串       |      |
| &emsp;\|-- orderCompletionRate | bigdecimal | 必须 |     | 订单完成率                |      |
| &emsp;\|-- orderCountList      | string     | 必须 |     | 每天的订单总量列表，以逗号分隔的字符串  |      |
| &emsp;\|-- totalOrderCount     | integer    | 必须 |     | 订单总数                 |      |
| &emsp;\|-- validOrderCount     | integer    | 必须 |     | 有效订单总数               |      |
| &emsp;\|-- validOrderCountList | string     | 必须 |     | 每天的有效订单数列表，以逗号分隔的字符串 |      |

3. 注意

- 使用流式处理获得列表的和：Integer totalOrderCount = orderCountList.stream().reduce(Integer::sum).get();
- 除法，分母不为 0
</details>