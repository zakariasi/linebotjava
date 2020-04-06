package com.dicoding.linebotjava;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "meninggal",
        "sembuh",
        "perawatan",
        "jumlahKasus"
})
public class CovidEvents {

    @JsonProperty("meninggal")
    private int meninggal;

    @JsonProperty("sembuh")
    private int sembuh;



    @JsonProperty("perawatan")
    public int perawatan;

    @JsonProperty("jumlahKasus")
    public int jumlahKasus;




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


}