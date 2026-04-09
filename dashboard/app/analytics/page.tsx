"use client";

import { useState } from "react";
import { motion } from "framer-motion";
import { Download, FileText, Sparkles } from "lucide-react";
import { toast } from "sonner";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { fadeInUp, scaleIn } from "@/lib/animations";

function getFilenameFromDisposition(contentDisposition: string | null): string | null {
  if (!contentDisposition) {
    return null;
  }

  const utf8Match = contentDisposition.match(/filename\*=UTF-8''([^;]+)/i);
  if (utf8Match?.[1]) {
    return decodeURIComponent(utf8Match[1]);
  }

  const plainMatch = contentDisposition.match(/filename="?([^";]+)"?/i);
  if (plainMatch?.[1]) {
    return plainMatch[1];
  }

  return null;
}

export default function AnalyticsPage() {
  const [isDownloading, setIsDownloading] = useState(false);

  const handleDownload = async () => {
    setIsDownloading(true);

    try {
      const response = await fetch("/api/export/txt");

      if (!response.ok) {
        throw new Error("Failed to download analytics report");
      }

      const blob = await response.blob();
      const objectUrl = URL.createObjectURL(blob);
      const filename =
        getFilenameFromDisposition(response.headers.get("content-disposition")) ||
        `analytics-report-${new Date().toISOString().slice(0, 10)}.txt`;

      const anchor = document.createElement("a");
      anchor.href = objectUrl;
      anchor.download = filename;
      document.body.appendChild(anchor);
      anchor.click();
      anchor.remove();
      URL.revokeObjectURL(objectUrl);

      toast.success("Analytics report downloaded successfully.");
    } catch (error) {
      console.error("Download failed:", error);
      toast.error("Unable to download analytics report.");
    } finally {
      setIsDownloading(false);
    }
  };

  return (
    <div className="space-y-8 max-w-7xl mx-auto">
      <motion.div
        initial={{ opacity: 0, y: -20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5 }}
      >
        <div className="relative">
          <h1 className="relative text-4xl font-bold">
            <span className="text-gradient-purple-pink">Analytics</span>
          </h1>
          <p className="text-muted-foreground mt-2 text-base">
            <Sparkles className="inline h-4 w-4 mr-2 text-electric-purple" />
            Export your complete interview-prep performance history
          </p>
        </div>
      </motion.div>

      <motion.div
        variants={fadeInUp}
        initial="hidden"
        animate="visible"
        transition={{ delay: 0.15 }}
      >
        <Card className="bg-gradient-to-br from-purple-50/50 via-white to-pink-50/50 dark:from-purple-950/20 dark:via-gray-900/50 dark:to-pink-950/20 border-2 border-purple-200/50 dark:border-purple-800/30 shadow-xl shadow-purple-500/10 overflow-hidden">
          <CardHeader className="pb-4">
            <CardTitle className="flex items-center gap-2 text-lg">
              <div className="flex h-8 w-8 items-center justify-center rounded-lg bg-gradient-to-br from-purple-500 to-pink-500 shadow-lg shadow-purple-500/30">
                <FileText className="h-4 w-4 text-white" />
              </div>
              <span className="bg-gradient-to-r from-purple-700 to-pink-600 bg-clip-text text-transparent">
                Download Analytics
              </span>
            </CardTitle>
          </CardHeader>
          <CardContent>
            <motion.div
              variants={scaleIn}
              initial="hidden"
              animate="visible"
              className="rounded-xl border border-purple-200/70 dark:border-purple-800/50 bg-white/70 dark:bg-gray-900/40 p-5"
            >
              <p className="text-sm text-muted-foreground mb-5">
                The TXT file includes all solved problem details with full submission history and complete mock interview logs.
              </p>
              <Button
                onClick={handleDownload}
                disabled={isDownloading}
                className="gap-2 bg-gradient-to-r from-purple-600 to-pink-600 text-white shadow-lg shadow-purple-500/30 hover:from-purple-700 hover:to-pink-700"
              >
                <Download className="h-4 w-4" />
                {isDownloading ? "Preparing file..." : "Download"}
              </Button>
            </motion.div>
          </CardContent>
        </Card>
      </motion.div>
    </div>
  );
}