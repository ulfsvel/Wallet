package com.ulfsvel.wallet.btc.entity;

import java.util.LinkedList;
import java.util.List;

public class GetBalance {

    private final List<String> addresses;

    private Integer minconf;

    private Integer maxconf;

    public GetBalance() {
        addresses = new LinkedList<>();
        maxconf = 9999999;
    }

    public Integer getMaxconf() {
        return maxconf;
    }

    public GetBalance setMaxconf(Integer maxconf) {
        this.maxconf = maxconf;
        return this;
    }

    public List<String> getAddresses() {
        return addresses;
    }

    public GetBalance addAddresses(String addresses) {
        this.addresses.add(addresses);
        return this;
    }

    public Integer getMinconf() {
        return minconf;
    }

    public GetBalance setMinconf(Integer minconf) {
        this.minconf = minconf;
        return this;
    }
}
