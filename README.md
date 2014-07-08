zhihu2kindle
============

知乎问答转kindle mobi 电子书, 并推送kindle

使用方式：在bin目录下，执行zhihu2kindle "知乎问答url(如http://www.zhihu.com/question/19616022)"

配置：
位置：conf/app.properties
- kindlegen：kindlegen的目录，本工具使用amazon官方的kindlegen，需要将其下载，可以直接放在bin目录，或者通过这里配置
- localPath：可以配置生成的mobi的路径
- email：配置知乎的用户名。由于知乎未登录的话，不能完全的内容，通过配置用户名和密码，可以获取全部内容
- password：知乎密码
- send.to.kinle：是否自动推送到kindle，默认否
- send.email：如果开启推送，需要指定推送的邮箱。这个需要在amazon中配置才能使用
- send.password：推送密码
- send.email.smtp：smtp地址
- send.email.smtp.port：smtp端口
- send.email.ssl：smtp是否ssl加密
- kindle.addr：要推送的kindle地址，一般是xxx@kindle.com


自行编译：
```sh
git clone https://github.com/linq/zhihu2kindle.git
cd zhihu2kindle
mvn package appassembler:assemble
```

下载：http://pan.baidu.com/s/1c0zTg32
