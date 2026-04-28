---
name: Don't remove containers on port conflict
description: When a port is already in use, find a new port instead of removing existing containers
type: feedback
---

When spinning up Docker containers and encountering a port-already-in-use error, do NOT remove or stop whatever is already using that port. Instead, remap the Docker container to a different host port (e.g., change `5432:5432` to `5433:5432`).

**Why:** The existing process on that port may be unrelated in-progress work the user cares about.

**How to apply:** On any port conflict during `docker compose up`, edit the compose file to use a free host port and retry.
