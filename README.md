# math

实现数据运算的库
- 组合运算

  - DimCombinationMaker
    按照维度组合，比如：有3款衣服，每款都有2中颜色，枚举所有衣服的型号。
    ```java
    List<DimCombineDetail<String, String>> details = DimCombinationMaker.<String, String>builder()
                    .dimension("款式", "长款", "短款")
                    .dimension("颜色", "红", "绿")
                    .build()
                    .generateDetail()
                    .collect(Collectors.toList());
    details.get(0).getNormalResult(); // 获取第一个组合结果
    ```
  - GroupCombinationMaker
  - GroupMaker


- 排列运算

- 树型枚举

