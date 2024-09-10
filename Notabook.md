

## 注解
### Swagger

| 注解               | 说明                                               |
|-------------------|--------------------------------------------------|
| @Api              | 用在类上，表示对类的说明，如 Controller，通过 tags 参数进行描述         |
| @ApiModel         | 用在类上，表示对类的说明，如 Entity、DTO、VO，通过 description 参数描述 |
| @ApiModelProperty | 用在属性上，描述属性信息，通过 value 参数描述                       |
| @ApiOperation     | 用在方法上，说明方法的用途、作用，如 Controller 的方法，通过 value 参数描述  |

### @Result
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