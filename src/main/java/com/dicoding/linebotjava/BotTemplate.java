package com.dicoding.linebotjava;

import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.event.source.RoomSource;
import com.linecorp.bot.model.event.source.Source;
import com.linecorp.bot.model.event.source.UserSource;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.template.ButtonsTemplate;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.model.profile.UserProfileResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;



@Service
public class BotTemplate {



        public TemplateMessage carouselEvents(CovidEvents covidEvents) {
            int i;
            int fid, kodeProvi, kasusPosi, kasusSemb, kasusMeni;
            String provinsi;
            CarouselColumn column;
            List<CarouselColumn> carouselColumn = new ArrayList<>();
            for (i = 0; i < covidEvents.getData().size(); i++){
                fid = covidEvents.getData().get(i).getFid();
                kodeProvi = covidEvents.getData().get(i).getKodeProvi();
                kasusPosi = covidEvents.getData().get(i).getKasusPosi();
                kasusSemb = covidEvents.getData().get(i).getKasusSemb();
                kasusMeni = covidEvents.getData().get(i).getKasusMeni();
                provinsi = covidEvents.getData().get(i).getProvinsi();

                column = new CarouselColumn(provinsi, provinsi.substring(0, (provinsi.length() < 40)?provinsi.length():40), provinsi,
                        Arrays.asList(
                                new MessageAction("Summary", "Hello"),
                                new URIAction("View Page", provinsi),
                                new MessageAction("Join Event", "join event #"+fid)
                        )
                );

                carouselColumn.add(column);
            }

            CarouselTemplate carouselTemplate = new CarouselTemplate(carouselColumn);
            return new TemplateMessage("Your search result", carouselTemplate);
        }

    }
