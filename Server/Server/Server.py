# coding=utf-8

from os import system
from socket import *
from thread import start_new_thread



import msgpack

host = ''
port = 52301
msglen = 8192
encoding = 'UTF-8'
backlog = 5

# 사용자 연결 핸들러 정의
def handler(clientsock, addr):
    print u''
    print u'────────────────────'
    print u''
    try:
        # 네트워킹
        unpacker = msgpack.Unpacker()
        while True:
            packet = clientsock.recv(msglen)
            if not packet: break;
            unpacker.feed(packet)
        clientsock.close()
        msg = unpacker.unpack()

        # 디시리얼라이즈
        version = msg['version']
        data = msg['data']

        pref = data['pref']
        logs = data['logs']

        age = pref['age']
        job = pref['job']
        sex = pref['sex']

        print u'○ ', addr
        print u'version :', version
        print u'data\t┬ pref\t┬ age :', age
        print u'\t│\t│ job :', job
        print u'\t│\t└ sex :', sex
        print u'\t│'
        print u'\t└ logs\t: (', len(logs), u')'
        for log in logs:
            print u'\t\t', log
        print u''
        print repr(msg)
    except Exception as e:
        print addr, u'Connection aborted by an error (', e, u')'

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
        clientsock, (addr, port) = serversock.accept()
        start_new_thread(handler, (clientsock, addr))
