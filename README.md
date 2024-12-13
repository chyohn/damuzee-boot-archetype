# 项目简介

通过该脚手架可以生成具有领域驱动设计思想的spring boot2项目。

# 模块介绍
|模块| 描述                         |
|-|----------------------------|
|damuzee-bizcommon-archetype| 多模块工程公共模块脚手架               |
|damuzee-bizassign-archetype| 多模块工程业务模块脚手架               |
|damuzee-single-archetype| 单体工程脚手架                    |
|damuzee-boot-parent | BOM管理所有maven依赖             |
|damuzee-boot-spec| 公共规范实现：json、多租户、交互规范、错误码规范 |

# build

```shell
mvn clean install -Dmaven.test.skip=true
```

# 脚手架使用
## 创建多业务模块工程
分为2步骤：
1. 使用damuzee-bizcommon-archetype脚手架生成公共模块, 命令：
```shell
mvn archetype:generate \
-DgroupId=你的组名如com.damuzee \
-DartifactId=你的模块名如biz-parent \
-Dversion=你的项目版本如1.0.0-SNAPSHOT \
-DarchetypeGroupId=com.damuzee \
-DarchetypeArtifactId=damuzee-bizcommon-archetype \
-DarchetypeVersion=2.0.0-SNAPSHOT 
```

2. 在生成的公共模块目录下使用damuzee-bizassign-archetype脚手架生成各个业务模块，命令如下：
```shell
mvn archetype:generate \
-DgroupId=你的组名如com.damuzee \
-DartifactId=你的模块名如biz-order \
-Dversion=你的项目版本如1.0.0-SNAPSHOT \
-DarchetypeGroupId=com.damuzee \
-DarchetypeArtifactId=damuzee-bizassign-archetype \
-DarchetypeVersion=2.0.0-SNAPSHOT \
-DmainModule=你的父模块名如biz-parent \
-DmainModuleVersion=你的父模块版本如1.0.0-SNAPSHOT \
-DmainModulePackage=你的父模块组名com.damuzee
```

## 创建单体工程
直接使用damuzee-single-archetype脚手架生成工程
```shell
mvn archetype:generate \
-DgroupId=你的组名如com.damuzee \
-DartifactId=你的模块名如biz-single \
-Dversion=你的项目版本如1.0.0-SNAPSHOT \
-DarchetypeGroupId=com.damuzee \
-DarchetypeArtifactId=damuzee-single-archetype \
-DarchetypeVersion=2.0.0-SNAPSHOT 
```