"use client";

import { motion, AnimatePresence } from "framer-motion";
import { User, Settings, LogOut, Loader2 } from "lucide-react";
import { Button } from "@/components/ui/button";
import { SearchInput } from "@/components/ui/enhanced-input";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { EnhancedTooltip } from "@/components/ui/tooltip";
import { useState, useEffect } from "react";
import { fadeInUp, buttonRipple } from "@/lib/animations";
import { usePathname, useRouter } from "next/navigation";

interface QuickSearchResult {
  id: number;
  title: string;
  problemId: string;
  platform: string;
  company?: string;
  difficulty: string;
  patterns: { id: number; name: string; category: string }[];
}

interface AuthUserProfile {
  id: string;
  email: string;
}

export function Navbar() {
  const router = useRouter();
  const pathname = usePathname();

  const [searchQuery, setSearchQuery] = useState("");
  const [debouncedSearch, setDebouncedSearch] = useState("");
  const [isScrolled, setIsScrolled] = useState(false);
  const [searchFocused, setSearchFocused] = useState(false);
  const [quickResults, setQuickResults] = useState<QuickSearchResult[]>([]);
  const [isSearching, setIsSearching] = useState(false);
  const [isLoggingOut, setIsLoggingOut] = useState(false);
  const [authUser, setAuthUser] = useState<AuthUserProfile | null>(null);

  useEffect(() => {
    const handleScroll = () => {
      setIsScrolled(window.scrollY > 20);
    };
    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, []);

  useEffect(() => {
    let isMounted = true;

    async function fetchAuthUser() {
      try {
        const response = await fetch("/api/auth/me", {
          method: "GET",
          credentials: "same-origin",
          cache: "no-store",
        });

        if (!response.ok) {
          if (isMounted) {
            setAuthUser(null);
          }
          return;
        }

        const payload = await response.json();
        const data = payload?.data;

        if (
          isMounted &&
          data &&
          typeof data.id === "string" &&
          typeof data.email === "string"
        ) {
          setAuthUser({ id: data.id, email: data.email });
        }
      } catch {
        if (isMounted) {
          setAuthUser(null);
        }
      }
    }

    fetchAuthUser();

    return () => {
      isMounted = false;
    };
  }, []);

  useEffect(() => {
    if (pathname !== "/problems") {
      return;
    }

    const params = new URLSearchParams(window.location.search);
    const queryFromUrl = (params.get("search") || "").trim();
    setSearchQuery(queryFromUrl);
    setDebouncedSearch(queryFromUrl);
  }, [pathname]);

  useEffect(() => {
    const timer = window.setTimeout(() => {
      setDebouncedSearch(searchQuery.trim());
    }, 250);

    return () => window.clearTimeout(timer);
  }, [searchQuery]);

  useEffect(() => {
    const shouldSearch = debouncedSearch.length >= 2;
    if (!shouldSearch) {
      setQuickResults([]);
      setIsSearching(false);
      return;
    }

    let isActive = true;

    async function fetchQuickResults() {
      try {
        setIsSearching(true);
        const response = await fetch(
          `/api/problems?search=${encodeURIComponent(debouncedSearch)}&limit=6`
        );

        if (!response.ok) {
          throw new Error("Failed to search problems");
        }

        const data = await response.json();
        if (isActive) {
          setQuickResults(Array.isArray(data.problems) ? data.problems : []);
        }
      } catch (error) {
        console.error("Navbar quick search failed:", error);
        if (isActive) {
          setQuickResults([]);
        }
      } finally {
        if (isActive) {
          setIsSearching(false);
        }
      }
    }

    fetchQuickResults();

    return () => {
      isActive = false;
    };
  }, [debouncedSearch]);

  const showQuickResults = searchFocused && searchQuery.trim().length > 0;

  const executeSearch = (query: string) => {
    const trimmed = query.trim();
    window.dispatchEvent(new CustomEvent("headerSearch", { detail: trimmed }));

    if (!trimmed) {
      router.push("/problems");
      return;
    }
    router.push(`/problems?search=${encodeURIComponent(trimmed)}`);
  };

  const handleSearchKeyDown = (event: React.KeyboardEvent<HTMLInputElement>) => {
    if (event.key === "Enter") {
      event.preventDefault();
      executeSearch(searchQuery);
      setSearchFocused(false);
    }

    if (event.key === "Escape") {
      setSearchFocused(false);
    }
  };

  const handleResultClick = (problemId: number) => {
    setSearchFocused(false);
    router.push(`/problems/${problemId}`);
  };

  const handleLogout = async () => {
    if (isLoggingOut) {
      return;
    }

    try {
      setIsLoggingOut(true);
      await fetch("/api/auth/logout", {
        method: "POST",
        credentials: "same-origin",
      });
    } catch (error) {
      console.error("Logout failed:", error);
    } finally {
      router.replace("/login");
      router.refresh();
      setIsLoggingOut(false);
    }
  };

  return (
    <motion.header
      className="sticky top-0 z-30 flex h-16 items-center justify-between px-6 transition-all duration-300 border-b border-white/10 shadow-md"
      style={{
        background: isScrolled 
          ? "rgba(255, 255, 255, 0.9)" 
          : "rgba(255, 255, 255, 0.8)",
        backdropFilter: "blur(16px)",
        boxShadow: isScrolled
          ? "0 4px 20px rgba(0, 0, 0, 0.1), 0 1px 3px rgba(0, 0, 0, 0.05)"
          : "0 2px 10px rgba(0, 0, 0, 0.06)",
      }}
      variants={fadeInUp}
      initial="initial"
      animate="animate"
    >
      {/* Enhanced Background Gradient */}
      <motion.div 
        className="absolute inset-0 bg-gradient-to-r from-electric-purple/5 via-transparent to-bright-pink/5 opacity-0"
        animate={{ opacity: isScrolled ? 1 : 0 }}
        transition={{ duration: 0.3 }}
      />

      {/* Left Spacer - matches width of right section for perfect centering */}
      <div className="flex-1" />

      {/* Centered Search Bar */}
      <div className="flex items-center justify-center flex-1 max-w-xl relative z-10">
        <motion.div 
          className="relative w-full max-w-md"
          animate={{ 
            scale: searchFocused ? 1.01 : 1,
          }}
          transition={{ duration: 0.2 }}
        >
          <div 
            className={`relative rounded-full transition-all duration-300 ${
              searchFocused 
                ? "shadow-lg shadow-electric-purple/20 ring-2 ring-electric-purple/30" 
                : "shadow-sm hover:shadow-md"
            }`}
          >
            <SearchInput
              placeholder="Search problems, patterns, companies..."
              variant="glass"
              size="default"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              onSearchClear={() => {
                setSearchQuery("");
                setQuickResults([]);
              }}
              onKeyDown={handleSearchKeyDown}
              onFocus={() => setSearchFocused(true)}
              onBlur={() => setSearchFocused(false)}
              className={`rounded-full border-gray-200/60 bg-gray-50/80 hover:bg-white/90 transition-all duration-300 ${
                searchFocused ? "bg-white border-electric-purple/40" : ""
              }`}
            />
          </div>
          
          {/* Search suggestions overlay */}
          <AnimatePresence>
            {showQuickResults && (
              <motion.div
                initial={{ opacity: 0, y: 10, scale: 0.95 }}
                animate={{ opacity: 1, y: 0, scale: 1 }}
                exit={{ opacity: 0, y: 10, scale: 0.95 }}
                transition={{ duration: 0.2, ease: "easeOut" }}
                className="absolute top-full left-0 right-0 mt-3 p-4 bg-white/95 backdrop-blur-xl border border-gray-200/50 rounded-2xl shadow-xl shadow-gray-200/50 z-50"
              >
                <div className="text-xs text-muted-foreground mb-3 font-medium">
                  Quick results for "{searchQuery.trim()}"
                </div>

                {isSearching ? (
                  <div className="flex items-center gap-2 px-3 py-2 text-sm text-muted-foreground">
                    <Loader2 className="h-4 w-4 animate-spin" />
                    Searching...
                  </div>
                ) : quickResults.length === 0 ? (
                  <div className="px-3 py-2 text-sm text-muted-foreground">
                    No matches found. Press Enter to search all problems.
                  </div>
                ) : (
                  <div className="space-y-1">
                    {quickResults.map((problem) => {
                      const primaryPattern = problem.patterns?.[0]?.name;
                      return (
                        <button
                          key={problem.id}
                          type="button"
                          onMouseDown={(event) => {
                            event.preventDefault();
                            handleResultClick(problem.id);
                          }}
                          className="w-full text-left px-3 py-2 hover:bg-gradient-to-r hover:from-electric-purple/10 hover:to-bright-pink/5 rounded-lg transition-all duration-200"
                        >
                          <div className="text-sm font-medium text-foreground truncate">
                            {problem.title}
                          </div>
                          <div className="text-xs text-muted-foreground mt-0.5">
                            {problem.platform} {problem.problemId}
                            {problem.company ? ` • ${problem.company}` : ""}
                            {primaryPattern ? ` • ${primaryPattern}` : ""}
                          </div>
                        </button>
                      );
                    })}
                  </div>
                )}
              </motion.div>
            )}
          </AnimatePresence>
        </motion.div>
      </div>

      {/* Right Section */}
      <div className="flex items-center justify-end gap-3 flex-1 relative z-10">
        {/* Profile Menu */}
        <EnhancedTooltip content="Account settings" variant="glass">
          <DropdownMenu>
            <DropdownMenuTrigger asChild>
              <motion.div variants={buttonRipple} whileHover="hover" whileTap="tap">
                <Button variant="ghost" size="icon" className="rounded-full overflow-hidden">
                  <motion.div 
                    className="flex h-8 w-8 items-center justify-center rounded-full bg-gradient-to-br from-electric-purple to-bright-pink shadow-glow-purple"
                    whileHover={{ 
                      background: "linear-gradient(135deg, #7C3AED 0%, #EC4899 100%)",
                      scale: 1.05,
                    }}
                  >
                    <User className="h-4 w-4 text-white" />
                  </motion.div>
                </Button>
              </motion.div>
            </DropdownMenuTrigger>
            <DropdownMenuContent align="end" className="w-56 bg-white/90 backdrop-blur-md border-white/20">
              <DropdownMenuLabel>
                <div className="flex flex-col space-y-1">
                  <p className="text-sm font-medium">{authUser ? `ID: ${authUser.id}` : "ID: -"}</p>
                  <p className="text-xs text-muted-foreground">{authUser?.email ?? "-"}</p>
                </div>
              </DropdownMenuLabel>
              <DropdownMenuSeparator />
              
              <motion.div whileHover={{ x: 4 }} transition={{ duration: 0.2 }}>
                <DropdownMenuItem className="cursor-pointer">
                  <User className="mr-2 h-4 w-4" />
                  Profile
                </DropdownMenuItem>
              </motion.div>
              
              <motion.div whileHover={{ x: 4 }} transition={{ duration: 0.2 }}>
                <DropdownMenuItem className="cursor-pointer">
                  <Settings className="mr-2 h-4 w-4" />
                  Settings
                </DropdownMenuItem>
              </motion.div>
              
              <DropdownMenuSeparator />
              
              <motion.div whileHover={{ x: 4 }} transition={{ duration: 0.2 }}>
                <DropdownMenuItem
                  className="text-red-600 cursor-pointer hover:bg-red-50"
                  onSelect={(event) => {
                    event.preventDefault();
                    void handleLogout();
                  }}
                  disabled={isLoggingOut}
                >
                  {isLoggingOut ? (
                    <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                  ) : (
                    <LogOut className="mr-2 h-4 w-4" />
                  )}
                  {isLoggingOut ? "Logging out..." : "Log out"}
                </DropdownMenuItem>
              </motion.div>
            </DropdownMenuContent>
          </DropdownMenu>
        </EnhancedTooltip>
      </div>
      
      {/* Floating particles effect */}
      <div className="absolute top-0 left-1/4 w-1 h-1 bg-electric-purple/20 rounded-full animate-float" />
      <div className="absolute bottom-0 right-1/3 w-1 h-1 bg-bright-pink/20 rounded-full animate-float" style={{ animationDelay: "1s" }} />
    </motion.header>
  );
}
