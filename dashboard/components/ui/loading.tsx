"use client";

import { motion } from "framer-motion";
import { cn } from "@/lib/utils";
import { loadingDots, pulseGlow, shimmer } from "@/lib/animations";

interface LoadingSpinnerProps {
  size?: "sm" | "default" | "lg";
  variant?: "dots" | "spinner" | "pulse" | "shimmer";
  className?: string;
}

export function LoadingSpinner({ size = "default", variant = "dots", className }: LoadingSpinnerProps) {
  const sizeClasses = {
    sm: "w-4 h-4",
    default: "w-8 h-8", 
    lg: "w-12 h-12",
  };

  if (variant === "dots") {
    return (
      <div className={cn("flex items-center justify-center gap-1", className)}>
        {[0, 1, 2].map((i) => (
          <motion.div
            key={i}
            className={cn(
              "rounded-full bg-gradient-to-r from-electric-purple to-bright-pink",
              size === "sm" ? "w-2 h-2" : size === "lg" ? "w-4 h-4" : "w-3 h-3"
            )}
            variants={loadingDots}
            initial="loading"
            animate="loading"
            custom={i}
          />
        ))}
      </div>
    );
  }

  if (variant === "pulse") {
    return (
      <motion.div
        className={cn(
          "rounded-full bg-gradient-to-r from-electric-purple to-bright-pink",
          sizeClasses[size],
          className
        )}
        variants={pulseGlow}
        animate="pulse"
      />
    );
  }

  if (variant === "shimmer") {
    return (
      <div
        className={cn(
          "rounded-lg bg-gradient-to-r from-muted via-muted-foreground/20 to-muted animate-shimmer",
          sizeClasses[size],
          className
        )}
        style={{
          backgroundSize: "200% 100%",
        }}
      />
    );
  }

  // Default spinner
  return (
    <motion.div
      className={cn(
        "border-4 border-muted border-t-electric-purple rounded-full",
        sizeClasses[size],
        className
      )}
      animate={{ rotate: 360 }}
      transition={{ duration: 1, repeat: Infinity, ease: "linear" }}
    />
  );
}

interface LoadingCardProps {
  title?: string;
  description?: string;
  className?: string;
}

export function LoadingCard({ title = "Loading...", description, className }: LoadingCardProps) {
  return (
    <motion.div
      className={cn(
        "p-6 rounded-xl bg-gradient-to-br from-white/10 to-white/5 backdrop-blur-md border border-white/20",
        className
      )}
      variants={shimmer}
      animate="shimmer"
    >
      <div className="flex items-center gap-3 mb-4">
        <LoadingSpinner variant="pulse" size="sm" />
        <div>
          <h3 className="text-sm font-semibold">{title}</h3>
          {description && (
            <p className="text-xs text-muted-foreground mt-1">{description}</p>
          )}
        </div>
      </div>
      
      <div className="space-y-3">
        <div className="h-3 bg-gradient-to-r from-muted via-muted-foreground/20 to-muted rounded animate-shimmer" />
        <div className="h-3 bg-gradient-to-r from-muted via-muted-foreground/20 to-muted rounded animate-shimmer w-3/4" />
        <div className="h-3 bg-gradient-to-r from-muted via-muted-foreground/20 to-muted rounded animate-shimmer w-1/2" />
      </div>
    </motion.div>
  );
}