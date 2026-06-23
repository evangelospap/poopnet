"use client";

import { useEffect, useState } from "react";

import { getInitialThemeMode, themes } from "../theme";
import type { Mood, ThemeMode } from "../types";
import { AppHeader } from "./AppHeader";
import { BottomNav } from "./BottomNav";
import { FeedSection } from "./FeedSection";
import { SessionCard } from "./SessionCard";
import { StatsSection } from "./StatsSection";
import { StatusChips } from "./StatusChips";

export function DashboardScreen() {
  const [themeMode, setThemeMode] = useState<ThemeMode>(getInitialThemeMode);
  const [isRunning, setIsRunning] = useState(false);
  const [mood, setMood] = useState<Mood>("light");
  const [pushEnabled, setPushEnabled] = useState(true);
  const [elapsed, setElapsed] = useState(0);
  const [reactions, setReactions] = useState<Record<string, string>>({});
  const [message, setMessage] = useState("");

  const theme = themes[themeMode];
  const isDark = themeMode === "dark";

  useEffect(() => {
    document.documentElement.style.colorScheme = themeMode;
    window.localStorage.setItem("poop-vibe-theme", themeMode);
  }, [themeMode]);

  useEffect(() => {
    if (!isRunning) {
      return;
    }

    const interval = window.setInterval(() => {
      setElapsed((current) => current + 1);
    }, 1000);

    return () => window.clearInterval(interval);
  }, [isRunning]);

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
        <StatusChips
          isDark={isDark}
          pushEnabled={pushEnabled}
          theme={theme}
        />
        <SessionCard
          elapsed={elapsed}
          isDark={isDark}
          isRunning={isRunning}
          mood={mood}
          setIsRunning={setIsRunning}
          setMood={setMood}
          theme={theme}
        />
        <StatsSection isDark={isDark} theme={theme} />
        <FeedSection
          isDark={isDark}
          message={message}
          reactions={reactions}
          setMessage={setMessage}
          setReactions={setReactions}
          theme={theme}
        />
      </div>

      <BottomNav theme={theme} />
    </main>
  );
}

