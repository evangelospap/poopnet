"use client";

import { useState } from "react";

import { AppScreenFrame } from "./AppScreenFrame";

const stats = [
  ["Sessions", "142"],
  ["Streak", "5d"],
  ["Close", "3"],
  ["Reactions", "84"],
];

const preferenceRows = ["Units & Time Format", "Export Data", "Connected Devices"];

function ToggleRow({
  checked,
  label,
  onToggle,
  theme,
}: {
  checked: boolean;
  label: string;
  onToggle: () => void;
  theme: { muted: string; primary: string; text: string };
}) {
  return (
    <button
      className="flex min-h-11 w-full items-center justify-between gap-4 text-left"
      onClick={onToggle}
      type="button"
    >
      <span className={`text-sm font-black ${theme.text}`}>{label}</span>
      <span
        className={`relative h-7 w-12 rounded-full transition ${
          checked ? theme.primary : "bg-black/20"
        }`}
      >
        <span
          className={`absolute top-1 size-5 rounded-full bg-white transition ${
            checked ? "left-6" : "left-1"
          }`}
        />
      </span>
    </button>
  );
}

export function ProfileScreen() {
  const [visibility, setVisibility] = useState("Private");
  const [showMood, setShowMood] = useState(true);
  const [allowComments, setAllowComments] = useState(true);
  const [hideTime, setHideTime] = useState(false);
  const [pushEnabled, setPushEnabled] = useState(true);
  const [friendRequests, setFriendRequests] = useState(true);
  const [streakReminders, setStreakReminders] = useState(false);

  return (
    <AppScreenFrame activeHref="/profile">
      {({ theme, themeMode }) => (
        <>
          <section className={`rounded-lg border p-5 ${theme.card}`}>
            <div className="flex items-start gap-4">
              <span className={`grid size-16 place-items-center rounded-full border text-xl font-black ${theme.avatar}`}>
                EP
              </span>
              <div className="min-w-0 flex-1">
                <div className="flex items-start justify-between gap-3">
                  <div className="min-w-0">
                    <h2 className={`truncate text-3xl font-black ${theme.text}`}>
                      Evangelos
                    </h2>
                    <p className={`mt-1 text-sm font-semibold ${theme.muted}`}>
                      @evangelos
                    </p>
                  </div>
                  <button
                    aria-label="Edit profile"
                    className={`grid size-10 place-items-center rounded-lg transition active:scale-95 ${theme.inactiveButton}`}
                    type="button"
                  >
                    ✎
                  </button>
                </div>
                <div className="mt-3 flex flex-wrap gap-2">
                  <span className={`rounded-lg border px-3 py-1 text-xs font-black ${theme.onlineChip}`}>
                    Online
                  </span>
                  <span className={`rounded-lg border px-3 py-1 text-xs font-black ${theme.tertiaryChip}`}>
                    {themeMode === "dark" ? "Dark" : "Light"}
                  </span>
                </div>
              </div>
            </div>
          </section>

          <section className="mt-5 grid grid-cols-4 gap-2">
            {stats.map(([label, value]) => (
              <article className={`rounded-lg border p-3 text-center ${theme.card}`} key={label}>
                <p className={`text-xl font-black ${theme.text}`}>{value}</p>
                <p className={`mt-1 text-[10px] font-black uppercase ${theme.subtle}`}>
                  {label}
                </p>
              </article>
            ))}
          </section>

          <section className={`mt-5 rounded-lg border p-5 ${theme.card}`}>
            <h3 className={`text-lg font-black ${theme.text}`}>Privacy</h3>
            <div className="mt-4 grid grid-cols-3 gap-2">
              {["Private", "Close", "Friends"].map((option) => (
                <button
                  className={`min-h-10 rounded-lg border px-2 text-sm font-black transition active:scale-95 ${
                    visibility === option ? theme.primary : theme.inactiveButton
                  }`}
                  key={option}
                  onClick={() => setVisibility(option)}
                  type="button"
                >
                  {option}
                </button>
              ))}
            </div>
            <div className="mt-4 divide-y divide-white/5">
              <ToggleRow
                checked={showMood}
                label="Show Mood"
                onToggle={() => setShowMood((current) => !current)}
                theme={theme}
              />
              <ToggleRow
                checked={allowComments}
                label="Allow Comments"
                onToggle={() => setAllowComments((current) => !current)}
                theme={theme}
              />
              <ToggleRow
                checked={hideTime}
                label="Hide Exact Time"
                onToggle={() => setHideTime((current) => !current)}
                theme={theme}
              />
            </div>
          </section>

          <section className={`mt-5 rounded-lg border p-5 ${theme.card}`}>
            <h3 className={`text-lg font-black ${theme.text}`}>Notifications</h3>
            <div className="mt-3 divide-y divide-white/5">
              <ToggleRow
                checked={pushEnabled}
                label="Push Enabled"
                onToggle={() => setPushEnabled((current) => !current)}
                theme={theme}
              />
              <ToggleRow
                checked={friendRequests}
                label="Friend Requests"
                onToggle={() => setFriendRequests((current) => !current)}
                theme={theme}
              />
              <ToggleRow
                checked={streakReminders}
                label="Streak Reminders"
                onToggle={() => setStreakReminders((current) => !current)}
                theme={theme}
              />
              <div className="flex min-h-11 items-center justify-between gap-4">
                <span className={`text-sm font-black ${theme.text}`}>Quiet Hours</span>
                <span className={`text-sm font-black ${theme.muted}`}>
                  10 PM - 7 AM
                </span>
              </div>
            </div>
          </section>

          <section className={`mt-5 rounded-lg border p-5 ${theme.card}`}>
            <h3 className={`text-lg font-black ${theme.text}`}>Preferences</h3>
            <div className="mt-3 divide-y divide-white/5">
              {preferenceRows.map((row) => (
                <button
                  className="flex min-h-12 w-full items-center justify-between text-left"
                  key={row}
                  type="button"
                >
                  <span className={`text-sm font-black ${theme.text}`}>{row}</span>
                  <span className={`text-lg ${theme.primaryText}`}>›</span>
                </button>
              ))}
            </div>
          </section>

          <section className={`mt-5 rounded-lg border p-4 ${theme.card}`}>
            <div className="grid grid-cols-2 gap-3">
              <button
                className={`min-h-11 rounded-lg border text-sm font-black transition active:scale-95 ${theme.inactiveButton}`}
                type="button"
              >
                Sign Out
              </button>
              <button
                className="min-h-11 rounded-lg border border-[#ff6b6b]/30 bg-[#ff6b6b]/10 text-sm font-black text-[#ffb4ab] transition active:scale-95"
                type="button"
              >
                Delete Account
              </button>
            </div>
          </section>
        </>
      )}
    </AppScreenFrame>
  );
}
