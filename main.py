# print("Hello The Brave New World")

import sys
from Adafruit_IO import MQTTClient
from simple_AI import *
from rasberry_physical import *
import schedule
import time
AIO_FEED_ID = ["sensor_1", "sensor_2", "sensor_3", "actuator_1", "actuator_2","actuator_3"] #,
AIO_USERNAME = "truonghuy"
AIO_KEY = "aio_gfWr45d8qH5AiuJ6wU373U5IfijK"

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