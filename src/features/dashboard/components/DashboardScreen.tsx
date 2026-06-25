"use client";

import { useEffect, useState } from "react";

import { createFinishedSession } from "../api/poopVibeApi";
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
  const [sessionStartedAt, setSessionStartedAt] = useState<Date | null>(null);
  const [sessionMessage, setSessionMessage] = useState("");
  const [isSubmittingSession, setIsSubmittingSession] = useState(false);

  useEffect(() => {
    if (!isRunning) {
      return;
    }

    const interval = window.setInterval(() => {
      setElapsed((current) => current + 1);
    }, 1000);

    return () => window.clearInterval(interval);
  }, [isRunning]);

  async function toggleSession(userId: number) {
    setSessionMessage("");

    if (!isRunning) {
      setSessionStartedAt(new Date());
      setElapsed(0);
      setIsRunning(true);
      return;
    }

    const finishedAt = new Date();
    const startedAt =
      sessionStartedAt || new Date(finishedAt.getTime() - Math.max(elapsed, 1) * 1000);

    setIsSubmittingSession(true);
    try {
      await createFinishedSession(userId, mood, startedAt, finishedAt);
      setIsRunning(false);
      setSessionStartedAt(null);
      setSessionMessage("Vibe logged and friends notified.");
    } catch (error) {
      setSessionMessage(error instanceof Error ? error.message : "Could not log session.");
    } finally {
      setIsSubmittingSession(false);
    }
  }

  return (
    <AppScreenFrame activeHref="/">
      {({ isDark, pushEnabled, pushMessage, selectedUser, theme }) => (
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
            isSubmitting={isSubmittingSession}
            mood={mood}
            onToggleSession={() => toggleSession(selectedUser.id)}
            pushMessage={pushMessage}
            sessionMessage={sessionMessage}
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
