package com.vgu.iotandroiddashboard;

import static com.vgu.iotandroiddashboard.R.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.Charset;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    MQTTHelper mqttHelper;
    TextView txtTemp, txtHumid, txtCamera; // a pointer ?
    Switch btnLED, btnPUMP;
    String[] feedIDs = {"sensor-1", "sensor-2", "actuator-1", "actuator-2"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);
        startMQTT();

        txtTemp = findViewById(id.txtTemperature);    // the pointer point to the object in the XML
        txtHumid = findViewById(id.txtHumidity);      // need to be put after the setContentView() function
        txtCamera = findViewById(id.txtCamera);
        btnLED = findViewById(id.btnLED);
        btnPUMP = findViewById(id.btnPUMP);

        for(String feedID:feedIDs){
            okHTTPRequest(feedID);
        }

        btnLED.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b == true){
                    sendDataMQTT("truonghuy/feeds/actuator-1", "1");
                }else {
                    sendDataMQTT("truonghuy/feeds/actuator-1", "0");
                }
            }
        });
        btnPUMP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b == true){
                    sendDataMQTT("truonghuy/feeds/actuator-2", "1");
                }else {
                    sendDataMQTT("truonghuy/feeds/actuator-2", "0");
                }
            }
        });

    }

    public void okHTTPRequest(String feedID){
        OkHttpClient client = new OkHttpClient();
        String curl = "https://io.adafruit.com/api/v2/truonghuy/feeds/" + feedID;
        System.out.println(curl);
        Request request = new Request.Builder()
                .url(curl)
                .build();
        client.newCall(request).enqueue(new Callback() {
            String lastValue = "";
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    String myResponse = response.body().string();
                    try {
                        JSONObject jObject = new JSONObject(myResponse);
                        lastValue = jObject.getString("last_value");
                        System.out.println("test1" +" " + lastValue + " " + lastValue.getClass().getName());
                        sendDataMQTT("truonghuy/feeds/" + feedID, lastValue);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void sendDataMQTT(String topic, String value){
        MqttMessage msg = new MqttMessage();
        msg.setId(1234);
        msg.setQos(0);
        msg.setRetained(true);

        byte[] b = value.getBytes(Charset.forName("UTF-8"));
        msg.setPayload(b);

        try {
            mqttHelper.mqttAndroidClient.publish(topic, msg);
        }catch (MqttException e){

        }

    }

    public void startMQTT(){
        mqttHelper = new MQTTHelper(this);

        // Lamda instruction or Asynchronous instruction
        mqttHelper.setCallback(new MqttCallbackExtended() {

            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

            }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.d("TEST", topic + " *** " + message.toString());
                if(topic.contains("sensor-1")){
                    txtTemp.setText(message.toString());
                }
                else if(topic.contains("sensor-2")){
                    txtHumid.setText(message.toString());
                }
                else if(topic.contains("actuator-1")){
                    if(message.toString().equals("1")){
                        btnLED.setChecked(true);
                    }
                    else{
                        btnLED.setChecked(false);
                    }
                }
                else if(topic.contains("actuator-2")){
                    if(message.toString().equals("1")){
                        btnPUMP.setChecked(true);
                    }
                    else{
                        btnPUMP.setChecked(false);
                    }
                }
                else if(topic.contains("vision-detection")){
                    if(message.toString().equals("1")){
                        txtCamera.setText("There is someone nearby");
                    }
                    else{
                        txtCamera.setText("See no body");
                    }
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });


    }
}

/*
TextView txtTemperature;

    txtTemperature = (TextView) findViewById(R.id.txtTemperature);
    txtTemperature.setText("37");
 */


//txtTemp.setText("37");

//    public String readJSONData(String inputJSON) throws JSONException {
//        JSONObject jObject = new JSONObject(inputJSON);
//        String lastValue = jObject.getString("last_value");
//
//        return lastValue;
//    }

//    final String curl = "https://io.adafruit.com/api/v2/truonghuy/feeds?x-aio-key=aio_CxSu66P5tzhIuOyIOdbLSoZrYQqc";
//
//    // Http request
//    OkHttpClient client = new OkHttpClient();
//    TextView txtString;
//    public String ADA_api = "https://reqres.in/api/users/2";
//
//    final OkHttpClient client = new OkHttpClient();
//
//    String run(String url) throws IOException {
//        Request request = new Request.Builder()
//                .url(url)
//                .build();
//
//        try (Response response = client.newCall(request).execute()) {
//            return response.body().string();
//        }
//    }

//        okHTTPRequest("https://io.adafruit.com/api/v2/truonghuy/feeds/sensor-1");
//        okHTTPRequest("https://io.adafruit.com/api/v2/truonghuy/feeds/sensor-2");
//        okHTTPRequest("https://io.adafruit.com/api/v2/truonghuy/feeds/actuator-1");
//        okHTTPRequest("https://io.adafruit.com/api/v2/truonghuy/feeds/actuator-2");

//    okHTTPRequest("sensor-1");
//        okHTTPRequest("sensor-2");
//        okHTTPRequest("actuator-1");
//        okHTTPRequest("actuator-2");