# IntiVentory Development Workflow

This document defines the end-to-end development lifecycle for **IntiVentory**. The project owner controls product decisions and version-control approvals. The AI Director coordinates repository analysis and phase briefs; Friday (or another IDE coding agent) implements only explicitly authorized assignments.

For role-specific guidelines, see [AI Director Instructions](../AI-DIRECTOR.md) and [Agent Instructions](../AGENTS.md).

---

## 1. Required End-to-End Workflow

Follow this strict sequence for every new feature, phase, or maintenance task:

```
Repository Analysis ➔ Discussion ➔ Phase Definition ➔ Owner Approval ➔ Branch Preparation ➔ Implementation ➔ Testing & Validation ➔ Owner Review ➔ Commit / PR ➔ Merge ➔ Next Phase
```

> [!IMPORTANT]  
> **Separate Authorization Gates:** Repository analysis, branch preparation, implementation execution, and version-control merge actions are distinct authorization gates. Never combine them without explicit project owner instruction.

---

## 2. Stage 1 — Repository Analysis (Read-Only)

When starting a new work session or evaluating repository status:

1. Verify default branch (`main`), current working branch, and recent commit history.
2. Inspect core documentation: [README.md](../README.md), [AGENTS.md](../AGENTS.md), [AI-DIRECTOR.md](../AI-DIRECTOR.md), [PRD.md](PRD.md), [ARCHITECTURE.md](ARCHITECTURE.md), [ACCEPTANCE-TESTS.md](ACCEPTANCE-TESTS.md), [ROADMAP.md](ROADMAP.md), [PROGRESS.md](PROGRESS.md), [BRANDING.md](BRANDING.md), and phase records.
3. Inspect application source code (`app/src/main/java/com/boyidapu/homestock/`), resources (`app/src/main/res/`), unit tests (`app/src/test/`), and Gradle scripts (`build.gradle`, `app/build.gradle`).
4. Reconcile implementation status against documentation claims.
5. Report findings to the owner without editing repository files.

> [!CAUTION]  
> An analysis assignment is strictly read-only. Do not edit source files, create branches, write application code, commit, or open pull requests.

---

## 3. Stage 2 — Discussion & Phase Definition

Discuss analysis findings and recommendations with the project owner. Resolve user-visible behavior and technical constraints before coding.

Define a single-objective phase brief with:
* Phase number and descriptive title.
* Problem statement and intended user outcome.
* Explicit in-scope and out-of-scope boundaries.
* UI and interaction expectations.
* Architecture and data integrity rules.
* Acceptance criteria and regression scenarios.
* Verification requirements (unit tests and physical-device checks).

---

## 4. Stage 3 — Owner Approval & Branch Preparation

Wait for explicit project owner approval of the phase brief before preparing version control branches.

Branch Naming Standards:
* **Product Features:** `phase/NN-short-description`
* **Maintenance & Docs:** `chore/short-description`

Before creating or reusing a branch:
1. Inspect local `git status` and currently checked-out branch.
2. Fetch `origin` and compare local history with `origin/main`.
3. Report starting commit hash and branch status to the owner.

> [!NOTE]  
> Preserve uncommitted work. If the working tree is dirty or branches are unexpectedly diverged, stop and report immediately. Never reset, stash, or force-push without explicit owner authorization.

---

## 5. Stage 4 — Implementation Execution

Begin implementation only after the owner approves the phase brief and confirms the target branch is prepared.

The coding agent must:
* Read [AGENTS.md](../AGENTS.md) and relevant technical specifications.
* Inspect existing code before writing modifications.
* Work strictly within the target branch and approved scope.
* Use built-in IDE file editing tools (`write_file`, `replace_file_content`). Shell-based file edits (`sed`, `echo`, `cat`) are strictly prohibited.
* Preserve existing architecture, offline-first stance, and data integrity constraints.
* Add or update focused unit tests.
* Deliver a detailed completion report.

---

## 6. Stage 5 — Automated Validation & Physical-Device Review

After implementation:
1. Run applicable Gradle unit tests (`gradlew.bat test` / `./gradlew test`).
2. Build debug APK (`gradlew.bat assembleDebug` / `./gradlew assembleDebug`).
3. Report exact execution commands and actual outputs.
4. Provide a focused, step-by-step physical-device testing checklist for the project owner.

### Evidence Qualification Matrix

| Evidence Type | Source | Significance |
| :--- | :--- | :--- |
| **Agent-Reported Output** | IDE Coding Agent (Friday) | Confirms local build/test execution output during coding. |
| **CI Execution Results** | GitHub Actions Pipeline | Confirms build and test pass on remote baseline. |
| **Physical-Device Review** | Project Owner (Mahesh) | Authoritative acceptance for user-visible functionality and UX. |

> [!IMPORTANT]  
> A successful Gradle build or unit test run does not replace physical-device testing. Owner device acceptance does not prove automated unit tests were executed. Maintain clear evidence distinction across all reports.

---

## 7. Stage 6 — Owner Review, Commit, Pull Request, & Merge

The project owner tests user-visible functionality on a physical device.

Only after explicit owner acceptance:
1. Stage and commit changes using Conventional Commits format (`feat(...)`, `docs(...)`, `chore(...)`).
2. Push the approved branch to `origin`.
3. Open a detailed Pull Request containing scope summary, changed files, and validation evidence.
4. Merge into `main` **only with explicit owner authorization**.

---

## 8. Stage 7 — Baseline Update & Follow-up

After merging into `main`:
1. Verify `main` contains the accepted commit.
2. Update [README.md](../README.md), [ROADMAP.md](ROADMAP.md), [PROGRESS.md](PROGRESS.md), and [phases/README.md](phases/README.md).
3. Reconcile completed GitHub issues or pull requests.
4. Begin subsequent phase planning from the updated `main` baseline.

---

## 9. Fundamental Architectural Principles

* **Product First:** Build for daily household routines across the three fixed categories (Food & Kitchen, Home Care & Cleaning, Personal Care & Cosmetics).
* **Single Source of Truth:** Room SQLite database is the authority for products and transactions. Stock ($\sum \text{Add} - \sum \text{Use}$) and spending ($\sum \text{Add}$) are dynamically derived from saved transaction entities.
* **Dimension Safety:** Mass (`kg`/`g`), Liquid (`L`/`ml`), and Units (`count`) are strictly segregated. Never convert between incompatible dimensions.
* **100% Local-First & Privacy:** Zero network permissions (`android.permission.INTERNET` omitted), zero cloud sync, zero telemetry, zero ads.
* **Transaction Safeguards:** 30-minute time-limited individual transaction deletion from immutable `createdAtEpochMs`; permanent measurement dimension lock via `hasEverHadTransactions`.
* **Deferred Functionality:** JSON backup/restore remains deferred beyond the accepted core MVP.
