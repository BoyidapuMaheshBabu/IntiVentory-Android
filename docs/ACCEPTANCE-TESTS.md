# IntiVentory Acceptance Test Catalogue

**Document Status:** Reference acceptance test catalogue for the accepted Phase 01–07 MVP.

> [!NOTE]  
> **Traceability Catalogue Note:** This catalogue defines expected application outcomes and regression scenarios. Checkboxes serve as a requirement traceability matrix across phases. Physical-device functional acceptance for Phases 01–07 was confirmed by the project owner (Mahesh) on 2026-11-02 (see [Phase 07 verification](phases/phase-07-verification.md)). Individual checklist items remain a specification reference rather than a line-by-line automated execution log.

---

## A. Onboarding & Home Screen

- [ ] Fresh install without a saved display name opens Name Setup screen.
- [ ] Blank or whitespace-only names are rejected and cannot be saved.
- [ ] User display name accepts at most **10 characters** in Welcome, Home, and Settings.
- [ ] Saving a valid name persists locally in DataStore; subsequent app launches skip onboarding.
- [ ] Greeting uses device-local calendar time (*Good morning*, *Good afternoon*, *Good evening*) and saved display name.
- [ ] Home screen layout places greeting top-left and Settings gear icon top-right.
- [ ] Home screen displays a clickable *This month* spending preview card below the top row.
- [ ] Tapping the spending preview card opens Insights with *This month* selected.
- [ ] No current-month purchases displays `₹0` and a clear no-purchases message.
- [ ] Home screen displays exactly three large rectangular cards: *Food & Kitchen*, *Home Care & Cleaning*, *Personal Care & Cosmetics*.
- [ ] Top-level category creation is disabled; no *Snacks* or *Drinks* categories exist.
- [ ] All core screens function fully offline (airplane mode).

---

## B. Product Catalogue & Management

- [ ] Starter products appear within their assigned fixed category.
- [ ] Starter product seed runs once; a deleted preset item is not recreated on relaunch.
- [ ] Creating a product asks for name (max 40 chars) and measurement dimension only (Mass, Liquid, Units).
- [ ] Product creation does not ask for a preferred unit, starting quantity, or price.
- [ ] Blank or whitespace-only product names are rejected.
- [ ] New products start at zero recorded stock.
- [ ] Mass products accept `kg`/`g`; Liquid products accept `L`/`ml`; Units products accept whole counts.
- [ ] A product with zero transaction history allows changing measurement dimension freely.
- [ ] Once any transaction exists, `hasEverHadTransactions` locks the measurement dimension permanently.
- [ ] Product row displays name, current derived stock, plus (`+`) action, and three-dot menu.
- [ ] Tapping the main row opens Product Transaction History; tapping plus opens Add/Use chooser.
- [ ] Three-dot menu offers *Edit*, *Archive*, and *Delete permanently* actions.
- [ ] Editing product name or permitted dimension preserves product identity and transaction history.
- [ ] Save Changes is disabled when form values are unchanged or invalid.

---

## C. Add (Purchase) Transactions

- [ ] Add form offers quantity-entry units compatible with the product's dimension.
- [ ] Add requires positive quantity, compatible unit, INR amount paid (`₹`), and purchase date.
- [ ] Money input accepts up to two decimal places and is parsed/stored as integer paise (`Long`).
- [ ] `₹0.00` is valid for free purchases; it adds stock while contributing `₹0` to spending.
- [ ] Purchase date defaults to today's local date; past dates permitted; future dates rejected.
- [ ] Saving creates one `ADD` transaction entity and increases stock immediately.
- [ ] Product history, stock balance, *This month* card, and Insights totals update without manual refresh.
- [ ] Rapid double-tap on Save is debounced and cannot create duplicate entries.

---

## D. Use (Consumption) Transactions

- [ ] Use form displays current derived stock preview before quantity input.
- [ ] Use requires positive quantity, compatible unit, and usage date; no money field is shown.
- [ ] Usage date defaults to today's local date; past dates permitted; future dates rejected.
- [ ] Zero stock blocks Use action with a clear error message.
- [ ] Requesting a usage quantity exceeding current stock is rejected atomically without creating a transaction.
- [ ] Successfully recording Use reduces calculated stock immediately.
- [ ] Use transactions never alter historical purchase spending totals.

---

## E. Unit Conversion & Dimension Safety

- [ ] `10 kg` rice purchased; `1 kg` used; `9 kg` remains.
- [ ] `2 kg` rice purchased; `500 g` used; `1.5 kg` remains.
- [ ] `1 L` cleaner purchased; `200 ml` used; `800 ml` remains.
- [ ] `5` soap bars purchased; `1` used; `4` remain.
- [ ] Mass (`kg`/`g`), Liquid (`L`/`ml`), and Units (`count`) cannot be mixed or converted cross-dimension.
- [ ] Decimal input up to 3 decimal places allowed for `kg`/`L`; `g`/`ml` and `Units` accept whole integers.

