# Code, Learn & Practice
## Preparación
```
 dam  ~  mkdir -p ~/dam/{bin,logs,units}
 dam  ~  export DAM=~/dam
 dam  ~  echo 'export DAM=~/dam' >> ~/.bashrc
 dam  ~  systemctl --user --version | head -n1
systemd 255 (255.4-1ubuntu8.6)
 dam  ~  systemctl --user status --no-pager | head -n5
● a108pc10
    State: running
    Units: 261 loaded (incl. loaded aliases)
     Jobs: 0 queued
   Failed: 0 units
```
---
## Bloque 1 — Conceptos
### 1. ¿Qué es systemd y en qué se diferencia de SysV init?

systemd es un sistema init moderno (PID 1) + gestor de servicios para Linux, que arranca y administra servicios, dependencias, sesiones y más. https://es.wikipedia.org/wiki/Systemd
- systemd arranca servicios en paralelo y resuelve dependencias automáticamente. https://www.computernetworkingnotes.com/linux-tutorials/differences-between-sysvinit-upstart-and-systemd.html
- Usa unit files declarativos en vez de scripts de shell. https://www.loggly.com/ultimate-guide/linux-logging-with-systemd/
- Integra logging (journald), control de recursos (cgroups) y activación por socket / eventos. https://www.loggly.com/ultimate-guide/linux-logging-with-systemd/
- SysV init es más simple, secuencial, sin gestión sofisticada de dependencias ni control fino de recursos. https://www.computernetworkingnotes.com/linux-tutorials/differences-between-sysvinit-upstart-and-systemd.html
### 2. Servicio vs proceso.

Un proceso es una instancia en ejecución de un programa (con su PID, memoria, etc.).
Un servicio (o demonio) es un proceso o conjunto de procesos gestionados por el sistema, típicamente persistentes, que ofrecen algún tipo de funcionalidad al sistema o a otros programas. En systemd, los servicios se representan como unit files tipo .service
### 3. ¿Qué son los cgroups y para qué sirven?

cgroups (“control groups”) es una característica del kernel Linux que permite agrupar procesos en jerarquías y controlar/limitar/monitorizar su uso de recursos (CPU, memoria, I/O, red, etc.). https://man7.org/linux/man-pages/man7/cgroups.7.html
- Limitar el consumo de memoria o CPU de grupos de procesos.
- Aislar procesos para contenedores.
- Contabilizar recursos usados por servicios.
- Evitar “fork bombs” imponiendo límites de cantidad de procesos. https://securitylabs.datadoghq.com/articles/container-security-fundamentals-part-4/
### 4. ¿Qué es un unit file y tipos (service, timer, socket, target)?

Un unit file es un archivo de configuración (formato ini) que define cómo systemd debe gestionar un recurso (servicio, socket, objetivo, etc.). https://www.freedesktop.org/software/systemd/man/systemd.unit.html
- .service: define un servicio que arranca un proceso. https://unix.stackexchange.com/questions/159462/what-is-systemds-target-service-and-socket
- .socket: define un socket (TCP, UNIX) para activación por demanda del servicio asociado. https://unix.stackexchange.com/questions/159462/what-is-systemds-target-service-and-socket
- .timer: agenda la activación de servicios, similar a cron, con calendarios o intervalos. https://wiki.archlinux.org/title/Systemd/Timers
- .target: unidad que agrupa otras unidades para representar estados o fases del sistema (equivalente moderno a runlevels). https://unix.stackexchange.com/questions/159462/what-is-systemds-target-service-and-socket
### 5. ¿Qué hace journalctl y cómo ver logs de usuario?

journalctl es la herramienta para consultar los registros del journal recolectados por systemd-journald. https://www.freedesktop.org/software/systemd/man/journalctl.html?utm_source=chatgpt.com
- journalctl — muestra todos los logs accesibles al usuario. https://www.freedesktop.org/software/systemd/man/journalctl.html
- journalctl -u nombre.service — filtra logs de ese servicio. https://unix.stackexchange.com/questions/225401/how-to-see-full-log-from-systemctl-status-service
- journalctl --user -u servicio_usuario o journalctl --user --user-unit=servicio — logs de servicios de usuario (dependiendo de la versión) https://serverfault.com/questions/806469/how-to-allow-a-user-to-use-journalctl-to-see-user-specific-systemd-service-logs
- Necesitas que el journal sea persistente (Storage=persistent en journald.conf) para que se guarden entre reinicios. https://serverfault.com/questions/806469/how-to-allow-a-user-to-use-journalctl-to-see-user-specific-systemd-service-logs
---
## Bloque 2 — Práctica guiada
### 2.1 — PIDs básicos
11. PID de tu shell y su PPID.
```
 dam  ~  echo "PID=$$  PPID=$PPID"
PID=17887  PPID=17878

```
**Pregunta:** ¿Qué proceso es el padre (PPID) de tu shell ahora?

