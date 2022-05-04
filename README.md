# blade-log

这是一个简单的日志实现，可能是一个简易版的 `logback`。

<a href="http://search.maven.org/#search%7Cga%7C1%7Cblade-log"><img src="https://img.shields.io/maven-central/v/com.hellokaton/blade-log.svg?style=flat-square"></a>

## 特性

- 格式化日志输出
- 彩色日志打印
- 输出日志到文件
- 多种日志级别
- 不依赖第三方库
- 按文件大小切割

## 使用

加入依赖

```xml
<dependency>
    <groupId>com.hellokaton</groupId>
    <artifactId>blade-log</artifactId>
    <version>0.2.0</version>
</dependency>
```

## 配置

```bash
logger.name=app
logger.root-level=INFO
logger.dir=./logs
```

