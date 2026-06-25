import Link from "next/link";

import type { DemoUser } from "../api/poopVibeApi";
import type { DashboardTheme } from "../theme";
import type { ThemeMode } from "../types";

type AppHeaderProps = {
  demoUsers: DemoUser[];
  isDark: boolean;
  pushEnabled: boolean;
  pushBusy: boolean;
  selectedUser: DemoUser;
  setSelectedUserId: (userId: number) => void;
  setThemeMode: (updater: (current: ThemeMode) => ThemeMode) => void;
  theme: DashboardTheme;
  togglePush: () => void;
};

export function AppHeader({
  demoUsers,
  isDark,
  pushEnabled,
  pushBusy,
  selectedUser,
  setSelectedUserId,
  setThemeMode,
  theme,
  togglePush,
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
          <select
            aria-label="Demo identity"
            className={`h-10 rounded-lg border px-2 text-xs font-black outline-none transition ${theme.avatar}`}
            value={selectedUser.id}
            onChange={(event) => setSelectedUserId(Number(event.target.value))}
          >
            {demoUsers.map((user) => (
              <option key={user.id} value={user.id}>
                {user.label}
              </option>
            ))}
          </select>
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
            aria-label={pushEnabled ? "Disable push notifications" : "Enable push notifications"}
            disabled={pushBusy}
            type="button"
            onClick={togglePush}
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
          <Link
            href="/profile"
            className={`grid size-9 place-items-center overflow-hidden rounded-lg border text-xs font-black transition active:scale-95 ${theme.avatar}`}
            aria-label="Open profile"
          >
            {selectedUser.initials}
          </Link>
        </div>
      </div>
    </header>
  );
}
