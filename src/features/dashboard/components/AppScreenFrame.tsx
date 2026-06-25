"use client";

import { type ReactNode, useEffect, useState } from "react";

import {
  demoUsers,
  detectBrowserPushEnabled,
  disableBrowserPush,
  enableBrowserPush,
  type DemoUser,
} from "../api/poopVibeApi";
import { getInitialThemeMode, themes, type DashboardTheme } from "../theme";
import type { ThemeMode } from "../types";
import { AppHeader } from "./AppHeader";
import { BottomNav } from "./BottomNav";

type AppScreenFrameRenderProps = {
  isDark: boolean;
  pushEnabled: boolean;
  pushMessage: string;
  selectedUser: DemoUser;
  theme: DashboardTheme;
  themeMode: ThemeMode;
};

type AppScreenFrameProps = {
  activeHref: string;
  children: (props: AppScreenFrameRenderProps) => ReactNode;
};

export function AppScreenFrame({ activeHref, children }: AppScreenFrameProps) {
  const [themeMode, setThemeMode] = useState<ThemeMode>(getInitialThemeMode);
  const [pushEnabled, setPushEnabled] = useState(false);
  const [pushBusy, setPushBusy] = useState(false);
  const [pushMessage, setPushMessage] = useState("");
  const [selectedUserId, setSelectedUserId] = useState(() => {
    if (typeof window === "undefined") {
      return demoUsers[0].id;
    }

    return Number(window.localStorage.getItem("poop-vibe-demo-user-id")) || demoUsers[0].id;
  });

  const theme = themes[themeMode];
  const isDark = themeMode === "dark";
  const selectedUser =
    demoUsers.find((user) => user.id === selectedUserId) || demoUsers[0];

  useEffect(() => {
    document.documentElement.style.colorScheme = themeMode;
    window.localStorage.setItem("poop-vibe-theme", themeMode);
  }, [themeMode]);

  useEffect(() => {
    window.localStorage.setItem("poop-vibe-demo-user-id", String(selectedUser.id));
    detectBrowserPushEnabled()
      .then(setPushEnabled)
      .catch(() => setPushEnabled(false));
  }, [selectedUser.id]);

  async function togglePush() {
    setPushBusy(true);
    setPushMessage("");

    try {
      if (pushEnabled) {
        await disableBrowserPush(selectedUser.id);
        setPushEnabled(false);
        setPushMessage("Push disabled for this browser.");
      } else {
        await enableBrowserPush(selectedUser.id);
        setPushEnabled(true);
        setPushMessage("Push enabled for this browser.");
      }
    } catch (error) {
      setPushMessage(error instanceof Error ? error.message : "Push setup failed.");
    } finally {
      setPushBusy(false);
    }
  }

  return (
    <main className={`min-h-screen transition-colors duration-300 ${theme.main}`}>
      <AppHeader
        demoUsers={demoUsers}
        isDark={isDark}
        pushEnabled={pushEnabled}
        pushBusy={pushBusy}
        selectedUser={selectedUser}
        setSelectedUserId={setSelectedUserId}
        setThemeMode={setThemeMode}
        theme={theme}
        togglePush={togglePush}
      />

      <div className="mx-auto flex min-h-screen w-full max-w-md flex-col px-4 pb-28 pt-20">
        {children({ isDark, pushEnabled, pushMessage, selectedUser, theme, themeMode })}
      </div>

      <BottomNav activeHref={activeHref} theme={theme} />
    </main>
  );
}
