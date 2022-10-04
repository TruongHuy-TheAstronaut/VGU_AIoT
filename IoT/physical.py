import time

print("Sensor and Actuator")

import serial.tools.list_ports

def getPort():
    ports = serial.tools.list_ports.comports()
    N = len(ports)
    commPort = "None"
    for i in range(0, N):
        port = ports[i]
        strPort = str(port)
        if "USB Serial" in strPort:
            splitPort = strPort.split(" ")
            commPort = (splitPort[0])
    return commPort

portName = getPort()

if portName != "None":
    ser = serial.Serial(port = portName, baudrate = 9600)

# ser = serial.Serial(port = portName, baudrate = 9600)
# print(ser)

relay1_ON = [0, 6, 0, 0, 0, 255, 200, 91] # crc 16-bit checksum
relay1_OFF = [0, 6, 0, 0, 0, 0, 136, 27]

relay2_ON = [15, 6, 0, 0 ,0, 255, 200, 164]
relay2_OFF = [15, 6, 0, 0, 0, 0, 136, 228]

# def turn_ON_or_OFF(myList = [], *args):
#     ser.write(myList)

def setDevice1(state):
    if state == True:
        ser.write(relay1_ON)
    else:
        ser.write(relay1_OFF)

def setDevice2(state):
    if state == True:
        ser.write(relay2_ON)
    else:
        ser.write(relay2_OFF)

#
# while True:
#     ser.write(relay1_ON)
#     time.sleep(2)
#     ser.write(relay1_OFF)
#     time.sleep(10)
#
# # 255 all 1 => on
# # 200,91 => error code
# # CRC 16 bit, crc table, check sum