# API Use Cases

### ✅ Create New Gift Card

- Issue new card with zero balance allowed.
- Generate secure card code using **Apache Commons Validator** (Luhn method).

### ✅ Update Gift Card Balance

- Card number must be valid.
- Card status must allow the update.
- Credit and Debit operations.

### ✅ Update Gift Card Expire Date

- Card number must be valid.
- Card status must allow the update.

### ✅ Cancel Gift Card

- Card number must be valid.
- Card status must allow the update.

### ✅ Exchange Balance Between Cards

- Both card numbers must be valid.
- Source card must have sufficient balance.
- Status of both cards must be ACTIVE.

### ✅ PCI DSS

- PCI DSS is an industry-standard to ensure security in card-based applications.

### ✅ Publish important events

- On credit and debit operations the app publish a topic to a message broker.

### 🔁 Bulk Operations

- Each card must have a valid code, balance, and status.