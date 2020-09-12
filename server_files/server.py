import socket
import time
from PIL import Image
import io
from PIL import ImageFile
ImageFile.LOAD_TRUNCATED_IMAGES = True

def get_bytes_stream(sock, length):
    buf = b''
    try:
        step = length
        while True:
            data = sock.recv(step)
            buf += data
            if len(buf) == length:
                break
            elif len(buf) < length:
                step = length - len(buf)
    except Exception as e:
        print(e)
    return buf[:length]

host = '203.255.176.79'
port = 8088 
 
server_sock = socket.socket(socket.AF_INET)
server_sock.bind((host, port))
server_sock.listen(1)

print("기다리는 중")
client_sock, addr = server_sock.accept()
print('Connected by', addr, '\n')

len_bytes_string = bytearray(client_sock.recv(1024))[2:]
len_bytes = len_bytes_string.decode("utf-8")
length = int(len_bytes)

img_bytes = get_bytes_stream(client_sock, length)
print(img_bytes)
print(len(img_bytes))

with open("img1.png", "wb") as writer:
    writer.write(img_bytes)
    
print("Done")

client_sock.close()
server_sock.close()