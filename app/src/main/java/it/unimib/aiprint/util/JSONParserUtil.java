package it.unimib.aiprint.util;

import android.app.Application;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import it.unimib.aiprint.model.image.ImageApiResponse;

public class JSONParserUtil {
    private final Application application;
    public JSONParserUtil(Application application) {
        this.application = application;
    }

    public ImageApiResponse parseJSONFileWithGSon(String fileName) throws IOException {
        InputStream inputStream = application.getAssets().open(fileName);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        return new Gson().fromJson(bufferedReader, ImageApiResponse.class);
    }
}
