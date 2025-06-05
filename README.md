# 功能设计

银行交易管理系统demo。主要逻辑是用户存取款记录。

## API文档：
http://localhost:8080/swagger-ui/index.html。  
## 前端页面：
http://localhost:8080  
页面含 增、删、改、查基本功能。暂无css样式。  
接口采用Ajax异步请求。已引入jquery3.7.1版本。  
***说明： 前端列表页面目前固定查第1页 pageSize为10。 todo 引入分页插件.***

## 接口功能：
* createTransaction 创建一条记录
* deleteTransaction 删除一条记录
* updateTransaction 修改一条记录
* page 分页查询记录

# 技术选型与架构设计

1. 可扩展性
2. 高性能
3. 高可靠性

## 项目模块 采用DDD设计模式
核心分层模块：  
1. controller：提供http 接口
2. application：对外接口抽象实现，服务定义在api，可以对提供rpc服务，调用domain层逻辑。
3. domain：实现具体业务逻辑
4. infrastructure：底层依赖层，数据模块、核心依赖、配置等
5. common：工具类常量等
6. config: 系统配置

## 接口文档
接口文档采用swagger，通过注解方式，生成文档。快速联调。

## 高性能

1. 多线程技术，提高cpu 利用率
2. 减小锁粒度，如对订单id进行加锁，而不是对整个账户加锁。
3. LRU缓存方案，对分页缓存数据，减少数据库查询次数。对相同查询参数进行缓存查询。
 备注：实际项目一般不会对分页数据做缓存，项目中只是为了演示作用

## 高可靠性

springboot体系结构，后期可快速接入springcloud相关技术栈，实现集群提升可靠性。
项目为单机demo，暂不考虑集群问题。

## java依赖 

springboot web基础功能    
lombok ：set get 生成  
jackson：json序列化
springdoc-openapi-starter-webmvc-ui：接口文档生成

## 前端页面 依赖
jquery3.7.1 版本。主要用于异步请求后端接口  

# 测试说明
考虑因素：
1. 功能：增删改查功能是否正常
2. 安全性： 
* 项目暂无身份验证 接口安全暂不考虑。
* 因为数据存在内存中，项目对创建交易最大数量做了限制。防止oom。通过bank.transaction.maxCount配置
3. 性能：压力测试情况

## 功能单元测试
项目对增、删、改、查 功能做了rest接口单元测试。写在TransactionControllerTest中。通过mvn test 执行。

## 功能页面测试

通过页面点击查看功能正常、页面显示正常

## 功能自动化测试

可以构建自动化测试平台，或编写自动化脚本进行测试

1. 接口自动化测试
2. 页面自动化测试
3. 性能自动化测试

## 性能测试

### 性能测试方案：
1. 压测工具压测：可以使用jmeter、gatling等压测工具。
2. 全链路新能测试。

本项目是单机demo不涉及其他服务，暂采用jmeter对接口进行压测 
#### 测试条件 
在本地电脑环境测试：
* 8核16G SSD硬盘
* 500线程 循环10次 即5000个请求，压测接口
* 测试工具：jmeter,jmx目录下的文件为createTransaction接口的压测脚本。（其他几个接口压测结果和这个类似）
* 项目jvm配置 -Xms2048m -Xmx2048m -XX:+UseG1GC
* 项目tomcat线程最大数采用默认200
* 测试结果：
  1. 接口平均响应时间： 8ms左右
  2. 接口吞吐量： 4800/s左右
  3. 接口错误率： 0%
  4. jvm内存正常，线程数正常。因为接口里没有复杂逻辑，且是保持在内存中的，吞吐量比较高
![img.png](pic/img.png)
  
# 上线运维

上线一般需要通过持续集成流水线进行。本demo使用Dockerfile构建镜像，手动运行镜像方式。

1. 先使用 mvn package 打包
2. 然后使用 docker build -t bank-transaction-app:1.0 . 执行镜像
3. 使用 docker run bank-transaction-app:1.0

docker编排可采用k8s，增强服务健壮性，可维护性。  
监控可采用prometheus，grafana。
