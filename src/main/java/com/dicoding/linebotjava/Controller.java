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
        if(!event.getSource().getUserId().isEmpty()) {
            String userId = event.getSource().getUserId();
            UserProfileResponse profile = getProfile(userId);
            replyText(event.getReplyToken(), "Hello, " + profile.getDisplayName());
        } else {
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

        List<Message> msgArray = new ArrayList<>();
        msgArray.add(new TextMessage("Daftar Data Covid-19 : \n\n[A1] Data Covid-19 di Indonesia\n[A2] Data Covid-19 provinsi di Indonesia\n[A3] Data Covid-19 kecamatan di kota Medan"));


        if (textMessageContent.getText().toLowerCase().contains("flex")) {
            replyFlexMessage(event.getReplyToken());
        } else if(textMessageContent.getText().toLowerCase().contains("cvd")) {
//            replyText(event.getReplyToken(), covid(textMessageContent.getText()));
        } else if(textMessageContent.getText().toLowerCase().contains("covid")) {
            showEventSummary(event.getReplyToken());
        } else if(textMessageContent.getText().toLowerCase().contains("menu")) {
            replyText(event.getReplyToken(), msgArray);
        }
    }

//    List<Message> msgArray = new ArrayList<>();
//    msgArray.add(new TextMessage(textMessageContent.getText()));
//    msgArray.add(new StickerMessage("1", "106"));
//    ReplyMessage replyMessage = new ReplyMessage(event.getReplyToken(), msgArray);
//    reply(replyMessage);

//    private String covid(String input){
//        getCovidEventsData();
//
//        String inputUser = input;
//        int eventIndex = 0;
//        if (inputUser.length() == 5) {
//            eventIndex = Integer.parseInt(String.valueOf(inputUser.charAt(3)) + String.valueOf(inputUser.charAt(4)));
//        }
//        if (inputUser.length() == 4) {
//            eventIndex = Integer.parseInt(String.valueOf(inputUser.charAt(3)));
//        }
//
////        Datum eventData = covidEvents.getData().get(eventIndex);;
//////        int fid = eventData.getFid();
////        String a = String.valueOf(fid);
//
////        return a;
//    }


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

    private void getCovidEventsData() {
        // Act as client with GET method
        String URI = "https://indonesia-covid-19.mathdro.id/api/provinsi";
        System.out.println("URI: " +  URI);

        try (CloseableHttpAsyncClient client = HttpAsyncClients.createDefault()) {
            client.start();
            //Use HTTP Get to retrieve data
            HttpGet get = new HttpGet(URI);

            Future<HttpResponse> future = client.execute(get, null);
            HttpResponse responseGet = future.get();
            System.out.println("HTTP executed");
            System.out.println("HTTP Status of response: " + responseGet.getStatusLine().getStatusCode());

            // Get the response from the GET request
            InputStream inputStream = responseGet.getEntity().getContent();
            String encoding = StandardCharsets.UTF_8.name();
            String jsonResponse = IOUtils.toString(inputStream, encoding);

            System.out.println("Got result");
//            System.out.println(jsonResponse);

            ObjectMapper objectMapper = new ObjectMapper();
            covidEvents = objectMapper.readValue(jsonResponse, CovidEvents.class);

        } catch (InterruptedException | ExecutionException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showEventSummary(String replyToken) {
        try {
            if (covidEvents == null) {
                getCovidEventsData();
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