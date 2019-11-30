# canal项目介绍和启动配置

<a name="zZLph"></a>
## 项目介绍
canal是阿里巴巴的一个开源MySQL数据库binlog服务监听中间件，可以监听数据库变更，并发送变更信息<br />项目根据binlog变更，实现MySQL到其他数据源的同步等。

<a name="XYnp1"></a>
## 依赖服务
本项目依赖服务

- jdk 1.8
- mysql 5.7
- canal 1.1.4
<a name="esMrj"></a>
## MySQL设置
<a name="C0ar5"></a>
#### 1：配置binlog开启日志收集
由于是MySQL5.7，没有网上说的cnf配置。直接在MySQL安装目录<br />默认在：C:\ProgramData\MySQL\MySQL Server 5.7  -->my.ini设置如下，打开binlog

```xml
[mysqld]
log-bin=mysql-bin #添加这一行就ok
binlog-format=ROW #选择row模式
server_id=1 
```
可以通过命令查看看是否成功：show variables like ‘bin_log%’;<br />log_bin为ON即可
<a name="Dd6Fa"></a>
#### 2：创建子用户，赋予相关权限，用于读取 MySQL Binlog 日志

```sql
CREATE USER canal IDENTIFIED BY 'canal';  
GRANT SELECT, SHOW VIEW, REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'canal'@'%';
-- GRANT ALL PRIVILEGES ON *.* TO 'canal'@'%' ; 需要具有SHOW VIEW 权限
FLUSH PRIVILEGES;
```

<a name="4pG2S"></a>
## 项目启动
<a name="xarCh"></a>
#### 1：下载canal，修改canal目录下  conf/example/instance.properties

```xml
canal.instance.master.address = 127.0.0.1:3306
canal.instance.dbUsername = canal
canal.instance.dbPassword = canal
canal.instance.defaultDatabaseName = test # 选择对 test 数据库进行监控
canal.instance.filter.regex = .\..  # 正则匹配需要监控的表
```

<a name="nXbgO"></a>
#### 2:运行canal bin目录下startup.bat,linux系统运行startup.sh

到此，中间件服务启动成功，数据库权限开启，只需要运行项目，监听日志即可<br />项目中 com.example.canalkafkademo.SimpleClient.java，main启动
<a name="dr97i"></a>
## 测试
<a name="mhMsG"></a>
#### 1：数据库新增id=1的记录
![image.png](https://cdn.nlark.com/yuque/0/2019/png/181210/1575130038073-a64cad1b-5cd4-4d77-8e80-6ff43f9b95d9.png#align=left&display=inline&height=83&name=image.png&originHeight=165&originWidth=425&size=10219&status=done&style=none&width=212.5)
<a name="uVeP7"></a>
#### 2：控制台日志可以看到变更记录
![image.png](https://cdn.nlark.com/yuque/0/2019/png/181210/1575130053996-82171d43-ffaa-4f17-a523-a00a02533c49.png#align=left&display=inline&height=71&name=image.png&originHeight=141&originWidth=843&size=14469&status=done&style=none&width=421.5)


<a name="P17aF"></a>
## 后记

- 通过canal中间件，可以看到canal伪装为slave像主库发送请求，根据master发送过来的binlog,自己解析log，并把变更事件发送出来，我们可以根据他送出来的rowData，像其他数据源同步数据。
- 主要流程 
  - 配置MySQL-->启动服务-->启动项目-->监听事件-->事件处理；
  - 具体可以查看看官网文档：[https://github.com/alibaba/canal/wiki/QuickStart](https://github.com/alibaba/canal/wiki/QuickStart)
  - 有没有很简单，亲手操作一下把！
  - //TODO 本项目只接了canal，没有kafka，后续陆续加上

<br />
