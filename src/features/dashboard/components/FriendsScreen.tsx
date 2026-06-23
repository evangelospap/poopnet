"use client";

import { useState } from "react";

import { AppScreenFrame } from "./AppScreenFrame";

const requests = [
  { initials: "MK", name: "Mika", detail: "3 mutual friends" },
  { initials: "NR", name: "Nora", detail: "Sent 12m ago" },
];

const closeFriends = [
  {
    initials: "A",
    name: "Alex",
    status: "online",
    vibe: "Feeling light 🍃",
    streak: "12d streak 🔥",
    reactions: ["💩", "🙌", "🧻"],
  },
  {
    initials: "J",
    name: "Jordan",
    status: "away",
    vibe: "Dropped a heavy one 💩",
    streak: "10d streak 🔥",
    reactions: ["🔥", "🙌"],
  },
  {
    initials: "S",
    name: "Sam",
    status: "online",
    vibe: "Details hidden",
    streak: "4d streak",
    reactions: ["💩", "🧻", "🔥"],
  },
];

const activity = [
  "Alex dropped a vibe, details hidden",
  "Jordan reacted with 🔥",
  "Sam kept it private but spicy",
];

const suggestions = [
  { initials: "LV", name: "Lena", detail: "2 shared streaks" },
  { initials: "TO", name: "Theo", detail: "Nearby friend" },
];

