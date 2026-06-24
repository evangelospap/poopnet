import { existsSync, readFileSync } from "node:fs";
import { resolve } from "node:path";
import { spawn } from "node:child_process";

const defaultPort = "3001";
const envFromFiles = loadEnvFiles([".env", ".env.local"]);
const env = { ...envFromFiles, ...process.env };
const frontendPort = env.FRONTEND_PORT || defaultPort;
const nextCli = resolve("node_modules", "next", "dist", "bin", "next");

const child = spawn(process.execPath, [nextCli, "dev", "-p", frontendPort], {
  env,
  stdio: "inherit",
  windowsHide: true,
});

child.on("exit", (code, signal) => {
  if (signal) {
    process.kill(process.pid, signal);
    return;
  }

  process.exit(code ?? 0);
});

function loadEnvFiles(files) {
  const loaded = {};

  for (const file of files) {
    if (!existsSync(file)) {
      continue;
    }

    Object.assign(loaded, parseEnv(readFileSync(file, "utf8")));
  }

  return loaded;
}

function parseEnv(contents) {
  const parsed = {};

  for (const rawLine of contents.split(/\r?\n/)) {
    const line = rawLine.trim();

    if (!line || line.startsWith("#")) {
      continue;
    }

    const normalizedLine = line.startsWith("export ") ? line.slice(7).trim() : line;
    const separatorIndex = normalizedLine.indexOf("=");

    if (separatorIndex === -1) {
      continue;
    }

    const key = normalizedLine.slice(0, separatorIndex).trim();
    const value = normalizedLine.slice(separatorIndex + 1).trim();

    if (!key) {
      continue;
    }

    parsed[key] = stripEnvQuotes(value);
  }

  return parsed;
}

function stripEnvQuotes(value) {
  if (
    (value.startsWith('"') && value.endsWith('"')) ||
    (value.startsWith("'") && value.endsWith("'"))
  ) {
    return value.slice(1, -1);
  }

  return value;
}

