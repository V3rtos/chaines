# BridgeNet / Assembly

Assembly - Вложенный модуль системы Bridgenet, отвечающий за обработку 
<br>унифицированных ресурсов и конфигураций для сборки проекта.

---

## BUILD

В процессе компиляции и сборки проекта, скрипты получают все необходимые
<br>ресурсы и конфигурации проекта из директории 'etc' модуля `assembly`, перемещая
<br>их в директорию 'etc' в папке со сборкой '.build'.
<br>
<br>При дальнейшем запуске уже скомпилированного файла `bridgenet-server.jar`
<br>на виртуальной машине, модуль автоматически обнаруживает ресурсы,
<br>расположенные в корневой папке проекта в директории 'etc', и работает уже с ними.
<br>
<br>При запуске сервера на локальной машине (из IDE) - ресурсы подгружаются
<br>напрямую из директории 'etc' модуля `assembly`.

---

## API

Для того чтобы использовать ресурсы системы Bridgenet, необходимо
<br>проинжектить основной сервис этого модуля:
```java
@Inject
private ResourcesAssembly assembly;
```

<br>После чего из этого сервиса мы сможем получить данные для чтения этих самых ресурсов.
<br>Общий список доступных ресурсов хранится в виде констант в классе `me.moonways.bridgenet.assembly.ResourcesTypes`,
оттуда мы можем получить наименование нужного нам ресурса:
```java
String filename = ResourcesTypes.MTP_SETTINGS_JSON;
String filename = ResourcesTypes.METRICS_SETTINGS_JSON;
String filename = ResourcesTypes.RSI_CONFIG_XML;
```

Дальнейший процесс чтения производится при помощи вспомогательных утилит,
<br>например таких как `me.moonways.bridgenet.assembly.util.StreamToStringUtils`:

```java
public String readResourceContent(String resourceName) {
    return StreamToStringUtils.toStringFull(assembly.readResourceStream(resourceName), charset);
}
```
```java
String json = this.readResourceContent(ResourcesTypes.METRICS_SETTINGS_JSON);
```
или напрямую из основного сервиса `ResourcesAssembly`:
```java
String json = assembly.readResourceFullContent(ResourcesTypes.METRICS_SETTINGS_JSON);
```

---