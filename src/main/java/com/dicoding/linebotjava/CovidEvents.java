package com.dicoding.linebotjava;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "data",
        "meninggal",
        "sembuh",
        "perawatan",
        "jumlahKasus",
        "name",
        "value"

})
public class CovidEvents {


    @JsonProperty("meninggal")
    public int getMeninggal() {
        return meninggal;
    }
    @JsonProperty("meninggal")
    public void setMeninggal(int meninggal) {
        this.meninggal = meninggal;
    }
    @JsonProperty("sembuh")
    public int getSembuh() {
        return sembuh;
    }
    @JsonProperty("sembuh")
    public void setSembuh(int sembuh) {
        this.sembuh = sembuh;
    }
    @JsonProperty("perawatan")
    public int getPerawatan() {
        return perawatan;
    }
    @JsonProperty("perawatan")
    public void setPerawatan(int perawatan) {
        this.perawatan = perawatan;
    }
    @JsonProperty("jumlahKasus")
    public int getJumlahKasus() {
        return jumlahKasus;
    }
    @JsonProperty("jumlahKasus")
    public void setJumlahKasus(int jumlahKasus) {
        this.jumlahKasus = jumlahKasus;
    }

    @JsonProperty("perawatan")
    private int perawatan;
    @JsonProperty("jumlahKasus")
    private int jumlahKasus;
    @JsonProperty("meninggal")
    private int meninggal;
    @JsonProperty("sembuh")
    private int sembuh;


    @JsonProperty("data")
    private List<Datum> data = null;

//    @JsonProperty("kec")
//    private List<Datum> kec = null;
//
//    @JsonProperty("kec")
//    public List<Datum> getKec() {
//        return kec;
//    }
//
//    @JsonProperty("kec")
//    public void setKec(List<Datum> kec) {
//        this.kec = kec;
//    }

//    @JsonProperty("kec")
//    public List<Datum> getKec() {
//    return kec;
//}
//    @JsonProperty("kec")
//    public void setKec(List<Datum> kec) {
//        this.kec = kec;
//    }
//
//    @JsonProperty("kec")
//    private List<Datum> kec;


    @JsonProperty("data")
    public List<Datum> getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(List<Datum> data) {
        this.data = data;
    }




}