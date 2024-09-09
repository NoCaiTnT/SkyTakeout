

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