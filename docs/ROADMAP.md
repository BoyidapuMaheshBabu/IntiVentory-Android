# IntiVentory Development Roadmap

**Current Status:** Core MVP Phases 01–07 are recorded complete. On 2026-11-02, project owner (Mahesh) confirmed physical-device testing of the application's functionality and reported that all features worked as expected.  
**Accepted Baseline:** `main` branch contains the complete implementation through Phase 07.  
**Merge Baseline History:** Planning & branding merged via PR #1; Phase 01 via PR #2; Phase 02 via PR #3; Phase 03 via PR #5; Phase 04 via PR #6; Phase 05 via PR #7; Phase 06 via PR #8; AI Workflow guidance via PR #14.

---

## Planning Sign-off

- [x] Review and approve product requirements, architecture, and acceptance criteria.
- [x] Confirm target phone support (Android 8.0+ / API 26+ minimum, API 35/36 compile/target).
- [x] Agree on three fixed categories, starter product catalog, and measurement dimensions (Mass, Liquid, Units).
- [x] Confirm transaction history, 30-minute deletion cutoff, and permanent measurement dimension lock safeguards.
- [x] Merge planning baseline into `main`.

---

## Phase 01 — Android Foundation & Setup

- [x] Native Kotlin Android project, single Activity (`MainActivity`), single app module (`:app`), Groovy Gradle build scripts, and Gradle Version Catalog (`libs.versions.toml`).
- [x] Jetpack Compose + Material 3, Navigation 3, ViewModels, Kotlin Coroutines & Flow, Room SQLite DB, and Preferences DataStore.
- [x] Manual `AppContainer` dependency injection container.
- [x] Package structure organization (`app`, `core`, `data`, `domain`, `navigation`, `ui`).
- [x] Theme, local database, preferences, and routing configuration.
- [x] Launch minimal app on physical phone and verify Telugu font glyph rendering.

**Acceptance:** Accepted by Mahesh following physical-device launch and icon verification. Friday reported successful debug build and 13 passing unit tests (see [Phase 01 verification](phases/phase-01-verification.md)).

---

## Phase 02 — Welcome & Home Screen

- [x] First-run user display name input, validation (max 10 chars), and local DataStore persistence.
- [x] Time-based greeting top-left and Settings gear icon top-right.
- [x] Clickable *This month* spending preview card using calendar month boundaries.
- [x] Spending preview card opens Insights screen with *This month* selected.
- [x] Three fixed rectangular category hero cards (*Food & Kitchen*, *Home Care & Cleaning*, *Personal Care & Cosmetics*).

**Acceptance:** Accepted by Mahesh following physical-device usability review (see [Phase 02 verification](phases/phase-02-verification.md)).

---

## Phase 03 — Product Catalogue & Management

- [x] Seed 15 starter products once with zero stock and preserve deliberate user deletions.
- [x] Fixed category product lists and custom product creation (name + Mass/Liquid/Units).
- [x] Product row showing name, current derived stock, plus (`+`) action, and three-dot menu.
- [x] Measurement dimension lock: modification allowed only before first transaction; locked permanently via `hasEverHadTransactions` upon first transaction.
- [x] 40-character limit for product names; 10-character limit for display name.
- [x] Atomic permanent deletion of product and associated transaction entities.
- [x] Archive and restore mechanism; Settings > Archived Products screen.

**Acceptance:** Accepted by Mahesh on October 24, 2026, following physical-device re-testing (see [Phase 03 verification](phases/phase-03-verification.md)).

---

## Phase 04 — Add/Use Transactions & Stock Rules

- [x] Plus action opens separate Add (purchase) and Use (consumption) choices.
- [x] Add form: quantity, compatible unit, INR amount paid (`₹`), and purchase date.
- [x] Use form: current stock preview, quantity, compatible unit, and usage date.
- [x] Exact base-unit conversions (`kg ↔ g`, `L ↔ ml`, `count`) using `QuantityUtils` `BigDecimal` arithmetic.
- [x] Atomic stock validation preventing negative stock on Use actions.
- [x] Immediately update Room-derived stock, history, and spending totals.

**Acceptance:** Accepted by Mahesh on October 24, 2026; 47 unit tests reported passing (see [Phase 04 verification](phases/phase-04-verification.md)).

---

## Phase 05 — Product Transaction History & Safeguards

- [x] Tapping product row opens scrollable date-grouped transaction history log.
- [x] Add entries show quantity, unit, amount paid, and stock impact; Use entries show quantity, unit, and stock impact.
- [x] Edit any transaction (Add: quantity, unit, amount paid, date; Use: quantity, unit, date).
- [x] Strict 30-minute time-limited deletion window based on immutable `createdAtEpochMs`.
- [x] Preserve transaction ID and `createdAtEpochMs` on edits; reject corrections that would result in negative stock.
- [x] Maintain permanent measurement dimension lock even if all transaction rows are deleted.

**Acceptance:** Accepted by Mahesh and merged via [PR #7](https://github.com/BoyidapuMaheshBabu/IntiVentory-Android/pull/7); 54 unit tests reported passing (see [Phase 05 verification](phases/phase-05-verification.md)).

---

## Phase 06 — Spending Dashboard & Insights

- [x] Home *This month* card links directly to Insights screen.
- [x] Five global period filters: *Last 7 days*, *This month*, *Last 3 months*, *Custom date range*, *All time*.
- [x] Summary card displaying total expenditure (`₹`), purchase record count, and average cost per purchase.
- [x] Time-bucketed spending trend bar chart (daily, weekly, monthly).
- [x] Category share breakdown across the 3 fixed categories.
- [x] Top 5 spending products ranking.

**Acceptance:** Accepted by Mahesh and merged via [PR #8](https://github.com/BoyidapuMaheshBabu/IntiVentory-Android/pull/8) (see [Phase 06 verification](phases/phase-06-verification.md)).

---

## Phase 07 — Verification & Intended-User Review

- [x] Physical-device end-to-end testing by project owner (Mahesh) on 2026-11-02.
- [x] Confirmed functional acceptance across onboarding, product creation, Add/Use transactions, 30-minute deletion cutoff, archive/restore, and Insights analytics.

**Acceptance:** Completed and physically accepted on 2026-11-02 (see [Phase 07 verification](phases/phase-07-verification.md)).

---

## Deferred Scope — JSON Backup/Restore

JSON backup/restore remains outside the accepted core MVP. It is deferred and will require an approved phase brief before implementation.

---

## Branch & Workflow Protocol

1. Product feature branches use `phase/NN-short-description`.
2. Maintenance/doc branches use `chore/short-description`.
3. All new work branches must originate from the latest accepted `origin/main`.
4. Merges into `main` require physical-device acceptance by the project owner.
