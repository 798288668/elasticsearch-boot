# elasticsearch-boot

基于Spring Boot + elasticsearch 搭建的全文搜索示例

### 效果如下：

![image](https://github.com/lufengc/elasticsearch-boot/blob/master/screenshots/a.jpeg)

### 安装elasticsearch
```
$ wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-5.5.1.zip
$ unzip elasticsearch-5.5.1.zip
$ cd elasticsearch-5.5.1/
$ ./bin/elasticsearch
```

```
$ curl localhost:9200

{
  "name" : "atntrTf",
  "cluster_name" : "elasticsearch",
  "cluster_uuid" : "tf9250XhQ6ee4h7YI11anA",
  "version" : {
    "number" : "5.5.1",
    "build_hash" : "19c13d0",
    "build_date" : "2017-07-18T20:44:24.823Z",
    "build_snapshot" : false,
    "lucene_version" : "6.6.0"
  },
  "tagline" : "You Know, for Search"
}
```  

> 默认情况下，Elastic 只允许本机访问，如果需要远程访问，可以修改 Elastic 安装目录的config/elasticsearch.yml文件，
去掉network.host的注释，将它的值改成0.0.0.0，然后重新启动 Elastic。线上服务要设成具体的 IP。
```
network.host: 0.0.0.0
```

> 安装中文分词插件。这里使用的是 ik，也可以考虑其他插件（比如 smartcn）
```
$ ./bin/elasticsearch-plugin install https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v5.5.1/elasticsearch-analysis-ik-5.5.1.zip
```
