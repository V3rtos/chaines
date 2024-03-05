<div align="center">

<!--suppress CheckImageSize -->
<img src=".assets/logo.png" alt="drawing" width="500"/>

# BRIDGENET

Protocol connections clouds & manipulations<br>
built on layer-services architecture.

</div>

---

## Что это такое?

Это протокольная система, обеспечивающая многопоточное<br>
соединение и связь между внутренними серверами и игроками<br>
играющими на них.<br>

Также в эту систему встроено несколько API,<br>
позволяющих осуществлять мобильную коррекцию, манипулирование и маршрутизацию<br>
данных по нужным каналам и процессам<br>

---

## Модули и их API

Система Bridgenet - модульная система. Каждый модуль отвечает за определенную задачу
<br>жизнедеятельности системы.
<br>В указанных ниже документациях можно разобрать каждый их них подробнее:

* [Application Programming Interface (API)](.docs/api.md):
    * [API / Autorun](.docs/api/autorun-api.md)
    * [API / Command](.docs/api/commands-api.md)
    * [API / Event](.docs/api/events-api.md)
    * [API / Inject](.docs/api/inject-api.md)
    * [API / JAXB](.docs/api/jaxb-api.md)
    * [API / Proxy](.docs/api/proxy-api.md)
    * [API / Scheduler](.docs/api/scheduler-api.md)
* [Assembly](.docs/assembly.md)
* [Bootstrap](.docs/bootstrap.md)
* [Connector](.docs/connector.md)
* [JDBC](.docs/jdbc.md)
* [Metrics](.docs/metrics.md)
* [Message Transfer Protocol (MTP)](.docs/mtp.md)
* [REST](.docs/rest.md)
* [Services and Endpoints](.docs/services.md)
    * [ENDPOINT / Auth](.docs/services/auth-endpoint.md)
    * [ENDPOINT / Bus](.docs/services/bus-endpoint.md)
    * [ENDPOINT / Friends](.docs/services/friends-endpoint.md)
    * [ENDPOINT / Games](.docs/services/games-endpoint.md)
    * [ENDPOINT / GUI](.docs/services/gui-endpoint.md)
    * [ENDPOINT / Guilds](.docs/services/guilds-endpoint.md)
    * [ENDPOINT / Parties](.docs/services/parties-endpoint.md)
    * [ENDPOINT / Players](.docs/services/players-endpoint.md)
    * [ENDPOINT / Reports](.docs/services/reports-endpoint.md)
    * [ENDPOINT / Servers](.docs/services/servers-endpoint.md)
    * [ENDPOINT / Socials](.docs/services/socials-endpoint.md)
* [Test-Engine](.docs/test-engine.md)

---

## Как пользоваться?

В корневой директории проекта находится скрипт под названием `bridgenet`,<br>
его необходимо запускать из терминала. Если мы введем данную команду, то<br>
получим список доступных команд и флагов, а также описание их процессов.<br>
<br>

Пройдемся по актуальным на момент написания документации:

---

```shell
$ ./bridgenet -e
```
_или можно иначе:_
```shell
$ ./bridgenet endpoints
```

- Данная команда выполняет полную компиляцию, конфигурацию и 
последующую сборку всех сервисов и их эндпоинтов.

---

```shell
$ ./bridgenet -a
```
_или можно иначе:_
```shell
$ ./bridgenet assemblyEndpoints
```

- Данная команда выполняет конфигурацию скомпилированных сервисов в сборке.

---

```shell
$ ./bridgenet -j
```
_или можно иначе:_
```shell
$ ./bridgenet jar
```

- Данная команда выполняет последовательную Maven компиляцию основных модулей проекта BridgeNet.

---

```shell
$ ./bridgenet -b
```
_или можно иначе:_
```shell
$ ./bridgenet build
```

- Данная команда выполняет полную (включая сервисы) и последовательную компиляцию всех модулей проекта BridgeNet.

---

## Сборка системы

После выполнения скриптов и команд, информация к которым предоставлена выше -<br>
в локальном проекте должна будет создасться папка `.build`<br>
<br>
На актуальный момент написания документации полное содержимое выглядит следующим образом:

<img src=".assets/build_directory_view.png"/>

Так выглядит полноценная и готовая сборка системы BridgeNet.

---

## Запуск и тестирование

Локальный запуск системы происходит из единственного класса во всем проекте,<br>
который содержит статический `main(String[] args)` метод:<br>
`me.moonways.bridgenet.bootstrap.AppStarter`

Для тестирования отдельных систем и подсистем был реализован 
модуль `test-engine`

Его основной код содержит базовую реализацию интеграции Bridgenet-сервера 
в запуск юнит-тестов, а каждый юнит-тест отдельно запускает 
локальную сборку Bridgenet-сервера.

---

