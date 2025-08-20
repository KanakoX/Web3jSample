package com.kanako;

import com.kanako.contract.MyToken;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import java.math.BigInteger;

public class DeployContract {

    public static void main(String[] args) throws Exception {
        // 连接至币安测试网节点
        Web3j web3j = Web3j.build(new HttpService("https://bsc-testnet.publicnode.com"));
        String clientVersion = web3j.web3ClientVersion().send().getWeb3ClientVersion();
        System.out.println("Client version: " + clientVersion);
        // 读取环境变量中的私钥
        String privateKey = System.getenv("BSC_TESTNET_PRIVATE_KEY_KKT");
        // 定义 BSC 测试网的链 ID
        long BSC_TESTNET_CHAIN_ID = 97L;
        // 加载私钥
        Credentials credentials = Credentials.create(privateKey);
        // 创建使用指定了链 ID 的交易管理器
        // 解决 EIP-155 错误
        TransactionManager transactionManager = new RawTransactionManager(
                web3j,
                credentials,
                BSC_TESTNET_CHAIN_ID // 传入 BSC 测试网的链 ID
        );

        // 合约部署
        MyToken contract = MyToken.deploy(
                web3j,
                transactionManager,
                new DefaultGasProvider(),
                "KnkToken", "KKT", BigInteger.valueOf(18)
        ).send();

        String contractAddress = contract.getContractAddress();
        System.out.println("Smart contract deployed at address: " + contractAddress);
    }
}