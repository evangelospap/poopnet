import type { Dispatch, SetStateAction } from "react";

import { formatElapsed } from "../format";
import type { DashboardTheme } from "../theme";
import type { Mood } from "../types";

type SessionCardProps = {
  elapsed: number;
  isDark: boolean;
  isRunning: boolean;
  mood: Mood;
  setIsRunning: Dispatch<SetStateAction<boolean>>;
  setMood: Dispatch<SetStateAction<Mood>>;
  theme: DashboardTheme;
};

export function SessionCard({
  elapsed,
  isDark,
  isRunning,
  mood,
  setIsRunning,
  setMood,
  theme,
}: SessionCardProps) {
  return (
    <section
      className={`mt-6 overflow-hidden rounded-lg border p-6 transition-colors duration-300 ${theme.card}`}
    >
      <div className="relative z-10 flex flex-col items-center text-center">
        <h2 className={`max-w-xs text-3xl font-black leading-9 ${theme.text}`}>
          Ready to drop a vibe?
        </h2>

        <button
          type="button"
          onClick={() => setIsRunning((current) => !current)}
          className={`mt-8 grid size-48 place-items-center rounded-full border-4 text-center transition active:scale-[0.98] ${theme.timerShell}`}
          aria-label={isRunning ? "Finish session" : "Start session"}
        >
          <span
            className={`grid size-36 place-items-center rounded-full text-5xl shadow-lg ${
              isDark
                ? "bg-[#4be277] text-[#003915] shadow-[#4be277]/20"
                : "bg-[#012d1d] text-white shadow-black/10"
            }`}
          >
            {isRunning ? "🚽" : "💩"}
          </span>
        </button>

        <div className="mt-5 space-y-1">
          <p className={`text-xs font-black uppercase ${theme.muted}`}>
            {isRunning ? "Current session" : "Last session"}
          </p>
          <p className={`text-3xl font-black tabular-nums ${theme.primaryText}`}>
            {isRunning ? formatElapsed(elapsed) : "2h 45m ago"}
          </p>
        </div>

        <div className="mt-5 grid w-full grid-cols-3 gap-2">
          {(["light", "steady", "spicy"] as Mood[]).map((option) => (
            <button
              className={`rounded-lg px-2 py-2 text-sm font-black transition active:scale-95 ${
                mood === option ? theme.primary : theme.inactiveButton
              }`}
              key={option}
              onClick={() => setMood(option)}
              type="button"
            >
              {option}
            </button>
          ))}
        </div>

        <button
          className={`mt-4 w-full rounded-lg border px-4 py-4 text-base font-black transition active:scale-[0.98] ${theme.primaryBorder} ${theme.primaryText}`}
          type="button"
        >
          Feeling {mood} 🍃
        </button>
      </div>
    </section>
  );
}

