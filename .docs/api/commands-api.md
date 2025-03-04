# BridgeNet / API / Commands

## Обзор системы команд

Система команд BridgeNet автоматически сканирует классы, помеченные аннотацией `@Command`, и регистрирует их через класс `CommandRegistry`. Команда может состоять из одного основного обработчика (mentor executor) и дополнительных подкоманд (producer executors). Обработка команды осуществляется через объект `CommandSession`, который передаёт данные об отправителе, аргументах и помогает выводить справочные сообщения.

## Создание новой команды

### Определение класса команды

Создайте новый класс, который будет представлять вашу команду, и пометьте его аннотацией `@Command`.  
Например, чтобы создать команду `gamemenu`, добавьте:

```java
package my.plugin.commands;

import me.moonways.bridgenet.api.command.CommandSession;
import me.moonways.bridgenet.api.command.annotation.Command;
import me.moonways.bridgenet.api.command.annotation.MentorExecutor;
import me.moonways.bridgenet.api.command.sender.ConsoleCommandSender;
import org.bukkit.ChatColor;

@Command("gamemenu")
public class GameMenuCommand {
    // Далее будут определены методы-обработчики команды
}
```

### Создание методов-обработчиков

В классе команды необходимо определить метод, который будет обрабатывать вызов команды без подкоманды. Для этого используйте аннотацию `@MentorExecutor`.

```java
@MentorExecutor
public void defaultCommand(CommandSession session) {
    // Проверка, что команда вызывается игроком, а не консолью
    if (session.getSender() instanceof ConsoleCommandSender) {
        session.getSender().sendMessage(ChatColor.RED + "Эта команда доступна только игрокам!");
        return;
    }
    
    // Выполнение логики команды
    session.getSender().sendMessage(ChatColor.GREEN + "Команда gamemenu выполнена локально на этом сервере!");
    
    // Здесь можно добавить вызов локальной логики, например, открытие GUI
}
```

### Использование дополнительных аннотаций

Вы можете дополнительно использовать следующие аннотации:

- **@Alias** – для указания дополнительных имен (алиасов) команды.  
  Пример:
  ```java
  @Alias("menu")
  ```
- **@Permission** – для задания требуемых прав для выполнения команды.  
  Пример:
  ```java
  @Permission("gamemenu.use")
  ```
- **@ProducerExecutor** – для создания подкоманд, если требуется разделение логики на несколько методов.  
  Пример:
  ```java
  @ProducerExecutor("help")
  public void help(CommandSession session) {
      session.printDefaultMessage("Используйте /gamemenu для открытия игрового меню.");
  }
  ```

## Регистрация команды
В основном классе плагина (наследнике `JavaPlugin`) вызовите метод `registerCommand` из `CommandRegistry`:

   ```java
   @Override
   public void onEnable() {
       // Регистрация команды через BridgeNet
       commandRegistry.registerCommand(new GameMenuCommand());
       getLogger().info("Команда 'gamemenu' зарегистрирована и готова к работе!");
   }
   ```

## Пример команды

Ниже приведён полный пример реализации команды `gamemenu`:

**GameMenuCommand.java**

```java
package my.plugin.commands;

import me.moonways.bridgenet.api.command.CommandSession;
import me.moonways.bridgenet.api.command.annotation.Command;
import me.moonways.bridgenet.api.command.annotation.MentorExecutor;
import me.moonways.bridgenet.api.command.sender.ConsoleCommandSender;
import org.bukkit.ChatColor;

@Command("gamemenu")
public class GameMenuCommand {

    @MentorExecutor
    public void defaultCommand(CommandSession session) {
        // Проверяем, что команда вызывается игроком
        if (session.getSender() instanceof ConsoleCommandSender) {
            session.getSender().sendMessage(ChatColor.RED + "Эта команда доступна только игрокам!");
            return;
        }
        
        // Логика команды: отправляем сообщение игроку
        session.getSender().sendMessage(ChatColor.GREEN + "Команда gamemenu выполнена локально на этом сервере!");
    }
}
```

**MyLocalPlugin.java**

```java
package my.plugin;

import me.moonways.bridgenet.api.command.CommandRegistry;
import me.moonways.bridgenet.api.inject.Inject;
import org.bukkit.plugin.java.JavaPlugin;
import my.plugin.commands.GameMenuCommand;

public final class MyLocalPlugin extends JavaPlugin {

    @Inject
    private CommandRegistry commandRegistry;

    @Override
    public void onEnable() {
        // Ручная регистрация команды в системе BridgeNet
        commandRegistry.registerCommand(new GameMenuCommand());
        getLogger().info("Команда 'gamemenu' успешно зарегистрирована!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Плагин выключен.");
    }
}
```