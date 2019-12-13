#### FileZillaServer用户批量创建工具
使用JavaFX开发的桌面小工具，适用于批量创建大量的FilezillaServer FTP账户的场景    
程序主界面:    
![main](https://raw.githubusercontent.com/Staroon/FileZillaServerUserCreater/master/img/main.png)

#### 运行方式
运行环境要求：java 1.8+    
前往[releases页面](https://github.com/Staroon/FileZillaServerUserCreater/releases)下载最新的已编译版本，解压，目录结构：    
```
│  startup-debug.bat
│  startup.bat
│
├─launcher
│      FileZillaServerUserCreater.jar
│
└─tools
        FileZilla_Server_cn-0_9_46.exe
        FTP信息配置表_模板.xlsx
```
其中tools目录下放置了FileZilla_Server 0.9.46中文汉化版本的安装包和账号信息配置表的EXCEL模板，需要配置文件夹名称、账号、密码、文件夹分组四项。    
![conf](https://raw.githubusercontent.com/Staroon/FileZillaServerUserCreater/master/img/conf.png)    
双击startup.bat或者startup-debug.bat文件即可启动工具。

#### 使用说明
- 指定FTP根目录（不能指定到磁盘根目录）    
  该工具会自动在所指定的FTP根目录下按照 `文件夹分组/文件夹名称/ `的结构创建每个账号的主目录，选择完FTP根目录后可通过工具菜单快速打开该目录。
- 选择账号配置表    
  选择修改后的EXCEL配置表模板即可。
- 点击生成配置文件    
  上述两项都选择完成以后点击`点击生成配置文件`按钮即可快速生成FileZilla Server的账号配置文件到桌面，同时会显示所处理的账号个数。    
  ![success](https://raw.githubusercontent.com/Staroon/FileZillaServerUserCreater/master/img/success.png)    
  可进入FTP根目录查看文件夹创建情况：    
  ![tree](https://raw.githubusercontent.com/Staroon/FileZillaServerUserCreater/master/img/tree.png)   
- 替换FileZilla Server安装目录下的`FileZilla Server.xml`文件
  如果使用tools目录下的安装包安装的并且将FileZilla Server安装到了默认目录的话，点击工具左上角的`打开FileZilla安装目录`按钮可快速进入到FileZilla Server安装目录，复制桌面上生成的`FileZilla Server.xml`覆盖掉FileZilla安装目录下的同名文件（谨慎操作，注意备份，会覆盖掉已有的账号配置）。
- 进入服务管理器重启FileZilla Server服务即可加载最新的账号配置。

#### 其他事项
- 当前版本不支持自定义账号的权限，默认具有权限：    
  - 文件下载
  - 文件上传
  - 文件编辑
  - 创建子目录
  - 查看文件列表
  - 查看子目录文件
- 不具备权限：    
  - 文件删除
  - 目录删除






