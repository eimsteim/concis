## Concis 

[![spring-boot](https://img.shields.io/badge/spring--boot-2.6.4-green.svg) ](https://gitee.com/link?target=http%3A%2F%2Fspring.io%2Fprojects%2Fspring-boot)[![mybatis-plus](https://img.shields.io/badge/mybatis--plus-3.5.2-blue.svg) ](https://gitee.com/link?target=http%3A%2F%2Fmp.baomidou.com)[![MySQL](https://img.shields.io/badge/MySQL-5.7-9cf.svg) ](https://gitee.com/link?target=https%3A%2F%2Fwww.hutool.cn%2F)[![beetl](https://img.shields.io/badge/antd-4.20.0-red.svg)](https://gitee.com/link?target=http%3A%2F%2Fibeetl.com%2F) [![React](https://img.shields.io/badge/React-17.0.0-yellow.svg)](https://gitee.com/link?target=http%3A%2F%2Fibeetl.com%2F)

`Concis` 是一个极致简洁的后台管理系统快速开发脚手架，技术栈采用Spring Boot + Ant Design Pro，Concis 的本意就是`简洁`，我们不希望在这个框架里加入过多的功能，只想把后台管理系统所必须的功能做到极致。



### 设计特点

- `Concis`以依赖的方式嵌入到开发者自己的Spring Boot项目中，而不是独立运行，能更方便地搭建基于开发者自己命名空间的新项目；

- 独创的`一键式`打包发布机制，将前后端代码统一管理、打包和发布，保证了前后端版本一致性，并显著降低了软件部署的工作量；

- 使用`React`+`Antd`实现前端高度组件化，将单个组件的所有逻辑限制在单个文件中，更容易实现复用；

- 热插拔式的缓存和鉴权组件设计，并提供了开箱即用的缺省实现；

- 基于`MyBatis Plus`实现了轻贫血模型，并参考`DDD`思想划分了功能边界，使未来的业务逻辑更容易复用；

  

### 快速开始

与其他快速开发框架不同，`Concis`并不能直接启动运行，而是需要嵌入开发者自己的业务工程，具体方法如下：

1. 使用`Git`拉取`concis-parent`源代码到本地；
2. 使用`Maven`分别`instlal`安装`concis-kernel`和`concis-sys`模块；
3. 新建一个Spring Boot工程（后简称A），并将`init.sql`初始化到`MySQL`数据库；
4. 在A工程的`pom.xml`文件中引入`concis-sys`依赖；
5. 在A工程的`/src/main/`目录下新建`web`文件夹；
6. 将`portal-admin`目录整个拷贝到上述`web`文件夹下；
7. 分别启动A工程和`portal-admin`，打开浏览器访问`localhost:8000`，即可看到登录页。



### 开发文档

详细开发文档可以关注我的公众号，回复`concis`获取访问地址:laughing:



