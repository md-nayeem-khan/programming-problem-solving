import type { Metadata } from "next";
import { Inter, Geist } from "next/font/google";
import "./globals.css";
import { cn } from "@/lib/utils";
import { TooltipProvider } from "@/components/ui/tooltip";
import { Toaster } from "sonner";

const geist = Geist({subsets:['latin'],variable:'--font-sans'});

const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: "AlgoMetrics - Interview Preparation Analytics Dashboard",
  description: "Track competitive programming problems and improve interview preparation with pattern mastery insights and analytics",
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en" className={cn("font-sans", geist.variable)} suppressHydrationWarning>
      <body className={cn(inter.className, "antialiased")}>
        <TooltipProvider delayDuration={300}>
          {children}
          <Toaster position="top-right" richColors />
        </TooltipProvider>
      </body>
    </html>
  );
}
