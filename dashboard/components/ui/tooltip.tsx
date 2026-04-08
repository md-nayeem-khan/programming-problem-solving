"use client";

import * as React from "react";
import * as TooltipPrimitive from "@radix-ui/react-tooltip";
import { motion, AnimatePresence } from "framer-motion";
import { cn } from "@/lib/utils";
import { tooltipEnhanced } from "@/lib/animations";

const TooltipProvider = TooltipPrimitive.Provider;

const TooltipRoot = TooltipPrimitive.Root;

const TooltipTrigger = TooltipPrimitive.Trigger;

interface TooltipContentProps extends React.ComponentPropsWithoutRef<typeof TooltipPrimitive.Content> {
  variant?: "default" | "premium" | "glass" | "glow";
  showArrow?: boolean;
}

const TooltipContent = React.forwardRef<
  React.ElementRef<typeof TooltipPrimitive.Content>,
  TooltipContentProps
>(({ className, sideOffset = 4, variant = "default", showArrow = true, children, ...props }, ref) => {
  const variantClasses = {
    default: "bg-popover text-popover-foreground border border-border",
    premium: "bg-gradient-to-br from-electric-purple/90 to-bright-pink/90 text-white border-0 shadow-glow-purple",
    glass: "bg-white/10 backdrop-blur-md text-foreground border border-white/20 shadow-glass",
    glow: "bg-gradient-to-br from-electric-purple/20 to-bright-pink/20 backdrop-blur-md text-foreground border border-electric-purple/30 shadow-glow-electric-purple",
  };

  return (
    <TooltipPrimitive.Portal>
      <TooltipPrimitive.Content
        ref={ref}
        sideOffset={sideOffset}
        className={cn("z-50 overflow-hidden rounded-md px-3 py-1.5 text-sm", variantClasses[variant], className)}
        asChild
        {...props}
      >
        <motion.div
          variants={tooltipEnhanced}
          initial="hidden"
          animate="visible"
          exit="hidden"
        >
          {children}
          {showArrow && (
            <TooltipPrimitive.Arrow 
              className={cn(
                "fill-current",
                variant === "premium" && "text-electric-purple",
                variant === "glass" && "text-white/10",
                variant === "glow" && "text-electric-purple/30",
                variant === "default" && "text-border"
              )}
            />
          )}
        </motion.div>
      </TooltipPrimitive.Content>
    </TooltipPrimitive.Portal>
  );
});
TooltipContent.displayName = TooltipPrimitive.Content.displayName;

interface EnhancedTooltipProps {
  children: React.ReactNode;
  content: React.ReactNode;
  variant?: "default" | "premium" | "glass" | "glow";
  showArrow?: boolean;
  side?: "top" | "right" | "bottom" | "left";
  align?: "start" | "center" | "end";
  delayDuration?: number;
  disabled?: boolean;
}

export function EnhancedTooltip({
  children,
  content,
  variant = "default",
  showArrow = true,
  side = "top",
  align = "center",
  delayDuration = 200,
  disabled = false,
}: EnhancedTooltipProps) {
  if (disabled) {
    return <>{children}</>;
  }

  return (
    <TooltipProvider delayDuration={delayDuration}>
      <TooltipRoot>
        <TooltipTrigger asChild>
          {children}
        </TooltipTrigger>
        <TooltipContent
          variant={variant}
          showArrow={showArrow}
          side={side}
          align={align}
        >
          {content}
        </TooltipContent>
      </TooltipRoot>
    </TooltipProvider>
  );
}

// Legacy compatibility
function Tooltip({
  ...props
}: React.ComponentProps<typeof TooltipPrimitive.Root>) {
  return <TooltipPrimitive.Root data-slot="tooltip" {...props} />
}

export { TooltipProvider, Tooltip, TooltipTrigger, TooltipContent };
