# Implementierung

- Die Anwendung ist [Java 24](https://openjdk.org/projects/jdk/24/) geschrieben.
- Die Api-Spezifikation kann im [Swagger Editor](https://editor-next.swagger.io/?url=https://raw.githubusercontent.com/xtay2/one-armed-bandit/refs/heads/master/docs/api-v1.yaml) eingesehen werden.

## How to run?
Spring Anwendung starten:
```bash
./gradlew clean bootRun
```

---

# Aufgabe: Einarmiger Bandit

Die Aufgabe ist es, einen einarmigen Banditen (Glückspielautomat) zu implementieren.
Gespielt werden soll mittels REST-Aufrufen.
Die Request- und Response-Bodys sollen im JSON-Format übergeben bzw. entgegengenommen werden.

Entwickle so gewissenhaft, wie du Software auch im Alltag entwickeln würdest.

### Regeln

Das Spiel funktioniert nach dem bekannten Prinzip: Ein Spieler versucht sein Glück am Automaten und zieht am Hebel.
Ein Spiel kostet 3 Kredits.
Die Maschine hat drei Räder, auf denen jeweils entweder ein Apfel, eine Banane oder eine Clementine erscheinen.
Wenn zufällig alle drei Räder übereinstimmen, gewinnt der Spieler.
In diesem Fall werden je nach Obstsorte folgende Gewinnsummen ausgezahlt:

- 3 Äpfel: 10 Kredits
- 3 Bananen: 15 Kredits
- 3 Clementinen: 20 Kredits

Ein Spieler kann Geld einzahlen oder es sich auszahlen lassen.

### Nicht-funktionale Anforderungen

1. Die Anwendung ist in _Java_ oder _Kotlin_ geschrieben.
2. Die Anwendung ist self-contained, zum Beispiel mit Hilfe von Spring Boot.  
   Sie lässt sich direkt starten mit: `java -jar <application.jar>`
3. Die Anwendung lässt sich via _Maven_ oder _Gradle_ ohne spezielle Anpassungen erstellen.
4. Stelle sicher, dass das Spiel funktioniert

### Optionale Anforderungen

Wenn noch ausreichend Zeit zur Verfügung steht kannst du die folgende optionale
Anforderung implementieren:
Der Spieler kann seinen Einsatz für ein Spiel erhöhen. Für seine Risikofreude wird er
im Gewinnfall mit entsprechend mehr Kredits belohnt

### Hilfestellungen

- https://de.wikipedia.org/wiki/Einarmiger_Bandit
- https://projects.spring.io/spring-boot/
- http://spring.io/guides/gs/rest-service/
- http://martinfowler.com/bliki/TestPyramid.html  