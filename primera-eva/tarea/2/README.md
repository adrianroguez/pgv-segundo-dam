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

```
14. Crea el servicio de usuario fecha-log.service (Type=simple, ejecuta tu script).
```

```
15. Diferencia enable vs start (modo usuario). Habilita el timer.
```

```
16. Logs recientes del servicio de usuario con journalctl --user.
```

```
### 2.3 — Observación de procesos sin root
17. Puertos en escucha (lo que puedas ver como usuario).
```

```
18. Ejecuta un proceso bajo cgroup de usuario con límite de memoria.
```

```
19. Observa CPU en tiempo real con top (si tienes htop, también vale).
```

```
20. Traza syscalls de tu propio proceso (p. ej., el sleep anterior).
```

```
### 2.4 — Estados y jerarquía (sin root)
21. Árbol de procesos con PIDs.
```

```
22. Estados y relación PID/PPID.
```

```
23. Suspende y reanuda uno de tus procesos (no crítico).
```

```
24. (Opcional) Genera un zombie controlado (no requiere root).
```

```
2.5 — Limpieza (solo tu usuario)
```

```
