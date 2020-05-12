# volatile语义分析
1. `可见性`：对共享变量的修改，其他线程马上可以感知到
2. `原子性`：不能保证
3. `有序性`：
   1. 重排序（编译阶段、指令优化阶段）时，volatile修饰变量之前的代码，不能调整到该变量的后面
   2. 重排序（编译阶段、指令优化阶段）时，volatile修饰变量之后的代码，不能调整到该变量的前面
   3. as-if-seria
   4. happens-before
   
# volatile在JMM及硬件内存架构上的体现
1. 缓存行 (`Cache Line`) 便是 CPU Cache 中的最小单位，CPU Cache 由若干缓存行组成，一个缓存行的大小通常是 64 字节(这取决于 CPU)，
   并且它有效地引用主内存中的一块地址。一个 Java 的 long 类型是 8 字节，因此在一个缓存行中可以存 8 个 long 类型的变量。
2. 一个线程修改volatile修饰的变量后，其他线程工作内存中对应的变量副本会立刻被置为无效（即`Cache Line`被置为无效），如果其他线程
   需要获取该变量，只能从主内存中重新读取，从而保证了`可见性`

# volatile与synchronized的区别
1. `使用`：volatile只能修饰变量，synchronized只能修饰方法和语句块
2. `原子性`：synchronized可以保证原子性，volatile不能保证原子性
3. `可见性`：都可以保证可见性，但实现原理不同
   1. volatile对变量加了lock（汇编）
   2. synchronized使用monitorEnter和monitorexit
4. `有序性`：volatile能保证有序，synchronized可以保证有序性，但是代价（重量级）并发退化到串行
5. `其他`：synchronized会引起阻塞，volatile不会引起阻塞

   

