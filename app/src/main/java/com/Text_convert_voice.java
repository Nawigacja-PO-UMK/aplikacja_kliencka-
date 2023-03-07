package com;

import android.annotation.SuppressLint;
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
    @SuppressLint("SuspiciousIndentation")
    public void Speech(String text)
    {
        if(!text.equals(""))
        textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
    }
}
