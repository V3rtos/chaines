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

* [Bootstrap](.docs/bootstrap.md)
* [Assembly](.docs/assembly.md)
* [API Modules](.docs/api.md):
    * [API / Delayed Runnable`s](.docs/api/autorun-api.md)
    * [API / Event`s Subscribing](.docs/api/events-api.md)
    * [API / Dependency Injection](.docs/api/inject-api.md)
    * [API / Method Intercepting](.docs/api/proxy-api.md)
    * [API / Scheduling Task`s](.docs/api/scheduler-api.md)
* [Connector`s](.docs/connector.md)
* [Testing Engine](.docs/test-engine.md)
* [Database Engine](.docs/jdbc.md)
* [Profiler](.docs/profiler)
* [General Protocol](.docs/mtp.md)
* [Rest API](.docs/rest.md)
* [Services and Endpoints](.docs/services.md)
    * [ENDPOINT / AUTH](.docs/services/auth-endpoint.md)
    * [ENDPOINT / BUS](.docs/services/bus-endpoint.md)
    * [ENDPOINT / COMMANDS](.docs/services/commands-endpoint.md)
    * [ENDPOINT / FRIENDS](.docs/services/friends-endpoint.md)
    * [ENDPOINT / GAMES](.docs/services/games-endpoint.md)
    * [ENDPOINT / GUI](.docs/services/gui-endpoint.md)
    * [ENDPOINT / GUILDS](.docs/services/guilds-endpoint.md)
    * [ENDPOINT / LANGUAGE](.docs/services/language-endpoint.md)
    * [ENDPOINT / PARTIES](.docs/services/parties-endpoint.md)
    * [ENDPOINT / PERMISSIONS](.docs/services/permissions-endpoint.md)
    * [ENDPOINT / PLAYERS](.docs/services/players-endpoint.md)
    * [ENDPOINT / REPORTS](.docs/services/reports-endpoint.md)
    * [ENDPOINT / SERVERS](.docs/services/servers-endpoint.md)
    * [ENDPOINT / SETTINGS](.docs/services/settings-endpoint.md)

---

## Как пользоваться?

В корневой директории проекта находится скрипт под названием `bridgenet`,<br>
его необходимо запускать из терминала. Если мы введем данную команду, то<br>
получим список доступных команд и флагов, а также описание их процессов.<br>
<br>

Пройдемся по актуальным на момент написания документации:

---

```shell
$ ./bridgenet endpoints
```

- Данная команда выполняет полную компиляцию, конфигурацию и
  последующую сборку всех сервисов и их эндпоинтов.

---

```shell
$ ./bridgenet assemblyEndpoints
```

- Данная команда выполняет конфигурацию скомпилированных сервисов в сборке.

---

```shell
$ ./bridgenet jar
```

- Данная команда выполняет последовательную Maven компиляцию основных модулей проекта BridgeNet.

---

```shell
$ ./bridgenet build
```

- Данная команда выполняет полную (включая сервисы) и последовательную компиляцию всех модулей проекта BridgeNet.

---

## Сборка системы

После выполнения скриптов и команд, информация к которым предоставлена выше -<br>
в локальном проекте должна будет создаться папка `.build`<br>
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
модуль `testing`, разбитый на несколько модулей:

- **Test-Data**: Сборка модельных компонентов, вспомогательных тестированию - константы, базовые реализации абстракций,
  и прочее...
- **Test-Engine**: Фреймворк, базирующийся на кастомном раннере JUnit, вспомогающий к автоматизации процессов
  тестирования относительно системы Bridgenet.
- **Test-Units**: Юнит-тесты

