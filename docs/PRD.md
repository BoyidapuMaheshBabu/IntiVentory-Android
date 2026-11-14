# IntiVentory Product Requirements Document (PRD)

**Document Status:** Baseline PRD for the accepted Phase 01–07 MVP.  
**Product Identity:** IntiVentory (Telugu: *ఇంటి వెంటరీ*)  
**Tagline:** *మన ఇంటి సరుకులు, మన ఇంటి లెక్కలు.* (Our household supplies, our household accounts.)  
**Technical Identifiers:** Application ID: `com.boyidapu.homestock` | Repository: `IntiVentory-Android`

---

## 1. Purpose & User Persona

**IntiVentory** is a local-first native Android app designed for tracking household products, purchases, consumption, remaining quantities, and purchase expenditure. Built specifically for offline household use, it replaces physical registers or complex spreadsheets with a simple, reliable, and privacy-preserving tool.

---

## 2. Onboarding & First Launch

* **Visual Identity:** Display approved app name **IntiVentory**, logo artwork ([BRANDING.md](BRANDING.md)), and Telugu tagline (*మన ఇంటి సరుకులు, మన ఇంటి లెక్కలు.*).
* **Display Name Input:** Prompt for user name on first launch.
  * Trim leading/trailing whitespace.
  * Reject blank or whitespace-only inputs.
  * Limit display name to a maximum of **10 characters** in Welcome, Home, and Settings.
  * Persist locally in Preferences DataStore; skip onboarding on subsequent launches.
* **Greeting:** Show a friendly time-based greeting (*Good morning*, *Good afternoon*, *Good evening*) using device-local time.
* **Privacy & Access:** No user accounts, remote sign-ins, or network connections are required.

---

## 3. Home Screen

Fixed screen layout order:
1. **Top Row:** Time-based greeting on top-left; Settings gear icon on top-right.
2. **This Month Spending Card:** Clickable card displaying total purchase spending (`₹`) recorded from the first calendar day of the current month through today, including the date range. Tapping opens Spending Insights with *This month* selected. If no purchases exist, show `₹0` and a clear no-purchases message.
3. **Category Hero Cards:** Exactly three large rectangular clickable cards:
   * **Food & Kitchen**
   * **Home Care & Cleaning**
   * **Personal Care & Cosmetics**

> [!NOTE]  
> No top-level category creation and no separate *Snacks* or *Drinks* categories are allowed. Detailed date filters remain exclusive to the Insights screen.

---

## 4. Fixed Categories & Preset Catalog

Preset starter products seeded once upon initial database creation:

* 🌾 **Food & Kitchen:** Rice, Dal, Flour, Oil, Spices.
* 🧹 **Home Care & Cleaning:** Toilet Cleaner, Floor Cleaner, Dishwashing Liquid, Detergent, Disinfectant.
* 🧴 **Personal Care & Cosmetics:** Soap, Shampoo, Toothpaste, Hair Oil, Cream.

Users may add custom products within any of the three fixed categories. Starter products begin at zero stock. Deliberately deleted preset products must not be recreated on subsequent app launches.

---

## 5. Product Catalog & Creation Rules

* **Product Row Display:** Shows name and current calculated stock on the left, plus (`+`) action button and three-dot menu on the right. Tapping the main row opens Product Transaction History.
* **Product Creation Form:** Asks for product name (maximum **40 characters**) and measurement dimension only.
  * Fixed category is pre-selected by current screen context.
  * Does not ask for starting quantity, unit price, or preferred unit.
  * New products begin with zero recorded stock.
* **Measurement Dimensions:**
  * **Mass:** Compatible entry units are `kg` and `g` (stored in integer grams).
  * **Liquid:** Compatible entry units are `L` and `ml` (stored in integer millilitres).
  * **Units:** Compatible entry units are whole pieces/items (stored in integer counts).
* **Permanent Measurement Lock:**
  * Measurement dimension can be modified freely while a product has **zero** transaction history.
  * Once any transaction is recorded, `hasEverHadTransactions` locks the measurement dimension permanently, even if current stock later returns to zero.

---

## 6. Product Actions & Safety Safeguards

### Plus (`+`) Action
Opens a choice between **Add** and **Use**:
* **Add:** Purchase / Stock-In transaction.
* **Use:** Consumption / Stock-Out transaction.

