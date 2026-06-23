export type Mood = "light" | "steady" | "spicy";

export type ThemeMode = "dark" | "light";

export type FeedItem = {
  name: string;
  avatar: string;
  action: string;
  detail: string;
  darkAccentClass: string;
  lightAccentClass: string;
  reactions: string[];
};

export type StatCard = {
  label: string;
  value: string;
  icon: string;
};

export type NavItem = {
  label: string;
  icon: string;
  href: string;
};
