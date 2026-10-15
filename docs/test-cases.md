# Test Cases – DemoBlaze E2E Automation

## Home Page / Navigation

| ID | Feature | Preconditions | Test Data | Steps | Expected Result | Priority | Type | Status |
|----|---------|--------------|-----------|-------|-----------------|----------|------|--------|
| DB-SMOKE-001 | Home Page | App reachable | None | Open https://www.demoblaze.com/ | Page loads with non-blank title | Blocker | Smoke | Automated |
| DB-SMOKE-002 | Navigation | Page loaded | None | Check Login and Cart links in nav | Both links visible | Critical | Smoke | Automated |
| DB-SMOKE-003 | Products | Page loaded | None | Check product card count | Count > 0 | Critical | Smoke | Automated |
| DB-SMOKE-004 | Categories | Page loaded | None | Check sidebar category links | Links present | Normal | Smoke | Automated |
| DB-SMOKE-005 | Phone Cat. | Page loaded | None | Click Phones category | Products refresh, count > 0 | Normal | Smoke | Automated |
| DB-SMOKE-006 | Laptop Cat. | Page loaded | None | Click Laptops category | Products refresh, count > 0 | Normal | Smoke | Automated |

## Login

| ID | Feature | Preconditions | Test Data | Steps | Expected Result | Priority | Type | Status |
|----|---------|--------------|-----------|-------|-----------------|----------|------|--------|
| DB-LOGIN-001 | Login | QA account exists | existingUser() | Open login modal, enter valid creds, click Log in | Nav shows username, Logout visible | Blocker | Regression | Automated |
| DB-LOGIN-002 | Login | Page loaded | Wrong password | Open login modal, enter invalid creds | Alert appears, Logout not visible | Critical | Regression | Automated |
| DB-LOGIN-003 | Login | Page loaded | Blank username | Open login modal, leave username blank | Login fails; Logout not visible | Normal | Regression | Automated |
| DB-LOGIN-004 | Logout | Logged in | existingUser() | Click Logout | Login link reappears, greeting gone | Critical | Regression | Automated |
| DB-LOGIN-005 | Login | Page loaded | Bad credentials | Multiple invalid combinations | All rejected | Normal | Regression | Automated |

## Registration

| ID | Feature | Preconditions | Test Data | Steps | Expected Result | Priority | Type | Status |
|----|---------|--------------|-----------|-------|-----------------|----------|------|--------|
| DB-SIGNUP-001 | Signup | Page loaded | uniqueUser() | Open signup, fill unique user, click Sign up | Alert: "Sign up successful." | Critical | Regression | Automated |
| DB-SIGNUP-002 | Signup | QA account exists | EXISTING_USERNAME | Open signup, enter duplicate user | Alert: "This user already exist." | Normal | Regression | Automated |
| DB-SIGNUP-003 | Signup | Page loaded | Blank fields | Open signup, submit blank form | Alert appears | Minor | Regression | Automated |
| DB-SIGNUP-004 | Signup | Page loaded | uniqueUser() | Register new user, then log in | Login succeeds after registration | Critical | Regression | Automated |

## Product Catalogue

| ID | Feature | Preconditions | Test Data | Steps | Expected Result | Priority | Type | Status |
|----|---------|--------------|-----------|-------|-----------------|----------|------|--------|
| DB-PROD-001 | Products | Page loaded | None | Check home page product count | Count > 0 | Critical | Regression | Automated |
| DB-PROD-002 | Product Detail | Home page loaded | None | Click first product | Name, price, description all non-blank | Critical | Regression | Automated |
| DB-PROD-003 | Product Price | Product page open | None | Read price text and parse | Price > 0 as number | Normal | Regression | Automated |
| DB-PROD-004 | Add to Cart Btn | Product page open | None | Check Add to Cart button | Button visible | Critical | Regression | Automated |
| DB-PROD-005 | Add to Cart | Product page open | None | Click Add to Cart | Alert: "Product added." | Critical | Regression | Automated |
| DB-PROD-006 | Phone Prices | Home loaded | None | Open phone, read price | Price > 0 | Normal | Regression | Automated |

## Shopping Cart

| ID | Feature | Preconditions | Test Data | Steps | Expected Result | Priority | Type | Status |
|----|---------|--------------|-----------|-------|-----------------|----------|------|--------|
| DB-CART-001 | Add to Cart | Product page | None | Add product, open cart | Product name appears in cart | Blocker | Regression | Automated |
| DB-CART-002 | Cart Total | Product added | None | Check cart total vs sum of prices | Total == sum ± $0.01 | Critical | Regression | Automated |
| DB-CART-003 | Remove Product | Product in cart | None | Click Delete link, wait for row gone | Product row removed | Critical | Regression | Automated |
| DB-CART-004 | Multi-Product | Home loaded | None | Add phone + laptop | Cart has ≥ 1 item; total correct | Normal | Regression | Automated |
| DB-CART-005 | Cart Nav | Home loaded | None | Click Cart in nav | URL contains cart.html | Normal | Regression | Automated |

## Checkout / Purchase

| ID | Feature | Preconditions | Test Data | Steps | Expected Result | Priority | Type | Status |
|----|---------|--------------|-----------|-------|-----------------|----------|------|--------|
| DB-E2E-001 | Full Purchase | Fresh session | validCustomer() | Home→Product→Cart→Order→Confirm | SweetAlert with "Thank you" | Blocker | E2E | Automated |
| DB-E2E-002 | Incomplete Form | Product in cart | Blank name+card | Open order, submit blank name/card | Alert: "Please fill out Name and Creditcard." | Critical | E2E | Automated |
| DB-E2E-003 | Cart Total | Product in cart | None | Check total is positive before checkout | Total > 0 | Normal | E2E | Automated |
