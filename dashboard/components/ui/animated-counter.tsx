"use client";

import * as React from "react";
import { motion, useMotionValue, useTransform, animate } from "framer-motion";
import { cn } from "@/lib/utils";

interface AnimatedCounterProps {
  value: number;
  duration?: number;
  delay?: number;
  className?: string;
  prefix?: string;
  suffix?: string;
  decimal?: number;
  separator?: string;
  gradient?: boolean;
  size?: "sm" | "default" | "lg" | "xl";
}

const sizeClasses = {
  sm: "text-lg",
  default: "text-2xl",
  lg: "text-3xl", 
  xl: "text-4xl",
};

export function AnimatedCounter({
  value,
  duration = 1,
  delay = 0,
  className,
  prefix = "",
  suffix = "",
  decimal = 0,
  separator = ",",
  gradient = false,
  size = "default",
}: AnimatedCounterProps) {
  const count = useMotionValue(0);
  const rounded = useTransform(count, (latest) => {
    return formatNumber(Math.round(latest * Math.pow(10, decimal)) / Math.pow(10, decimal));
  });

  const [hasAnimated, setHasAnimated] = React.useState(false);

  function formatNumber(num: number): string {
    if (separator && num >= 1000) {
      return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, separator);
    }
    return num.toFixed(decimal);
  }

  React.useEffect(() => {
    if (!hasAnimated) {
      const controls = animate(count, value, {
        duration,
        delay,
        ease: [0.25, 0.46, 0.45, 0.94],
      });

      setHasAnimated(true);

      return controls.stop;
    }
  }, [count, value, duration, delay, hasAnimated]);

  return (
    <motion.span
      className={cn(
        "font-bold tabular-nums",
        sizeClasses[size],
        gradient && "text-gradient-purple-pink",
        className
      )}
      initial={{ opacity: 0, scale: 0.8 }}
      animate={{ opacity: 1, scale: 1 }}
      transition={{ duration: 0.3, delay }}
    >
      {prefix}
      <motion.span>{rounded}</motion.span>
      {suffix}
    </motion.span>
  );
}

interface CountUpStatsProps {
  stats: Array<{
    label: string;
    value: number;
    prefix?: string;
    suffix?: string;
    decimal?: number;
    color?: "purple" | "blue" | "green" | "orange";
  }>;
  className?: string;
  staggerDelay?: number;
}

export function CountUpStats({ stats, className, staggerDelay = 0.1 }: CountUpStatsProps) {
  const colorClasses = {
    purple: "text-electric-purple",
    blue: "text-electric-blue",
    green: "text-vibrant-green",
    orange: "text-sunset-orange",
  };

  return (
    <div className={cn("grid grid-cols-2 gap-4", className)}>
      {stats.map((stat, index) => (
        <motion.div
          key={stat.label}
          className="text-center"
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.4, delay: index * staggerDelay }}
        >
          <AnimatedCounter
            value={stat.value}
            prefix={stat.prefix}
            suffix={stat.suffix}
            decimal={stat.decimal || 0}
            delay={index * staggerDelay + 0.3}
            className={cn(
              "block",
              stat.color && colorClasses[stat.color]
            )}
          />
          <p className="text-xs text-muted-foreground mt-1">{stat.label}</p>
        </motion.div>
      ))}
    </div>
  );
}