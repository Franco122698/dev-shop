# Branch Strategy & Git Workflow

## 🌳 Branch Structure

```
main (Production)
├── test (Staging)
│    └── dev (Development)
```

### Main Branch (main)
- **Purpose:** Production-ready code
- **Protection:** Requires PR review before merge
- **Deployed to:** Azure AKS Production
- **Triggered by:** Merging test → main
- **Pipeline:** main.yml (builds and deploys to Azure)

### Test Branch (test)
- **Purpose:** Staging/QA testing
- **Protection:** Requires PR review before merge
- **Deployed to:** Azure AKS Staging
- **Triggered by:** Merging dev → test
- **Pipeline:** test.yml (runs integration tests, builds Docker images tagged :test)

### Dev Branch (dev)
- **Purpose:** Active development
- **Protection:** Optional PR reviews
- **Deployed to:** Local testing only
- **Triggered by:** Feature branch merges
- **Pipeline:** dev.yml (runs unit tests)

## 📋 Feature Branch Workflow

### 1. Create Feature Branch
```bash
# Always branch from dev
git checkout dev
git pull origin dev
git checkout -b feature/your-feature-name
```

**Branch naming conventions:**
```
feature/add-product-filters      # New features
bugfix/fix-race-condition         # Bug fixes
refactor/improve-performance      # Refactoring
docs/update-api-docs              # Documentation
```

### 2. Develop Locally
```bash
# Make changes, commit frequently
git add .
git commit -m "feat: add product filters"

# Test locally with Docker Compose
docker-compose up
mvn test
```

### 3. Push and Create PR
```bash
# Push feature branch
git push -u origin feature/your-feature-name

# Create PR on GitHub:
# - Title: Clear description
# - Description: What changed and why
# - Target: dev branch
# - Reviewers: Assign team members
```

### 4. PR Reviews & Merging
```bash
# After approval, merge to dev
git checkout dev
git pull origin dev
git merge feature/your-feature-name
git push origin dev

# Delete feature branch
git branch -d feature/your-feature-name
git push origin --delete feature/your-feature-name
```

## 🔄 Release Workflow: dev → test → main

### Step 1: Merge Dev to Test
```bash
# Create PR from dev to test
git checkout test
git pull origin test
git merge dev
git push origin test
# OR create PR on GitHub

# GitHub Actions automatically runs test.yml
# - Runs integration tests
# - Builds Docker images with :test tag
# - Deploys to staging environment
```

### Step 2: Test in Staging
```bash
# QA team tests in staging environment
# Test all critical user paths
# Load testing
# Security scanning
```

### Step 3: Merge Test to Main (Release)
```bash
# After staging approval, create PR from test to main
git checkout main
git pull origin main
git merge test
git push origin main

# GitHub Actions automatically runs main.yml
# - Final tests
# - Builds Docker images with :latest and version tag
# - Pushes to Azure Container Registry
# - Deploys to production AKS
```

## 🔑 Commit Message Standards

```
<type>(<scope>): <subject>

<body>

<footer>
```

### Types
- **feat:** New feature
- **fix:** Bug fix
- **refactor:** Code restructure without feature change
- **perf:** Performance improvement
- **docs:** Documentation only
- **test:** Adding tests
- **chore:** Build, CI, dependencies

### Examples
```bash
# Feature
git commit -m "feat(inventory): add product search endpoint"

# Bug fix
git commit -m "fix(order): prevent race condition in stock deduction"

# Multiple changes
git commit -m "feat(api-gateway): add JWT validation and rate limiting

- Implement JWT token validation middleware
- Add rate limiter (100 req/min per IP)
- Add error handling for expired tokens"

# With issue reference
git commit -m "fix(inventory): correct stock calculation

Fixes #123"
```

## 🔒 Protected Branches

### Settings (GitHub)
```
Repo Settings → Branches → Add rule

Rule for: main
✓ Require pull request reviews
✓ Require status checks to pass
✓ Require branches to be up to date
✓ Dismiss stale pull request approvals
✓ Require code owners review (Optional)

Rule for: test
✓ Require pull request reviews
✓ Require status checks to pass
```

## 📊 CI/CD Pipeline Status

### Dev Pipeline (.github/workflows/dev.yml)
```
Feature branch push
  ↓
✅ Build with Maven
✅ Run unit tests
✅ Report test results
```

### Test Pipeline (.github/workflows/test.yml)
```
dev → test merge
  ↓
✅ Build with Maven
✅ Run integration tests
✅ Build Docker images (:test tag)
✅ (Future) Push to ACR
✅ (Future) Deploy to staging AKS
```

### Main Pipeline (.github/workflows/main.yml - TODO)
```
test → main merge
  ↓
✅ Build with Maven
✅ Run all tests
✅ Build Docker images (:latest, :v1.0.0)
✅ Push to Azure Container Registry
✅ Deploy to production AKS
✅ Smoke tests
✅ Slack notification
```

## 🆘 Common Scenarios

### Update Dev with Latest Main
```bash
git checkout dev
git pull origin main
git push origin dev
```

### Sync Feature Branch with Dev
```bash
git checkout feature/my-feature
git fetch origin
git rebase origin/dev
git push -f origin feature/my-feature
```

### Undo Last Commit
```bash
# Keep changes locally
git reset --soft HEAD~1

# Discard changes
git reset --hard HEAD~1

# Undo pushed commit
git revert HEAD
git push origin dev
```

### Emergency Hotfix (Main → Dev)
```bash
# Create from main
git checkout main
git checkout -b hotfix/critical-bug

# Make fix, commit
git commit -m "fix: critical security issue"

# Merge to main first
git checkout main
git merge hotfix/critical-bug
git push origin main

# Then immediately merge to test
git checkout test
git merge hotfix/critical-bug
git push origin test

# And to dev
git checkout dev
git merge hotfix/critical-bug
git push origin dev

# Delete hotfix branch
git branch -d hotfix/critical-bug
git push origin --delete hotfix/critical-bug
```

## 📈 Deployment Timeline

```
Monday 10:00 AM - Feature completed in Dev
          👇 (PR review 1-2 hours)

Monday 12:00 PM - Merged to Dev, pipeline runs ✅
          👇 (QA tests locally 2-4 hours)

Monday 3:00 PM - PR created Dev → Test
         👇 (Review + approval 30 min)

Monday 4:00 PM - Merged to Test, staging deployment ✅
          👇 (QA tests staging 4-8 hours)

Tuesday 10:00 AM - PR created Test → Main
         👇 (Final review 30 min)

Tuesday 11:00 AM - Merged to Main, production deployment ✅
          👇 (Smoke tests 15 min)

Tuesday 11:30 AM - Live in production! 🎉
```

## 🎓 Best Practices

1. **Small, focused PRs** - Easier to review, less merge conflicts
2. **Frequent commits** - Easier to track changes
3. **Clear commit messages** - Future developers will thank you
4. **Test locally before pushing** - Avoid failing pipelines
5. **One feature per branch** - Simplifies reverting if needed
6. **Always pull before push** - Prevents merge conflicts
7. **Keep branches short-lived** - Maximum 1 week before merging
8. **Review code thoroughly** - Quality gates matter

---

See `README.md` for project overview and `QUICKSTART.md` for getting started.

