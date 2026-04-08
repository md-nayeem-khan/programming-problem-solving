"use client";

import * as React from "react";
import { motion } from "framer-motion";
import { cn } from "@/lib/utils";
import { progressRing } from "@/lib/animations";

interface ProgressRingProps {
  value: number; // 0-100
  max?: number;
  size?: "sm" | "default" | "lg" | "xl";
  thickness?: "thin" | "default" | "thick";
  variant?: "purple" | "blue" | "green" | "orange" | "gradient";
  showValue?: boolean;
  children?: React.ReactNode;
  className?: string;
  animated?: boolean;
  glow?: boolean;
}

const sizeConfig = {
  sm: { width: 60, strokeWidth: 4, fontSize: "text-xs" },
  default: { width: 120, strokeWidth: 8, fontSize: "text-2xl" },
  lg: { width: 160, strokeWidth: 10, fontSize: "text-3xl" },
  xl: { width: 200, strokeWidth: 12, fontSize: "text-4xl" },
};

const thicknessConfig = {
  thin: 0.5,
  default: 1,
  thick: 1.5,
};

const variantConfig = {
  purple: {
    background: "stroke-purple-200 dark:stroke-purple-900",
    foreground: "stroke-electric-purple",
    glow: "drop-shadow-[0_0_10px_hsl(var(--electric-purple))]",
    gradient: "url(#purple-gradient)",
  },
  blue: {
    background: "stroke-blue-200 dark:stroke-blue-900",
    foreground: "stroke-electric-blue",
    glow: "drop-shadow-[0_0_10px_hsl(var(--neon-blue))]",
    gradient: "url(#blue-gradient)",
  },
  green: {
    background: "stroke-green-200 dark:stroke-green-900", 
    foreground: "stroke-vibrant-green",
    glow: "drop-shadow-[0_0_10px_hsl(var(--vibrant-green))]",
    gradient: "url(#green-gradient)",
  },
  orange: {
    background: "stroke-orange-200 dark:stroke-orange-900",
    foreground: "stroke-sunset-orange",
    glow: "drop-shadow-[0_0_10px_hsl(var(--sunset-orange))]",
    gradient: "url(#orange-gradient)",
  },
  gradient: {
    background: "stroke-gray-200 dark:stroke-gray-800",
    foreground: "",
    glow: "drop-shadow-[0_0_15px_rgba(124,58,237,0.5)]",
    gradient: "url(#rainbow-gradient)",
  },
};

export function ProgressRing({
  value,
  max = 100,
  size = "default",
  thickness = "default",
  variant = "purple",
  showValue = true,
  children,
  className,
  animated = true,
  glow = true,
}: ProgressRingProps) {
  const config = sizeConfig[size];
  const radius = (config.width - config.strokeWidth) / 2;
  const circumference = 2 * Math.PI * radius;
  const strokeWidth = config.strokeWidth * thicknessConfig[thickness];
  const progress = Math.min(Math.max(value, 0), max);
  const percentage = (progress / max) * 100;
  const variantStyle = variantConfig[variant];

  const center = config.width / 2;

  return (
    <div className={cn("relative inline-flex items-center justify-center", className)}>
      <svg
        width={config.width}
        height={config.width}
        className="transform -rotate-90"
        style={{ filter: glow ? variantStyle.glow : undefined }}
      >
        <defs>
          {/* Purple Gradient */}
          <linearGradient id="purple-gradient" x1="0%" y1="0%" x2="100%" y2="100%">
            <stop offset="0%" stopColor="hsl(var(--electric-purple))" />
            <stop offset="100%" stopColor="hsl(var(--bright-pink))" />
          </linearGradient>
          
          {/* Blue Gradient */}
          <linearGradient id="blue-gradient" x1="0%" y1="0%" x2="100%" y2="100%">
            <stop offset="0%" stopColor="hsl(var(--neon-blue))" />
            <stop offset="100%" stopColor="hsl(var(--electric-cyan))" />
          </linearGradient>
          
          {/* Green Gradient */}
          <linearGradient id="green-gradient" x1="0%" y1="0%" x2="100%" y2="100%">
            <stop offset="0%" stopColor="hsl(var(--vibrant-green))" />
            <stop offset="100%" stopColor="#10b981" />
          </linearGradient>
          
          {/* Orange Gradient */}
          <linearGradient id="orange-gradient" x1="0%" y1="0%" x2="100%" y2="100%">
            <stop offset="0%" stopColor="hsl(var(--sunset-orange))" />
            <stop offset="100%" stopColor="hsl(var(--coral-red))" />
          </linearGradient>
          
          {/* Rainbow Gradient */}
          <linearGradient id="rainbow-gradient" x1="0%" y1="0%" x2="100%" y2="100%">
            <stop offset="0%" stopColor="hsl(var(--electric-purple))" />
            <stop offset="25%" stopColor="hsl(var(--neon-blue))" />
            <stop offset="50%" stopColor="hsl(var(--vibrant-green))" />
            <stop offset="75%" stopColor="hsl(var(--sunset-orange))" />
            <stop offset="100%" stopColor="hsl(var(--bright-pink))" />
          </linearGradient>
        </defs>

        {/* Background Circle */}
        <circle
          cx={center}
          cy={center}
          r={radius}
          strokeWidth={strokeWidth}
          fill="none"
          className={variantStyle.background}
        />

        {/* Progress Circle */}
        <motion.circle
          cx={center}
          cy={center}
          r={radius}
          strokeWidth={strokeWidth}
          fill="none"
          stroke={variant === "gradient" ? variantStyle.gradient : undefined}
          className={variant !== "gradient" ? variantStyle.foreground : undefined}
          strokeLinecap="round"
          strokeDasharray={circumference}
          variants={animated ? progressRing : undefined}
          initial={animated ? "hidden" : undefined}
          animate={animated ? "visible" : undefined}
          custom={percentage}
          strokeDashoffset={animated ? undefined : circumference * (1 - percentage / 100)}
        />

        {/* Animated Glow Effect */}
        {glow && animated && (
          <motion.circle
            cx={center}
            cy={center}
            r={radius}
            strokeWidth={strokeWidth * 1.5}
            fill="none"
            stroke={variant === "gradient" ? variantStyle.gradient : undefined}
            className={variant !== "gradient" ? variantStyle.foreground : undefined}
            strokeLinecap="round"
            strokeDasharray={circumference}
            opacity={0.3}
            initial={{ strokeDashoffset: circumference }}
            animate={{ strokeDashoffset: circumference * (1 - percentage / 100) }}
            transition={{ duration: 1.5, ease: "easeOut", delay: 0.1 }}
          />
        )}
      </svg>

      {/* Center Content */}
      <div className="absolute inset-0 flex items-center justify-center">
        {children || (showValue && (
          <div className="text-center">
            <motion.div
              className={cn(
                "font-bold text-gradient-purple-pink",
                config.fontSize
              )}
              initial={animated ? { opacity: 0, scale: 0.5 } : undefined}
              animate={animated ? { opacity: 1, scale: 1 } : undefined}
              transition={animated ? { duration: 0.5, delay: 0.5 } : undefined}
            >
              {Math.round(percentage)}%
            </motion.div>
            {size !== "sm" && (
              <div className="text-xs text-muted-foreground mt-1">
                {value}{max !== 100 && `/${max}`}
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  );
}