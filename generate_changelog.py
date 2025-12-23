#!/usr/bin/env python3
"""Generate changelog entries from git commits using OpenAI."""

import os
import sys
import subprocess
import argparse
from openai import OpenAI, OpenAIError
from datetime import date

# =============================================================================
# CONFIGURATION
# =============================================================================
DEFAULT_MODEL = "gpt-5-mini"  # Change this to use a different OpenAI model
# =============================================================================


def get_git_commits():
    """Get commits since the last tag.

    Returns:
        tuple: (last_tag, list of commit messages)
    """
    # Get the last tag
    try:
        last_tag = subprocess.check_output(
            ["git", "describe", "--tags", "--abbrev=0"],
            text=True,
            stderr=subprocess.DEVNULL
        ).strip()
    except subprocess.CalledProcessError:
        print("No previous tags found. Include all commits? (y/n): ", end="")
        if input().strip().lower() != 'y':
            sys.exit(0)
        last_tag = None

    # Get commit messages since the last tag (or all commits if no tag)
    cmd = ["git", "log", "--pretty=format:%s"]
    if last_tag:
        cmd.insert(2, f"{last_tag}..HEAD")

    try:
        commits = subprocess.check_output(cmd, text=True).strip()
    except subprocess.CalledProcessError as e:
        print(f"Error getting git commits: {e}", file=sys.stderr)
        sys.exit(1)

    # Filter out empty strings
    return last_tag, [c for c in commits.split("\n") if c]

def generate_changelog(commits, model=None):
    """Generate changelog content using OpenAI.

    Args:
        commits: List of commit message strings
        model: OpenAI model to use (defaults to DEFAULT_MODEL)

    Returns:
        str: Generated changelog content
    """
    if not commits:
        return "No commits to include in the changelog."

    if model is None:
        model = DEFAULT_MODEL

    prompt = f"""
    You are a helpful assistant tasked with creating a changelog. Based on these Git commit messages, generate a clear, human-readable changelog:

    Commit messages:
    {commits}

    Format the changelog as follows:
    ### Features
    - List features here

    ### Fixes
    - List fixes here

    ### Breaking Changes
    - List breaking changes here (if any)

    Only include sections that have relevant changes. Omit empty sections.
    """

    api_key = os.environ.get("OPENAI_API_KEY")
    if not api_key:
        print("Error: OPENAI_API_KEY environment variable not set", file=sys.stderr)
        sys.exit(1)

    try:
        client = OpenAI(api_key=api_key)
        response = client.chat.completions.create(
            model=model,
            messages=[
                {"role": "system", "content": "You are a helpful assistant for software development."},
                {"role": "user", "content": prompt},
            ],
        )
        return response.choices[0].message.content.strip()
    except OpenAIError as e:
        print(f"Error calling OpenAI API: {e}", file=sys.stderr)
        sys.exit(1)

def update_changelog(version, changelog_content, dry_run=False):
    """Update CHANGELOG.md with a new version entry.

    Args:
        version: Version string (e.g., "1.0.0")
        changelog_content: Generated changelog content
        dry_run: If True, only print what would be written
    """
    changelog_file = "CHANGELOG.md"
    today = date.today().strftime("%Y-%m-%d")
    new_entry = f"## [{version}] - {today}\n{changelog_content}\n\n"

    if dry_run:
        print("\n--- DRY RUN (would prepend to CHANGELOG.md): ---")
        print(new_entry)
        return

    if os.path.exists(changelog_file):
        with open(changelog_file, "r+", encoding="utf-8") as f:
            old_content = f.read()
            f.seek(0, 0)
            f.write(new_entry + old_content)
    else:
        with open(changelog_file, "w", encoding="utf-8") as f:
            f.write(new_entry)

def main():
    """Main entry point."""
    parser = argparse.ArgumentParser(
        description="Generate changelog from git commits using OpenAI"
    )
    parser.add_argument(
        "version",
        nargs="?",
        help="Version number for the changelog entry (e.g., 1.0.0)"
    )
    parser.add_argument(
        "--dry-run",
        action="store_true",
        help="Preview the changelog without writing to file"
    )
    parser.add_argument(
        "--model",
        default=DEFAULT_MODEL,
        help=f"OpenAI model to use (default: {DEFAULT_MODEL})"
    )
    args = parser.parse_args()

    last_tag, commits = get_git_commits()
    if not commits:
        print("No new commits found.")
        sys.exit(0)

    print(f"Found {len(commits)} commits since {last_tag or 'beginning'}...")
    print(f"Using model: {args.model}")
    print("Generating changelog...")

    changelog_content = generate_changelog("\n".join(commits), model=args.model)

    print("\nGenerated Changelog:")
    print(changelog_content)

    # Get version from args or prompt
    if args.version:
        new_version = args.version
    else:
        new_version = input("\nEnter the new version (e.g., 1.0.0): ").strip()
        if not new_version:
            print("No version provided. Aborting.", file=sys.stderr)
            sys.exit(1)

    update_changelog(new_version, changelog_content, dry_run=args.dry_run)

    if args.dry_run:
        print("\nDry run complete. No changes written.")
    else:
        print(f"\nChangelog updated for version {new_version}!")


if __name__ == "__main__":
    main()
