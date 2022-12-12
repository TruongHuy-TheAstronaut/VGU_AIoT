package com.vgu.iotandroiddashboard;

import static com.vgu.iotandroiddashboard.R.*;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.Charset;

public class MainActivity extends AppCompatActivity {
    MQTTHelper mqttHelper;
    TextView txtTemp, txtHumid; // a pointer ?
    Switch btnLED, btnPUMP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);
        startMQTT();

        txtTemp = findViewById(id.txtTemperature);    // the pointer point to the object in the XML
        txtHumid = findViewById(id.txtHumidity);      // need to be put after the setContentView() function
        btnLED = findViewById(id.btnLED);
        btnPUMP = findViewById(id.btnPUMP);

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