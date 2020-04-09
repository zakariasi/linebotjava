package com.dicoding.linebotjava;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "data"

})
public class CovidEvents {


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