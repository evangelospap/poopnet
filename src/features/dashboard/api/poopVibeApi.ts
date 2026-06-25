import type { Mood } from "../types";

const API_BASE_URL =
  process.env.NEXT_PUBLIC_API_BASE_URL?.replace(/\/$/, "") ||
  "http://localhost:8080";

export type DemoUser = {
  id: number;
  label: string;
  initials: string;
};

export const demoUsers: DemoUser[] = [
  { id: 1001, label: "Evangelos", initials: "EP" },
  { id: 1002, label: "Alex", initials: "A" },
];

type PublicKeyResponse = {
  publicKey: string;
};

type PushKeys = {
  p256dh: string;
  auth: string;
};

type DeviceRegistration = {
  userId: number;
  endpoint: string;
  keys: PushKeys;
  app: "WEB";
  userAgent: string;
  pushEnabled: boolean;
};

type CreateSessionRequest = {
  clientId: string;
  userId: number;
  startTime: string;
  endTime: string;
  mood: "LIGHT" | "STEADY" | "SPICY";
  comfortLevel: "OKAY";
  privacy: "FRIENDS";
  note: string;
  syncedFromOffline: false;
};

export async function enableBrowserPush(userId: number) {
  assertPushSupported();

  const permission = await window.Notification.requestPermission();
  if (permission !== "granted") {
    throw new Error("Notifications are blocked for this browser.");
  }

  const [{ publicKey }, registration] = await Promise.all([
    fetchJson<PublicKeyResponse>("/api/v1/push/public-key"),
    window.navigator.serviceWorker.register("/poop-vibe-sw.js"),
  ]);

  const readyRegistration = await window.navigator.serviceWorker.ready;
  let subscription = await readyRegistration.pushManager.getSubscription();
  if (subscription) {
    await subscription.unsubscribe();
  }

  subscription = await readyRegistration.pushManager.subscribe({
    userVisibleOnly: true,
    applicationServerKey: urlBase64ToUint8Array(publicKey),
  });

  await registerSubscription(userId, subscription, true);
  return registration;
}

export async function disableBrowserPush(userId: number) {
  assertPushSupported();
  const registration = await window.navigator.serviceWorker.ready;
  const subscription = await registration.pushManager.getSubscription();
  if (!subscription) {
    return;
  }

  await registerSubscription(userId, subscription, false);
  await subscription.unsubscribe();
}

export async function detectBrowserPushEnabled() {
  if (!isPushSupported() || window.Notification.permission !== "granted") {
    return false;
  }

  const registration = await window.navigator.serviceWorker.getRegistration("/poop-vibe-sw.js");
  const subscription = await registration?.pushManager.getSubscription();
  return Boolean(subscription);
}

export async function createFinishedSession(
  userId: number,
  mood: Mood,
  startTime: Date,
  endTime: Date,
) {
  const payload: CreateSessionRequest = {
    clientId: window.crypto.randomUUID(),
    userId,
    startTime: startTime.toISOString(),
    endTime: endTime.toISOString(),
    mood: mood.toUpperCase() as CreateSessionRequest["mood"],
    comfortLevel: "OKAY",
    privacy: "FRIENDS",
    note: `Feeling ${mood}`,
    syncedFromOffline: false,
  };

  return fetchJson("/api/v1/poop-sessions", {
    method: "POST",
    body: JSON.stringify(payload),
  });
}

async function registerSubscription(
  userId: number,
  subscription: PushSubscription,
  pushEnabled: boolean,
) {
  const registration: DeviceRegistration = {
    userId,
    endpoint: subscription.endpoint,
    keys: subscriptionKeys(subscription),
    app: "WEB",
    userAgent: window.navigator.userAgent,
    pushEnabled,
  };

  return fetchJson("/api/v1/devices", {
    method: "POST",
    body: JSON.stringify(registration),
  });
}

function subscriptionKeys(subscription: PushSubscription): PushKeys {
  const p256dh = subscription.getKey("p256dh");
  const auth = subscription.getKey("auth");
  if (!p256dh || !auth) {
    throw new Error("The browser did not expose push encryption keys.");
  }

  return {
    p256dh: arrayBufferToBase64Url(p256dh),
    auth: arrayBufferToBase64Url(auth),
  };
}

async function fetchJson<T>(path: string, init?: RequestInit): Promise<T> {
  const response = await window.fetch(`${API_BASE_URL}${path}`, {
    ...init,
    headers: {
      "Content-Type": "application/json",
      ...init?.headers,
    },
  });

  if (!response.ok) {
    throw new Error(`API request failed with HTTP ${response.status}`);
  }

  return response.json() as Promise<T>;
}

function isPushSupported() {
  return (
    typeof window !== "undefined" &&
    "serviceWorker" in window.navigator &&
    "PushManager" in window &&
    "Notification" in window
  );
}

function assertPushSupported() {
  if (!isPushSupported()) {
    throw new Error("This browser does not support PWA push notifications.");
  }
}

function arrayBufferToBase64Url(buffer: ArrayBuffer) {
  const bytes = new Uint8Array(buffer);
  let binary = "";
  bytes.forEach((byte) => {
    binary += String.fromCharCode(byte);
  });

  return window
    .btoa(binary)
    .replace(/\+/g, "-")
    .replace(/\//g, "_")
    .replace(/=+$/, "");
}

function urlBase64ToUint8Array(value: string) {
  const padding = "=".repeat((4 - (value.length % 4)) % 4);
  const base64 = `${value}${padding}`.replace(/-/g, "+").replace(/_/g, "/");
  const raw = window.atob(base64);
  const output = new Uint8Array(raw.length);

  for (let index = 0; index < raw.length; index += 1) {
    output[index] = raw.charCodeAt(index);
  }

  return output;
}
