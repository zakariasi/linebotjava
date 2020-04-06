package com.dicoding.linebotjava;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "meninggal",
        "sembuh",
        "perawatan",
        "jumlahKasus",
        "id",
        "owner_id",
        "city_id",
        "quota"
})



public class Datum {

    @JsonProperty("id")
    private int id;
    @JsonProperty("owner_id")
    private int owner_id;

    @JsonProperty("id")
    public int getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(int id) {
        this.id = id;
    }

    @JsonProperty("owner_id")
    public int getOwner_id() {
        return owner_id;
    }

    @JsonProperty("owner_id")
    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }

    @JsonProperty("city_id")
    public int getCity_id() {
        return city_id;
    }

    @JsonProperty("city_id")
    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    @JsonProperty("quota")
    public int getQuota() {
        return quota;
    }

    @JsonProperty("quota")
    public void setQuota(int quota) {
        this.quota = quota;
    }

    @JsonProperty("city_id")
    private int city_id;
    @JsonProperty("quota")
    private int quota;


}
