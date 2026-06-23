import type { Dispatch, SetStateAction } from "react";

import { feedItems } from "../data";
import type { DashboardTheme } from "../theme";

type FeedSectionProps = {
  isDark: boolean;
  message: string;
  reactions: Record<string, string>;
  setMessage: Dispatch<SetStateAction<string>>;
  setReactions: Dispatch<SetStateAction<Record<string, string>>>;
  theme: DashboardTheme;
};

export function FeedSection({
  isDark,
  message,
  reactions,
  setMessage,
  setReactions,
  theme,
}: FeedSectionProps) {
  return (
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
  );
}