---

## F. Spending Accounting & Math

- [ ] Existing spending `₹5,000` plus new `₹5` purchase displays `₹5,005` immediately.
- [ ] Total represents actual purchase expenditure, not estimated inventory market value.
- [ ] Creating or editing a product alone does not alter spending.
- [ ] Recording Use changes stock balance only; past spending remains unchanged.
- [ ] Purchases for archived products remain included in spending totals and analytics.
- [ ] Currency calculations use integer paise to avoid binary floating-point drift.

---

## G. Spending Period Boundaries & Insights

- [ ] **Last 7 days:** Today minus 6 calendar days through Today.
- [ ] **This month:** First calendar day of current month through Today.
- [ ] **Last 3 months:** First calendar day of month two months prior through Today.
- [ ] **Custom date range:** Includes both start and end dates; start $\le$ end; future dates rejected.
- [ ] **All time:** Includes all saved `ADD` purchase records across app history.
- [ ] Summary card shows total spending (`₹`), purchase record count, and average cost per purchase.
- [ ] Spending trend bar chart buckets data by day, week, or month based on selected period.
- [ ] Category breakdown displays expenditure, percent share, and proportional bar for the 3 categories.
- [ ] Top spending products ranks up to 5 items descending by spending, marking archived items.
- [ ] Home's *This month* card matches Insights' *This month* total expenditure.

---

## H. Product History & Correction Safeguards

- [ ] Product row tap opens scrollable date-grouped transaction history.
- [ ] Entries are grouped by local calendar date (`Today`, `Yesterday`, or formatted date).
- [ ] Same-date entries are ordered newest recorded first.
- [ ] `ADD` entries show quantity, unit, amount paid, and stock impact.
- [ ] `USE` entries show quantity, unit, and stock impact (no money field).
- [ ] Any historical transaction can be edited regardless of age.
- [ ] Editing an entry updates existing record; `transactionId` and `createdAtEpochMs` are preserved.
- [ ] Edits resulting in negative derived stock are rejected atomically.

---

## I. Time-Limited Deletion Safeguards

- [ ] Delete action is available **only within 30 minutes** of original creation (`createdAtEpochMs`).
- [ ] At $\ge 30$ minutes, Delete is hidden/disabled in UI and rejected by repository/DAO logic.
- [ ] Deleting a transaction within the eligible window is atomic and updates stock and spending immediately.
- [ ] Editing a transaction does **not** reset or extend the 30-minute deletion window.
- [ ] After all transactions for a product are deleted, `hasEverHadTransactions` remains `true` and measurement dimension stays locked.

---

## J. Product Archive, Restore, & Permanent Delete

- [ ] Permanent delete prompts for explicit destructive confirmation before erasing product and all history.
- [ ] Permanent delete removes product and transaction entities atomically; spending totals recalculate.
- [ ] Deleting a starter preset product does not cause it to be recreated on subsequent launches.
- [ ] Archive hides product from active category lists while fully preserving stock, history, and spending.
- [ ] Settings > Archived Products displays archived items and provides one-tap Restore.
- [ ] Restoring an archived product returns it to its active category with history unchanged.

---

## K. Settings & About Screen

- [ ] Settings screen permits updating user display name (max 10 characters).
- [ ] Settings screen provides access to Archived Products and About screen.
- [ ] About screen (`ui/about`) displays app branding, Telugu tagline, version details, and developer credits.

---

## L. End-to-End Validation Scenario

1. Fresh launch: Enter display name "Priya".
2. Home: Verify time-based greeting, Settings gear, *This month* card (`₹0`), and 3 category cards.
3. Open *Food & Kitchen*, select *Rice*.
4. Add `10 kg` rice for `₹500` with today's date. Verify stock = `10 kg`, spending = `₹500`.
5. Use `1 kg` rice. Verify stock = `9 kg`, spending stays `₹500`.
6. Attempt to Use `10 kg` when `9 kg` remains; verify save is rejected with error.
7. Open *Rice* history; verify dated `ADD` and `USE` entries.
8. Record shampoo purchase for `₹5`. Verify total spending = `₹505`.
9. Test *Last 7 days*, *This month*, *Last 3 months*, *Custom*, and *All time* filters in Insights.
10. Archive a product with history; verify it disappears from category view but restores from Settings cleanly.
11. Permanently delete a separate product; verify associated history is erased and spending updates.
12. Restart app in airplane mode; verify zero data loss and offline functionality.
