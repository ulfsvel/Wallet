package com.ulfsvel.wallet.btc.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CreateTransaction {

    private final static String DATA_KEY = "data";
    private List<UnspentTxForFounding> inputList;
    private Map<String, String> outputList;
    private Long lockTime;
    private Boolean replaceable;

    public CreateTransaction() {
        outputList = new TreeMap<>();
        inputList = new LinkedList<>();
        lockTime = 0L;
        replaceable = Boolean.FALSE;
    }

    @JsonProperty("inputs")
    public List<UnspentTxForFounding> getInputList() {
        return inputList;
    }

    public CreateTransaction setInputList(List<UnspentTxForFounding> inputList) {
        this.inputList = inputList;
        return this;
    }

    @JsonProperty("outputs")
    public Map<String, String> getOutputList() {
        return outputList;
    }

    public CreateTransaction setOutputList(Map<String, String> outputList) {
        this.outputList = outputList;
        return this;
    }

    public CreateTransaction putOutput(String address, String amount) {
        this.outputList.put(address, amount);
        return this;
    }

    public CreateTransaction putInput(UnspentTxForFounding unspentTxForFounding) {
        this.inputList.add(unspentTxForFounding);
        return this;
    }

    public CreateTransaction setOutputData(String outputData) {
        this.outputList.put(DATA_KEY, outputData);
        return this;
    }

    @JsonProperty("locktime")
    public Long getLockTime() {
        return lockTime;
    }

    public CreateTransaction setLockTime(Long lockTime) {
        this.lockTime = lockTime;
        return this;
    }

    @JsonProperty("replaceable")
    public Boolean getReplaceable() {
        return replaceable;
    }

    public CreateTransaction setReplaceable(Boolean replaceable) {
        this.replaceable = replaceable;
        return this;
    }
}
