
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
- 查询

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

- 编辑员工信息

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
- 根据类型查询分类
  - 在 6. 菜品分类中已经实现

- 文件上传

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

- 新增菜品

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
- 根据 id 查询数据
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

- 修改菜品

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

<details>

<summary> 12. 店铺营业状态设置 </summary>

1. 需求分析
- 设置营业状态
- 管理端查询营业状态
- 用户端查询营业状态

2. 接口信息
- 设置营业状态

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

- 管理端查询营业状态

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

- 用户端查询营业状态

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

3. 具体实现
- 使用 Redis 进行存储
  - 1为营业，0为打烊

</details>

<details>

<summary> 13. 微信登录 </summary>

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

<summary> 14. 导入商品浏览功能 </summary>

1. 需求分析
- 查询分类
- 根据分类 id 查询菜品
- 根据分类 id 查询套餐
- 根据套餐 id 查询包含的菜品

2. 接口信息

- 查询分类

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

- 根据分类 id 查询菜品

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

- 根据分类 id 查询套餐

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

- 根据套餐 id 查询包含的菜品

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

<details>

<summary> 15. 缓存菜品信息 </summary>

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
<summary> 16. 缓存套餐信息（使用 Spring Cache） </summary>

1. 具体实现
- 导入 Spring Cache 和 Redis 相关的 Maven 坐标
- 在启动类上加入 @EnableCaching 注解，开启缓存注解功能
- 用户端接口 SetmealController 的 list 方法加入 @Cacheable 注解
- 管理端接口 SetmealController 的 add、update、delete、startOrStop 方法加入 @CacheEvict 注解
  - 新增、修改、删除、起售停售状态改变

</details>

<details>
<summary> 17. 购物车 </summary>

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
<summary> 18. 查看购物车 </summary>

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
<summary> 19. 清空购物车 </summary>

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