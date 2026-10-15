# Requirements Traceability Matrix – DemoBlaze E2E Automation

| Req ID | Requirement | Test Case IDs | Implementation Class | Status |
|--------|-------------|--------------|---------------------|--------|
| REQ-01 | Application home page loads without error | DB-SMOKE-001 | HomePageSmokeTest | Covered |
| REQ-02 | Navigation bar visible on all pages | DB-SMOKE-002 | HomePageSmokeTest | Covered |
| REQ-03 | Product catalogue displays items | DB-SMOKE-003, DB-PROD-001 | HomePageSmokeTest, ProductTests | Covered |
| REQ-04 | Category filtering (Phones, Laptops, Monitors) | DB-SMOKE-005, DB-SMOKE-006 | HomePageSmokeTest | Covered (Monitors not automated) |
| REQ-05 | Product detail shows name, price, description | DB-PROD-002, DB-PROD-003 | ProductTests | Covered |
| REQ-06 | Add to cart produces confirmation alert | DB-PROD-005 | ProductTests | Covered |
| REQ-07 | Cart shows added products | DB-CART-001 | CartTests | Covered |
| REQ-08 | Cart total equals sum of product prices | DB-CART-002, DB-E2E-003 | CartTests, PurchaseFlowTest | Covered |
| REQ-09 | Remove product from cart | DB-CART-003 | CartTests | Covered |
| REQ-10 | User registration with unique username | DB-SIGNUP-001 | SignupTests | Covered |
| REQ-11 | Duplicate username rejected | DB-SIGNUP-002 | SignupTests | Covered |
| REQ-12 | Blank signup fields produce alert | DB-SIGNUP-003 | SignupTests | Covered |
| REQ-13 | Newly registered user can log in | DB-SIGNUP-004 | SignupTests | Covered |
| REQ-14 | Valid login updates navigation bar | DB-LOGIN-001 | LoginTests | Covered |
| REQ-15 | Invalid credentials rejected with alert | DB-LOGIN-002, DB-LOGIN-005 | LoginTests | Covered |
| REQ-16 | Logout restores pre-login state | DB-LOGIN-004 | LoginTests | Covered |
| REQ-17 | Complete purchase journey end-to-end | DB-E2E-001 | PurchaseFlowTest | Covered |
| REQ-18 | Incomplete form blocked with alert | DB-E2E-002 | PurchaseFlowTest | Covered |
| REQ-19 | Purchase confirmation shows "Thank you" | DB-E2E-001 | PurchaseFlowTest | Covered |

## Coverage Gaps and Limitations

| Gap | Reason |
|-----|--------|
| Monitors category automated test | Manually verified; DemoBlaze has monitors but no unique flow |
| Multi-user concurrency | Shared demo environment; not reliably reproducible |
| Payment error scenarios | DemoBlaze simulates orders; no real card validation |
| Empty-cart checkout | Application allows clicking Place Order with empty cart |
| Session persistence across browsers | Out of scope for current iteration |
