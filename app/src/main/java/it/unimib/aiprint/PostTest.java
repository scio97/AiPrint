package it.unimib.aiprint;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class PostTest {

    Context context;
    String [] description, shopImageBase64, shopUrl, userId, userImage;
    int [] price, like;



    public PostTest (Context context){
        this.context = context;
        description = new String[8];
        shopImageBase64 = new String[8];
        shopUrl = new String[8];
        userId = new String[8];
        userImage = new String[8];
        price = new int[8];
        like = new int[8];
        loadJson();
    }

    private void loadJson(){
        try{
            //load json
            InputStream inputStream = context.getAssets().open("data.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            //fetch json file
            String json = new String (buffer, StandardCharsets.UTF_8);
            JSONArray jsonArray = new JSONArray(json);
            int max = jsonArray.length();

            // fetch each json object


            for(int i = 0; i < max; i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                description[i] = jsonObject.getString("description");
                shopImageBase64[i] = jsonObject.getString("shopImageBase64");
                shopUrl[i] = jsonObject.getString("shopUrl");
                userId[i] = jsonObject.getString("userId");
                userImage[i] = jsonObject.getString("userImage");
                price[i] = jsonObject.getInt("price");
                like[i] = jsonObject.getInt("like");

            }
        }catch(Exception e){
            Log.e("TAG", "loadJson: error " + e);
        }
    }



}
