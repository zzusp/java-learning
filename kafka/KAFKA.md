# kafka


## Topic
* topic
    * partition
        * segment（Partition 全局的第一个 Segment 从 0 开始，后续每个 Segment 文件名为上一个 Segment 文件最后一条消息的 offset 值。数值最大为 64 位 long 大小，19 位数字字符长度，没有数字用0填充。如 00000000000000368769.index 和 00000000000000368769.log。）
            * index file(.index)
                
            * data file(.log)
                * offset
                * MessageSize
                * data
