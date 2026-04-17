'use client';

import { FormEvent, useState } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';
import { motion } from 'framer-motion';
import { ArrowRight, BarChart3, Loader2, ShieldCheck, Sparkles, TimerReset } from 'lucide-react';

export default function LoginPage() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const nextPath = searchParams.get('next') || '/dashboard';

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const onSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setError('');
    setLoading(true);

    try {
      const response = await fetch('/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password }),
      });

      const payload = await response.json();
      if (!response.ok) {
        setError(payload?.error || 'Login failed');
        return;
      }

      router.replace(nextPath);
      router.refresh();
    } catch {
      setError('Unexpected error while logging in');
    } finally {
      setLoading(false);
    }
  };

  return (
    <main className="relative min-h-screen overflow-hidden bg-gradient-to-br from-white via-slate-50 to-violet-50/50 px-4 py-10 md:px-8">
      <div className="pointer-events-none absolute inset-0">
        <div className="absolute -top-24 left-1/2 h-72 w-72 -translate-x-1/2 rounded-full bg-electric-purple/20 blur-3xl" />
        <div className="absolute right-0 top-1/3 h-72 w-72 rounded-full bg-bright-pink/20 blur-3xl" />
        <div className="absolute bottom-0 left-0 h-80 w-80 rounded-full bg-neon-blue/15 blur-3xl" />
      </div>

      <div className="relative mx-auto grid max-w-6xl items-stretch gap-6 lg:grid-cols-2">
        <motion.section
          initial={{ opacity: 0, x: -24 }}
          animate={{ opacity: 1, x: 0 }}
          transition={{ duration: 0.45 }}
          className="hidden rounded-3xl border border-white/50 bg-white/60 p-8 backdrop-blur-xl shadow-elevation-3 lg:flex lg:flex-col lg:justify-between"
        >
          <div>
            <div className="inline-flex items-center gap-2 rounded-full border border-electric-purple/30 bg-white/70 px-3 py-1 text-xs font-medium text-electric-purple">
              <Sparkles className="h-3.5 w-3.5" />
              AlgoMetrics
            </div>

            <h1 className="mt-6 text-4xl font-bold leading-tight text-slate-900">
              Keep your interview momentum
              <span className="block text-gradient-purple-pink">with focused analytics.</span>
            </h1>

            <p className="mt-4 max-w-md text-sm text-slate-600">
              Sign in to continue tracking readiness, pattern confidence, and daily consistency from your personalized dashboard.
            </p>

            <div className="mt-8 space-y-3">
              <div className="flex items-center gap-3 rounded-2xl border border-white/70 bg-white/70 px-4 py-3 shadow-elevation-1">
                <div className="rounded-xl bg-gradient-purple-pink p-2 text-white shadow-glow-purple">
                  <BarChart3 className="h-4 w-4" />
                </div>
                <p className="text-sm font-medium text-slate-700">Live readiness score and progress insights</p>
              </div>
              <div className="flex items-center gap-3 rounded-2xl border border-white/70 bg-white/70 px-4 py-3 shadow-elevation-1">
                <div className="rounded-xl bg-gradient-blue-cyan p-2 text-white shadow-glow-blue">
                  <TimerReset className="h-4 w-4" />
                </div>
                <p className="text-sm font-medium text-slate-700">Track streaks, time benchmarks, and revision cycles</p>
              </div>
              <div className="flex items-center gap-3 rounded-2xl border border-white/70 bg-white/70 px-4 py-3 shadow-elevation-1">
                <div className="rounded-xl bg-gradient-green-emerald p-2 text-white shadow-glow-green">
                  <ShieldCheck className="h-4 w-4" />
                </div>
                <p className="text-sm font-medium text-slate-700">Secure session and private analytics data</p>
              </div>
            </div>
          </div>

          <p className="mt-10 text-xs text-slate-500">Precision practice. Measurable growth.</p>
        </motion.section>

        <motion.section
          initial={{ opacity: 0, y: 24 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.45, delay: 0.05 }}
          className="rounded-3xl border border-white/60 bg-white/85 p-6 shadow-elevation-4 backdrop-blur-xl sm:p-8"
        >
          <div className="flex items-center justify-between">
            <div>
              <p className="text-xs font-semibold uppercase tracking-[0.18em] text-electric-purple">Welcome back</p>
              <h2 className="mt-2 text-3xl font-bold text-slate-900">Sign in</h2>
            </div>
            <div className="rounded-xl border border-electric-purple/20 bg-electric-purple/10 px-3 py-1 text-xs font-medium text-electric-purple">
              Secure
            </div>
          </div>

          <p className="mt-3 text-sm text-slate-600">Use your account to access the AlgoMetrics dashboard.</p>

          <form onSubmit={onSubmit} className="mt-7 space-y-5">
            <div>
              <label htmlFor="email" className="mb-2 block text-sm font-medium text-slate-700">
                Email
              </label>
              <input
                id="email"
                type="email"
                autoComplete="email"
                required
                autoFocus
                value={email}
                onChange={(event) => setEmail(event.target.value)}
                className="h-12 w-full rounded-xl border border-slate-200 bg-white/80 px-4 text-slate-900 outline-none transition-all duration-200 placeholder:text-slate-400 focus:border-electric-purple/60 focus:ring-4 focus:ring-electric-purple/15"
                placeholder="name@email.com"
              />
            </div>

            <div>
              <label htmlFor="password" className="mb-2 block text-sm font-medium text-slate-700">
                Password
              </label>
              <input
                id="password"
                type="password"
                autoComplete="current-password"
                required
                value={password}
                onChange={(event) => setPassword(event.target.value)}
                className="h-12 w-full rounded-xl border border-slate-200 bg-white/80 px-4 text-slate-900 outline-none transition-all duration-200 placeholder:text-slate-400 focus:border-electric-purple/60 focus:ring-4 focus:ring-electric-purple/15"
                placeholder="Enter your password"
              />
            </div>

            {error ? (
              <p className="rounded-xl border border-red-200 bg-red-50 px-3 py-2 text-sm text-red-600">{error}</p>
            ) : null}

            <button
              type="submit"
              disabled={loading}
              className="group flex h-12 w-full items-center justify-center gap-2 rounded-xl bg-gradient-purple-pink px-4 text-sm font-semibold text-white shadow-glow-purple transition-all duration-300 hover:scale-[1.01] hover:shadow-glow-lg disabled:cursor-not-allowed disabled:opacity-60"
            >
              {loading ? (
                <>
                  <Loader2 className="h-4 w-4 animate-spin" />
                  Signing in...
                </>
              ) : (
                <>
                  Continue to dashboard
                  <ArrowRight className="h-4 w-4 transition-transform duration-300 group-hover:translate-x-0.5" />
                </>
              )}
            </button>
          </form>

          <div className="mt-5 rounded-xl border border-slate-200/80 bg-slate-50/80 p-3 text-xs text-slate-500">
            After sign-in, you will be redirected to <span className="font-semibold text-slate-700">{nextPath}</span>.
          </div>

          <p className="mt-6 text-sm text-slate-500">Need access? Contact your admin to provision an account.</p>
        </motion.section>
      </div>
    </main>
  );
}
