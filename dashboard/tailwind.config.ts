import type { Config } from "tailwindcss";

const config: Config = {
  darkMode: "class",
  content: [
    "./pages/**/*.{ts,tsx}",
    "./components/**/*.{ts,tsx}",
    "./app/**/*.{ts,tsx}",
    "./src/**/*.{ts,tsx}",
  ],
  theme: {
    container: {
      center: true,
      padding: "2rem",
      screens: {
        "2xl": "1400px",
      },
    },
    extend: {
      colors: {
        border: "hsl(var(--border))",
        input: "hsl(var(--input))",
        ring: "hsl(var(--ring))",
        background: "hsl(var(--background))",
        foreground: "hsl(var(--foreground))",
        primary: {
          DEFAULT: "hsl(var(--primary))",
          foreground: "hsl(var(--primary-foreground))",
        },
        secondary: {
          DEFAULT: "hsl(var(--secondary))",
          foreground: "hsl(var(--secondary-foreground))",
        },
        destructive: {
          DEFAULT: "hsl(var(--destructive))",
          foreground: "hsl(var(--destructive-foreground))",
        },
        muted: {
          DEFAULT: "hsl(var(--muted))",
          foreground: "hsl(var(--muted-foreground))",
        },
        accent: {
          DEFAULT: "hsl(var(--accent))",
          foreground: "hsl(var(--accent-foreground))",
        },
        popover: {
          DEFAULT: "hsl(var(--popover))",
          foreground: "hsl(var(--popover-foreground))",
        },
        card: {
          DEFAULT: "hsl(var(--card))",
          foreground: "hsl(var(--card-foreground))",
          readiness: "hsl(var(--card-readiness))",
          progress: "hsl(var(--card-progress))",
          pattern: "hsl(var(--card-pattern))",
          weak: "hsl(var(--card-weak))",
          time: "hsl(var(--card-time))",
          company: "hsl(var(--card-company))",
          revision: "hsl(var(--card-revision))",
          mock: "hsl(var(--card-mock))",
        },
        success: {
          DEFAULT: "hsl(var(--success))",
          foreground: "hsl(var(--success-foreground))",
        },
        warning: {
          DEFAULT: "hsl(var(--warning))",
          foreground: "hsl(var(--warning-foreground))",
        },
        danger: {
          DEFAULT: "hsl(var(--danger))",
          foreground: "hsl(var(--danger-foreground))",
        },
        info: {
          DEFAULT: "hsl(var(--info))",
          foreground: "hsl(var(--info-foreground))",
        },
        pattern: {
          strong: "hsl(var(--pattern-strong))",
          medium: "hsl(var(--pattern-medium))",
          weak: "hsl(var(--pattern-weak))",
        },
        company: {
          amazon: "hsl(var(--amazon-orange))",
          google: {
            blue: "hsl(var(--google-blue))",
            red: "hsl(var(--google-red))",
            yellow: "hsl(var(--google-yellow))",
            green: "hsl(var(--google-green))",
          },
          meta: "hsl(var(--meta-blue))",
          apple: "hsl(var(--apple-gray))",
          netflix: "hsl(var(--netflix-red))",
          microsoft: "hsl(var(--microsoft-blue))",
        },
        chart: {
          1: "hsl(var(--chart-1))",
          2: "hsl(var(--chart-2))",
          3: "hsl(var(--chart-3))",
          4: "hsl(var(--chart-4))",
          5: "hsl(var(--chart-5))",
        },
        // Premium Color Palette
        "electric-purple": "hsl(var(--electric-purple))",
        "bright-pink": "hsl(var(--bright-pink))",
        "neon-blue": "hsl(var(--neon-blue))",
        "electric-cyan": "hsl(var(--electric-cyan))",
        "vibrant-green": "hsl(var(--vibrant-green))",
        "golden-yellow": "hsl(var(--golden-yellow))",
        "sunset-orange": "hsl(var(--sunset-orange))",
        "coral-red": "hsl(var(--coral-red))",
        // Enhanced V2 Color Palette
        cyber: {
          start: "hsl(var(--cyber-start))",
          end: "hsl(var(--cyber-end))",
          accent: "hsl(var(--cyber-accent))",
        },
        aurora: {
          start: "hsl(var(--aurora-start))",
          mid: "hsl(var(--aurora-mid))",
          end: "hsl(var(--aurora-end))",
        },
        sunset: {
          start: "hsl(var(--sunset-start))",
          mid: "hsl(var(--sunset-mid))",
          end: "hsl(var(--sunset-end))",
        },
        ocean: {
          start: "hsl(var(--ocean-start))",
          mid: "hsl(var(--ocean-mid))",
          end: "hsl(var(--ocean-end))",
        },
      },
      borderRadius: {
        lg: "var(--radius)",
        md: "calc(var(--radius) - 2px)",
        sm: "calc(var(--radius) - 4px)",
      },
      keyframes: {
        "accordion-down": {
          from: { height: "0" },
          to: { height: "var(--radix-accordion-content-height)" },
        },
        "accordion-up": {
          from: { height: "var(--radix-accordion-content-height)" },
          to: { height: "0" },
        },
        "fade-in": {
          from: { opacity: "0" },
          to: { opacity: "1" },
        },
        "fade-out": {
          from: { opacity: "1" },
          to: { opacity: "0" },
        },
        "slide-up": {
          from: { transform: "translateY(20px)", opacity: "0" },
          to: { transform: "translateY(0)", opacity: "1" },
        },
        "slide-down": {
          from: { transform: "translateY(-20px)", opacity: "0" },
          to: { transform: "translateY(0)", opacity: "1" },
        },
        "slide-left": {
          from: { transform: "translateX(20px)", opacity: "0" },
          to: { transform: "translateX(0)", opacity: "1" },
        },
        "slide-right": {
          from: { transform: "translateX(-20px)", opacity: "0" },
          to: { transform: "translateX(0)", opacity: "1" },
        },
        "scale-up": {
          from: { transform: "scale(0.95)", opacity: "0" },
          to: { transform: "scale(1)", opacity: "1" },
        },
        "scale-down": {
          from: { transform: "scale(1.05)", opacity: "0" },
          to: { transform: "scale(1)", opacity: "1" },
        },
        shimmer: {
          "0%": { backgroundPosition: "-200% 0" },
          "100%": { backgroundPosition: "200% 0" },
        },
        float: {
          "0%, 100%": { transform: "translateY(0px)" },
          "50%": { transform: "translateY(-20px)" },
        },
        "pulse-glow": {
          "0%, 100%": { boxShadow: "0 0 20px rgba(59, 130, 246, 0.5)" },
          "50%": { boxShadow: "0 0 40px rgba(59, 130, 246, 0.8)" },
        },
        "count-up": {
          from: { transform: "scale(0.5)", opacity: "0" },
          to: { transform: "scale(1)", opacity: "1" },
        },
      },
      animation: {
        "accordion-down": "accordion-down 0.2s ease-out",
        "accordion-up": "accordion-up 0.2s ease-out",
        "fade-in": "fade-in 0.3s ease-out",
        "fade-out": "fade-out 0.3s ease-out",
        "slide-up": "slide-up 0.3s ease-out",
        "slide-down": "slide-down 0.3s ease-out",
        "slide-left": "slide-left 0.3s ease-out",
        "slide-right": "slide-right 0.3s ease-out",
        "scale-up": "scale-up 0.3s ease-out",
        "scale-down": "scale-down 0.3s ease-out",
        shimmer: "shimmer 2s linear infinite",
        float: "float 6s ease-in-out infinite",
        "pulse-glow": "pulse-glow 2s ease-in-out infinite",
        "count-up": "count-up 0.5s ease-out",
      },
      transitionDuration: {
        fast: "150ms",
        normal: "300ms",
        slow: "500ms",
        slower: "1000ms",
      },
      transitionTimingFunction: {
        spring: "cubic-bezier(0.68, -0.55, 0.265, 1.55)",
        "ease-out-back": "cubic-bezier(0.34, 1.56, 0.64, 1)",
      },
      backdropBlur: {
        xs: "2px",
      },
      boxShadow: {
        "glow-sm": "0 0 10px rgba(var(--glow-info-rgb), 0.3)",
        "glow-md": "0 0 20px rgba(var(--glow-info-rgb), 0.4), 0 0 40px rgba(var(--glow-info-rgb), 0.2)",
        "glow-lg": "0 0 30px rgba(var(--glow-info-rgb), 0.5), 0 0 60px rgba(var(--glow-info-rgb), 0.3)",
        "glow-success-sm": "0 0 10px rgba(var(--glow-success-rgb), 0.3)",
        "glow-success-md": "0 0 20px rgba(var(--glow-success-rgb), 0.4), 0 0 40px rgba(var(--glow-success-rgb), 0.2)",
        "glow-warning-sm": "0 0 10px rgba(var(--glow-warning-rgb), 0.3)",
        "glow-warning-md": "0 0 20px rgba(var(--glow-warning-rgb), 0.4), 0 0 40px rgba(var(--glow-warning-rgb), 0.2)",
        "glow-danger-sm": "0 0 10px rgba(var(--glow-danger-rgb), 0.3)",
        "glow-danger-md": "0 0 20px rgba(var(--glow-danger-rgb), 0.4), 0 0 40px rgba(var(--glow-danger-rgb), 0.2)",
      },
    },
  },
  plugins: [],
};

export default config;
