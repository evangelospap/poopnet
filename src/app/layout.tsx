import type { Metadata, Viewport } from "next";
import "./globals.css";

export const metadata: Metadata = {
  title: "Poop Vibe",
  description: "A playful PWA dashboard for logging sessions, tracking vibes, and keeping up with friends.",
  applicationName: "Poop Vibe",
};

export const viewport: Viewport = {
  themeColor: "#101413",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en" className="h-full antialiased">
      <body className="flex min-h-full flex-col">{children}</body>
    </html>
  );
}
