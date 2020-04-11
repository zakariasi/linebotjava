package com.dicoding.linebotjava;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.client.LineSignatureValidator;
import com.linecorp.bot.client.MessageContentResponse;
import com.linecorp.bot.model.Multicast;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.*;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.event.source.RoomSource;
import com.linecorp.bot.model.message.*;
import com.linecorp.bot.model.message.flex.container.FlexContainer;
import com.linecorp.bot.model.objectmapper.ModelObjectMapper;
import com.linecorp.bot.model.profile.UserProfileResponse;
import org.apache.commons.io.IOUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


@RestController
public class Controller {

    @Autowired
    @Qualifier("lineMessagingClient")
    private LineMessagingClient lineMessagingClient;

    private CovidEvents covidEvents = null;
    private CovidEvents covidEvents2 = null;
    private BotService botService;


    @Autowired
    @Qualifier("lineSignatureValidator")
    private LineSignatureValidator lineSignatureValidator;

    @RequestMapping(value="/webhook", method= RequestMethod.POST)
    public ResponseEntity<String> callback(
            @RequestHeader("X-Line-Signature") String xLineSignature,
            @RequestBody String eventsPayload)
    {
        try {
            if (!lineSignatureValidator.validateSignature(eventsPayload.getBytes(), xLineSignature)) {
                throw new RuntimeException("Invalid Signature Validation");
            }

            // parsing event
            ObjectMapper objectMapper = ModelObjectMapper.createNewObjectMapper();
            EventsModel eventsModel = objectMapper.readValue(eventsPayload, EventsModel.class);

//            eventsModel.getEvents().forEach((event)->{
//                if (event instanceof MessageEvent) {
//                    if  ((  (MessageEvent) event).getMessage() instanceof AudioMessageContent
//                            || ((MessageEvent) event).getMessage() instanceof ImageMessageContent
//                            || ((MessageEvent) event).getMessage() instanceof VideoMessageContent
//                            || ((MessageEvent) event).getMessage() instanceof FileMessageContent
//                    ) {
//                        String baseURL     = "https://linebot-zakkariassi.herokuapp.com";
//                        String contentURL  = baseURL+"/content/"+ ((MessageEvent) event).getMessage().getId();
//                        String contentType = ((MessageEvent) event).getMessage().getClass().getSimpleName();
//                        String textMsg     = contentType.substring(0, contentType.length() -14)
//                                + " yang kamu kirim bisa diakses dari link:\n "
//                                + contentURL;
//
//                        replyText(((MessageEvent) event).getReplyToken(), textMsg);
//                    } else {
//                        MessageEvent messageEvent = (MessageEvent) event;
//                        TextMessageContent textMessageContent = (TextMessageContent) messageEvent.getMessage();
//                        replyText(messageEvent.getReplyToken(), textMessageContent.getText());
//                    }
//                }
//            });

            eventsModel.getEvents().forEach((event)->{
                if (event instanceof MessageEvent) {
                    if (event.getSource() instanceof GroupSource || event.getSource() instanceof RoomSource) {
                        handleGroupRoomChats((MessageEvent) event);
                    } else {
                        handleOneOnOneChats((MessageEvent) event);
                    }
                }
            });

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    private void handleOneOnOneChats(MessageEvent event) {
        if  (event.getMessage() instanceof AudioMessageContent
                || event.getMessage() instanceof ImageMessageContent
                || event.getMessage() instanceof VideoMessageContent
                || event.getMessage() instanceof FileMessageContent
        ) {
            handleContentMessage(event);
        } else if(event.getMessage() instanceof TextMessageContent) {
            handleTextMessage(event);
        } else if(event.getMessage() instanceof StickerMessageContent) {
            replySticker(event.getReplyToken(), "11538", "51626496");
        }
        else {
            replyText(event.getReplyToken(), "Unknown Message");
        }
    }



    private void handleGroupRoomChats(MessageEvent event) {
        TextMessageContent textMessageContent = (TextMessageContent) event.getMessage();

//        if(!event.getSource().getUserId().isEmpty()) {
//            String userId = event.getSource().getUserId();
//            UserProfileResponse profile = getProfile(userId);
//            replyText(event.getReplyToken(), "Hello, " + profile.getDisplayName());
//        } else

        List<Message> msgDaftarMenu = new ArrayList<>();
        msgDaftarMenu.add(new TextMessage("Daftar Data Covid-19 : \n\n[A1] Data Covid-19 di Indonesia\n[A2] Data Covid-19 provinsi Indonesia\n[A3] Data Covid-19 kecamatan di kota Medan"));

        String daftarProvinsi = "[P0] DKI Jakarta\n[P1] Jawa Barat\n[P2] Jawa Timur\n[P3] Banten\n[P4] Jawa Tengah\n[P5] Sulawesi Selatan\n[P6] Bali\n[P7] Papua\n[P8] Sumatera Utara\n[P9] D.I Yogyakarta\n[P10] Indonesia\n[P11] Kalimantan Timur\n[P12] Kepulauan Riau\n[P13] Kalimantan Selatan\n[P14] Kalimantan Tengah\n[P15] Sumatera Barat\n[P16] Sumatera Selatan\n[P17] Nusa Tenggara Barat\n[P18] Kalimantan Utara\n[P19] Lampung\n[P20] Sulawesi Tenggara\n[P21] Riau\n[P22] Kalimantan Barat\n[P23] Sulawesi Utara\n[P24] Aceh\n[P25] Sulawesi Tengah\n[P26] Kepulauan Bangka Belitung\n[P27] Maluku\n[P28] Jambi\n[P29] Bengkulu\n[P30] Sulawesi Barat\n[P31] Maluku Utara\n[P32] Papua Barat\n[P33] Nusa Tenggara Timur\n[P34] Gorontalo";
        String daftarKecamatanMedan = "[KC0] Medan Amplas\n[KC1] Medan Area\n[KC2] Medan Barat\n[KC3] Medan Baru\n[KC4] Medan Belawan\n[KC5] Medan Deli\n[KC6] Medan Denai\n[KC7] Medan Helvetia\n[KC8] Medan Johor\n[KC9] Medan Kota\n[KC10] Medan Labuhan\n[KC11] Medan Maimun\n[KC12] Medan Marelan\n[KC13] Medan Perjuangan\n[KC14] Medan Petisah\n[KC15] Medan Polonia\n[KC16] Medan Selayang\n[KC17] Medan Sunggal\n[KC18] Medan Tembung\n[KC19] Medan Timur\n[KC20] Medan Tuntungan\n";


        if(textMessageContent.getText().equals("hai")){
            replyText(event.getReplyToken(), "Hai juga");
        } else if(textMessageContent.getText().toLowerCase().contains("menu")) {
            replyText(event.getReplyToken(), msgDaftarMenu);
        } else if(textMessageContent.getText().toLowerCase().contains("a1")) {
            replyText(event.getReplyToken(), "Data Covid-19 di Indonesia");
        } else if(textMessageContent.getText().toLowerCase().contains("a2")) {
            replyText(event.getReplyToken(), daftarProvinsi);
        } else if(textMessageContent.getText().toLowerCase().contains("a3")) {
            replyText(event.getReplyToken(), daftarKecamatanMedan);
        } else if(textMessageContent.getText().toLowerCase().contains("kc")) {
            replyText(event.getReplyToken(), covid(textMessageContent.getText()) );
        } else if(textMessageContent.getText().toLowerCase().contains("p")) {
            replyText(event.getReplyToken(), covidProvinsi(textMessageContent.getText()) );
        }
        else {
            replyText(event.getReplyToken(), "Hello, what is your name?");
        }
    }

    private void handleContentMessage(MessageEvent event) {
        String baseURL     = "https://linebot-zakkariassi.herokuapp.com";
        String contentURL  = baseURL+"/content/"+ event.getMessage().getId();
        String contentType = event.getMessage().getClass().getSimpleName();
        String textMsg     = contentType.substring(0, contentType.length() -14)
                + " yang kamu kirim bisa diakses dari link:\n "
                + contentURL;

        replyText(event.getReplyToken(), textMsg);
    }

    private void handleTextMessage(MessageEvent event) {
        TextMessageContent textMessageContent = (TextMessageContent) event.getMessage();

        List<Message> msgDaftarMenu = new ArrayList<>();
        msgDaftarMenu.add(new TextMessage("Daftar Data Covid-19 : \n\n[A1] Data Covid-19 di Indonesia\n[A2] Data Covid-19 provinsi Indonesia\n[A3] Data Covid-19 kecamatan di kota Medan"));

        String daftarProvinsi = "[P0] DKI Jakarta\n[P1] Jawa Barat\n[P2] Jawa Timur\n[P3] Banten\n[P4] Jawa Tengah\n[P5] Sulawesi Selatan\n[P6] Bali\n[P7] Papua\n[P8] Sumatera Utara\n[P9] D.I Yogyakarta\n[P10] Indonesia\n[P11] Kalimantan Timur\n[P12] Kepulauan Riau\n[P13] Kalimantan Selatan\n[P14] Kalimantan Tengah\n[P15] Sumatera Barat\n[P16] Sumatera Selatan\n[P17] Nusa Tenggara Barat\n[P18] Kalimantan Utara\n[P19] Lampung\n[P20] Sulawesi Tenggara\n[P21] Riau\n[P22] Kalimantan Barat\n[P23] Sulawesi Utara\n[P24] Aceh\n[P25] Sulawesi Tengah\n[P26] Kepulauan Bangka Belitung\n[P27] Maluku\n[P28] Jambi\n[P29] Bengkulu\n[P30] Sulawesi Barat\n[P31] Maluku Utara\n[P32] Papua Barat\n[P33] Nusa Tenggara Timur\n[P34] Gorontalo";
        String daftarKecamatanMedan = "[KC0] Medan Amplas\n[KC1] Medan Area\n[KC2] Medan Barat\n[KC3] Medan Baru\n[KC4] Medan Belawan\n[KC5] Medan Deli\n[KC6] Medan Denai\n[KC7] Medan Helvetia\n[KC8] Medan Johor\n[KC9] Medan Kota\n[KC10] Medan Labuhan\n[KC11] Medan Maimun\n[KC12] Medan Marelan\n[KC13] Medan Perjuangan\n[KC14] Medan Petisah\n[KC15] Medan Polonia\n[KC16] Medan Selayang\n[KC17] Medan Sunggal\n[KC18] Medan Tembung\n[KC19] Medan Timur\n[KC20] Medan Tuntungan\n";

        if (textMessageContent.getText().toLowerCase().contains("flex")) {
            replyFlexMessage(event.getReplyToken());
        } else if(textMessageContent.getText().toLowerCase().contains("cvd")) {
//            replyText(event.getReplyToken(), covid(textMessageContent.getText()));
        } else if(textMessageContent.getText().toLowerCase().contains("covid")) {
            showEventSummary(event.getReplyToken());
        } else if(textMessageContent.getText().toLowerCase().contains("menu")) {
            replyText(event.getReplyToken(), msgDaftarMenu);
        } else if(textMessageContent.getText().toLowerCase().equals("a1")) {
            replyText(event.getReplyToken(), covidIndonesia(textMessageContent.getText()));
        } else if(textMessageContent.getText().toLowerCase().contains("a2")) {
            replyText(event.getReplyToken(), daftarProvinsi);
        } else if(textMessageContent.getText().toLowerCase().contains("a3")) {
            replyText(event.getReplyToken(), daftarKecamatanMedan);
        } else if(textMessageContent.getText().toLowerCase().contains("kc")) {
            replyText(event.getReplyToken(), covid(textMessageContent.getText()) );
        } else if(textMessageContent.getText().toLowerCase().contains("p")) {
            replyText(event.getReplyToken(), covidProvinsi(textMessageContent.getText()) );
        }
    }

    private String covidIndonesia(String input){

        if (covidEvents2 == null) {
            getCovidEventsDataProvinsi();
        }




        Datum eventData2 = (Datum) covidEvents2.getData().get(1);

        int totalPosi = 0;
        int totalMeninggal = 0;
        int totalSembuh = 0;

        for(int i = 0; i <35; i++){
            totalPosi = totalPosi + covidEvents2.getData().get(i).getKasusPosi();
            totalMeninggal = totalMeninggal + covidEvents2.getData().get(i).getKasusMeni();
            totalSembuh = totalSembuh + covidEvents2.getData().get(i).getSembuh();
        }



        String dataIndonesia = String.format("Total Positif: %s\nTotal meninggal: %s\nTotal sembuh: %s", String.valueOf(totalPosi), String.valueOf(totalMeninggal), String.valueOf(totalSembuh));



        return dataIndonesia;
    }


    private String covidProvinsi(String input){

        if (covidEvents2 == null) {
            getCovidEventsDataProvinsi();
        }


        String inputUser = input;
        int eventIndex = 0;
        if (inputUser.length() == 3) {
            eventIndex = Integer.parseInt(String.valueOf(inputUser.charAt(1)) + String.valueOf(inputUser.charAt(2)));
        }
        if (inputUser.length() == 2) {
            eventIndex = Integer.parseInt(String.valueOf(inputUser.charAt(1)));
        }


        Datum eventData2 = (Datum) covidEvents2.getData().get(eventIndex);

        String namaProvinsi  = eventData2.getProvinsi().toLowerCase();
        String kasusPositif = String.valueOf(eventData2.getKasusPosi());
        String kasusSembuh = String.valueOf(eventData2.getKasusSemb());
        String kasusMeninggal = String.valueOf(eventData2.getKasusMeni());





        String dataProvinsi = String.format("Nama provinsi: %s\nTotal positif: %s\nTotal sembuh: %s\nTotal meninggal: %s", namaProvinsi, kasusPositif, kasusSembuh, kasusMeninggal);



        return dataProvinsi;
    }






    private String covid(String input){

        if (covidEvents == null) {
            getCovidEventsDataKecamatanMedan();
        }


        String inputUser = input;
        int eventIndex = 0;
        if (inputUser.length() == 4) {
            eventIndex = Integer.parseInt(String.valueOf(inputUser.charAt(2)) + String.valueOf(inputUser.charAt(3)));
        }
        if (inputUser.length() == 3) {
            eventIndex = Integer.parseInt(String.valueOf(inputUser.charAt(2)));
        }


        Datum eventData = (Datum) covidEvents.getData().get(1).getKec().get(eventIndex);

        String namaKecamatan  = eventData.getNama_kecamatan().toLowerCase();
        String odp = String.valueOf(eventData.getOdp());
        String pdp = String.valueOf(eventData.getPdp());
        String positif = String.valueOf(eventData.getPositif());
        String meninggalPositif = String.valueOf(eventData.getMeninggal_positif());
        String meninggalPdp = String.valueOf(eventData.getMeninggal_pdp());
        String sembuh = String.valueOf(eventData.getSembuh());



        String dataKecamatan = String.format("Nama Kecamatan: %s\nTotal ODP: %s\nTotal PDP: %s\nTotal positif: %s\nTotal meninggal positif: %s\nTotal meninggal pdp: %s\nTotal sembuh: %s", namaKecamatan, odp, pdp, positif, meninggalPositif, meninggalPdp, sembuh);



        return dataKecamatan;
    }


    @RequestMapping(value = "/content/{id}", method = RequestMethod.GET)
    public ResponseEntity content(
            @PathVariable("id") String messageId
    ){
        MessageContentResponse messageContent = getContent(messageId);

        if(messageContent != null) {
            HttpHeaders headers = new HttpHeaders();
            String[] mimeType = messageContent.getMimeType().split("/");
            headers.setContentType(new MediaType(mimeType[0], mimeType[1]));

            InputStream inputStream = messageContent.getStream();
            InputStreamResource inputStreamResource = new InputStreamResource(inputStream);

            return new ResponseEntity<>(inputStreamResource, headers, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private MessageContentResponse getContent(String messageId) {
        try {
            return lineMessagingClient.getMessageContent(messageId).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private void reply(ReplyMessage replyMessage) {
        try {
            lineMessagingClient.replyMessage(replyMessage).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private void replyFlexMessage(String replyToken) {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            String flexTemplate = IOUtils.toString(classLoader.getResourceAsStream("flex_event.json"));


            ObjectMapper objectMapper = ModelObjectMapper.createNewObjectMapper();
            FlexContainer flexContainer = objectMapper.readValue(flexTemplate, FlexContainer.class);


            ReplyMessage replyMessage = new ReplyMessage(replyToken, new FlexMessage("Covid", flexContainer));
            reply(replyMessage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void replyText(String replyToken, String messageToUser){
        TextMessage textMessage = new TextMessage(messageToUser);
        ReplyMessage replyMessage = new ReplyMessage(replyToken, textMessage);
        reply(replyMessage);
    }

    private void replyText(String replyToken, List<Message> msgArray){
        ReplyMessage replyMessage = new ReplyMessage(replyToken, msgArray);
        reply(replyMessage);
    }

    private void replySticker(String replyToken, String packageId, String stickerId){
        StickerMessage stickerMessage = new StickerMessage(packageId, stickerId);
        ReplyMessage replyMessage = new ReplyMessage(replyToken, stickerMessage);
        reply(replyMessage);
    }

    @RequestMapping(value="/pushmessage/{id}/{message}", method=RequestMethod.GET)
    public ResponseEntity<String> pushmessage(
            @PathVariable("id") String userId,
            @PathVariable("message") String textMsg

    ){
        TextMessage textMessage = new TextMessage(textMsg);
        PushMessage pushMessage = new PushMessage(userId, textMessage);
        push(pushMessage);

        return new ResponseEntity<String>("Push message:"+textMsg+"\nsent to: "+userId, HttpStatus.OK);
    }

    private void push(PushMessage pushMessage){
        try {
            lineMessagingClient.pushMessage(pushMessage).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping(value="/multicast", method=RequestMethod.GET)
    public ResponseEntity<String> multicast(){
        String[] userIdList = {
                "U5033a111bff36e72d69ca748ec1bc9d5"};
        Set<String> listUsers = new HashSet<String>(Arrays.asList(userIdList));
        if(listUsers.size() > 0){
            String textMsg = "Ini pesan multicast";
            sendMulticast(listUsers, textMsg);
        }
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    private void sendMulticast(Set<String> sourceUsers, String txtMessage){
        TextMessage message = new TextMessage(txtMessage);
        Multicast multicast = new Multicast(sourceUsers, message);

        try {
            lineMessagingClient.multicast(multicast).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public ResponseEntity<String> profile(){
        String userId = "U5033a111bff36e72d69ca748ec1bc9d5";
        UserProfileResponse profile = getProfile(userId);

        if (profile != null) {
            String profileName = profile.getDisplayName();
            TextMessage textMessage = new TextMessage("Hello, " + profileName);
            PushMessage pushMessage = new PushMessage(userId, textMessage);
            push(pushMessage);

            return new ResponseEntity<String>("Hello, "+profileName, HttpStatus.OK);
        }

        return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
    }

    private UserProfileResponse getProfile(String userId){
        try {
            return lineMessagingClient.getProfile(userId).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private void getCovidEventsDataKecamatanMedan(){
        String URI = "http://ec2-3-133-88-244.us-east-2.compute.amazonaws.com/medan_covid/";

        try (CloseableHttpAsyncClient client = HttpAsyncClients.createDefault()) {
            client.start();
            //Use HTTP Get to retrieve data
            HttpGet get = new HttpGet(URI);



            Future<HttpResponse> future = client.execute(get, null);


            HttpResponse responseGet = future.get();


            // Get the response from the GET request
            InputStream inputStream = responseGet.getEntity().getContent();


            String encoding = StandardCharsets.UTF_8.name();

            String jsonResponse = IOUtils.toString(inputStream, encoding);



            ObjectMapper objectMapper = new ObjectMapper();
            covidEvents = objectMapper.readValue(jsonResponse, CovidEvents.class);


//            Datum eventData = (Datum) covidEvents.getData().get(1).getKec().get(1);
//            Datum eventData2 = (Datum) covidEvents2.getData().get(1);

        } catch (InterruptedException | ExecutionException | IOException e) {
            throw new RuntimeException(e);
        }


    }

    private void getCovidEventsDataProvinsi() {

        String URI2 = "https://indonesia-covid-19.mathdro.id/api/provinsi";



        try (CloseableHttpAsyncClient client = HttpAsyncClients.createDefault()) {
            client.start();
            //Use HTTP Get to retrieve data

            HttpGet get2 = new HttpGet(URI2);




            Future<HttpResponse> future2 = client.execute(get2, null);



            HttpResponse responseGet2 = future2.get();

            // Get the response from the GET request

            InputStream inputStream2 = responseGet2.getEntity().getContent();


            String encoding = StandardCharsets.UTF_8.name();


            String jsonResponse2 = IOUtils.toString(inputStream2, encoding);


            ObjectMapper objectMapper = new ObjectMapper();

            covidEvents2 = objectMapper.readValue(jsonResponse2, CovidEvents.class);

//            Datum eventData = (Datum) covidEvents.getData().get(1).getKec().get(1);
//            Datum eventData2 = (Datum) covidEvents2.getData().get(1);

        } catch (InterruptedException | ExecutionException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showEventSummary(String replyToken) {
        try {
            if (covidEvents == null) {
                getCovidEventsDataKecamatanMedan();
            }


            Datum eventData = covidEvents.getData().get(0);

            ClassLoader classLoader = getClass().getClassLoader();
            String encoding         = StandardCharsets.UTF_8.name();
            String flexTemplate     = IOUtils.toString(classLoader.getResourceAsStream("flex_simple.json"), encoding);

//            flexTemplate = String.format(flexTemplate,
//
////                    eventData.getFid(),
////                    eventData.getKasusMeni(),
////                    eventData.getKasusSemb(),
////                    eventData.getKodeProvi()
//
//                    );

            ObjectMapper objectMapper = ModelObjectMapper.createNewObjectMapper();
            FlexContainer flexContainer = objectMapper.readValue(flexTemplate, FlexContainer.class);
            botService.reply(replyToken, new FlexMessage("Covid Events", flexContainer));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}