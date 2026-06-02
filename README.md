# Linkyou Server

Linkyou 的后端服务端仓库，采用 Maven 多模块 + Spring 生态，包含配置中心、身份服务、消息服务、聚合服务及公共模块。

## 项目结构

```text
server/
├── pom.xml                  # 根聚合 POM
├── docker-compose.yaml      # 本地完整依赖编排
├── docker-compose.dev.yaml  # 开发环境简化编排
├── bundle/                  # bundle 服务
├── config/                  # 配置中心服务
├── framework/               # 框架基础能力
├── identity/                # 身份认证服务
├── message/                 # 消息服务
└── shared/                  # 公共代码模块
```

## 技术栈

- Java 25
- Spring Boot 4.0.6
- Spring Cloud 2025.1.1
- Spring Cloud Alibaba 2025.1.0.0
- Maven 多模块构建
- PostgreSQL / Redis / MongoDB / RabbitMQ

> 版本来源：根 `pom.xml` 当前配置。

## 模块说明

- `config`：统一配置管理服务。
- `identity`：身份认证与授权相关业务。
- `message`：消息能力相关业务。
- `bundle`：聚合编排或对外统一服务入口。
- `framework`：基础框架和通用能力封装。
- `shared`：跨模块复用的公共代码。

## 本地开发前置条件

建议先准备以下环境：

- JDK 25
- Maven 3.9+
- Docker 与 Docker Compose（用于一键启动依赖）

## 快速开始

### 1) 构建全部模块

```bash
cd /Users/rong/Code/Github/Linkyou/server
mvn clean install
```

### 2) 仅运行测试

```bash
cd /Users/rong/Code/Github/Linkyou/server
mvn test
```

### 3) 启动本地依赖（Docker Compose）

```bash
cd /Users/rong/Code/Github/Linkyou/server
docker compose -f docker-compose.yaml up -d
```

### 4) 停止本地依赖

```bash
cd /Users/rong/Code/Github/Linkyou/server
docker compose -f docker-compose.yaml down
```

## 常用模块命令

在根目录执行（示例以 `identity` 模块为例）：

```bash
cd /Users/rong/Code/Github/Linkyou/server
mvn -pl identity -am clean package
```

## 端口与依赖（基于 compose 文件）

- 配置中心：`8900`
- 身份服务：`8901`
- 消息服务：`8902`
- 聚合服务：`8903`
- PostgreSQL：`5432`
- Redis：`6379`
- MongoDB：`27017`
- RabbitMQ：`5672`（管理端口见 `docker-compose.yaml`）

## 配置说明

- 示例环境变量可参考 `docker-compose.yaml` 与 `docker-compose.dev.yaml`。
- 生产环境请务必替换默认账号密码与第三方 OAuth 密钥。
- 建议通过外部配置中心或安全密钥管理系统注入敏感配置。

## 许可证

本项目使用仓库根目录 `LICENSE` 中声明的许可证。
