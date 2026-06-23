import type { FeedItem, NavItem, StatCard } from "./types";

export const feedItems: FeedItem[] = [
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

export const statCards: StatCard[] = [
  { label: "Sessions Today", value: "4", icon: "↗" },
  { label: "Avg Duration", value: "6m", icon: "◷" },
];

export const navItems: NavItem[] = [
  { label: "Dashboard", icon: "▦", href: "/" },
  { label: "Friends", icon: "👥", href: "/friends" },
  { label: "Stats", icon: "◫", href: "/stats" },
  { label: "Profile", icon: "◉", href: "/profile" },
];
