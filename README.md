# Web3j简易实现合约的部署

## 所需依赖
``` xml
<dependency>
    <groupId>org.web3j</groupId>
    <artifactId>core</artifactId>
    <version>4.14.0</version>
</dependency>
```

## 所需插件   
###### 合约编译文件(.json) 转 Java类  
<a href = "https://github.com/LFDT-web3j/web3j-maven-plugin">web3j-maven-plugin [Github]</a>
``` xml
<plugin>
    <groupId>org.web3j</groupId>
    <artifactId>web3j-maven-plugin</artifactId>
    <version>4.14.0</version>
    <configuration>
        <packageName>com.kanako</packageName>
        <sourceDestination>src/main/java/generated</sourceDestination>
        <nativeJavaType>true</nativeJavaType>
        <outputFormat>java,bin</outputFormat>
        <soliditySourceFiles>
            <directory>src/main/resources</directory>
            <includes>
                <include>**/*.sol</include>
            </includes>
        </soliditySourceFiles>
        <abiSourceFiles>
            <directory>src/main/resources</directory>
            <includes>
                <include>**/*.json</include>
            </includes>
        </abiSourceFiles>
        <outputDirectory>
            <java>src/java/generated</java>
            <bin>src/bin/generated</bin>
            <abi>src/abi/generated</abi>
        </outputDirectory>
        <contract>
            <includes>
                <include>greeter</include>
            </includes>
            <excludes>
                <exclude>mortal</exclude>
            </excludes>
        </contract>
        <pathPrefixes>
            <pathPrefix>dep=../dependencies</pathPrefix>
        </pathPrefixes>
    </configuration>
</plugin>
```

## 实现流程
连接到节点
``` java
Web3j web3j = Web3j.build(new HttpService(NODE-URL));
```
加载私钥
``` java
Credentials credentials = Credentials.create(privateKey);
```
部分测试网需要链 ID 信息，使用 `TransactionManager` 封装私钥和链 ID
``` java
TransactionManager transactionManager = new RawTransactionManager(
        web3j,
        credentials,
        BSC_TESTNET_CHAIN_ID  // 传入 BSC 测试网的链 ID
);
```
部署、加载合约
``` java
// 合约部署
MyToken contract = MyToken.deploy(
        web3j,
        transactionManager,
        new DefaultGasProvider(),
        "KnkToken", "KKT", BigInteger.valueOf(18)
).send();

// 合约加载
MyToken contract = MyToken.load(
        contractAddress,
        web3j,
        transactionManager,
        new DefaultGasProvider()
);
```