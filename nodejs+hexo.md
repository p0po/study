# 安装nodejs

* [官网](https://nodejs.org/download/)下载 `tar.gz的64位版本`

* 拷贝 `node-v0.12.2.tar.gz` 到指定目录

* 执行 `tar -xvf node-v0.12.2.tar.gz

* 环境变量
  * export NODE_HOME=/app/node-v0.12.2-darwin-x64
  * export PATH=$NODE_HOME/bin:$PATH

* 执行 `node -v` 结果为版本号即成功 `v0.12.2`

# 安装HEXO

  * npm install hexo-cli -g
  * hexo init blog
  * cd blog
  * npm install
  * hexo server