# SI HappyTalk

## 로컬 환경
### RabbitMQ
```
$ ./rabbitmq-plugins.bat enable --offline rabbitmq_management
$ ./rabbitmq-plugins.bat enable --offline rabbitmq_stomp
$ ./rabbitmq-plugins.bat enable --offline rabbitmq_web_stomp
```

### 형상관리
```
$ ssh-keygen
$ cat ~/.ssh/id_rsa.pub | ssh -l si -p 29418 172.27.245.77 keys add
sipasswd
$ ssh -l si -p 29418 172.27.245.77
# Eclipse 에서 클론 ssh://si@172.27.245.77:29418/si-okbank-api.git
```
