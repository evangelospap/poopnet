"use client";

import { useEffect, useState } from "react";

type Mood = "light" | "steady" | "spicy";
type ThemeMode = "dark" | "light";

type FeedItem = {
  name: string;
  avatar: string;
  action: string;
  detail: string;
  darkAccentClass: string;
  lightAccentClass: string;
  reactions: string[];
};

const feedItems: FeedItem[] = [
  {
    name: "Alex",
    avatar: "A",
    action: "just dropped a vibe",
    detail: '2m ago • Feeling "Heavy Heavy"',
    darkAccentClass: "bg-[#a4abc4] text-[#283044]",
    lightAccentClass: "bg-[#fdc003] text-[#261a00]",
    reactions: ["💩", "🙌", "🧻"],
  },
  {
    name: "Jordan",
    avatar: "J",
    action: "reached a 10-day streak",
    detail: "15m ago • Friends",
    darkAccentClass: "bg-[#15542e] text-[#88c796]",
    lightAccentClass: "bg-[#ff6b6b] text-white",
    reactions: ["🔥", "🙌"],
  },
  {
    name: "Sam",
    avatar: "S",
    action: "kept it private but spicy",
    detail: "45m ago • Details hidden",
    darkAccentClass: "bg-[#323634] text-[#bccbb9]",
    lightAccentClass: "bg-[#a5d0b9] text-[#002114]",
    reactions: ["💩", "🧻", "🔥"],
  },
];

const statCards = [
  { label: "Sessions Today", value: "4", icon: "↗" },
  { label: "Avg Duration", value: "6m", icon: "◷" },
];

const navItems = [
  { label: "Dashboard", icon: "▦", active: true },
  { label: "Friends", icon: "👥", active: false },
  { label: "Stats", icon: "◫", active: false },
  { label: "Profile", icon: "◉", active: false },
];

const themes = {
  dark: {
    main: "bg-[#0b0f0e] text-[#e0e3e1]",
    header: "border-[#3d4a3d] bg-[#101413]/80",
    nav: "border-[#3d4a3d] bg-[#101413]/85",
    brand: "text-[#4be277]",
    text: "text-[#e0e3e1]",
    muted: "text-[#bccbb9]",
    subtle: "text-[#869585]",
    card: "border-[#242c2a] bg-[#161b19]/80 backdrop-blur-xl",
    primary: "bg-[#4be277] text-[#003915]",
    primaryText: "text-[#4be277]",
    primaryBorder: "border-[#4be277]",
    primarySoft: "bg-[#4be277]/20",
    timerShell:
      "border-[#4be277]/20 bg-[#101413] shadow-[0_0_40px_rgba(34,197,94,0.15)]",
    onlineChip: "border-[#4be277]/20 bg-[#15542e]/40 text-[#96d5a3]",
    secondaryChip: "border-[#3d4a3d] bg-[#272b2a] text-[#bccbb9]",
    tertiaryChip: "border-[#3d4a3d] bg-[#181c1b] text-[#bccbb9]",
    inactiveButton: "border border-[#3d4a3d] bg-[#1c201f] text-[#bccbb9]",
    reactionIdle: "bg-[#272b2a] text-[#e0e3e1]",
    activeNav: "bg-[#15542e] text-[#88c796]",
    navText: "text-[#bccbb9]",
    input:
      "border-[#3d4a3d] bg-[#1c201f] text-[#e0e3e1] placeholder:text-[#869585] focus:border-[#4be277] focus:ring-[#4be277]/30",
    avatar: "border-[#3d4a3d] bg-[#1c201f] text-[#e0e3e1]",
    modeLabel: "Dark mode",
  },
  light: {
    main: "bg-[#fbf9f8] text-[#1b1c1c]",
    header: "border-black/10 bg-[#fbf9f8]/90",
    nav: "border-black/10 bg-white/95",
    brand: "text-[#012d1d]",
    text: "text-[#1b1c1c]",
    muted: "text-[#414844]",
    subtle: "text-[#717973]",
    card: "border-black/10 bg-white shadow-sm",
    primary: "bg-[#012d1d] text-white",
    primaryText: "text-[#1b4332]",
    primaryBorder: "border-[#1b4332]",
    primarySoft: "bg-[#c1ecd4]",
    timerShell: "border-[#1b4332]/15 bg-white shadow-sm",
    onlineChip: "border-[#a5d0b9] bg-[#c1ecd4] text-[#002114]",
    secondaryChip: "border-[#ffdf9e] bg-[#ffdf9e] text-[#261a00]",
    tertiaryChip: "border-black/10 bg-white text-[#414844]",
    inactiveButton: "border border-black/10 bg-[#f0eded] text-[#414844]",
    reactionIdle: "bg-[#f6f3f2] text-[#1b1c1c]",
    activeNav: "bg-[#c1ecd4] text-[#002114]",
    navText: "text-[#717973]",
    input:
      "border-black/10 bg-white text-[#1b1c1c] placeholder:text-[#717973] focus:border-[#1b4332] focus:ring-[#1b4332]/20",
    avatar: "border-black/10 bg-[#e4e2e1] text-[#012d1d]",
    modeLabel: "Light mode",
  },
} as const;

