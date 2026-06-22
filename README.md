# Poop Vibe

Next.js frontend plus Spring Boot backend for the Poop Vibe PWA.

## Development

Run both apps from the project root:

```bash
npm run dev:all
```

This starts:

- Frontend: `http://localhost:3000`
- Backend: `http://localhost:8080`
- H2 console: `http://localhost:8080/h2-console`
- Actuator health: `http://localhost:8080/actuator/health`

The combined runner prefixes logs with `[frontend]` and `[backend]` and stops both processes when you press `Ctrl+C`.
The backend logs its resolved URL at startup, for example `Poop Vibe backend served at http://localhost:8080`.
Set `APP_PUBLIC_HOST` to also log a LAN or public hostname.

## Separate Commands

Frontend only:

```bash
npm run dev
```

Backend only:

```bash
npm run dev:backend
```

Backend compile check:

```bash
cd backend/app
JAVA_TOOL_OPTIONS=-Djdk.attach.allowAttachSelf=true JAVA_HOME=/Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home mvn -DskipTests test-compile
```

## Backend Data

The backend uses local H2 storage at:

```text
backend/app/data/
```

That folder is ignored by Git because it contains local runtime data.
