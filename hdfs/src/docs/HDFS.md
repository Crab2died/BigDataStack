#                                               HDFS

## Name Node
   HDFS文件系统的管理节点，维护文件系统的目录树，维护文件/目录的元数据及文件对应数据块列表;接受用户操作请求;接收各DataNode的汇报信息。
### 维护的文件
   - fsimage: 元数据(metadata)镜像文件。存储某段时间NameNode内存元数据信息
   - edits: 操作日志文件
   - fstime: 最近一次checkpoint时间
   
### 存储元数据
   - 存储文件信息：名字，MD5，权限
   - 副本数，
   - block块信息，以及存储在DataNode的位置信息(host)


## Data Node
###
