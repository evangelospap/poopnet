import type { DashboardTheme } from "../theme";

type StatusChipsProps = {
  isDark: boolean;
  pushEnabled: boolean;
  theme: DashboardTheme;
};

export function StatusChips({ isDark, pushEnabled, theme }: StatusChipsProps) {
  return (
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
  );
}

