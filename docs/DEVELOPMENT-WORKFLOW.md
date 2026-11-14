# Development Workflow

This document defines the shared development lifecycle for IntiVentory. The project owner controls decisions and approvals. The AI Director coordinates analysis and planning; Friday or another coding agent implements only an approved assignment.

For role-specific instructions, see [AI Director Instructions](../AI-DIRECTOR.md) and [Agent Instructions](../AGENTS.md).

## 1. Required end-to-end workflow

Follow this sequence for every new feature or development phase:

**Repository Analysis → Discussion → Phase Definition → Owner Approval → Branch Preparation → Implementation → Testing → Owner Review → Commit / Pull Request → Merge → Next Phase**

Do not skip stages. Analysis, branch preparation, implementation, and merge are separate authorization gates.

## 2. Stage 1 — Repository analysis (read-only)

When starting work with a new AI assistant or returning to an existing project:

1. Inspect the repository URL, default branch, and current accepted baseline.
2. Read the README, `AGENTS.md`, `AI-DIRECTOR.md`, product requirements, architecture, acceptance tests, roadmap, progress, development workflow, and relevant ADRs and phase records.
3. Inspect the repository tree and relevant source, test, resource, and configuration files.
4. Inspect the latest `main` commit, relevant branches, recent commits, open issues, and pull requests when available.
5. Compare the implementation with the documentation and identify inconsistencies, deferred work, and evidence limitations.

An analysis-only task is read-only. Do not edit files, create or move branches, write application code, commit, push, open a pull request, or merge.

The analysis report must describe the product, stack, architecture, implemented behavior, current branch/baseline, phase status, relevant issues, risks, and one recommended next step. Separate verified facts from owner-reported testing, agent-reported results, and assumptions.

A GitHub issue, roadmap item, suggested feature, or agent recommendation does not authorize implementation.

## 3. Stage 2 — Discussion and phase definition

Discuss the analysis and recommended next step with the project owner. Agree on the product behavior and important edge cases before assigning implementation.

Define one focused phase with:
- a phase number and descriptive name;
- a clear problem and intended outcome;
- explicit in-scope and out-of-scope work;
- user-visible behavior and UI expectations;
- applicable product and architecture constraints;
- acceptance criteria and regression scenarios;
- required automated tests and physical-device checks;
- documentation that must be updated.

Record approved changes in the relevant source-of-truth documents. Do not treat discussion, a suggestion, or silence as approval.

## 4. Stage 3 — Owner approval and branch preparation

Wait for the owner's explicit approval of the phase scope.

Use:
- `phase/NN-short-description` for product phases;
- `chore/short-description` for repository or documentation maintenance.

Branch preparation is a separate task from implementation.

Before creating or reusing a branch:
1. Inspect local `git status` and the current branch.
2. Fetch and inspect the latest remote state.
3. Verify the latest accepted `origin/main` commit.
4. Check whether the requested branch already exists and compare its history with `origin/main`.
5. Report the planned branch name and starting commit.

Preserve uncommitted changes and local-only commits. If the working tree is dirty, the branch has unexpected commits, or histories have diverged, stop and report. Do not reset, clean, stash, rebase, force-push, or overwrite work without explicit owner direction.

Create a new product phase branch from the latest accepted `origin/main`. Reuse an existing branch only after verifying it is safe. If the branch exists but is behind, fast-forward only when doing so is safe and there are no unique commits or local changes that could be lost.

The branch-preparation task must stop after reporting the prepared branch and its starting commit. It must not implement the feature.

## 5. Stage 4 — Implementation

Begin implementation only after the owner has approved the phase and confirmed the intended branch is ready.

The coding agent must:
- read `AGENTS.md` and the relevant product/engineering documents;
- inspect the actual implementation before editing;
- work only on the approved branch and scope;
- preserve the existing architecture, offline behavior, local data, and user-facing behavior outside the approved change;
- add or update focused tests;
- avoid unrelated refactoring, dependencies, and features;
- stop and ask the owner when a product or architectural decision is not covered by the approved phase.

Do not combine branch preparation with implementation unless the owner explicitly asks for both in one assignment.

## 6. Stage 5 — Automated validation and physical-device review

After implementation:
- run the applicable Gradle unit tests and build tasks;
- report the exact commands and actual results;
- report failures, skipped checks, and limitations honestly;
- provide a focused physical-device testing checklist;
- inspect changed files and diffs before asking for acceptance.

Keep the evidence types separate:
- coding-agent-reported results;
- actual Gradle or CI execution output;
- project-owner physical-device observations and acceptance.

A successful test or build does not replace physical-device review. Physical-device acceptance does not prove that automated tests ran. Do not mark individual acceptance-test scenarios complete without evidence for those scenarios.

## 7. Stage 6 — Owner review, commit, pull request, and merge

The project owner reviews the implementation and tests the user-visible behavior on the intended device.

Only after explicit owner acceptance:
1. Prepare or perform the commit and push steps that the owner authorizes.
2. Open a reviewable pull request with a concise objective, scope, changed files, and validation evidence.
3. Review the final diff and any outstanding checks.
4. Merge only with the owner's explicit authorization.

Do not commit, push, create a pull request, merge, publish, or release automatically based only on a coding-agent completion report. Product work must not be merged before owner acceptance.

## 8. Stage 7 — Update the project and begin the next phase

After a phase is accepted and merged:
- confirm the accepted work is present on the updated `main`;
- update README, PRD, architecture, acceptance tests, roadmap, progress, phase records, and ADRs as needed;
- reconcile completed or obsolete issues and pull requests;
- create the next phase from the updated accepted `main`;
- repeat the complete workflow from repository analysis.

Do not start a new phase from an outdated branch or assume its earlier baseline is still current.

## 9. Product and engineering guardrails

### Product first

Build for the intended household routine. Preserve the agreed product scope: three fixed categories, clear product rows, separate Add and Use actions, reliable stock calculations, purchase-spending accuracy, and understandable transaction history.

### Single source of truth

Room is the source of truth for product and transaction records. Stock and spending must be derived from saved transactions. Do not introduce a second independently editable total in UI state or preferences. Persist each user action once and observe database changes to refresh the UI.

### Data integrity before decoration

Never allow negative stock, incompatible-unit arithmetic, duplicate transaction submissions, lost purchase history, or misleading spending totals. Keep stock checks and writes atomic where required. Test domain calculations independently of the UI.

### Offline operation and privacy

The accepted core app is local-first and works without internet. Do not add accounts, cloud services, analytics, ads, remote APIs, or unnecessary permissions without an explicitly approved scope change. JSON backup/restore remains deferred and is not an implemented feature in the accepted MVP.

### AI-assisted, owner-controlled

AI tools may help research, plan, implement, and test. The project owner remains responsible for decisions and acceptance. The AI Director must not jump from analysis to implementation. Coding agents must not infer authorization from an issue, roadmap item, old prompt, or their own recommendation.

## 10. Evidence and reporting

Keep implementation-agent reports, CI results, and project-owner physical-device observations distinguishable. The acceptance-test catalogue is a requirements and traceability reference; unchecked scenarios must not be described as individually passed. Keep README, roadmap, phase records, and portfolio claims aligned with evidence recorded in the repository.

For the detailed role boundaries and prompt-stage rules, see [AI-DIRECTOR.md](../AI-DIRECTOR.md) and [AGENTS.md](../AGENTS.md).
