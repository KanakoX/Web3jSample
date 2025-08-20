package com.kanako;

import com.kanako.contract.MyToken;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;

import java.io.IOException;
import java.math.BigInteger;

public class LoadContract {
    private static final String contractAddress = "0x50198C7968f0BE10F0C83aDa7015406250aEc691";
    private static final String accountAddress = "0xA0876289973bdFcC08FdE0A2c03a2098A97037f7";

    public static void main(String[] args) throws Exception {
        // 连接至币安测试网节点
        Web3j web3j = Web3j.build(new HttpService("https://bsc-testnet.publicnode.com"));
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

        // 加载合约
        MyToken contract = MyToken.load(
                contractAddress,
                web3j,
                transactionManager,
                new DefaultGasProvider());
        BigInteger retval = contract.balanceOf(accountAddress).send();
        System.out.println(retval);
    }
}
