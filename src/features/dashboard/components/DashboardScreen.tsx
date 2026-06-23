"use client";

import { useEffect, useState } from "react";

import type { Mood } from "../types";
import { AppScreenFrame } from "./AppScreenFrame";
import { FeedSection } from "./FeedSection";
import { SessionCard } from "./SessionCard";
import { StatsSection } from "./StatsSection";
import { StatusChips } from "./StatusChips";

export function DashboardScreen() {
  const [isRunning, setIsRunning] = useState(false);
  const [mood, setMood] = useState<Mood>("light");
  const [elapsed, setElapsed] = useState(0);
  const [reactions, setReactions] = useState<Record<string, string>>({});
  const [message, setMessage] = useState("");

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
    <AppScreenFrame activeHref="/">
      {({ isDark, pushEnabled, theme }) => (
        <>
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
        </>
      )}
    </AppScreenFrame>
  );
}
