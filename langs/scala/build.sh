#!/usr/bin/env bash
set -euo pipefail
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

# Function to get sbt command
get_sbt_cmd() {
  if command -v sbt >/dev/null 2>&1; then
    echo "sbt"
    return 0
  fi
  
  # Try to use sbt-extras script if available
  local sbt_script="$SCRIPT_DIR/sbt"
  if [ -f "$sbt_script" ] && [ -x "$sbt_script" ]; then
    echo "$sbt_script"
    return 0
  fi
  
  # Download sbt-extras if not present
  if [ ! -f "$sbt_script" ]; then
    echo "sbt is not installed. Downloading sbt launcher..." >&2
    if command -v curl >/dev/null 2>&1; then
      curl -s https://raw.githubusercontent.com/paulp/sbt-extras/master/sbt > "$sbt_script"
      chmod +x "$sbt_script"
      echo "$sbt_script"
      return 0
    elif command -v wget >/dev/null 2>&1; then
      wget -q -O "$sbt_script" https://raw.githubusercontent.com/paulp/sbt-extras/master/sbt
      chmod +x "$sbt_script"
      echo "$sbt_script"
      return 0
    else
      echo "Error: sbt is not installed and cannot download sbt launcher (curl or wget required)." >&2
      echo "Please install sbt: https://www.scala-sbt.org/download.html" >&2
      return 1
    fi
  fi
  
  return 1
}

SBT_CMD=$(get_sbt_cmd) || exit 1
"$SBT_CMD" assembly
