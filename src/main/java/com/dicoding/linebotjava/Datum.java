package com.dicoding.linebotjava;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "judul",
        "sumber",
        "tanggal_update",
        "total_odp",
        "total_pdp",
        "total_positif",
        "total_meninggal_positif",
        "total_meninggal_pdp",
        "total_sembuh",
        "kec",
        "nama_kecamatan",
        "odp",
        "pdp",
        "positif",
        "meninggal_positif",
        "meninggal_pdp",
        "sembuh",
        "fid",
        "kodeProvi",
        "provinsi",
        "kasusPosi",
        "kasusSemb",
        "kasusMeni"
})



public class Datum {

    @JsonProperty("judul")
    private String judul;

    @JsonProperty("sumber")
    private String sumber;

    @JsonProperty("tanggal_update")
    private String tanggal_update;

    @JsonProperty("total_odp")
    private int total_odp;

    @JsonProperty("total_pdp")
    private int total_pdp;

    @JsonProperty("total_positif")
    private int total_positif;

    @JsonProperty("total_meninggal_positif")
    private int total_meninggal_positif;

    @JsonProperty("total_meninggal_pdp")
    private int total_meninggal_pdp;

    @JsonProperty("total_sembuh")
    private int total_sembuh;

    @JsonProperty("kec")
    public List<Datum> getKec() {
        return kec;
    }
    @JsonProperty("kec")
    public void setKec(List<Datum> kec) {
        this.kec = kec;
    }

    @JsonProperty("kec")
    private List<Datum> kec;

    @JsonProperty("nama_kecamatan")
    private String nama_kecamatan;

    @JsonProperty("odp")
    private int odp;

    @JsonProperty("pdp")
    private int pdp;

    @JsonProperty("positif")
    private int positif;

    @JsonProperty("meninggal_positif")
    private int meninggal_positif;

    @JsonProperty("meninggal_pdp")
    private int meninggal_pdp;

    @JsonProperty("sembuh")
    private int sembuh;

    @JsonProperty("judul")
    public String getJudul() {
        return judul;
    }
    @JsonProperty("judul")
    public void setJudul(String judul) {
        this.judul = judul;
    }
    @JsonProperty("sumber")
    public String getSumber() {
        return sumber;
    }
    @JsonProperty("sumber")
    public void setSumber(String sumber) {
        this.sumber = sumber;
    }
    @JsonProperty("tanggal_update")
    public String getTanggal_update() {
        return tanggal_update;
    }
    @JsonProperty("tanggal_update")
    public void setTanggal_update(String tanggal_update) {
        this.tanggal_update = tanggal_update;
    }
    @JsonProperty("total_odp")
    public int getTotal_odp() {
        return total_odp;
    }
    @JsonProperty("total_odp")
    public void setTotal_odp(int total_odp) {
        this.total_odp = total_odp;
    }
    @JsonProperty("total_pdp")
    public int getTotal_pdp() {
        return total_pdp;
    }
    @JsonProperty("total_pdp")
    public void setTotal_pdp(int total_pdp) {
        this.total_pdp = total_pdp;
    }
    @JsonProperty("total_positif")
    public int getTotal_positif() {
        return total_positif;
    }
    @JsonProperty("total_positif")
    public void setTotal_positif(int total_positif) {
        this.total_positif = total_positif;
    }
    @JsonProperty("total_meninggal_positif")
    public int getTotal_meninggal_positif() {
        return total_meninggal_positif;
    }
    @JsonProperty("total_meninggal_positif")
    public void setTotal_meninggal_positif(int total_meninggal_positif) {
        this.total_meninggal_positif = total_meninggal_positif;
    }
    @JsonProperty("total_meninggal_pdp")
    public int getTotal_meninggal_pdp() {
        return total_meninggal_pdp;
    }
    @JsonProperty("total_meninggal_pdp")
    public void setTotal_meninggal_pdp(int total_meninggal_pdp) {
        this.total_meninggal_pdp = total_meninggal_pdp;
    }
    @JsonProperty("total_sembuh")
    public int getTotal_sembuh() {
        return total_sembuh;
    }
    @JsonProperty("total_sembuh")
    public void setTotal_sembuh(int total_sembuh) {
        this.total_sembuh = total_sembuh;
    }
    @JsonProperty("nama_kecamatan")
    public String getNama_kecamatan() {
        return nama_kecamatan;
    }
    @JsonProperty("nama_kecamatan")
    public void setNama_kecamatan(String nama_kecamatan) {
        this.nama_kecamatan = nama_kecamatan;
    }
    @JsonProperty("odp")
    public int getOdp() {
        return odp;
    }
    @JsonProperty("odp")
    public void setOdp(int odp) {
        this.odp = odp;
    }
    @JsonProperty("pdp")
    public int getPdp() {
        return pdp;
    }
    @JsonProperty("pdp")
    public void setPdp(int pdp) {
        this.pdp = pdp;
    }
    @JsonProperty("positif")
    public int getPositif() {
        return positif;
    }
    @JsonProperty("positif")
    public void setPositif(int positif) {
        this.positif = positif;
    }
    @JsonProperty("meninggal_positif")
    public int getMeninggal_positif() {
        return meninggal_positif;
    }
    @JsonProperty("meninggal_positif")
    public void setMeninggal_positif(int meninggal_positif) {
        this.meninggal_positif = meninggal_positif;
    }
    @JsonProperty("meninggal_pdp")
    public int getMeninggal_pdp() {
        return meninggal_pdp;
    }
    @JsonProperty("meninggal_pdp")
    public void setMeninggal_pdp(int meninggal_pdp) {
        this.meninggal_pdp = meninggal_pdp;
    }
    @JsonProperty("sembuh")
    public int getSembuh() {
        return sembuh;
    }
    @JsonProperty("sembuh")
    public void setSembuh(int sembuh) {
        this.sembuh = sembuh;
    }

    @JsonProperty("fid")
    private int fid;

    @JsonProperty("kodeProvi")
    private int kodeProvi;

    @JsonProperty("kasusSemb")
    private int kasusSemb;

    @JsonProperty("kasusMeni")
    private int kasusMeni;

    @JsonProperty("provinsi")
    private String provinsi;

    @JsonProperty("kasusPosi")
    private int kasusPosi;


    @JsonProperty("provinsi")
    public String getProvinsi() {
        return provinsi;
    }

    @JsonProperty("provinsi")
    public void setProvinsi(String provinsi) {
        this.provinsi = provinsi;
    }

    @JsonProperty("kasusPosi")
    public int getKasusPosi() {
        return kasusPosi;
    }

    @JsonProperty("kasusPosi")
    public void setKasusPosi(int kasusPosi) {
        this.kasusPosi = kasusPosi;
    }


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
