# coding=utf-8

from socket import *
from thread import start_new_thread

host = ''
port = 52301
msglen = 1024
encoding = 'UTF-8'
backlog = 5

def respond(input):
    # ustr = unicode(input, encoding)
    # ulist = list(ustr)
    # ulist.reverse()
    # ustr = u''.join(ulist)
    #str = ustr.encode(encoding)
    return str(len(input))

# 사용자 연결 핸들러 정의
def handler(clientsock, addr):
    print addr, '- connected!'
    try:
        while 1:
            data = clientsock.recv(msglen)
            if not data: break
            print addr, 'recv :', unicode(data, encoding)
            response = respond(data)
            clientsock.send(response)
            print addr, 'sent :', unicode(response, encoding)
        clientsock.close()
        print addr, '- connection closed gently'
    except Exception as e:
        print addr, '- connection closed by an error (', e, ')'

# Main 함수
# 리스너 소켓을 등록하여, 사용자 연결을 대기한다
# 사용자 연결이 들어올경우 해당 사용자에 대한 스레드를 하나 생성하고,
# 다시 다음 사용자 입력을 기다린다
if __name__ == '__main__':
    serversock = socket(AF_INET, SOCK_STREAM)
    serversock.setsockopt(SOL_SOCKET, SO_REUSEADDR, 1)
    serversock.bind((host, port))
    serversock.listen(backlog)
    print u'● 서버 작동시작'
    while 1:
        clientsock, addr = serversock.accept()
        start_new_thread(handler, (clientsock, addr))