### Three-Dot Menu
* **Edit:** Product name (max 40 characters) and measurement dimension (subject to the `hasEverHadTransactions` lock rule).
* **Delete Permanently:** Prompts for explicit destructive confirmation warning that all associated history will be permanently erased. Executes an atomic Room transaction deleting the product and all transaction entities. Recalculates all spending totals.
* **Archive / Hide:** Hides the product from active category lists while fully preserving current derived stock, all transaction records, and historical spending totals.
* **Restore:** Accessible via **Settings > Archived Products**. Restores the product to its active category with stock and history intact.

---

## 7. Add (Purchase) Transactions

* **Form Fields:** Product name, positive quantity, compatible unit (`kg`/`g`, `L`/`ml`, or count), total amount paid in INR (`₹`), and purchase date.
* **Quantity Rules:** Positive numbers only. Decimal input up to 3 decimal places for `kg`/`L`; whole integers for `g`/`ml` and `Units`.
* **Amount Paid:** Required, non-negative, up to 2 decimal places. `₹0.00` is permitted for free purchases.
* **Date Rules:** Defaults to today's local date. Past dates permitted; future dates rejected.
* **Effect:** Saves one `ADD` transaction entity, increases calculated stock, and updates all affected spending totals immediately.

---

## 8. Use (Consumption) Transactions

* **Form Fields:** Current stock preview, positive quantity to consume, compatible unit, and usage date.
* **Date Rules:** Defaults to today's local date. Past dates permitted; future dates rejected.
* **Atomic Stock Validation:**
  * If stock is zero, block Use with an explanatory error.
  * If requested quantity exceeds current stock, reject save, display available stock, and record no transaction.
* **Effect:** Saves one `USE` transaction entity and reduces stock immediately. *Use transactions never affect spending totals.*

---

## 9. Derived Stock & Money Math

Stored base units: grams (`g`) for Mass, millilitres (`ml`) for Liquid, and pieces (`count`) for Units.

$$\text{Current Stock} = \sum \text{Add Quantities (in base units)} - \sum \text{Use Quantities (in base units)}$$

* **Currency Storage:** Total paid amounts are parsed to integer paise (`Long`) to eliminate floating-point drift.
* **Dimension Safety:** Mass, Liquid, and Units must never be mixed.

---

## 10. Product History & Correction Safeguards

### Auditable History List
* Vertically scrollable, date-grouped (`Today`, `Yesterday`, or formatted date).
* Ordered by transaction date descending; same-date entries ordered newest recorded first.
* `ADD` entries show quantity, unit, amount paid (`₹`), and stock impact.
* `USE` entries show quantity, unit, and stock impact (no money field).
* Shows product-specific total purchase spending.

### Editing Transactions
* Any saved transaction can be edited regardless of age.
* `ADD` transactions allow editing quantity, unit, amount paid, and transaction date.
* `USE` transactions allow editing quantity, unit, and transaction date.
* Edits preserve original `transactionId` and immutable `createdAtEpochMs`.
* Edits that would cause negative derived stock are rejected atomically.

### 30-Minute Time-Limited Deletion
* Individual transaction deletion is available **only within 30 minutes** of original creation (`createdAtEpochMs`).
* At $\ge 30$ minutes, deletion is blocked in both UI and repository/DAO layers.
* Editing a transaction does not reset or extend the 30-minute countdown.

---

## 11. Spending Dashboard & Insights

* **Five Period Filters:** *Last 7 days*, *This month*, *Last 3 months*, *Custom date range*, and *All time*.
* **Summary Card:** Total purchase expenditure (`₹`), total purchase record count, and average cost per purchase.
* **Spending Trend Chart:** Time-bucketed bar chart (daily, weekly, or monthly depending on filter).
* **Category Share:** Proportional spending breakdown across the three fixed categories.
* **Top Spending Products:** Ranks top 5 spending items in the selected timeframe.

---

## 12. Settings & About Screen

* **Settings Screen:** Update user display name, manage archived products, and access the About screen.
* **Archived Products Screen:** List all archived products and provide a one-tap **Restore** action.
* **About Screen (`ui/about`):** Displays application branding, Telugu tagline, version details, and open-source project info.

---

## 13. Privacy & Deferred Features

* **100% Local Storage:** Room SQLite database + Preferences DataStore.
* **Zero Network Code:** `android.permission.INTERNET` omitted. No cloud sync, ads, or analytics SDKs.
* **Deferred Features:** JSON backup/restore is explicitly deferred beyond the core MVP.
