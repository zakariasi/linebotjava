package com.dicoding.linebotjava;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class CovidApiTest {


    public static void main(String args[]){
        String URI = "http://ec2-3-133-88-244.us-east-2.compute.amazonaws.com/medan_covid/";
        String URI2 = "https://indonesia-covid-19.mathdro.id/api/provinsi";
        String URI3 = "https://indonesia-covid-19.mathdro.id/api/";

        System.out.println("URI: " +  URI);
        System.out.println("URI: " +  URI2);
        System.out.println("URI: " +  URI3);

        CovidEvents covidEvents = null;
        CovidEvents covidEvents2 = null;
        CovidEvents covidEvents3 = null;

        try (CloseableHttpAsyncClient client = HttpAsyncClients.createDefault()) {
            client.start();
            //Use HTTP Get to retrieve data
            HttpGet get = new HttpGet(URI);
            HttpGet get2 = new HttpGet(URI2);
            HttpGet get3 = new HttpGet(URI3);

            Future<HttpResponse> future = client.execute(get, null);
            Future<HttpResponse> future2 = client.execute(get2, null);
            Future<HttpResponse> future3 = client.execute(get3, null);

            HttpResponse responseGet = future.get();
            HttpResponse responseGet2 = future2.get();
            HttpResponse responseGet3 = future3.get();
//            System.out.println("HTTP executed");
//            System.out.println("HTTP Status of response: " + responseGet.getStatusLine().getStatusCode());

            // Get the response from the GET request
            InputStream inputStream = responseGet.getEntity().getContent();
            InputStream inputStream2 = responseGet2.getEntity().getContent();
            InputStream inputStream3 = responseGet3.getEntity().getContent();

            String encoding = StandardCharsets.UTF_8.name();

            String jsonResponse = IOUtils.toString(inputStream, encoding);
            String jsonResponse2 = IOUtils.toString(inputStream2, encoding);
            String jsonResponse3 = IOUtils.toString(inputStream3, encoding);
//
            System.out.println("Got result API 1");
            System.out.println(jsonResponse);

            System.out.println("Got result API 2");
            System.out.println(jsonResponse2);

            System.out.println("Got result API 3");
            System.out.println(jsonResponse3);

            ObjectMapper objectMapper = new ObjectMapper();
            covidEvents = objectMapper.readValue(jsonResponse, CovidEvents.class);
            covidEvents2 = objectMapper.readValue(jsonResponse2, CovidEvents.class);

//
            Datum eventData = (Datum) covidEvents.getData().get(1).getKec().get(1);
            Datum eventData2 = (Datum) covidEvents2.getData().get(1);


            int totalPosi = 0;
            int totalMeninggal = 0;
            for(int i = 0; i <35; i++){
                totalPosi = totalPosi + covidEvents2.getData().get(i).getKasusPosi();
                totalMeninggal = totalMeninggal + covidEvents2.getData().get(i).getKasusMeni();
            }

            System.out.println("total kasus positif: " + totalPosi);
            System.out.println("total kasus meninggal: " + totalMeninggal);

//
            int fid = eventData2.getKasusPosi();



            String nama_kecamatan = eventData.getNama_kecamatan();


            System.out.println("Data API kecamatan");
            System.out.println(nama_kecamatan);

            System.out.println("Data API provinsi");
            System.out.println(fid);








        } catch (InterruptedException | ExecutionException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
