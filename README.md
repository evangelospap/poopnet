# Poop Vibe

Poop Vibe is a playful social wellness app for logging bathroom sessions, tracking comfort and mood trends, and sharing selective updates with friends. The product combines a mobile-first Next.js PWA interface with a Spring Boot API that stores sessions, friendships, feed activity, stats, device registrations, and activity logs.

## What It Does

- Logs sessions with start/end times, mood, comfort level, privacy, notes, media, comments, and reactions.
- Tracks personal stats such as streaks, average duration, daily trends, mood breakdowns, and comfort trends.
- Shows a privacy-aware feed so users can share public or friends-only activity while keeping sensitive details hidden.
- Manages friend requests, accepted friendships, friend suggestions, and lightweight social reactions.
- Registers devices and push-token preferences for PWA notification flows.
- Records activity events for operational visibility through backend activity logs and actuator metrics.

## Project Shape

The app is split into two local applications:

- `src/` - Next.js 16 and React 19 frontend for the PWA-style dashboard, friends, stats, and profile screens.
- `backend/app/` - Java 25 and Spring Boot 4 API for persistence, business rules, metrics, and local development data.

The frontend currently uses local UI state and shared dashboard components. The backend exposes the domain API and stores development data in PostgreSQL.

## Main Screens

- Dashboard: session timer, mood selection, today stats, and recent feed activity.
- Friends: friend requests, close friends, recent social activity, and suggested friends.
- Stats: weekly trends, streak summaries, mood breakdowns, comfort trends, privacy mix, and best-time insights.
- Profile: user-facing account and preference surface.

## Backend Domains

- Users
- Poop sessions
- Session media, comments, and reactions
- Friendships
- Feed
- Stats
- Devices
- Activity logs

## Local Development

Run both apps from the project root:

```bash
cp .env.example .env
docker compose up -d postgres
npm run dev:all
```

This starts:

- Frontend: `http://localhost:3001`
- Backend: `http://localhost:8080`
- Actuator health: `http://localhost:8080/actuator/health`

The combined runner prefixes logs with `[frontend]` and `[backend]` and stops both processes when you press `Ctrl+C`. The backend logs its resolved URL at startup, for example `Poop Vibe backend served at http://localhost:8080`. Set `APP_PUBLIC_HOST` to also log a LAN or public hostname.

## Separate Commands

Frontend only:

```bash
npm run dev
```

To use another frontend port:

```bash
FRONTEND_PORT=3002 npm run dev
```

Backend only:

```bash
npm run dev:backend
```

Database only:

```bash
docker compose up -d postgres
```

Backend compile check:

```bash
cd backend/app
JAVA_TOOL_OPTIONS=-Djdk.attach.allowAttachSelf=true JAVA_HOME=/Library/Java/JavaVirtualMachines/temurin-25.jdk/Contents/Home mvn -DskipTests test-compile
```

## Database

The backend uses PostgreSQL for local and production-like development. Copy `.env.example` to `.env`, adjust values if needed, then start Postgres with Docker Compose:

```bash
cp .env.example .env
docker compose up -d postgres
```

Schema migrations are managed by Flyway under `backend/app/src/main/resources/db/migration/`.
