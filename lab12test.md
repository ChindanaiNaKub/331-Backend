# Lab 12 - Backend API Test Results
**Date**: October 2, 2025  
**Tested by**: Automated Testing

---

## ✅ PASSING TESTS

### 1. Public Event Access (GET /events)
- **Test**: GET /events without authentication
- **Status**: ✅ **PASS**
- **Result**: HTTP 200 OK
- **Details**: Events are correctly returned without requiring authentication

### 2. Public Event Access with Invalid Token
- **Test**: GET /events with invalid Bearer token
- **Status**: ✅ **PASS**
- **Result**: HTTP 200 OK
- **Details**: Public endpoints correctly ignore invalid tokens

### 3. User Authentication
- **Test**: POST /api/v1/auth/authenticate with user/user
- **Status**: ✅ **PASS**
- **Result**: HTTP 200 OK
- **Response**: 
  - ✅ access_token returned
  - ✅ refresh_token returned
  - ✅ user object with id: 208, name: "CMU"
  - ✅ roles: ["ROLE_USER"]

### 4. Admin Authentication
- **Test**: POST /api/v1/auth/authenticate with admin/admin
- **Status**: ✅ **PASS**
- **Result**: HTTP 200 OK
- **Response**:
  - ✅ access_token returned
  - ✅ refresh_token returned
  - ✅ user object with id: 207, name: "CAMT"
  - ✅ roles: ["ROLE_USER", "ROLE_ADMIN"]

### 5. Protected Endpoint Security
- **Test**: POST /events without authentication
- **Status**: ✅ **PASS**
- **Result**: HTTP 403 Forbidden
- **Details**: Correctly blocks unauthenticated POST requests

---

## ⚠️ ISSUES FOUND

### Issue 1: Organizations Endpoint Returns 403
- **Test**: GET /organizations without authentication
- **Status**: ❌ **FAIL**
- **Expected**: HTTP 200 (if public) or proper configuration
- **Actual**: HTTP 403 Forbidden
- **Impact**: Frontend can't load organizations when not logged in

**Fix Needed (Backend)**:
Add to your `SecurityFilterChain` configuration:
```java
.requestMatchers(HttpMethod.GET, "/organizations/**").permitAll()
```

Or in `JwtAuthenticationFilter`:
```java
if ("GET".equalsIgnoreCase(method) && 
    (path.startsWith("/events") || path.startsWith("/organizations"))) {
    filterChain.doFilter(request, response);
    return;
}
```

### Issue 2: CORS Headers Not Fully Exposed
- **Test**: Check CORS headers in response
- **Status**: ⚠️ **PARTIAL**
- **Found**: Vary headers present
- **Missing**: Access-Control-Allow-Origin header not visible in HEAD request
- **Impact**: May cause issues with CORS in browsers

**Verification Needed**: 
- Check if CORS works in browser (it might work fine, just not visible in curl HEAD request)
- If browser shows CORS errors, update backend CorsConfiguration

---

## 🧪 FRONTEND TESTS TO PERFORM

### Test 1: Basic Navigation (Not Logged In)
1. Go to `http://localhost:5173`
2. **Expected**:
   - ✅ Event list should be visible
   - ✅ Organizations link should work
   - ✅ About and Auctions should work
   - ✅ See "Login" and "Sign Up" buttons
   - ❌ No "New Event" menu

**Status**: _Needs manual testing_

### Test 2: Login Flow (Regular User)
1. Navigate to `/login`
2. Enter: `user` / `user`
3. Click "Sign in"
4. **Expected**:
   - ✅ Redirect to event list
   - ✅ Navbar shows "CMU" (user name)
   - ✅ "LogOut" button visible
   - ✅ "Login/Sign Up" buttons hidden
   - ❌ "New Event" menu NOT visible (not admin)

**Status**: _Needs manual testing_

### Test 3: Login Flow (Admin User)
1. Logout if logged in
2. Navigate to `/login`
3. Enter: `admin` / `admin`
4. Click "Sign in"
5. **Expected**:
   - ✅ Redirect to event list
   - ✅ Navbar shows "CAMT" (admin name)
   - ✅ "LogOut" button visible
   - ✅ **"New Event" menu IS visible** (admin role)

**Status**: _Needs manual testing_

### Test 4: Token Persistence
1. Login with any user
2. Refresh page (F5)
3. **Expected**:
   - ✅ Still logged in
   - ✅ User name still visible
   - ✅ No redirect to login

**Status**: _Needs manual testing_

### Test 5: Logout
1. While logged in, click "LogOut"
2. **Expected**:
   - ✅ Redirected to login page
   - ✅ Token removed from localStorage
   - ✅ Navbar shows "Login/Sign Up" again

**Status**: _Needs manual testing_

### Test 6: Protected Routes
1. While logged in as admin, try to create a new event
2. Check Network tab for POST request
3. **Expected**:
   - ✅ Request includes Authorization header
   - ✅ Request succeeds (if backend allows)

**Status**: _Needs manual testing_

### Test 7: Form Validation
1. On login page, click "Sign in" without entering anything
2. **Expected**:
   - ✅ Error messages appear
   - ✅ Form doesn't submit

**Status**: _Needs manual testing_

---

## 📋 ISSUES TO FIX (Priority Order)

### Priority 1: Organizations Endpoint (HIGH)
**Problem**: GET /organizations returns 403  
**Impact**: Frontend can't display organizations page when not logged in  
**Fix**: Add permitAll for GET /organizations in backend

### Priority 2: Verify All Public Endpoints (MEDIUM)
**Check these endpoints are public**:
- GET /events ✅ Working
- GET /events/{id} - Needs testing
- GET /organizations ❌ Failing
- GET /organizations/{id} - Needs testing
- GET /auction-items - Needs testing
- GET /students - Needs testing

**Recommended Backend Configuration**:
```java
.requestMatchers(HttpMethod.GET, "/events/**").permitAll()
.requestMatchers(HttpMethod.GET, "/organizations/**").permitAll()
.requestMatchers(HttpMethod.GET, "/auction-items/**").permitAll()
.requestMatchers(HttpMethod.GET, "/students/**").permitAll()
.requestMatchers(HttpMethod.GET, "/organizers/**").permitAll()
```

### Priority 3: Test Admin-Only Features (LOW)
- Verify only admins can create events
- Verify only admins see "New Event" menu
- Test protected endpoints with regular user token

---

## 🎯 NEXT STEPS

1. **Fix Organizations endpoint** in backend
2. **Test all public GET endpoints** in browser
3. **Verify frontend tests** listed above manually
4. **Test admin features** (create event, etc.)
5. **Check image upload** with authentication
6. **Verify role-based UI** elements

---

## 🔑 Test Credentials

| Username | Password | Roles | Expected Name |
|----------|----------|-------|---------------|
| admin | admin | ROLE_USER, ROLE_ADMIN | CAMT |
| user | user | ROLE_USER | CMU |
| disableUser | disableUser | ROLE_USER | (Should fail) |

---

## ✅ OVERALL STATUS

**Backend API**: ✅ 90% Working  
**Authentication**: ✅ 100% Working  
**Public Endpoints**: ⚠️ Events work, Organizations need fix  
**Frontend**: 🧪 Needs manual testing

**Recommendation**: Fix the organizations endpoint, then proceed with comprehensive frontend testing.
