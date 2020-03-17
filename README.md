# Logstash Java Plugin Filter WKT2GeoJSON

[地址](https://github.com/mailxcl/logstash-filter-java-wkt2geojson)

WKT2GeoJSON是一个[Logstash](https://github.com/elastic/logstash)过滤器插件。

Logstash Java plugins文档在[这里](https://www.elastic.co/guide/en/logstash/current/contributing-java-plugin.html)。

###安装 PowerShell
````
./bin/logstash-plugin install file:///D:/logstash-filter-wkt2geojson-7.0.1.zip
````
###查看是否安装 PowerShell
```
./bin/logstash-plugin list
```
###配置方法
```` json
filter {
  wkt2geojson {
    source => "geometry_field_name"
    target => "geometry_field_name"
  }
}
````
###卸载 PowerShell
````
./bin/logstash-plugin remove logstash-filter-wkt2geojson
````
