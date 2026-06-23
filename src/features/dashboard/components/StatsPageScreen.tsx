"use client";

import { AppScreenFrame } from "./AppScreenFrame";

const weeklyBars = [
  { day: "Mon", value: 68 },
  { day: "Tue", value: 42 },
  { day: "Wed", value: 84 },
  { day: "Thu", value: 52 },
  { day: "Fri", value: 92 },
  { day: "Sat", value: 58 },
  { day: "Sun", value: 74 },
];

const moodBreakdown = [
  { label: "Light", icon: "🍃", count: 14, width: "68%" },
  { label: "Steady", icon: "✓", count: 9, width: "48%" },
  { label: "Spicy", icon: "🌶", count: 5, width: "30%" },
  { label: "Rough", icon: "▦", count: 2, width: "16%" },
];

const comfortTrends = [
  { label: "Smooth", value: "72%" },
  { label: "Relieved", value: "64%" },
  { label: "Strained", value: "22%" },
  { label: "Chaotic", value: "12%" },
];

export function StatsPageScreen() {
  return (
    <AppScreenFrame activeHref="/stats">
      {({ isDark, theme }) => (
        <>
          <section className="space-y-3">
            <h2 className={`text-3xl font-black leading-9 ${theme.text}`}>Stats</h2>
            <div className="flex flex-wrap gap-2">
              <span className={`rounded-lg border px-3 py-1.5 text-xs font-black ${theme.tertiaryChip}`}>
                This week
              </span>
              <span className="rounded-lg border border-yellow-500/30 bg-yellow-500/10 px-3 py-1.5 text-xs font-black text-yellow-500">
                5 day streak
              </span>
            </div>
          </section>

          <section className={`mt-6 rounded-lg border p-5 ${theme.card}`}>
            <div className="grid grid-cols-3 gap-3 text-center">
              {[
                ["Current", "5d"],
                ["Best", "12d"],
                ["Average", "6m"],
              ].map(([label, value]) => (
                <div key={label}>
                  <p className={`text-[10px] font-black uppercase ${theme.subtle}`}>
                    {label}
                  </p>
                  <p className={`mt-2 text-3xl font-black ${theme.primaryText}`}>
                    {value}
                  </p>
                </div>
              ))}
            </div>
          </section>

          <section className={`mt-5 rounded-lg border p-5 ${theme.card}`}>
            <div className="flex items-center justify-between">
              <h3 className={`text-lg font-black ${theme.text}`}>Weekly Trend</h3>
              <span className={`text-xs font-black ${theme.subtle}`}>Mon-Sun</span>
            </div>
            <div className="mt-5 flex h-36 items-end justify-between gap-2">
              {weeklyBars.map((bar, index) => (
                <div className="flex flex-1 flex-col items-center gap-2" key={bar.day}>
                  <div className="flex h-28 w-full items-end rounded-lg bg-black/10 px-1">
                    <span
                      className={`w-full rounded-md ${
                        index === 4
                          ? "bg-yellow-500"
                          : index === 2
                            ? "bg-[#ff6b6b]"
                            : isDark
                              ? "bg-[#4be277]"
                              : "bg-[#1b4332]"
                      }`}
                      style={{ height: `${bar.value}%` }}
                    />
                  </div>
                  <span className={`text-[10px] font-black ${theme.subtle}`}>
                    {bar.day}
                  </span>
                </div>
              ))}
            </div>
          </section>

          <section className={`mt-5 rounded-lg border p-5 ${theme.card}`}>
            <h3 className={`text-lg font-black ${theme.text}`}>Mood Breakdown</h3>
            <div className="mt-4 space-y-4">
              {moodBreakdown.map((mood) => (
                <div key={mood.label}>
                  <div className="mb-2 flex items-center justify-between">
                    <span className={`text-sm font-black ${theme.text}`}>
                      {mood.icon} {mood.label}
                    </span>
                    <span className={`text-xs font-black ${theme.subtle}`}>
                      {mood.count}
                    </span>
                  </div>
                  <div className="h-2 rounded-full bg-black/20">
                    <div
                      className={isDark ? "h-2 rounded-full bg-[#4be277]" : "h-2 rounded-full bg-[#1b4332]"}
                      style={{ width: mood.width }}
                    />
                  </div>
                </div>
              ))}
            </div>
          </section>

          <section className={`mt-5 rounded-lg border p-5 ${theme.card}`}>
            <h3 className={`text-lg font-black ${theme.text}`}>Comfort Trend</h3>
            <div className="mt-4 space-y-3">
              {comfortTrends.map((trend) => (
                <div className="grid grid-cols-[82px_1fr_42px] items-center gap-3" key={trend.label}>
                  <span className={`text-sm font-black ${theme.muted}`}>
                    {trend.label}
                  </span>
                  <div className="h-2 rounded-full bg-black/20">
                    <div
                      className="h-2 rounded-full bg-[#a4abc4]"
                      style={{ width: trend.value }}
                    />
                  </div>
                  <span className={`text-right text-xs font-black ${theme.subtle}`}>
                    {trend.value}
                  </span>
                </div>
              ))}
            </div>
          </section>

          <section className="mt-5 grid grid-cols-2 gap-3">
            <article className={`rounded-lg border p-4 ${theme.card}`}>
              <p className={`text-xs font-black uppercase ${theme.subtle}`}>
                Privacy
              </p>
              <div className="mt-3 space-y-2">
                {["Private 70%", "Friends 20%", "Close 10%"].map((item) => (
                  <p className={`text-sm font-black ${theme.text}`} key={item}>
                    {item}
                  </p>
                ))}
              </div>
            </article>
            <article className={`rounded-lg border p-4 ${theme.card}`}>
              <p className={`text-xs font-black uppercase ${theme.subtle}`}>
                Best Time
              </p>
              <p className={`mt-3 text-xl font-black ${theme.text}`}>Morning</p>
              <p className={`mt-1 text-xs font-semibold ${theme.muted}`}>
                8:00 AM - 10:00 AM ☀
              </p>
            </article>
          </section>
        </>
      )}
    </AppScreenFrame>
  );
}
