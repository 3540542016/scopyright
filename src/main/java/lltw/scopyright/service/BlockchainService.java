package lltw.scopyright.service;

import lltw.scopyright.fisco.CopyrightRegistry;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.client.exceptions.ClientException;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;


import java.io.File;
import java.math.BigInteger;

@Service
@Slf4j
public class BlockchainService {

    private final Client client;
    private final CryptoKeyPair keyPair;
    private final CopyrightRegistry contract;


    public BlockchainService() {
        String configFile = new File("src/main/resources/config-example.toml").getAbsolutePath();
        BcosSDK sdk = BcosSDK.build(configFile);
        this.client = sdk.getClient(1);
        this.keyPair = client.getCryptoSuite().createKeyPair();
        this.contract = deployContract();
    }

    private CopyrightRegistry deployContract() {
        try {
            return CopyrightRegistry.deploy(client, keyPair, "0x7cffcbeeb6628add0fe8d3bc6a809040d562318f");
        } catch (ClientException | ContractException e) {
            throw new RuntimeException("Failed to deploy contract", e);
        }
    }


    public TransactionReceipt registerWork(String title, String description) {
        return callContractFunction(() -> contract.registerWork(title, description));
    }

    public TransactionReceipt reviewWork(Long workId, boolean approve , String copyright) {
        return callContractFunction(() -> contract.reviewWork(BigInteger.valueOf(workId), approve,copyright));
    }



    private TransactionReceipt callContractFunction(ContractFunctionCall call) {
        try {
            TransactionReceipt receipt = call.execute();
            System.out.println("Transaction hash: " + receipt.getTransactionHash());
            return receipt;
        } catch (ContractException e) {
            e.printStackTrace();
            return null;
        }
    }

    @FunctionalInterface
    private interface ContractFunctionCall {
        TransactionReceipt execute() throws ContractException;
    }
}
