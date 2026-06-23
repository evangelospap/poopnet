import type { Dispatch, SetStateAction } from "react";

import type { DashboardTheme } from "../theme";
import type { ThemeMode } from "../types";

type AppHeaderProps = {
  isDark: boolean;
  pushEnabled: boolean;
  setPushEnabled: Dispatch<SetStateAction<boolean>>;
  setThemeMode: Dispatch<SetStateAction<ThemeMode>>;
  theme: DashboardTheme;
};

export function AppHeader({
  isDark,
  pushEnabled,
  setPushEnabled,
  setThemeMode,
  theme,
}: AppHeaderProps) {
  return (
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
  );
}

