self.addEventListener("push", (event) => {
  let data = {
    title: "Poop Vibe 💩",
    body: "A friend finished a poop vibe.",
    url: "/friends",
    icon: "/favicon.ico",
    tag: "poop-vibe-session",
  };

  if (event.data) {
    try {
      data = { ...data, ...event.data.json() };
    } catch {
      data.body = event.data.text();
    }
  }

  event.waitUntil(
    self.registration.showNotification(data.title, {
      body: data.body,
      icon: data.icon,
      badge: data.icon,
      tag: data.tag,
      data: { url: data.url },
    }),
  );
});

self.addEventListener("notificationclick", (event) => {
  event.notification.close();
  const targetUrl = new URL(event.notification.data?.url || "/friends", self.location.origin).href;

  event.waitUntil(
    self.clients.matchAll({ type: "window", includeUncontrolled: true }).then((clients) => {
      const existingClient = clients.find((client) => client.url === targetUrl);
      if (existingClient) {
        return existingClient.focus();
      }

      return self.clients.openWindow(targetUrl);
    }),
  );
});
