# 项目简介

[必选] 项目的一般介绍。

# 功能特性

[必选] 列出项目支持的功能及特性。

- 特性xxxxx。
- 特性xxxxx。
- 特性xxxxx。

# 项目结构

[必选] 列出项目的工程结构，模块及相应的描述。

| 模块                | 描述      |
|-------------------|---------|
| damuzee-biz-bootstrap | 启动入口模块  |
| damuzee-biz-foundation | 业务域公共模块 |
| damuzee-biz-module1   | 业务域-模块1 |
| damuzee-biz-module2   | 业务域-模块2 |

## damuzee-biz-bootstrap结构说明

该模块为公共域启动入口模块，只做启动入口yml配置及启动入口bootstrap类 不做任务业务

|  模块   | 描述  |
|  ----  | ----  |
| bootstrap-web  | web后端服务启动入口模块|
| bootstrap-job  | 定时任务后台服务启动入口模块 |
| bootstrap-api  | dubbo服务启动入口模块 |

## damuzee-biz-foundation结构说明

该模块为公共域公共业务及配置等模块，子业务域有公用的部分会抽离在该模块

|  模块   | 描述                                                                            |
|  ----  |-------------------------------------------------------------------------------|
| damuzee-biz-infra  | 基础类公用业务例如：db,redis,rpc,mq，数据库的cofing配置以及通用实现等                                 |
| damuzee-biz-adapter  | 防腐适配层，主要用于公用业务适配|
| damuzee-biz-domain  | 公共业务模块，比如有一个业务可能是很多业务域都会使用的就在这里提供。一般来说场景较少                                    |
| damuzee-biz-sdk  | 对外dubbo的sdk，多业务域都定义在该模块。可以使用模块进行业务sdk区分也可使用包                                  |
| damuzee-biz-spec  | 公用业务域基础包，例如通用的vo,类型转换，枚举，工具类等定义                                               |

## 业务域模块说明

各个业务域自己的业务模块。下表使用module1做结构说明 其它业务域都相同

|  模块   | 描述                                              |
|  ----  |-------------------------------------------------|
| damuzee-biz-module1-api  | 公共域bootstrap-api入口对应的自己的dubbo服务实现               |
| damuzee-biz-module1-web  | 公共域bootstrap-web入口对应自己的contronller业务实现          |
| damuzee-biz-module1-job  | 公共域bootstrap-job入口对应系统的定时任务相关业务实现               |
| damuzee-biz-module1-application  | 各个业务域跨域处理提供组合服务,负责业务的编排和组合                      |
| damuzee-biz-module1-spec  | 子模块独有的基础包，例如通用的vo,类型转换，枚举，工具类等定义                |
| damuzee-biz-module1-infra  | 子模块特殊的一些db,redis,mq,rpc等处理以及对应mapper数据库操作都在该层处理 |
| damuzee-biz-module1-domain  | 领域层，该领域业务逻辑主要存放的地方                              |

# 业务流程

[必选] 列出项目的核心业务流程。

# 技术架构

[可选] 技术架构图

# 外部依赖

[可选] 列出项目所依赖的其他系统，比如重试系统依赖DDC。

| 系统名称 | 上游/下游 | 依赖方式 | 依赖说明 |
|------|-------|------|------|
| x    | x     | x    | x    |

# 项目文档

[可选]列出项目中的需求文档，PRD文档，参考文档等。

| 文档用途 | 文档地址 |
|------|------|
| xxxx | xxxx |

# FAQ

[可选]  列出项目中常见的问题列表。

## FAQ1: xxxx

答：xxxx