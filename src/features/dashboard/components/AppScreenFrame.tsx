"use client";

import { type ReactNode, useEffect, useState } from "react";

import { getInitialThemeMode, themes, type DashboardTheme } from "../theme";
import type { ThemeMode } from "../types";
import { AppHeader } from "./AppHeader";
import { BottomNav } from "./BottomNav";

type AppScreenFrameRenderProps = {
  isDark: boolean;
  pushEnabled: boolean;
  theme: DashboardTheme;
  themeMode: ThemeMode;
};

type AppScreenFrameProps = {
  activeHref: string;
  children: (props: AppScreenFrameRenderProps) => ReactNode;
};

export function AppScreenFrame({ activeHref, children }: AppScreenFrameProps) {
  const [themeMode, setThemeMode] = useState<ThemeMode>(getInitialThemeMode);
  const [pushEnabled, setPushEnabled] = useState(true);

  const theme = themes[themeMode];
  const isDark = themeMode === "dark";

  useEffect(() => {
    document.documentElement.style.colorScheme = themeMode;
    window.localStorage.setItem("poop-vibe-theme", themeMode);
  }, [themeMode]);

  return (
    <main className={`min-h-screen transition-colors duration-300 ${theme.main}`}>
      <AppHeader
        isDark={isDark}
        pushEnabled={pushEnabled}
        setPushEnabled={setPushEnabled}
        setThemeMode={setThemeMode}
        theme={theme}
      />

      <div className="mx-auto flex min-h-screen w-full max-w-md flex-col px-4 pb-28 pt-20">
        {children({ isDark, pushEnabled, theme, themeMode })}
      </div>

      <BottomNav activeHref={activeHref} theme={theme} />
    </main>
  );
}
