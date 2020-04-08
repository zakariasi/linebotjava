package com.dicoding.linebotjava;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "fid",
        "kodeProvi",
        "kasusSemb",
        "kasusMeni"
})



public class Datum {

    @JsonProperty("fid")
    private int fid;

    @JsonProperty("kodeProvi")
    private int kodeProvi;

    @JsonProperty("kasusSemb")
    private int kasusSemb;

    @JsonProperty("kasusMeni")
    private int kasusMeni;

    @JsonProperty("fid")
    public int getFid() {
        return fid;
    }

    @JsonProperty("fid")
    public void setFid(int fid) {
        this.fid = fid;
    }

    @JsonProperty("kodeProvi")
    public int getKodeProvi() {
        return kodeProvi;
    }

    @JsonProperty("kodeProvi")
    public void setKodeProvi(int kodeProvi) {
        this.kodeProvi = kodeProvi;
    }

    @JsonProperty("kasusSemb")
    public int getKasusSemb() {
        return kasusSemb;
    }

    @JsonProperty("kasusSemb")
    public void setKasusSemb(int kasusSemb) {
        this.kasusSemb = kasusSemb;
    }

    @JsonProperty("kasusMeni")
    public int getKasusMeni() {
        return kasusMeni;
    }

    @JsonProperty("kasusMeni")
    public void setKasusMeni(int kasusMeni) {
        this.kasusMeni = kasusMeni;
    }




}
