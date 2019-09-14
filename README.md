# keep-younger-admin
a admin system for keep-younger

*** 

## IDEA 生成模板文件方法（Mapper.xml）举例

`File` -- `Settings` -- `Editor` -- `File and Code Templates` -- \
面板中选择：`+`(添加) -- `name` 填写：`mapper`， `extension` 填写 `xml`， \
内容填写：

    <?xml version="1.0" encoding="UTF-8" ?>
    <!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
            "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
    <mapper namespace="">
    
    </mapper>
保存 \
新建文件选项便会有`mapper`这个选项，直接可创建`Mapper.xml`文件

*** 

## IDEA 设置去除mapper.xml文件中SQL语句警告

`File` -- `Settings` -- `Editor` -- `Inspections` -- `SQL ` -- \
找到其中的 `No data sources configure` 、 \
`SQL dialect datection` 和 `Unresolved reference` 选项 \
去掉勾选，保存。\
黄色警告解除，但会有淡绿色底色，有洁癖的可以找： \
`settings` -- `Editor` -- `Color Scheme` -- `General` \
面板中的 `Folded text with highlighting`，将右边的`Background`勾选去掉。（我试的绿色去不掉。。）
