// @ts-nocheck
"use client";

import * as React from "react";
import { motion, useMotionValue, useSpring, useTransform } from "framer-motion";
import { cn } from "@/lib/utils";
import { cardHoverTilt, shimmer } from "@/lib/animations";

interface InteractiveCardProps {
  children: React.ReactNode;
  className?: string;
  tiltIntensity?: number;
  glowOnHover?: boolean;
  shimmerOnHover?: boolean;
  scaleOnHover?: boolean;
  variant?: "default" | "glass" | "premium" | "minimal";
  disabled?: boolean;
  onClick?: () => void;
}

export function InteractiveCard({
  children,
  className,
  tiltIntensity = 1,
  glowOnHover = true,
  shimmerOnHover = false,
  scaleOnHover = true,
  variant = "default",
  disabled = false,
  onClick,
}: InteractiveCardProps) {
  const x = useMotionValue(0);
  const y = useMotionValue(0);
  
  const mouseXSpring = useSpring(x, { stiffness: 400, damping: 40 });
  const mouseYSpring = useSpring(y, { stiffness: 400, damping: 40 });

  const rotateX = useTransform(mouseYSpring, [-0.5, 0.5], [15 * tiltIntensity, -15 * tiltIntensity]);
  const rotateY = useTransform(mouseXSpring, [-0.5, 0.5], [-15 * tiltIntensity, 15 * tiltIntensity]);

  const handleMouseMove = (e: React.MouseEvent<HTMLDivElement>) => {
    if (disabled) return;
    
    const rect = e.currentTarget.getBoundingClientRect();
    const width = rect.width;
    const height = rect.height;
    
    const mouseX = e.clientX - rect.left;
    const mouseY = e.clientY - rect.top;
    
    const xPct = mouseX / width - 0.5;
    const yPct = mouseY / height - 0.5;
    
    x.set(xPct);
    y.set(yPct);
  };

  const handleMouseLeave = () => {
    if (disabled) return;
    x.set(0);
    y.set(0);
  };

  const variantClasses = {
    default: "bg-card border border-border",
    glass: "bg-gradient-to-br from-white/10 to-white/5 backdrop-blur-md border border-white/20",
    premium: "bg-gradient-to-br from-electric-purple/10 via-bright-pink/5 to-electric-cyan/10 border border-electric-purple/20",
    minimal: "bg-transparent border-0",
  };

  return (
    <motion.div
      className={cn(
        "relative rounded-xl overflow-hidden transition-all duration-300",
        "transform-gpu", // Enable GPU acceleration
        variantClasses[variant],
        !disabled && glowOnHover && "hover:shadow-glow-electric-purple/20",
        !disabled && scaleOnHover && "hover:scale-[1.02]",
        className
      )}
      style={{
        rotateX: disabled ? 0 : rotateX,
        rotateY: disabled ? 0 : rotateY,
        transformStyle: "preserve-3d",
      }}
      onMouseMove={handleMouseMove}
      onMouseLeave={handleMouseLeave}
      whileHover={!disabled ? "hover" : undefined}
      variants={disabled ? undefined : cardHoverTilt}
      onClick={onClick}
    >
      {/* Shimmer overlay */}
      {shimmerOnHover && !disabled && (
        <motion.div
          className="absolute inset-0 opacity-0 bg-gradient-to-r from-transparent via-white/10 to-transparent -translate-x-full"
          variants={shimmer}
          style={{
            background: "linear-gradient(90deg, transparent 0%, rgba(255,255,255,0.1) 50%, transparent 100%)",
          }}
        />
      )}

      {/* Glow effect overlay */}
      {glowOnHover && !disabled && (
        <div className="absolute inset-0 rounded-xl bg-gradient-to-br from-electric-purple/0 via-bright-pink/0 to-electric-cyan/0 group-hover:from-electric-purple/5 group-hover:via-bright-pink/5 group-hover:to-electric-cyan/5 transition-all duration-300 pointer-events-none" />
      )}

      {/* Content */}
      <div className="relative z-10">
        {children}
      </div>

      {/* Floating orbs for premium variant */}
      {variant === "premium" && !disabled && (
        <>
          <div className="absolute -top-4 -right-4 w-8 h-8 bg-gradient-to-br from-electric-purple/20 to-bright-pink/20 rounded-full blur-sm opacity-0 group-hover:opacity-100 transition-all duration-500" />
          <div className="absolute -bottom-4 -left-4 w-6 h-6 bg-gradient-to-br from-electric-cyan/20 to-electric-blue/20 rounded-full blur-sm opacity-0 group-hover:opacity-100 transition-all duration-500 delay-100" />
        </>
      )}
    </motion.div>
  );
}

interface FloatingActionButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  icon: React.ReactNode;
  label?: string;
  variant?: "default" | "premium" | "danger" | "success";
  size?: "sm" | "default" | "lg";
  position?: "bottom-right" | "bottom-left" | "top-right" | "top-left";
}

export function FloatingActionButton({
  icon,
  label,
  variant = "default",
  size = "default",
  position = "bottom-right",
  className,
  ...props
}: FloatingActionButtonProps) {
  const sizeClasses = {
    sm: "w-12 h-12",
    default: "w-14 h-14",
    lg: "w-16 h-16",
  };

  const positionClasses = {
    "bottom-right": "fixed bottom-6 right-6",
    "bottom-left": "fixed bottom-6 left-6",
    "top-right": "fixed top-20 right-6",
    "top-left": "fixed top-20 left-6",
  };

  const variantClasses = {
    default: "bg-gradient-to-r from-electric-purple to-bright-pink hover:from-electric-purple/90 hover:to-bright-pink/90 shadow-glow-purple",
    premium: "bg-gradient-to-r from-gold to-amber-400 hover:from-gold/90 hover:to-amber-400/90 shadow-glow-gold",
    danger: "bg-gradient-to-r from-red-500 to-pink-500 hover:from-red-500/90 hover:to-pink-500/90 shadow-glow-red",
    success: "bg-gradient-to-r from-success-vibrant to-lime-400 hover:from-success-vibrant/90 hover:to-lime-400/90 shadow-glow-success",
  };

  return (
    <motion.button
      className={cn(
        "rounded-full text-white flex items-center justify-center z-50",
        "backdrop-blur-sm border border-white/20",
        "transform-gpu transition-all duration-300",
        sizeClasses[size],
        positionClasses[position],
        variantClasses[variant],
        className
      )}
      whileHover={{ scale: 1.1, rotate: 5 }}
      whileTap={{ scale: 0.95 }}
      initial={{ scale: 0, rotate: -180 }}
      animate={{ scale: 1, rotate: 0 }}
      transition={{ type: "spring", stiffness: 400, damping: 25 }}
      {...props}
    >
      <motion.div
        whileHover={{ rotate: 360 }}
        transition={{ duration: 0.6, ease: "easeOut" }}
      >
        {icon}
      </motion.div>
      
      {label && (
        <motion.span
          className="absolute right-full mr-3 px-2 py-1 bg-black/80 text-white text-xs rounded whitespace-nowrap"
          initial={{ opacity: 0, x: 10 }}
          whileHover={{ opacity: 1, x: 0 }}
          transition={{ duration: 0.2 }}
        >
          {label}
        </motion.span>
      )}
    </motion.button>
  );
}