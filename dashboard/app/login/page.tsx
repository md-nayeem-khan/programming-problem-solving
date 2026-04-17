import { Suspense } from 'react';
import LoginPageClient from './login-page-client';

export default function LoginPage() {
  return (
    <Suspense fallback={<div className="min-h-screen bg-gradient-to-br from-white via-slate-50 to-violet-50/50" />}>
      <LoginPageClient />
    </Suspense>
  );
}
