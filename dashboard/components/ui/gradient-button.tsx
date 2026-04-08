// @ts-nocheck
"use client";

import * as React from "react";
import { motion, HTMLMotionProps } from "framer-motion";
import { Slot } from "@radix-ui/react-slot";
import { cva, type VariantProps } from "class-variance-authority";
import { cn } from "@/lib/utils";
import { buttonPress, rippleEffect } from "@/lib/animations";

const gradientButtonVariants = cva(
  "inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-xl text-sm font-semibold transition-all duration-300 focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 overflow-hidden relative",
  {
    variants: {
      variant: {
        purple: "bg-gradient-purple-pink text-white shadow-glow hover:shadow-glow-lg",
        blue: "bg-gradient-blue-cyan text-white shadow-glow-blue hover:shadow-glow-blue",
        green: "bg-gradient-green-emerald text-white shadow-glow-green hover:shadow-glow-green",
        orange: "bg-gradient-orange-red text-white shadow-glow-orange hover:shadow-glow-orange",
        gold: "bg-gradient-gold-orange text-white shadow-glow-orange hover:shadow-glow-orange",
        glass: "glass backdrop-blur-md border-white/20 text-white hover:bg-white/20",
        outline: "border-2 border-electric-purple text-electric-purple hover:bg-electric-purple hover:text-white",
      },
      size: {
        sm: "h-8 px-3 text-xs",
        default: "h-10 px-4 py-2",
        lg: "h-12 px-6 text-base",
        xl: "h-14 px-8 text-lg",
        icon: "h-10 w-10",
      },
      glow: {
        none: "",
        soft: "shadow-glow-sm",
        medium: "shadow-glow",
        strong: "shadow-glow-lg animate-pulse-glow",
      },
    },
    defaultVariants: {
      variant: "purple",
      size: "default",
      glow: "medium",
    },
  }
);

interface GradientButtonProps
  extends React.ButtonHTMLAttributes<HTMLButtonElement>,
    VariantProps<typeof gradientButtonVariants> {
  asChild?: boolean;
  ripple?: boolean;
}

const GradientButton = React.forwardRef<HTMLButtonElement, GradientButtonProps>(
  ({ className, variant, size, glow, asChild = false, ripple = true, children, onMouseDown, ...props }, ref) => {
    const [ripples, setRipples] = React.useState<Array<{ x: number; y: number; id: number }>>([]);

    const createRipple = (event: React.MouseEvent<HTMLButtonElement>) => {
      if (!ripple) return;
      
      const button = event.currentTarget;
      const rect = button.getBoundingClientRect();
      const x = event.clientX - rect.left;
      const y = event.clientY - rect.top;
      const id = Date.now();

      setRipples(prev => [...prev, { x, y, id }]);

      // Remove ripple after animation
      setTimeout(() => {
        setRipples(prev => prev.filter(ripple => ripple.id !== id));
      }, 600);
      
      // Call original handler if provided
      onMouseDown?.(event);
    };

    // Don't use motion with asChild
    if (asChild) {
      return (
        <Slot 
          className={cn(gradientButtonVariants({ variant, size, glow, className }))} 
          ref={ref as any}
          onMouseDown={onMouseDown}
          {...(props as any)}
        >
          {children}
        </Slot>
      );
    }

    return (
      <motion.button
        className={cn(gradientButtonVariants({ variant, size, glow, className }))}
        ref={ref}
        variants={buttonPress}
        initial="rest"
        whileHover="hover"
        whileTap="pressed"
        onMouseDown={createRipple as any}
        {...(props as any)}
      >
        {/* Gradient Background Animation */}
        <div className="absolute inset-0 bg-gradient-to-r from-transparent via-white/20 to-transparent -skew-x-12 -translate-x-full group-hover:translate-x-full transition-transform duration-700 ease-out" />
        
        {/* Ripple Effects */}
        {ripples.map((ripple) => (
          <motion.span
            key={ripple.id}
            className="absolute rounded-full bg-white/30 pointer-events-none"
            style={{
              left: ripple.x - 10,
              top: ripple.y - 10,
              width: 20,
              height: 20,
            }}
            initial={{ scale: 0, opacity: 0.5 }}
            animate={{ scale: 4, opacity: 0 }}
            transition={{ duration: 0.6, ease: "easeOut" }}
          />
        ))}

        {/* Content */}
        <span className="relative z-10 flex items-center gap-2">
          {children}
        </span>
      </motion.button>
    );
  }
);

GradientButton.displayName = "GradientButton";

export { GradientButton, gradientButtonVariants };
export type { GradientButtonProps };