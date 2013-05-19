# coding=cp949

from socket import *
import thread

BUFF = 1024
HOST = '127.0.0.1'
PORT = 52301

def response(key):
    return key

def handler(clientsock, addr):
    while 1:
        data = clientsock.recv(BUFF)
        if not data: break
        print repr(addr) + ' recv:' + repr(data)
        clientsock.send(response(data))
        print repr(addr) + ' sent:' + repr(response(data))

    clientsock.close()
    print repr(addr), '- closed connection'

if __name__ == '__main__':
    serversock = socket(AF_INET, SOCK_STREAM)
    serversock.setsockopt(SOL_SOCKET, SO_REUSEADDR, 1)
    serversock.bind((HOST, PORT)) # gethostname()
    serversock.listen(100)
    while 1:
        print 'waiting for connection ... '
        clientsock, addr = serversock.accept()
        print 'connected from:', addr
        thread.start_new_thread(handler, (clientsock, addr))