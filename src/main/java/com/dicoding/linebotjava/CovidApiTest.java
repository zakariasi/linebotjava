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

        String URIKAWAL1 = "https://api.kawalcorona.com/positif/";
        String URIKAWAL2 = "https://api.kawalcorona.com/meninggal/";
        String URIKAWAL3 = "https://api.kawalcorona.com/sembuh/";


        CovidEvents covidEvents = null;
        CovidEvents covidEvents2 = null;
        CovidEvents covidEvents4 = null;

        CovidDunia covidDunia1 = null;
        CovidDunia covidDunia2 = null;
        CovidDunia covidDunia3 = null;

        try (CloseableHttpAsyncClient client = HttpAsyncClients.createDefault()) {
            client.start();
            //Use HTTP Get to retrieve data
            HttpGet get = new HttpGet(URI);
            HttpGet get2 = new HttpGet(URI2);
            HttpGet get3 = new HttpGet(URI3);

            HttpGet getKAWAL1 = new HttpGet(URIKAWAL1);
            HttpGet getKAWAL2 = new HttpGet(URIKAWAL2);
            HttpGet getKAWAL3 = new HttpGet(URIKAWAL3);




            Future<HttpResponse> future = client.execute(get, null);
            Future<HttpResponse> future2 = client.execute(get2, null);
            Future<HttpResponse> future3 = client.execute(get3, null);

            Future<HttpResponse> KAWAL1 = client.execute(getKAWAL1, null);
            Future<HttpResponse> KAWAL2 = client.execute(getKAWAL2, null);
            Future<HttpResponse> KAWAL3 = client.execute(getKAWAL3, null);



            HttpResponse responseGet = future.get();
            HttpResponse responseGet2 = future2.get();
            HttpResponse responseGet3 = future3.get();

            HttpResponse KAWALKORONA1 = KAWAL1.get();
            HttpResponse KAWALKORONA2 = KAWAL2.get();
            HttpResponse KAWALKORONA3 = KAWAL3.get();



            // Get the response from the GET request
            InputStream inputStream = responseGet.getEntity().getContent();
            InputStream inputStream2 = responseGet2.getEntity().getContent();
            InputStream inputStream3 = responseGet3.getEntity().getContent();

            InputStream inputKAWAL1 = KAWALKORONA1.getEntity().getContent();
            InputStream inputKAWAL2 = KAWALKORONA2.getEntity().getContent();
            InputStream inputKAWAL3 = KAWALKORONA3.getEntity().getContent();


            String encoding = StandardCharsets.UTF_8.name();

            String jsonResponse = IOUtils.toString(inputStream, encoding);
            String jsonResponse2 = IOUtils.toString(inputStream2, encoding);
            String jsonResponse3 = IOUtils.toString(inputStream3, encoding);

            String jsonResponseKAWAL1 = IOUtils.toString(inputKAWAL1, encoding);
            String jsonResponseKAWAL2 = IOUtils.toString(inputKAWAL2, encoding);
            String jsonResponseKAWAL3 = IOUtils.toString(inputKAWAL3, encoding);

            System.out.println("Got result API KAWAL 1");
            System.out.println(jsonResponseKAWAL1);

            System.out.println("Got result API KAWAL 2");
            System.out.println(jsonResponseKAWAL2);

            System.out.println("Got result API KAWAL 3");
            System.out.println(jsonResponseKAWAL3);






//            System.out.println("Got result API 1");
//            System.out.println(jsonResponse);
//
//            System.out.println("Got result API 2");
//            System.out.println(jsonResponse2);
//
//            System.out.println("Got result API 3");
//            System.out.println(jsonResponse3);


            ObjectMapper objectMapper = new ObjectMapper();
            covidEvents = objectMapper.readValue(jsonResponse, CovidEvents.class);
            covidEvents2 = objectMapper.readValue(jsonResponse2, CovidEvents.class);

            covidDunia1 =  objectMapper.readValue(jsonResponseKAWAL1, CovidDunia.class);
            covidDunia2 =  objectMapper.readValue(jsonResponseKAWAL2, CovidDunia.class);
            covidDunia3 =  objectMapper.readValue(jsonResponseKAWAL3, CovidDunia.class);

            String name1 = covidDunia1.getName();
            String name2 = covidDunia2.getName();
            String name3 = covidDunia3.getName();


            System.out.println(name1);
            System.out.println(name2);
            System.out.println(name3);





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
