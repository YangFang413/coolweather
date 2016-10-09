# CoolWeather
best practice of android.

####该应用实现的功能  
1. 通过中国天气网的API罗列出全国所有的省、市、县的列表  
2. 通过新浪天气API提供的天气数据实现查看全国任何地方（省市县）的天气信息（可自由切换城市）  
3. 提供手动更新及后台自动更新的功能  

####细节部分  
1. 数据库中包含3个表：Province, City, County，每个表对应一个model  
2. 新浪API提供的数据是XML格式的数据，采用PULL解析方式进行解析，解析完之后用haredPreference进行存储  

APP还能继续优化。 

####附API地址  
*新浪天气API：http://php.weather.sina.com.cn/xml.php?city=%D6%D8%C7%EC&password=DJOYnieT8234jlsK&day=0*  
city后面的一串代码是由汉字变成GB2312格式的编码形式（除了此处，URL的其余内容是不需要变更的），只需要将中国天气网查询到的省市县的汉字进行转换后替换进去方可。    

*中国天气网API：http://www.weather.com.cn/data/list3/city.xml*  
（目前已经开始收费了。只有查询省市县的还能用）
把该页查询到的省份保存下来，并且使用汉字前面的省级代号去查询该省的市，如北京*http://www.weather.com.cn/data/list3/city01.xml*  
再用相同的方式去查询县。
