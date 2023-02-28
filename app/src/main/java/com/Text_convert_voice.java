package com;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class Text_convert_voice {

    TextToSpeech textToSpeech;

    public Text_convert_voice(Context context)
    {
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                if(status!=TextToSpeech.ERROR)
                {
                    textToSpeech.setLanguage(Locale.getDefault());
                }
            }
        });
    }
    public void Speech(String text)
    {
        textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
    }
}
