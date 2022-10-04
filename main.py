print("Hello The Brave New World")

import sys
from Adafruit_IO import MQTTClient
# from simple_ai import *
from physical import *
import time

AIO_FEED_ID = ["sensor_1", "sensor_2", "sensor_3", "actuator_1", "actuator_2", "vision_detection"]
AIO_USERNAME = "truonghuy"
AIO_KEY = "aio_PXVN21tAWJcKoCCthvhVAnu7mish"

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
    print("Nhan du lieu" + feed_id + " : "+ payload)
    state = True if (str(payload) == "1") else False
    # let state = (str(payload) == "1") ? True : False
    if feed_id == "actuator_1":
        setDevice1(state)
    if feed_id == "actuator_2":
        setDevice2(state)

client = MQTTClient(AIO_USERNAME , AIO_KEY)
client.on_connect = connected
client.on_disconnect = disconnected
client.on_message = message
client.on_subscribe = subscribe
client.connect()
client.loop_background()
#
# def set_state(client, feed_id, payload):
#     state = (str(payload) == "1") ? True: False
#     if feed_id == "actuator_1":
#         setDevice1(state)
#     if feed_id == "actuator_2":
#         setDevice2(state)

while True:
    pass


# while True:
#     time.sleep(5)
#     image_capture()
#     ai_result = image_detector()
#     client.publish("vision_detection", ai_result)