export function FriendsScreen() {
  const [query, setQuery] = useState("");
  const [selectedReactions, setSelectedReactions] = useState<Record<string, string>>({});

  return (
    <AppScreenFrame activeHref="/friends">
      {({ isDark, theme }) => (
        <>
          <section className="space-y-3">
            <div className="flex items-end justify-between gap-4">
              <div>
                <h2 className={`text-3xl font-black leading-9 ${theme.text}`}>
                  Friends
                </h2>
                <p className={`mt-1 text-sm font-semibold ${theme.muted}`}>
                  Small circle, big vibes.
                </p>
              </div>
              <div className="grid gap-2 text-right">
                <span
                  className={`rounded-lg border px-3 py-1 text-xs font-black ${theme.onlineChip}`}
                >
                  12 friends
                </span>
                <span
                  className={`rounded-lg border px-3 py-1 text-xs font-black ${theme.tertiaryChip}`}
                >
                  3 online
                </span>
              </div>
            </div>

            <div className="grid grid-cols-[1fr_auto] gap-2">
              <label className="relative block">
                <span className="sr-only">Find a friend</span>
                <input
                  className={`h-12 w-full rounded-lg border px-4 outline-none transition focus:ring-1 ${theme.input}`}
                  onChange={(event) => setQuery(event.target.value)}
                  placeholder="Find a friend"
                  type="search"
                  value={query}
                />
              </label>
              <button
                aria-label="Add friend"
                className={`grid size-12 place-items-center rounded-lg text-2xl font-black transition active:scale-95 ${theme.primary}`}
                type="button"
              >
                +
              </button>
            </div>
          </section>

          <section className="mt-6 space-y-3">
            <h3 className={`text-lg font-black ${theme.text}`}>Requests</h3>
            <div
              className={`rounded-lg border p-2 transition-colors duration-300 ${theme.card}`}
            >
              {requests.map((request) => (
                <div
                  className="grid grid-cols-[auto_1fr_auto_auto] items-center gap-3 border-b border-white/5 px-2 py-3 last:border-b-0"
                  key={request.name}
                >
                  <span className={`grid size-10 place-items-center rounded-lg border text-sm font-black ${theme.avatar}`}>
                    {request.initials}
                  </span>
                  <span className="min-w-0">
                    <span className={`block font-black ${theme.text}`}>
                      {request.name}
                    </span>
                    <span className={`block truncate text-xs font-semibold ${theme.subtle}`}>
                      {request.detail}
                    </span>
                  </span>
                  <button
                    aria-label={`Accept ${request.name}`}
                    className={`grid size-9 place-items-center rounded-lg text-sm font-black ${theme.primary}`}
                    type="button"
                  >
                    ✓
                  </button>
                  <button
                    aria-label={`Decline ${request.name}`}
                    className={`grid size-9 place-items-center rounded-lg text-sm font-black ${theme.inactiveButton}`}
                    type="button"
                  >
                    ×
                  </button>
                </div>
              ))}
            </div>
          </section>

          <section className="mt-6 space-y-3">
            <h3 className={`text-lg font-black ${theme.text}`}>Close Friends</h3>
            <div className="space-y-3">
              {closeFriends.map((friend) => (
                <article
                  className={`rounded-lg border p-4 transition-colors duration-300 ${theme.card}`}
                  key={friend.name}
                >
                  <div className="flex items-start gap-3">
                    <span className={`relative grid size-11 place-items-center rounded-lg border text-sm font-black ${theme.avatar}`}>
                      {friend.initials}
                      <span
                        className={`absolute -right-0.5 -top-0.5 size-3 rounded-full border-2 ${
                          isDark ? "border-[#161b19]" : "border-white"
                        } ${friend.status === "online" ? "bg-[#4be277]" : "bg-[#fdc003]"}`}
                      />
                    </span>
                    <div className="min-w-0 flex-1">
                      <div className="flex items-start justify-between gap-3">
                        <div className="min-w-0">
                          <p className={`font-black ${theme.text}`}>{friend.name}</p>
                          <p className={`mt-1 truncate text-xs font-semibold ${theme.subtle}`}>
                            {friend.vibe}
                          </p>
                        </div>
                        <span className={`shrink-0 rounded-lg px-2 py-1 text-[10px] font-black ${theme.primarySoft} ${theme.primaryText}`}>
                          {friend.streak}
                        </span>
                      </div>
                      <div className="mt-3 flex flex-wrap gap-2">
                        {friend.reactions.map((reaction) => (
                          <button
                            className={`min-h-9 rounded-lg px-3 text-base transition active:scale-95 ${
                              selectedReactions[friend.name] === reaction
                                ? theme.primary
                                : theme.reactionIdle
                            }`}
                            key={reaction}
                            type="button"
                            onClick={() =>
                              setSelectedReactions((current) => ({
                                ...current,
                                [friend.name]: reaction,
                              }))
                            }
                          >
                            {reaction}
                          </button>
                        ))}
                      </div>
                    </div>
                  </div>
                </article>
              ))}
            </div>
          </section>

          <section className="mt-6 grid gap-3">
            <h3 className={`text-lg font-black ${theme.text}`}>Recent Activity</h3>
            <div className={`rounded-lg border p-4 ${theme.card}`}>
              <div className="space-y-3">
                {activity.map((item) => (
                  <div className="flex gap-3" key={item}>
                    <span className={`mt-2 size-2 rounded-full ${isDark ? "bg-[#4be277]" : "bg-[#1b4332]"}`} />
                    <p className={`text-sm font-semibold ${theme.muted}`}>{item}</p>
                  </div>
                ))}
              </div>
            </div>
          </section>

          <section className="mt-6 space-y-3">
            <h3 className={`text-lg font-black ${theme.text}`}>Suggested</h3>
            <div className="grid grid-cols-2 gap-3">
              {suggestions.map((friend) => (
                <article
                  className={`rounded-lg border p-4 transition-colors duration-300 ${theme.card}`}
                  key={friend.name}
                >
                  <span className={`grid size-10 place-items-center rounded-lg border text-sm font-black ${theme.avatar}`}>
                    {friend.initials}
                  </span>
                  <p className={`mt-3 font-black ${theme.text}`}>{friend.name}</p>
                  <p className={`mt-1 min-h-8 text-xs font-semibold ${theme.subtle}`}>
                    {friend.detail}
                  </p>
                  <button
                    className={`mt-3 min-h-10 w-full rounded-lg text-sm font-black transition active:scale-95 ${theme.primary}`}
                    type="button"
                  >
                    Add
                  </button>
                </article>
              ))}
            </div>
          </section>
        </>
      )}
    </AppScreenFrame>
  );
}
