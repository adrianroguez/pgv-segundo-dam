# Code, Learn & Practice(Programación de Servicios y Procesos: "Procesos")
## Bloque 1: Conceptos básicos (teoría)
### 1. Define qué es un proceso y en qué se diferencia de un programa.
Un programa es un conjunto de instrucciones estático almacenado (por ejemplo en disco). Un proceso es la instancia activa de ese programa en ejecución, con su propio estado, recursos asignados por el sistema operativo.
https://en.wikipedia.org/wiki/Operating_system
### 2. Explica qué es el kernel y su papel en la gestión de procesos.
El kernel es la parte central del sistema operativo que administra recursos de hardware, crea y destruye procesos, planifica su ejecución, ejecuta cambios de contexto, gestiona memoria, y se encarga de la comunicación entre procesos.
https://en.wikipedia.org/wiki/Operating_system
### 3. ¿Qué son PID y PPID? Explica con un ejemplo.
PID: identificador único que el kernel asigna a cada proceso activo. 
PPID: identificador del proceso padre (el que creó al proceso actual).
Ejemplo: Si un proceso con PID = 100 crea otro proceso (hijo) con fork(), el hijo podría tener PID = 200; su PPID será 100. (Este esquema es estándar en Unix-like).
https://en.wikipedia.org/wiki/Process_identifier
### 4. Describe qué es un cambio de contexto y por qué es costoso.
Un cambio de contexto (context switch) es cuando el sistema operativo detiene un proceso/thread, guarda su estado (registros, contador de programa, puntero de pila, etc.), y carga el estado de otro para continuar su ejecución. Es costoso porque implica múltiples operaciones de guardado/restauración, puede afectar cachés y la eficiencia, y no realiza trabajo de usuario mientras se hace.
https://en.wikipedia.org/wiki/Context_switch
### 5. Explica qué es un PCB (Process Control Block) y qué información almacena.
El PCB es la estructura de datos del sistema operativo para cada proceso. Almacena: estado del proceso (nuevo, listo, ejecutando, esperando, terminado), PID, registros de CPU, contador de programa, punteros de pila, información de memoria, prioridad, uso de recursos, lista de archivos abiertos, etc.
https://en.wikipedia.org/wiki/Process_control_block
### 6. Diferencia entre proceso padre y proceso hijo.
Un proceso padre es aquel que crea otro proceso (el hijo), típicamente mediante llamada como fork(). El proceso hijo es el proceso creado; hereda algunos atributos del padre, pero tiene su propio PID, espacio de ejecución, etc.
### 7. Explica qué ocurre cuando un proceso queda huérfano en Linux.
Si un proceso padre termina antes que su hijo, este último queda huérfano (orphan process). En sistemas Unix-like, dichos procesos son automáticamente adoptados por un proceso del sistema (normalmente init o un “subreaper”) para asegurar que sigan teniendo un padre válido.
https://en.wikipedia.org/wiki/Orphan_process
### 8. ¿Qué es un proceso zombie? Da un ejemplo de cómo puede ocurrir.
Un proceso zombie es aquel que ha terminado (exit) pero cuyo estado de salida aún no ha sido recogido por su padre (no se ha llamado wait()/waitpid()). Permanece en la tabla de procesos con su entrada, aunque ya no utiliza CPU. Sólo tras que el padre lo “recoja”, se libera completamente.
https://en.wikipedia.org/wiki/Zombie_process
### 9. Diferencia entre concurrencia y paralelismo.
Concurrencia: varias tareas pueden progresar (estar activas) durante el mismo intervalo de tiempo, pero no necesariamente al mismo instante. Puede lograrse con un único núcleo mediante alternancia (context switching). 
https://www.geeksforgeeks.org/operating-systems/difference-between-concurrency-and-parallelism/
Paralelismo: múltiples tareas se ejecutan simultáneamente al mismo instante, típicamente usando múltiples núcleos o procesadores.
https://www.geeksforgeeks.org/operating-systems/difference-between-concurrency-and-parallelism/
### 10. Explica qué es un hilo (thread) y en qué se diferencia de un proceso.
Un hilo es la unidad básica de ejecución dentro de un proceso: cada hilo tiene su propio contador de programa, registros y pila, pero comparte con otros hilos del mismo proceso el espacio de direcciones, memoria, archivos, etc. A diferencia de procesos, los hilos tienen menor sobrecarga al crearse o cambiarse entre ellos, porque comparten muchos recursos que un proceso completo debería duplicar.
https://en.wikipedia.org/wiki/Thread_%28computing%29
---
## Bloque 2: Práctica con comandos en Linux
### 11. Usa echo $$ para mostrar el PID del proceso actual.
```bash
echo $$
```
### 12. Usa echo $PPID para mostrar el PID del proceso padre.
```bash
echo $PPID
```
### 13. Ejecuta pidof systemd y explica el resultado.
```bash
pidof systemd
```
### 14. Abre un programa gráfico (ejemplo: gedit) y usa pidof para obtener sus PID.
```bash
gedit &
pidof gedit
```
### 15. Ejecuta ps -e y explica qué significan sus columnas.
```bash
ps -e
```
- PID: ID del proceso.
- TTY: terminal asociado.
- TIME: tiempo de CPU usado.
- CMD: comando ejecutado.
### 16. Ejecuta ps -f y observa la relación entre procesos padre e hijo.
```bash
ps -f
```
### 17. Usa ps -axf o pstree para mostrar el árbol de procesos y dibújalo.
```bash
ps -axf
# o
pstree
```
### 18. Ejecuta top o htop y localiza el proceso con mayor uso de CPU.
```bash
top
# o
htop
```
### 19. Ejecuta sleep 100 en segundo plano y busca su PID con ps.
```bash
sleep 100 &
ps -e | grep sleep
```
### 20. Finaliza un proceso con kill y comprueba con ps que ya no está.
```bash
kill <PID>
ps -e | grep <PID>
```
---
## Bloque 3: Procesos y jerarquía
### 21. Identifica el PID del proceso init/systemd y explica su función.
```bash
pidof systemd
```
### 22. Explica qué ocurre con el PPID de un proceso hijo si su padre termina antes.
El hijo se convierte en huérfano y su PPID pasa a ser el de init (o un subreaper en Linux).
### 23. Ejecuta un programa que genere varios procesos hijos y observa sus PIDs con ps.
```bash
(for i in {1..3}; do sleep 60 & done; wait)
ps -f | grep sleep
```
### 24. Haz que un proceso quede en estado suspendido con Ctrl+Z y réanúdalo con fg.
```bash
gedit
```
Se suspende conCtrl+Z y se reanuda con:
```bash
fg
```
### 25. Lanza un proceso en segundo plano con & y obsérvalo con jobs.
```bash
gedit &
jobs
```
### 26. Explica la diferencia entre los estados de un proceso: Running, Sleeping, Zombie, Stopped.
- R (Running): en ejecución.
- S (Sleeping): esperando un evento.
- Z (Zombie): terminado, pendiente de que su padre recoja su estado.
- T (Stopped): detenido.
### 27. Usa ps -eo pid,ppid,stat,cmd para mostrar los estados de varios procesos.
```bash
ps -eo pid,ppid,stat,cmd
```
### 28. Ejecuta watch -n 1 ps -e y observa cómo cambian los procesos en tiempo real.
```bash
watch -n 1 ps -e
```
### 29. Explica la diferencia entre ejecutar un proceso con & y con nohup.
- &: lanza en segundo plano, pero se cierra si termina la shell.
- nohup: mantiene el proceso aunque se cierre la sesión.
### 30. Usa ulimit -a para ver los límites de recursos de procesos en tu sistema.
```bash
ulimit -a
```
