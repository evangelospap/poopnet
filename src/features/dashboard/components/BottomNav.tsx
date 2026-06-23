import Link from "next/link";

import { navItems } from "../data";
import type { DashboardTheme } from "../theme";

type BottomNavProps = {
  activeHref: string;
  theme: DashboardTheme;
};

export function BottomNav({ activeHref, theme }: BottomNavProps) {
  return (
    <nav
      className={`fixed inset-x-0 bottom-0 z-30 border-t backdrop-blur-xl transition-colors duration-300 ${theme.nav}`}
    >
      <div className="mx-auto grid h-20 max-w-md grid-cols-5 px-2">
        {navItems.slice(0, 2).map((item) => (
          <Link
            href={item.href}
            className={`flex flex-col items-center justify-center gap-1 text-xs font-black transition active:scale-95 ${
              item.href === activeHref ? theme.primaryText : theme.navText
            }`}
            key={item.label}
          >
            <span
              className={`grid min-h-8 min-w-12 place-items-center rounded-lg px-3 text-lg ${
                item.href === activeHref ? theme.activeNav : ""
              }`}
              aria-hidden="true"
            >
              {item.icon}
            </span>
            <span>{item.label}</span>
          </Link>
        ))}

        <Link
          href="/"
          className={`-mt-8 flex flex-col items-center justify-center gap-1 text-xs font-black transition active:scale-95 ${theme.navText}`}
        >
          <span
            className={`grid size-14 place-items-center rounded-full text-3xl shadow-lg ${theme.primary}`}
          >
            +
          </span>
          <span className="sr-only">Log</span>
        </Link>

        {navItems.slice(2).map((item) => (
          <Link
            href={item.href}
            className={`flex flex-col items-center justify-center gap-1 text-xs font-black transition active:scale-95 ${
              item.href === activeHref ? theme.primaryText : theme.navText
            }`}
            key={item.label}
          >
            <span
              className={`grid min-h-8 min-w-12 place-items-center rounded-lg px-3 text-lg ${
                item.href === activeHref ? theme.activeNav : ""
              }`}
              aria-hidden="true"
            >
              {item.icon}
            </span>
            <span>{item.label}</span>
          </Link>
        ))}
      </div>
    </nav>
  );
}
