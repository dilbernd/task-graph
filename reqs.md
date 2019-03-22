# Reqs #

```
Mario, 14:45
Tasks are identified by a unique string and every task has a set of other tasks that must be done before it can be completed.
Given a `Map<String, Set<String>>` with each key being the ID of a task, and each value being the IDs of tasks that must be
done before that specific task, and no cyclic dependencies between any two tasks, write a function that returns a list of
task IDs in an order where all dependencies for a task are in front of it in the list.
`List<String> sort(Map<String, Set<String>> tasks);`
Please attach your answer in a source file to your answer mail.


17:54
hmm das war auf den ersten blick einfacher als echt
und wie ist diese code-frage aufgekommen? für interviews bei der xaidat gemacht oder…?
hab jetzt nur extrem fragmentarisch dran denken können weil kinder definitiv merken dass programmierpuzzler net mit ihnen
spielen sind aber auf den 1. blick bin i net draufgekommen, möcht aber noch probieren
die funktion, die die task-list retourniert: sollen das alle tasks im graph sein, oder mit input a bestimmter task (und seine
rekursiven deps davor? oder eine liste von tasks und sie und alle ihre rekursiven deps?

Mario, 17:57
Alle tasks im Graphen in einer Liste in der sie ohne blockade der reihe nach ausgeführt werden können
```
