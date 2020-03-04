package com.ulfsvel.wallet.eth.entity;


import com.ulfsvel.wallet.common.entity.Wallet;
import org.web3j.protocol.core.methods.response.Transaction;

import javax.persistence.*;

@Entity
public class EthTransaction {

    public static EthTransaction create(Transaction transaction) {
        return new EthTransaction()
                .setHash(transaction.getHash())
                .setNonce(transaction.getNonce().toString())
                .setBlockHash(transaction.getBlockHash())
                .setBlockNumber(transaction.getBlockNumber().toString())
                .setTransactionIndex(transaction.getTransactionIndex().toString())
                .setFrom(transaction.getFrom())
                .setTo(transaction.getTo())
                .setValue(transaction.getValue().toString())
                .setGasPrice(transaction.getGasPrice().toString())
                .setGas(transaction.getGas().toString())
                .setInput(transaction.getInput())
                .setCreates(transaction.getCreates())
                .setPublicKey(transaction.getPublicKey())
                .setRaw(transaction.getRaw())
                .setR(transaction.getR())
                .setS(transaction.getS())
                .setV(transaction.getV());
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    private Wallet wallet;

    private String hash;
    private String nonce;
    private String blockHash;
    private String blockNumber;
    private String transactionIndex;
    private String from;
    private String to;
    private String value;
    private String gasPrice;
    private String gas;
    private String input;
    private String creates;
    private String publicKey;
    private String raw;
    private String r;
    private String s;
    private long v;

    public Long getId() {
        return id;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public EthTransaction setWallet(Wallet wallet) {
        this.wallet = wallet;
        return this;
    }

    public String getHash() {
        return hash;
    }

    public EthTransaction setHash(String hash) {
        this.hash = hash;
        return this;
    }

    public String getNonce() {
        return nonce;
    }

    public EthTransaction setNonce(String nonce) {
        this.nonce = nonce;
        return this;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public EthTransaction setBlockHash(String blockHash) {
        this.blockHash = blockHash;
        return this;
    }

    public String getBlockNumber() {
        return blockNumber;
    }

    public EthTransaction setBlockNumber(String blockNumber) {
        this.blockNumber = blockNumber;
        return this;
    }

    public String getTransactionIndex() {
        return transactionIndex;
    }

    public EthTransaction setTransactionIndex(String transactionIndex) {
        this.transactionIndex = transactionIndex;
        return this;
    }

    public String getFrom() {
        return from;
    }

    public EthTransaction setFrom(String from) {
        this.from = from;
        return this;
    }

    public String getTo() {
        return to;
    }

    public EthTransaction setTo(String to) {
        this.to = to;
        return this;
    }

    public String getValue() {
        return value;
    }

    public EthTransaction setValue(String value) {
        this.value = value;
        return this;
    }

    public String getGasPrice() {
        return gasPrice;
    }

    public EthTransaction setGasPrice(String gasPrice) {
        this.gasPrice = gasPrice;
        return this;
    }

    public String getGas() {
        return gas;
    }

    public EthTransaction setGas(String gas) {
        this.gas = gas;
        return this;
    }

    public String getInput() {
        return input;
    }

    public EthTransaction setInput(String input) {
        this.input = input;
        return this;
    }

    public String getCreates() {
        return creates;
    }

    public EthTransaction setCreates(String creates) {
        this.creates = creates;
        return this;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public EthTransaction setPublicKey(String publicKey) {
        this.publicKey = publicKey;
        return this;
    }

    public String getRaw() {
        return raw;
    }

    public EthTransaction setRaw(String raw) {
        this.raw = raw;
        return this;
    }

    public String getR() {
        return r;
    }

    public EthTransaction setR(String r) {
        this.r = r;
        return this;
    }

    public String getS() {
        return s;
    }

    public EthTransaction setS(String s) {
        this.s = s;
        return this;
    }

    public long getV() {
        return v;
    }

    public EthTransaction setV(long v) {
        this.v = v;
        return this;
    }
}