12. PID del systemd --user (manager de usuario) y explicación.
```
 dam  ~  pidof systemd || pgrep -u "$USER" -x systemd
3319

```
**Pregunta:** ¿Qué hace el user manager de systemd para tu sesión?

### 2.2 — Servicios de usuario con systemd
13. Prepara directorios y script de práctica.
```
mkdir -p ~/.config/systemd/user "$DAM"/{bin,logs}
cat << 'EOF' > "$DAM/bin/fecha_log.sh"
#!/usr/bin/env bash
mkdir -p "$HOME/dam/logs"
echo "$(date --iso-8601=seconds) :: hello from user timer" >> "$HOME/dam/logs/fecha.log"
EOF
chmod +x "$DAM/bin/fecha_log.sh"
```
14. Crea el servicio de usuario fecha-log.service (Type=simple, ejecuta tu script).
```
cat << 'EOF' > ~/.config/systemd/user/fecha-log.service
[Unit]
Description=Escribe fecha en $HOME/dam/logs/fecha.log

[Service]
Type=simple
ExecStart=%h/dam/bin/fecha_log.sh
EOF

systemctl --user daemon-reload
systemctl --user start fecha-log.service
systemctl --user status fecha-log.service --no-pager -l | sed -n '1,10p'

○ fecha-log.service - Escribe fecha en $HOME/dam/logs/fecha.log
     Loaded: loaded (/home/dam/.config/systemd/user/fecha-log.service; static)
     Active: inactive (dead)

sep 23 18:42:10 a108pc04 systemd[3313]: Started fecha-log.service - Escribe fecha en $HOME/dam/logs/fecha.log.
```
15. Diferencia enable vs start (modo usuario). Habilita el timer.
```
cat << 'EOF' > ~/.config/systemd/user/fecha-log.timer
[Unit]
Description=Timer (usuario): ejecuta fecha-log.service cada minuto

[Timer]
OnCalendar=*:0/1
Unit=fecha-log.service
Persistent=true

[Install]
WantedBy=timers.target
EOF

systemctl --user daemon-reload
systemctl --user enable --now fecha-log.timer
systemctl --user list-timers --all | grep fecha-log || true

Created symlink /home/dam/.config/systemd/user/timers.target.wants/fecha-log.timer → /home/dam/.config/systemd/user/fecha-log.timer.
Tue 2025-09-23 18:52:00 WEST  25s -                                       - fecha-log.timer                fecha-log.service
```
16. Logs recientes del servicio de usuario con journalctl --user.
```
journalctl --user -u fecha-log.service -n 10 --no-pager

sep 23 18:42:10 a108pc04 systemd[3313]: Started fecha-log.service - Escribe fecha en $HOME/dam/logs/fecha.log.
sep 23 18:52:22 a108pc04 systemd[3313]: Started fecha-log.service - Escribe fecha en $HOME/dam/logs/fecha.log.
sep 23 18:53:22 a108pc04 systemd[3313]: Started fecha-log.service - Escribe fecha en $HOME/dam/logs/fecha.log.
```
### 2.3 — Observación de procesos sin root
17. Puertos en escucha (lo que puedas ver como usuario).
```
lsof -i -P -n | grep LISTEN || ss -lntp

State   Recv-Q   Send-Q     Local Address:Port      Peer Address:Port  Process  
LISTEN  0        4096       127.0.0.53%lo:53             0.0.0.0:*              
LISTEN  0        32         192.168.122.1:53             0.0.0.0:*              
LISTEN  0        64               0.0.0.0:35531          0.0.0.0:*              
LISTEN  0        4096             0.0.0.0:51699          0.0.0.0:*              
LISTEN  0        64               0.0.0.0:2049           0.0.0.0:*              
LISTEN  0        4096          127.0.0.54:53             0.0.0.0:*              
LISTEN  0        4096           127.0.0.1:631            0.0.0.0:*              
LISTEN  0        4096             0.0.0.0:111            0.0.0.0:*              
LISTEN  0        4096             0.0.0.0:34137          0.0.0.0:*              
LISTEN  0        4096             0.0.0.0:8000           0.0.0.0:*              
LISTEN  0        4096             0.0.0.0:56321          0.0.0.0:*              
LISTEN  0        4096             0.0.0.0:36987          0.0.0.0:*              
LISTEN  0        4096                [::]:44183             [::]:*              
LISTEN  0        4096                   *:9100                 *:*              
LISTEN  0        64                  [::]:41167             [::]:*              
LISTEN  0        4096                [::]:42395             [::]:*              
LISTEN  0        4096               [::1]:631               [::]:*              
LISTEN  0        64                  [::]:2049              [::]:*              
LISTEN  0        4096                [::]:36161             [::]:*              
LISTEN  0        4096                [::]:111               [::]:*              
LISTEN  0        511                    *:80                   *:*              
LISTEN  0        4096                   *:22                   *:*              
LISTEN  0        4096                [::]:8000              [::]:*              
LISTEN  0        4096                [::]:38675             [::]:*    
```
18. Ejecuta un proceso bajo cgroup de usuario con límite de memoria.
```
systemd-run --user --scope -p MemoryMax=50M sleep 200
ps -eo pid,ppid,cmd,stat | grep "[s]leep 200"

Running as unit: run-r4fee977457b44213acd51a630f1f7c30.scope; invocation ID: 44c656b89f144423876fc55ea9dd7861
```
19. Observa CPU en tiempo real con top (si tienes htop, también vale).
```
top -b -n 1 | head -n 15

top - 19:01:53 up  3:59,  1 user,  load average: 0,74, 0,54, 0,48
Tareas: 408 total,   1 ejecutar,  406 hibernar,    0 detener,    1 zombie
%Cpu(s):  0,0 us,  0,6 sy,  0,0 ni, 98,1 id,  1,3 wa,  0,0 hi,  0,0 si,  0,0 st 
MiB Mem :  31453,3 total,  24455,4 libre,   4412,4 usado,   3051,4 búf/caché    
MiB Intercambio:   2048,0 total,   2048,0 libre,      0,0 usado.  27040,9 dispon

    PID USUARIO   PR  NI    VIRT    RES    SHR S  %CPU  %MEM     HORA+ ORDEN
      1 root      20   0   23236  13912   9176 S   0,0   0,0   0:01.82 systemd
      2 root      20   0       0      0      0 S   0,0   0,0   0:00.02 kthreadd
      3 root      20   0       0      0      0 S   0,0   0,0   0:00.00 pool_wo+
      4 root       0 -20       0      0      0 I   0,0   0,0   0:00.00 kworker+
      5 root       0 -20       0      0      0 I   0,0   0,0   0:00.00 kworker+
      6 root       0 -20       0      0      0 I   0,0   0,0   0:00.00 kworker+
      7 root       0 -20       0      0      0 I   0,0   0,0   0:00.00 kworker+
     10 root       0 -20       0      0      0 I   0,0   0,0   0:00.00 kworker+
```
20. Traza syscalls de tu propio proceso (p. ej., el sleep anterior).
```
pid=$(pgrep -u "$USER" -x sleep | head -n1)
strace -p "$pid" -e trace=nanosleep -tt -c -f 2>&1 | sed -n '1,10p'

strace: must have PROG [ARGS] or -p PID
Try 'strace -h' for more information.
```
### 2.4 — Estados y jerarquía (sin root)
21. Árbol de procesos con PIDs.
```
pstree -p | head -n 50

systemd(1)-+-ModemManager(1853)-+-{ModemManager}(1863)
           |                    |-{ModemManager}(1865)
           |                    `-{ModemManager}(1867)
           |-NetworkManager(1823)-+-{NetworkManager}(1858)
           |                      |-{NetworkManager}(1859)
           |                      `-{NetworkManager}(1860)
           |-accounts-daemon(1157)-+-{accounts-daemon}(1200)
           |                       |-{accounts-daemon}(1201)
           |                       `-{accounts-daemon}(1821)
           |-agetty(2227)
           |-apache2(2281)-+-apache2(2294)
           |               |-apache2(2295)
           |               |-apache2(2296)
           |               |-apache2(2297)
           |               `-apache2(2298)
           |-at-spi2-registr(3664)-+-{at-spi2-registr}(3673)
           |                       |-{at-spi2-registr}(3674)
           |                       `-{at-spi2-registr}(3675)
           |-avahi-daemon(1159)---avahi-daemon(1253)
           |-blkmapd(1077)
           |-colord(2018)-+-{colord}(2025)
           |              |-{colord}(2026)
           |              `-{colord}(2028)
           |-containerd(1995)-+-{containerd}(2020)
           |                  |-{containerd}(2021)
           |                  |-{containerd}(2022)
           |                  |-{containerd}(2023)
           |                  |-{containerd}(2024)
           |                  |-{containerd}(2037)
           |                  |-{containerd}(2038)
           |                  |-{containerd}(2052)
           |                  |-{containerd}(2053)
           |                  |-{containerd}(2054)
           |                  |-{containerd}(2059)
           |                  |-{containerd}(2060)
           |                  |-{containerd}(2061)
           |                  `-{containerd}(2516)
           |-containerd-shim(3134)-+-apache2(3157)-+-apache2(3271)
           |                       |               |-apache2(3272)
           |                       |               |-apache2(3273)
           |                       |               |-apache2(3274)
           |                       |               `-apache2(3275)
           |                       |-{containerd-shim}(3135)
           |                       |-{containerd-shim}(3136)
           |                       |-{containerd-shim}(3137)
           |                       |-{containerd-shim}(3138)
           |                       |-{containerd-shim}(3139)
           |                       |-{containerd-shim}(3140)
           |                       |-{containerd-shim}(3141)
           |                       |-{containerd-shim}(3142)

```
22. Estados y relación PID/PPID.
```
ps -eo pid,ppid,stat,cmd | head -n 20

    PID    PPID STAT CMD
      1       0 Ss   /sbin/init splash
      2       0 S    [kthreadd]
      3       2 S    [pool_workqueue_release]
      4       2 I<   [kworker/R-rcu_g]
      5       2 I<   [kworker/R-rcu_p]
      6       2 I<   [kworker/R-slub_]
      7       2 I<   [kworker/R-netns]
     10       2 I<   [kworker/0:0H-events_highpri]
     12       2 I<   [kworker/R-mm_pe]
     13       2 I    [rcu_tasks_kthread]
     14       2 I    [rcu_tasks_rude_kthread]
     15       2 I    [rcu_tasks_trace_kthread]
     16       2 S    [ksoftirqd/0]
     17       2 I    [rcu_preempt]
     18       2 S    [migration/0]
     19       2 S    [idle_inject/0]
     20       2 S    [cpuhp/0]
     21       2 S    [cpuhp/1]
     22       2 S    [idle_inject/1]
```
23. Suspende y reanuda uno de tus procesos (no crítico).
```
# Crea un proceso propio en segundo plano
sleep 120 &
pid=$!
# Suspende
kill -STOP "$pid"
# Estado
ps -o pid,stat,cmd -p "$pid"
# Reanuda
kill -CONT "$pid"
# Estado
ps -o pid,stat,cmd -p "$pid"

sleep 120 &
pid=$!
# Suspende
kill -STOP "$pid"
# Estado
ps -o pid,stat,cmd -p "$pid"
# Reanuda
kill -CONT "$pid"
# Estado
ps -o pid,stat,cmd -p "$pid"
[1] 72246

[1]+  Detenido                sleep 120
    PID STAT CMD
  72246 T    bash
    PID STAT CMD
  72246 S    sleep 120
```
24. (Opcional) Genera un zombie controlado (no requiere root).
```
cat << 'EOF' > "$DAM/bin/zombie.c"
#include <stdlib.h>
#include <unistd.h>
int main() {
  if (fork() == 0) { exit(0); } // hijo termina
  sleep(60); // padre no hace wait(), hijo queda zombie hasta que padre termine
  return 0;
}
EOF
gcc "$DAM/bin/zombie.c" -o "$DAM/bin/zombie" && "$DAM/bin/zombie" &
ps -el | grep ' Z '

[1]+  Hecho                   sleep 120
[1] 74798
0 Z  1001   11630   11618  0  80   0 -     0 -      ?        00:00:00 sd_espeak-ng-mb
```
2.5 — Limpieza (solo tu usuario)
```
systemctl --user disable --now fecha-log.timer
systemctl --user stop fecha-log.service
rm -f ~/.config/systemd/user/fecha-log.{service,timer}
systemctl --user daemon-reload
rm -rf "$DAM/bin" "$DAM/logs" "$DAM/units"
```
