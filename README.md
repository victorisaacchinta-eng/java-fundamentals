# java-fundamentals

A Java Swing practice project. Everything is in one file, `src/CoolDashboard.java`, which opens a dark-themed dashboard window drawn with custom `paintComponent` code.

## What it shows

- A gradient sidebar with five nav buttons. Clicking one highlights it and hovering shows a soft highlight; they do not switch views. The Log Out button has no action attached.
- A header with a greeting and a clock that updates every second (`javax.swing.Timer`).
- Three stat cards (Active Users, Revenue, Server Load). The values are hardcoded placeholders.
- A "Live Activity" bar chart of 14 bars. Every 900 ms the oldest value drops off and a new random value is added.

It uses the Nimbus look and feel when it is available and falls back to the default otherwise.

## Running it

Needs a JDK 8 or newer. There are no other dependencies.

```bash
javac -d out src/CoolDashboard.java
java -cp out CoolDashboard
```

`out/` is already in `.gitignore`. The repo also includes IntelliJ project files (`.idea/`, `javacooldashboard.java.iml`) if you prefer to open it there and run `main` directly.
