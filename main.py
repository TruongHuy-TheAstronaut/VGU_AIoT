# print("Hello The Brave New World")

import sys
from Adafruit_IO import MQTTClient
from AI.simple_AI import person_detector
from rasberry_physical import *
import schedule
import base64
import time
AIO_FEED_ID = ["sensor-1", "sensor-2", "actuator-1", "actuator-2", "vision-detection"]
AIO_USERNAME = "truonghuy"
encoded_key = "YWlvX2ZPdXcyNjQ1RTZvQXBtaUpNZHlsSHNnYUhJZ3c="
AIO_KEY = base64.b64decode(encoded_key)

def connected(client):
    print("Ket noi thanh cong ...")
    for topic in AIO_FEED_ID:
        client.subscribe(topic)

def subscribe(client , userdata , mid , granted_qos):
    print("Subscribe thanh cong ...")

def disconnected(client):
    print("Ngat ket noi ...")
    sys.exit (1)

def message(client , feed_id , payload):
    print("Nhan du lieu: " + payload)

client = MQTTClient(AIO_USERNAME , AIO_KEY)
client.on_connect = connected
client.on_disconnect = disconnected
client.on_message = message
client.on_subscribe = subscribe
client.connect()
client.loop_background()


schedule.every().day.at("6:00").do(setDevice1(False))
schedule.every().day.at("17:00").do(setDevice1(True))
while True:
    ai_result = person_detector()

    if(person_detector()=="Person"):
        print("AI_Output:", ai_result)
        client.publish("ai", ai_result)

        bongden1 = setDevice1(True)
        client.publish("actuator_1", bongden1)

    else:
        print("AI_Output:", ai_result)
        client.publish("ai", ai_result)
        time.sleep(120)
        bongden1 = setDevice1(False)
        client.publish("actuator_1", bongden1)
    time.sleep(3)
    schedule.run_pending()