# java-fundamentals

A Java Swing practice project: one file, `src/CoolDashboard.java`, that draws a dark-themed dashboard window with custom painting.

The window has a gradient sidebar with nav buttons (hover and selected states) and a Log Out button, a header with a greeting and a clock that ticks every second, three stat cards (Active Users, Revenue, Server Load) and a "Live Activity" bar chart. The chart shifts in a new random value every 900 ms. The numbers on the cards are fixed placeholders and the chart data is random, so nothing here is real data. It uses the Nimbus look and feel if it is installed.

## Running it

Needs a JDK (no other dependencies).

```bash
javac -d out src/CoolDashboard.java
java -cp out CoolDashboard
```

There is also an IntelliJ project (`.idea/`, `javacooldashboard.java.iml`) if you'd rather open it there.
