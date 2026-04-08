"use client";

import * as React from "react";
import { motion, AnimatePresence } from "framer-motion";
import { Eye, EyeOff, Search, X } from "lucide-react";
import { cn } from "@/lib/utils";
import { inputFocus, buttonRipple } from "@/lib/animations";

interface EnhancedInputProps extends Omit<React.InputHTMLAttributes<HTMLInputElement>, "size"> {
  variant?: "default" | "glass" | "premium" | "minimal";
  size?: "sm" | "default" | "lg";
  leftIcon?: React.ReactNode;
  rightIcon?: React.ReactNode;
  clearable?: boolean;
  loading?: boolean;
  error?: string;
  success?: boolean;
  showPasswordToggle?: boolean;
}

const EnhancedInput = React.forwardRef<HTMLInputElement, EnhancedInputProps>(
  ({
    className,
    variant = "default",
    size = "default",
    type = "text",
    leftIcon,
    rightIcon,
    clearable = false,
    loading = false,
    error,
    success = false,
    showPasswordToggle = false,
    value,
    onChange,
    ...props
  }, ref) => {
    const [isFocused, setIsFocused] = React.useState(false);
    const [showPassword, setShowPassword] = React.useState(false);
    const [internalValue, setInternalValue] = React.useState(value || "");

    const inputType = showPasswordToggle ? (showPassword ? "text" : "password") : type;

    const sizeClasses = {
      sm: "h-8 px-3 text-sm",
      default: "h-10 px-3 text-sm",
      lg: "h-12 px-4 text-base",
    };

    const variantClasses = {
      default: "bg-background border border-input",
      glass: "bg-white/60 backdrop-blur-md border border-gray-200/40",
      premium: "bg-gradient-to-r from-electric-purple/5 to-bright-pink/5 border border-electric-purple/20",
      minimal: "bg-transparent border-0 border-b-2 border-input rounded-none",
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
      const newValue = e.target.value;
      setInternalValue(newValue);
      onChange?.(e);
    };

    const handleClear = () => {
      setInternalValue("");
      if (onChange) {
        const syntheticEvent = {
          target: { value: "" },
          currentTarget: { value: "" },
        } as React.ChangeEvent<HTMLInputElement>;
        onChange(syntheticEvent);
      }
    };

    const hasValue = Boolean(internalValue || value);
    const shouldShowClear = clearable && hasValue && !loading;
    const shouldShowPasswordToggle = showPasswordToggle && type === "password";

    return (
      <div className="relative w-full">
        <motion.div
          className={cn(
            "relative flex items-center w-full rounded-md border transition-colors",
            "focus-within:outline-none focus-within:ring-2 focus-within:ring-ring focus-within:ring-offset-2",
            sizeClasses[size],
            variantClasses[variant],
            error && "border-destructive focus-within:ring-destructive",
            success && "border-success-vibrant focus-within:ring-success-vibrant",
            isFocused && !error && !success && "border-electric-purple shadow-glow-electric-purple/20",
            "disabled:cursor-not-allowed disabled:opacity-50",
            className
          )}
          variants={inputFocus}
          animate={isFocused ? "focused" : "unfocused"}
        >
          {/* Left Icon */}
          {leftIcon && (
            <div className="flex items-center justify-center pl-3">
              <motion.div
                animate={{ scale: isFocused ? 1.1 : 1 }}
                transition={{ duration: 0.2 }}
                className={cn(
                  "text-muted-foreground",
                  isFocused && "text-electric-purple",
                  error && "text-destructive",
                  success && "text-success-vibrant"
                )}
              >
                {leftIcon}
              </motion.div>
            </div>
          )}

          {/* Input */}
          <input
            ref={ref}
            type={inputType}
            className={cn(
              "flex-1 bg-transparent outline-none placeholder:text-muted-foreground disabled:cursor-not-allowed",
              leftIcon && "pl-2" // Add padding-left when there's a left icon
            )}
            value={value}
            onChange={handleChange}
            onFocus={() => setIsFocused(true)}
            onBlur={() => setIsFocused(false)}
            {...props}
          />

          {/* Right Icons */}
          <div className="flex items-center gap-1 pr-3">
            {/* Loading Spinner */}
            <AnimatePresence>
              {loading && (
                <motion.div
                  initial={{ opacity: 0, scale: 0 }}
                  animate={{ opacity: 1, scale: 1 }}
                  exit={{ opacity: 0, scale: 0 }}
                  className="w-4 h-4"
                >
                  <div className="w-4 h-4 border-2 border-electric-purple/20 border-t-electric-purple rounded-full animate-spin" />
                </motion.div>
              )}
            </AnimatePresence>

            {/* Clear Button */}
            <AnimatePresence>
              {shouldShowClear && (
                <motion.button
                  type="button"
                  onClick={handleClear}
                  variants={buttonRipple}
                  whileHover="hover"
                  whileTap="tap"
                  initial={{ opacity: 0, scale: 0 }}
                  animate={{ opacity: 1, scale: 1 }}
                  exit={{ opacity: 0, scale: 0 }}
                  className="w-4 h-4 text-muted-foreground hover:text-foreground transition-colors"
                >
                  <X size={14} />
                </motion.button>
              )}
            </AnimatePresence>

            {/* Password Toggle */}
            <AnimatePresence>
              {shouldShowPasswordToggle && (
                <motion.button
                  type="button"
                  onClick={() => setShowPassword(!showPassword)}
                  variants={buttonRipple}
                  whileHover="hover"
                  whileTap="tap"
                  initial={{ opacity: 0, scale: 0 }}
                  animate={{ opacity: 1, scale: 1 }}
                  exit={{ opacity: 0, scale: 0 }}
                  className="w-4 h-4 text-muted-foreground hover:text-foreground transition-colors"
                >
                  {showPassword ? <EyeOff size={14} /> : <Eye size={14} />}
                </motion.button>
              )}
            </AnimatePresence>

            {/* Right Icon */}
            {rightIcon && !loading && !shouldShowClear && !shouldShowPasswordToggle && (
              <motion.div
                animate={{ scale: isFocused ? 1.1 : 1 }}
                transition={{ duration: 0.2 }}
                className={cn(
                  "text-muted-foreground",
                  isFocused && "text-electric-purple",
                  error && "text-destructive",
                  success && "text-success-vibrant"
                )}
              >
                {rightIcon}
              </motion.div>
            )}
          </div>

          {/* Focus Indicator */}
          <motion.div
            className="absolute inset-x-0 bottom-0 h-0.5 bg-gradient-to-r from-electric-purple to-bright-pink"
            initial={{ scaleX: 0 }}
            animate={{ scaleX: isFocused ? 1 : 0 }}
            transition={{ duration: 0.3, ease: "easeOut" }}
            style={{ originX: 0.5 }}
          />
        </motion.div>

        {/* Error/Success Message */}
        <AnimatePresence>
          {(error || success) && (
            <motion.div
              initial={{ opacity: 0, y: -10 }}
              animate={{ opacity: 1, y: 0 }}
              exit={{ opacity: 0, y: -10 }}
              className={cn(
                "mt-1 text-xs",
                error && "text-destructive",
                success && "text-success-vibrant"
              )}
            >
              {error || (success && "Looks good!")}
            </motion.div>
          )}
        </AnimatePresence>
      </div>
    );
  }
);

EnhancedInput.displayName = "EnhancedInput";

interface SearchInputProps extends Omit<EnhancedInputProps, "leftIcon" | "type"> {
  onSearchClear?: () => void;
}

const SearchInput = React.forwardRef<HTMLInputElement, SearchInputProps>(
  ({ onSearchClear, ...props }, ref) => {
    return (
      <EnhancedInput
        ref={ref}
        type="search"
        leftIcon={<Search size={16} />}
        clearable
        placeholder="Search..."
        {...props}
        onChange={(e) => {
          props.onChange?.(e);
          if (e.target.value === "" && onSearchClear) {
            onSearchClear();
          }
        }}
      />
    );
  }
);

SearchInput.displayName = "SearchInput";

export { EnhancedInput, SearchInput };