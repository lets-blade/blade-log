# blade-log

这是一个简单的日志实现，可能是一个简易版的 `logback`。

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
    <groupId>com.bladejava</groupId>
    <artifactId>blade-log</artifactId>
    <version>0.1.5</version>
</dependency>
```

## 配置

```bash
com.blade.logger.rootLevel=INFO
com.blade.logger.dir=./logs
com.blade.logger.name=app
```

