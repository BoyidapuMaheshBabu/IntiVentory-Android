# Agent Instructions

These instructions apply to AI coding agents and automated contributors working in this repository, including Friday in Android Studio.

## Your Role

You are the **implementation agent**. The project owner and the AI Director decide what should be built and when. Your job is to inspect, implement, validate, and report only the task that has been explicitly assigned.

Read [AI-DIRECTOR.md](AI-DIRECTOR.md) and [docs/DEVELOPMENT-WORKFLOW.md](docs/DEVELOPMENT-WORKFLOW.md) for the owner's complete AI-assisted development workflow.

> [!CAUTION]  
> **An analysis request is not implementation approval. A phase recommendation, GitHub issue, roadmap entry, or previous agent assignment is not permission to start coding.**

---

## 1. Work-Stage Authorization

Respect the exact stage requested by the project owner.

### 🔍 Stage 1: Analysis-Only Assignment
When asked to analyze or investigate the repository:
- Inspect the actual repository, source code, documentation, Git status, current branch, and commit history.
- Report current state, architecture, completed and pending work, relevant risks, and a sensible next step.
- **Do NOT** edit files, modify documentation, or write application code.
- **Do NOT** create, rename, delete, reset, rebase, or move branches.
- **Do NOT** commit, push, create a pull request, merge, publish, or release.
- **Do NOT** begin implementation just because an issue or next phase exists.
- Stop after delivering the analysis report and wait for the owner's next instruction.

### 🌿 Stage 2: Branch-Preparation Assignment
Create or prepare a development branch only when the owner has approved the phase and explicitly assigned branch preparation.
- Inspect local working tree (`git status`) and currently checked-out branch first.
- Fetch `origin` and inspect the latest remote state.
- Start a new product phase from the latest accepted `origin/main`.
- Branch naming conventions: `phase/NN-short-description` for product work, `chore/short-description` for maintenance.
- If the target branch exists, inspect its commits and relationship to `origin/main` before reusing it.
- **Stop and report** if the working tree is dirty, local-only commits exist, or branches are unexpectedly diverged. Do not discard, overwrite, stash, reset, clean, or rebase work without explicit owner direction.
- **Do NOT** implement feature code as part of branch preparation.

### 🛠️ Stage 3: Implementation Assignment
Implement code **only after**:
1. The project owner has approved the phase scope;
2. The owner has confirmed the intended branch is ready; and
3. The current assignment explicitly authorizes implementation.

Work strictly within that approved scope. Do not add unrelated features, make unapproved architectural decisions, or jump to the next phase.

### 🚀 Stage 4: Commit, Pull Request, & Merge
- **Do NOT** commit, push, open a pull request, or merge automatically.
- Perform version-control actions **only** when explicitly authorized by the project owner.
- Product work must not be merged before the owner has reviewed and accepted the user-visible result on a physical device.

---

## 2. Tool Usage Rules & Safety Guardrails

> [!IMPORTANT]  
> **Strict Prohibition on Shell-Based File Edits:**  
> Never use `run_shell_command` with `sed`, `awk`, `perl`, `patch`, `echo`, `rm`, `touch`, or shell redirection (`>`, `>>`, `cat << EOF`) to create or modify files in the repository.  
> **Mandatory Tool Choice:** Always use built-in file tools: `write_file` (for full file replacement), `replace_file_content` (for a single contiguous edit), or `multi_replace_file_content` (for non-contiguous edits).

- **Read Files via IDE Tools:** Use `read_file` to inspect files so that unsaved memory buffers are preserved.
- **Search via Index Tools:** Use `find_declaration`, `find_usages`, `find_files`, and `grep` rather than running shell `find` or `grep`.
- **Git Operations via Shell:** Git status, branch creation, diff inspection, and commits may be executed via `run_shell_command` when authorized.

---

## 3. Product and Architecture Constraints

* **Native Android Stack:** Kotlin 2.1.0, Jetpack Compose Material 3, Navigation 3, single app module `:app`, Groovy `build.gradle` build scripts. Target SDK 35 (`minSdk 26`, `compileSdk 36`), Java 17.
* **100% Offline & Privacy-First:** `android.permission.INTERNET` is strictly prohibited. No cloud sync, remote databases, analytics, ads, or external APIs.
* **Single Source of Truth:** Room SQLite database is the authority for products and transactions. Stock ($\sum \text{Add} - \sum \text{Use}$) and purchase spending ($\sum \text{Add}$) are derived dynamically. Do not maintain separately editable balance totals.
* **Dimension Safety:** Mass (`kg`/`g`), Liquid (`L`/`ml`), and Units (`count`) are strictly segregated. Never convert across incompatible dimensions.
* **Transaction Safeguards:** 30-minute time-limited individual transaction deletion based on immutable `createdAtEpochMs`; permanent measurement dimension lock via `hasEverHadTransactions`.
* **Preferences DataStore:** Used exclusively for lightweight preferences (user display name, onboarding state). Never store products or transactions in DataStore.
* **Deferred Features:** JSON backup/restore is deferred beyond the accepted core MVP. Do not implement backup/restore without explicit approval.

---

## 4. Repository Hygiene & Validation

- Never commit signing keys, passwords, private data, `local.properties`, build outputs, or IDE metadata.
- Run applicable Gradle unit tests (`gradlew.bat test` / `./gradlew test`) and debug builds (`gradlew.bat assembleDebug` / `./gradlew assembleDebug`) after implementation changes.
- Distinguish between **agent-reported test output**, **CI pipeline checks**, and **owner physical-device observations**.
- Provide a clear, step-by-step physical-device testing checklist in your final report.

---

## 5. Completion Report Requirements

For an implementation assignment, deliver a completion report detailing:
1. **Approved Objective & Implemented Behavior**
2. **Changed Files & Target Branch Used**
3. **Exact Validation Commands & Output**
4. **Physical-Device Verification Checklist for Owner**
5. **Known Limitations or Deferred Items**
6. **Suggested Commit Message** (Conventional Commits format)

For an analysis or branch-preparation task, report only the requested stage findings and stop. Do not proceed to implementation without explicit authorization.
