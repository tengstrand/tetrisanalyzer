#!/usr/bin/env bash
set -euo pipefail
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"
if ! command -v sbt >/dev/null 2>&1; then
  echo "Error: sbt is not installed or not on PATH." >&2
  exit 1
fi
sbt assembly
