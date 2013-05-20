# coding=cp949

from socket import *
from thread import start_new_thread

host = ''
port = 52301
backlog = 5
recvlen = 1024

def response(key):
    return key

def handler(clientsock, addr):
    while 1:
        data = clientsock.recv(recvlen)
        if not data: break
        print repr(addr) + ' recv:' + repr(data)
        clientsock.send(response(data))
        print repr(addr) + ' sent:' + repr(response(data))

    clientsock.close()
    print repr(addr), '- closed connection'

if __name__ == '__main__':
    serversock = socket(AF_INET, SOCK_STREAM)
    serversock.setsockopt(SOL_SOCKET, SO_REUSEADDR, 1)
    serversock.bind((host, port))
    serversock.listen(backlog)
    while 1:
        print 'waiting ...',
        clientsock, addr = serversock.accept()
        print 'connected from:', addr
        start_new_thread(handler, (clientsock, addr))