function getInitialThemeMode(): ThemeMode {
  if (typeof window === "undefined") {
    return "dark";
  }

  const savedTheme = window.localStorage.getItem("poop-vibe-theme");

  return savedTheme === "light" || savedTheme === "dark" ? savedTheme : "dark";
}

function formatElapsed(seconds: number) {
  const hours = Math.floor(seconds / 3600);
  const minutes = Math.floor((seconds % 3600) / 60);
  const secs = seconds % 60;

  return [hours, minutes, secs]
    .map((unit) => String(unit).padStart(2, "0"))
    .join(":");
}

export default function Home() {
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
      <header
        className={`fixed inset-x-0 top-0 z-30 border-b backdrop-blur-xl transition-colors duration-300 ${theme.header}`}
      >
        <div className="mx-auto flex h-16 w-full max-w-md items-center justify-between px-4">
          <div className="flex items-center gap-2">
            <span className="text-2xl" aria-hidden="true">
              💩
            </span>
            <h1 className={`text-2xl font-black leading-8 ${theme.brand}`}>
              Poop Vibe
            </h1>
          </div>

          <div className="flex items-center gap-2">
            <button
              className={`grid size-10 place-items-center rounded-lg text-lg transition active:scale-95 ${
                isDark
                  ? "text-[#bccbb9] hover:text-[#4be277]"
                  : "text-[#717973] hover:text-[#1b4332]"
              }`}
              aria-label={`Switch to ${isDark ? "light" : "dark"} mode`}
              type="button"
              onClick={() =>
                setThemeMode((current) => (current === "dark" ? "light" : "dark"))
              }
              title={`Switch to ${isDark ? "light" : "dark"} mode`}
            >
              {isDark ? "☀" : "☾"}
            </button>
            <button
              className={`relative grid size-10 place-items-center rounded-lg text-xl transition active:scale-95 ${theme.navText}`}
              aria-label="Toggle push notifications"
              type="button"
              onClick={() => setPushEnabled((current) => !current)}
            >
              🔔
              {pushEnabled ? (
                <span
                  className={`absolute right-2 top-2 size-2 rounded-full ${
                    isDark ? "bg-[#4be277]" : "bg-[#fdc003]"
                  }`}
                />
              ) : null}
            </button>
            <button
              className={`grid size-9 place-items-center overflow-hidden rounded-lg border text-xs font-black transition active:scale-95 ${theme.avatar}`}
              aria-label="Open profile"
              type="button"
            >
              EP
            </button>
          </div>
        </div>
      </header>

      <div className="mx-auto flex min-h-screen w-full max-w-md flex-col px-4 pb-28 pt-20">
        <section className="space-y-3">
          <p className={`text-base font-medium ${theme.muted}`}>
            Clean logs, messy moments.
          </p>
          <div className="flex flex-wrap gap-2">
            <span
              className={`inline-flex items-center gap-2 rounded-lg border px-3 py-1.5 text-xs font-black ${theme.onlineChip}`}
            >
              <span
                className={
                  isDark
                    ? "size-2 rounded-full bg-[#4be277]"
                    : "size-2 rounded-full bg-[#1b4332]"
                }
              />
              Online
            </span>
            <span
              className={`rounded-lg border px-3 py-1.5 text-xs font-black ${theme.secondaryChip}`}
            >
              Push: {pushEnabled ? "On" : "Off"}
            </span>
            <span
              className={`rounded-lg border px-3 py-1.5 text-xs font-black ${theme.tertiaryChip}`}
            >
              {theme.modeLabel}
            </span>
          </div>
        </section>

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

        <section className="mt-5 grid grid-cols-2 gap-4">
          {statCards.map((card) => (
            <article
              className={`flex h-32 flex-col justify-between rounded-lg border p-4 transition-colors duration-300 ${theme.card}`}
              key={card.label}
            >
              <span className={`text-xs font-black ${theme.muted}`}>
                {card.label}
              </span>
              <div className="flex items-end justify-between">
                <span className={`text-3xl font-black ${theme.text}`}>
                  {card.value}
                </span>
                <span className={`text-xl ${theme.primaryText}`}>{card.icon}</span>
              </div>
            </article>
          ))}

          <article
            className={`relative col-span-2 overflow-hidden rounded-lg border p-5 transition-colors duration-300 ${theme.card}`}
          >
            <div className="relative z-10 flex items-center justify-between gap-4">
              <div className="flex items-center gap-4">
                <div
                  className={`grid size-12 place-items-center rounded-lg text-2xl ${theme.primarySoft}`}
                >
                  🔥
                </div>
                <div>
                  <p className={`text-2xl font-black ${theme.text}`}>5 Days</p>
                  <p className={`text-xs font-black ${theme.muted}`}>
                    Current Streak
                  </p>
                </div>
              </div>
              <span className="rounded-lg border border-yellow-500/30 bg-yellow-500/10 px-3 py-1 text-[10px] font-black uppercase text-yellow-500">
                Hot Streak
              </span>
            </div>
            <div
              className={
                isDark
                  ? "absolute -right-8 -top-10 size-32 rounded-full bg-[#4be277]/5 blur-3xl"
                  : "absolute -right-8 -top-10 size-32 rounded-full bg-[#fdc003]/15 blur-3xl"
              }
            />
          </article>
        </section>

        <section className="mt-6 space-y-4">
          <div className="flex items-center justify-between">
            <h3 className={`text-2xl font-black ${theme.text}`}>Vibe Check</h3>
            <button
              className={`text-sm font-black transition active:scale-95 ${theme.primaryText}`}
              type="button"
            >
              View All
            </button>
          </div>

          <div className="space-y-4">
            {feedItems.map((item) => (
              <article
                className={`rounded-lg border p-4 transition-colors duration-300 ${theme.card}`}
                key={item.name}
              >
                <div className="flex items-center gap-3">
                  <div
                    className={`grid size-10 place-items-center rounded-lg text-sm font-black ${
                      isDark ? item.darkAccentClass : item.lightAccentClass
                    }`}
                  >
                    {item.avatar}
                  </div>
                  <div className="min-w-0">
                    <p className={`font-black ${theme.text}`}>
                      {item.name}{" "}
                      <span className={`font-semibold ${theme.muted}`}>
                        {item.action}
                      </span>
                    </p>
                    <p className={`mt-1 text-xs font-semibold ${theme.subtle}`}>
                      {item.detail}
                    </p>
                  </div>
                </div>

                <div className="mt-3 flex flex-wrap gap-2">
                  {item.reactions.map((reaction) => (
                    <button
                      type="button"
                      className={`min-h-9 rounded-lg px-3 text-base transition active:scale-95 ${
                        reactions[item.name] === reaction
                          ? theme.primary
                          : theme.reactionIdle
                      }`}
                      key={reaction}
                      aria-label={`${reaction} reaction for ${item.name}`}
                      onClick={() =>
                        setReactions((current) => ({
                          ...current,
                          [item.name]: reaction,
                        }))
                      }
                    >
                      {reaction}
                    </button>
                  ))}
                </div>
              </article>
            ))}
          </div>

          <label className="relative block">
            <span className="sr-only">Send a vibe</span>
            <input
              className={`w-full rounded-lg border px-4 py-4 pr-12 outline-none transition focus:ring-1 ${theme.input}`}
              onChange={(event) => setMessage(event.target.value)}
              placeholder="Send a vibe..."
              type="text"
              value={message}
            />
            <button
              className={`absolute right-2 top-1/2 grid size-10 -translate-y-1/2 place-items-center rounded-lg transition active:scale-95 ${theme.primaryText}`}
              type="button"
              aria-label="Send vibe"
              onClick={() => setMessage("")}
            >
              ➤
            </button>
          </label>
        </section>
      </div>

      <nav
        className={`fixed inset-x-0 bottom-0 z-30 border-t backdrop-blur-xl transition-colors duration-300 ${theme.nav}`}
      >
        <div className="mx-auto grid h-20 max-w-md grid-cols-5 px-2">
          {navItems.slice(0, 2).map((item) => (
            <button
              type="button"
              className={`flex flex-col items-center justify-center gap-1 text-xs font-black transition active:scale-95 ${
                item.active ? theme.primaryText : theme.navText
              }`}
              key={item.label}
            >
              <span
                className={`grid min-h-8 min-w-12 place-items-center rounded-lg px-3 text-lg ${
                  item.active ? theme.activeNav : ""
                }`}
                aria-hidden="true"
              >
                {item.icon}
              </span>
              <span>{item.label}</span>
            </button>
          ))}

          <button
            type="button"
            className={`-mt-8 flex flex-col items-center justify-center gap-1 text-xs font-black transition active:scale-95 ${theme.navText}`}
          >
            <span
              className={`grid size-14 place-items-center rounded-full text-3xl shadow-lg ${theme.primary}`}
            >
              +
            </span>
            <span className="sr-only">Log</span>
          </button>

          {navItems.slice(2).map((item) => (
            <button
              type="button"
              className={`flex flex-col items-center justify-center gap-1 text-xs font-black transition active:scale-95 ${theme.navText}`}
              key={item.label}
            >
              <span className="text-lg" aria-hidden="true">
                {item.icon}
              </span>
              <span>{item.label}</span>
            </button>
          ))}
        </div>
      </nav>
    </main>
  );
}
