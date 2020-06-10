package com.ulfsvel.wallet.btc.entity;

public class ImportPublicKey {

    String pubkey;

    String label;

    Boolean rescan;

    public ImportPublicKey() {
        rescan = Boolean.FALSE;
    }

    public String getPubkey() {
        return pubkey;
    }

    public ImportPublicKey setPubkey(String pubkey) {
        this.pubkey = pubkey;
        return this;
    }

    public String getLabel() {
        return label;
    }

    public ImportPublicKey setLabel(String label) {
        this.label = label;
        return this;
    }

    public Boolean getRescan() {
        return rescan;
    }

    public ImportPublicKey setRescan(Boolean rescan) {
        this.rescan = rescan;
        return this;
    }
}
