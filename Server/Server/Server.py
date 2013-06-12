# coding=utf-8

from socket import *
from thread import start_new_thread
import msgpack
import MySQLdb

host = ''
port = 52301
msglen = 8192
encoding = 'UTF-8'
backlog = 100

# 사용자 연결 핸들러 정의
def handler(clientsock, addr):
    querylog = terminallog = filelog = ''
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
        age = pref['age']
        job = pref['job']
        sex = pref['sex']

        logs = data['logs']

        db = MySQLdb.connect('localhost','simnalamburt','AjrPotccoe,13trdo20!','simnalamburt')
        try:
            cursor = db.cursor()
            querylog = '  ┌ %-21s %-11s Age Job Sex\n' % ('Time', 'Duration')
            for (Time,Duration) in logs:
                cursor.execute('INSERT INTO ScreenLog(Time,Duration, Age, Job, Sex) VALUES(%d,%d,%d,%d,%d)' % (Time,Duration,age,job,sex))
                querylog += '  │ %-21d %-11d %-3d %-3d %-3d\n' % (Time,Duration,age,job,sex)
            cursor.close()
            db.commit()
        except MySQLdb.Error, e:
            db.rollback()
        finally:
            db.close()

        terminallog = '%s Sended %d logs\n' % (str(addr), len(logs))
        filelog = '\n%s%s' % (terminallog, querylog)
    except Exception, e:
        terminallog = '%s Caused an error : %s\n' % (str(addr), str(e))
        filelog = '\n%s' % terminallog
    finally:
        print terminallog,
        f = open('Server.py.log', 'a')
        f.write(filelog)
        f.close()


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
    try:
        while 1:
            clientsock, (addr, port) = serversock.accept()
            start_new_thread(handler, (clientsock, addr))
    except KeyError:
        print u'서버 종료'
    except KeyboardInterrupt:
        print u'서버 종료'
    except Exception, (errno, msg):
        print u'예외 발생 (%d)' % errno
        print msg
