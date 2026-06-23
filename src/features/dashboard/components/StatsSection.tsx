import { statCards } from "../data";
import type { DashboardTheme } from "../theme";

type StatsSectionProps = {
  isDark: boolean;
  theme: DashboardTheme;
};

export function StatsSection({ isDark, theme }: StatsSectionProps) {
  return (
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
  );
}

