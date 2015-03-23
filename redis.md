#安装redis


访问主页 http://redis.io/

找到Download it下的稳定版本下载链接 http://download.redis.io/releases/redis-2.8.19.tar.gz

终端执行：下载、解压、编译
```
$curl -o redis-2.8.19.tar.gz http://download.redis.io/releases/redis-2.8.19.tar.gz
# wget http://download.redis.io/releases/redis-2.8.19.tar.gz
$ tar xzf redis-2.8.19.tar.gz
$ cd redis-2.8.19
$ make

```

编译好的文件位置
```
$ src/redis-server
```

用内置的客户端工具操作：

```
$ src/redis-cli
redis> set foo bar
OK
redis> get foo
"bar"
```

在线命令操作地址：

http://try.redis.io/

