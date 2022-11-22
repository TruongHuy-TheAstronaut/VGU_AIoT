print("Hello The Brave New World")

import sys
from Adafruit_IO import MQTTClient
# from AI.simple_ai import *
from IoT.physical import *

AIO_FEED_ID = ["sensor-1", "sensor-2", "sensor-3", "actuator-1", "actuator-2", "vision-detection"]
AIO_USERNAME = "truonghuy"
AIO_KEY = "aio_NfpI49RKlhHiqlriMQ0LHSPLwBfn"

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
    # print("Nhan du lieu" + feed_id + " : "+ payload)
    state = True if (str(payload) == "1") else False
    # let state = (str(payload) == "1") ? True : False
    if feed_id == "actuator-1":
        setDevice1(state)
    if feed_id == "actuator-2":
        setDevice2(state)

client = MQTTClient(AIO_USERNAME , AIO_KEY)
client.on_connect = connected # passing function as object
client.on_disconnect = disconnected
client.on_message = message
client.on_subscribe = subscribe
client.connect() # call connect()
client.loop_background() # call loop_background()

while True:
    pass
    temperature = readTemperature()
    moisture = readMoisture()
    print(temperature, moisture)
    client.publish("sensor-1", temperature/100)
    client.publish("sensor-2", moisture/100)

# #Simple AI Code
# while True:
#     time.sleep(5)
#     image_capture()
#     ai_result = image_detector()
#     client.publish("vision_detection", ai_result)