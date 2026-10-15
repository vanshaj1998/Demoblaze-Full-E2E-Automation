# Defect Report Template

## Header
| Field | Value |
|-------|-------|
| Defect ID | DEF-XXX |
| Title | [Brief, clear description] |
| Related Requirement | REQ-XX |
| Related Test Case | DB-XXX-XXX |
| Reported By | [Name] |
| Date Reported | YYYY-MM-DD |
| Severity | Blocker / Critical / Major / Minor / Trivial |
| Priority | P1 / P2 / P3 / P4 |
| Status | New / Open / In Progress / Fixed / Verified / Closed / Won't Fix |

## Environment
| Field | Value |
|-------|-------|
| Application URL | https://www.demoblaze.com/ |
| Browser | Chrome 130.x / Firefox 131.x |
| OS | Windows 11 / Ubuntu 22.04 |
| Framework Version | 1.0.0 |
| Git Commit | [commit SHA] |
| Build / CI Run | Jenkins Build #XX |

## Description

### Steps to Reproduce
1. Step 1
2. Step 2
3. Step 3

### Expected Result
Describe what should happen.

### Actual Result
Describe what actually happened.

### Evidence
- Screenshot: `target/screenshots/ClassName.methodName_TIMESTAMP.png`
- Log snippet: (paste relevant excerpt)
- Allure report: link or build artifact reference

### Reproducibility
☐ Always  ☐ Intermittent (X/10 runs)  ☐ Unable to reproduce

## Root Cause Analysis
> To be completed by developer / QA.

## Notes
> Distinguish between:
> - Application defect (code/logic issue in the AUT)
> - Test/framework defect (automation bug, wrong assertion, stale locator)
> - Environment defect (network issue, shared data corruption, external API failure)
> - Flaky test (timing/synchronization issue in the test itself)
