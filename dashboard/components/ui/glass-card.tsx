"use client";

import * as React from "react";
import { motion, HTMLMotionProps } from "framer-motion";
import { cva, type VariantProps } from "class-variance-authority";
import { cn } from "@/lib/utils";
import { cardHover, glassMorphism } from "@/lib/animations";

const glassCardVariants = cva(
  "rounded-2xl backdrop-blur-xl border overflow-hidden relative transition-all duration-300",
  {
    variants: {
      variant: {
        default: "bg-white/10 border-white/20 shadow-xl",
        purple: "bg-gradient-to-br from-electric-purple/10 to-bright-pink/10 border-electric-purple/30 shadow-glow",
        blue: "bg-gradient-to-br from-electric-blue/10 to-electric-cyan/10 border-electric-blue/30 shadow-glow-blue",
        green: "bg-gradient-to-br from-vibrant-green/10 to-emerald-500/10 border-vibrant-green/30 shadow-glow-green",
        orange: "bg-gradient-to-br from-sunset-orange/10 to-coral-red/10 border-sunset-orange/30 shadow-glow-orange",
        mesh: "bg-mesh-purple border-white/20 shadow-2xl",
      },
      size: {
        sm: "p-4",
        default: "p-6",
        lg: "p-8",
        xl: "p-10",
      },
      blur: {
        sm: "backdrop-blur-sm",
        default: "backdrop-blur-md",
        lg: "backdrop-blur-lg",
        xl: "backdrop-blur-xl",
      },
      glow: {
        none: "",
        soft: "shadow-elevation-1",
        medium: "shadow-elevation-2",
        strong: "shadow-elevation-3",
      },
    },
    defaultVariants: {
      variant: "default",
      size: "default",
      blur: "default",
      glow: "medium",
    },
  }
);

interface GlassCardProps
  extends HTMLMotionProps<"div">,
    VariantProps<typeof glassCardVariants> {
  hover?: boolean;
  children: React.ReactNode;
}

const GlassCard = React.forwardRef<HTMLDivElement, GlassCardProps>(
  ({ className, variant, size, blur, glow, hover = true, children, ...props }, ref) => {
    return (
      <motion.div
        className={cn(glassCardVariants({ variant, size, blur, glow, className }))}
        ref={ref}
        variants={hover ? cardHover : undefined}
        initial={hover ? "rest" : undefined}
        whileHover={hover ? "hover" : undefined}
        style={{
          transformStyle: "preserve-3d",
        }}
        {...props}
      >
        {/* Animated Background Gradient */}
        <div className="absolute inset-0 opacity-50">
          <div className="absolute inset-0 bg-gradient-to-br from-white/5 via-transparent to-black/5" />
          <div className="absolute top-0 left-0 w-full h-px bg-gradient-to-r from-transparent via-white/50 to-transparent" />
          <div className="absolute top-0 left-0 w-px h-full bg-gradient-to-b from-transparent via-white/50 to-transparent" />
        </div>

        {/* Mesh Pattern Overlay (for mesh variant) */}
        {variant === "mesh" && (
          <div className="absolute inset-0 opacity-30">
            <svg
              width="100%"
              height="100%"
              xmlns="http://www.w3.org/2000/svg"
              className="absolute inset-0"
            >
              <defs>
                <pattern
                  id="mesh-pattern"
                  x="0"
                  y="0"
                  width="20"
                  height="20"
                  patternUnits="userSpaceOnUse"
                >
                  <circle
                    cx="10"
                    cy="10"
                    r="1"
                    fill="currentColor"
                    className="text-white/20"
                  />
                </pattern>
              </defs>
              <rect width="100%" height="100%" fill="url(#mesh-pattern)" />
            </svg>
          </div>
        )}

        {/* Floating Orbs for Enhanced Visual Appeal */}
        <div className="absolute -top-4 -right-4 w-24 h-24 bg-gradient-to-br from-white/20 to-transparent rounded-full blur-xl" />
        <div className="absolute -bottom-4 -left-4 w-16 h-16 bg-gradient-to-tr from-white/10 to-transparent rounded-full blur-lg" />

        {/* Content */}
        <div className="relative z-10">
          {children}
        </div>
      </motion.div>
    );
  }
);

GlassCard.displayName = "GlassCard";

export { GlassCard, glassCardVariants };
export type { GlassCardProps };