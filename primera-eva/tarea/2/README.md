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
1. ¿Qué es systemd y en qué se diferencia de SysV init?

2. Servicio vs proceso.

3. ¿Qué son los cgroups y para qué sirven?

4. ¿Qué es un unit file y tipos (service, timer, socket, target)?

5. ¿Qué hace journalctl y cómo ver logs de usuario?

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
