import type { ThemeMode } from "./types";

export const themes = {
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

export type DashboardTheme = (typeof themes)[ThemeMode];

export function getInitialThemeMode(): ThemeMode {
  if (typeof window === "undefined") {
    return "dark";
  }

  const savedTheme = window.localStorage.getItem("poop-vibe-theme");

  return savedTheme === "light" || savedTheme === "dark" ? savedTheme : "dark";
}

