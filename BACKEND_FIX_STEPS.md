# 🔧 Backend Fix for 403 Upload Error - STEP BY STEP

## The Problem
Your backend is rejecting image uploads with a **403 Forbidden** error because Spring Security's authorization filter is blocking anonymous requests to the upload endpoints.

## The Solution
You need to modify **TWO files** in your backend to properly handle anonymous uploads.

---

## Step 1: Fix JwtAuthenticationFilter.java

### Location
`/home/prab/Documents/CMU/ComponentBasedSoftware/331-Backend/src/main/java/se331/lab/security/config/JwtAuthenticationFilter.java`

### What to do

1. **Add these imports at the top of the file:**

```java
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
```

2. **Find the upload endpoint bypass code** (should look like this):

```java
// Allow upload endpoints without requiring JWT auth
if (path.equals("/uploadImage") || path.equals("/uploadFile")) {
  filterChain.doFilter(request, response);
  return;
}
```

3. **Replace it with this:**

```java
// Allow upload endpoints without requiring JWT auth
if (path.equals("/uploadImage") || path.equals("/uploadFile")) {
  // Set anonymous authentication to satisfy authorization filter
  AnonymousAuthenticationToken anonymousAuth = 
      new AnonymousAuthenticationToken(
          "anonymous", "anonymous", 
          java.util.Collections.singletonList(
              new SimpleGrantedAuthority("ROLE_ANONYMOUS")
          )
      );
  SecurityContextHolder.getContext().setAuthentication(anonymousAuth);
  filterChain.doFilter(request, response);
  return;
}
```

**Why this works:** This explicitly sets an anonymous authentication token in the security context, which satisfies Spring Security's authorization filter requirements while still bypassing JWT validation.

---

## Step 2: Verify SecurityConfiguration.java

### Location
`/home/prab/Documents/CMU/ComponentBasedSoftware/331-Backend/src/main/java/se331/lab/security/config/SecurityConfiguration.java`

### What to check

Make sure your `securityFilterChain` method has **permitAll()** for upload endpoints:

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
  http
    // ... other config ...
    .authorizeHttpRequests(auth -> auth
      .requestMatchers(HttpMethod.POST, "/uploadImage", "/uploadFile").permitAll()
      // ... other matchers ...
    )
    // ... other config ...
}
```

### Optional: Alternative Fix (Nuclear Option)

If the above doesn't work, you can add this bean method to **completely bypass** security for upload endpoints:

```java
@Bean
public WebSecurityCustomizer webSecurityCustomizer() {
  return (web) -> web.ignoring()
      .requestMatchers("/uploadImage", "/uploadFile");
}
```

This tells Spring Security to completely skip ALL security filters for these endpoints.

---

## Step 3: Restart Backend

After making changes:

```bash
cd /home/prab/Documents/CMU/ComponentBasedSoftware/331-Backend

# Stop the running backend (Ctrl+C in the terminal where it's running)

# Clean and restart
./mvnw clean spring-boot:run
# or if you're using Gradle:
./gradlew clean bootRun
```

---

## Step 4: Test the Fix

### Quick Terminal Test

```bash
# Test without any authentication
curl -i -X POST http://localhost:8080/uploadImage \
  -F "image=@/path/to/any/image.png"
```

**Expected Result:**
- ✅ Should return 200/201 with Firebase URL
- ✅ Or return 500 with Firebase error (if credentials aren't configured)
- ❌ Should **NOT** return 403 Forbidden

### Test from Frontend

1. Refresh your Vue app in the browser
2. Try uploading an image
3. Check the console - you should see:
   - "Not sending Authorization header for upload (endpoint should be public)"
   - No 403 errors
   - Either success or a different error (like Firebase configuration issues)

---

## What We Changed in Frontend

✅ **Already fixed in your vite-project:**
- Removed Authorization header from upload requests
- The ImageUpload component now sends requests without authentication

---

## Troubleshooting

### Still getting 403?

1. **Check if backend restarted properly**
   ```bash
   # Look for this in backend logs:
   # "Started Application in X.XXX seconds"
   ```

2. **Verify the changes were saved**
   ```bash
   cd /home/prab/Documents/CMU/ComponentBasedSoftware/331-Backend
   grep -A 5 "AnonymousAuthenticationToken" src/main/java/se331/lab/security/config/JwtAuthenticationFilter.java
   ```
   Should show your new code.

3. **Check backend logs for errors**
   Look in the terminal where backend is running for any stack traces.

### Getting 500 errors?

That's actually **progress**! A 500 error means:
- ✅ Authentication/authorization is working
- ❌ Firebase/cloud storage configuration issue
- Check your `application.yml` or `application.properties` for cloud storage credentials

### Getting CORS errors?

Make sure your CORS config allows requests from `http://localhost:5173`:

```java
@Bean
public CorsConfigurationSource corsConfigurationSource() {
  CorsConfiguration configuration = new CorsConfiguration();
  configuration.addAllowedOrigin("http://localhost:5173");
  configuration.addAllowedMethod("*");
  configuration.addAllowedHeader("*");
  configuration.setAllowCredentials(true);
  // ... rest of config
}
```

---

## Quick Command Reference

```bash
# Navigate to backend
cd /home/prab/Documents/CMU/ComponentBasedSoftware/331-Backend

# Edit the JWT filter
nano src/main/java/se331/lab/security/config/JwtAuthenticationFilter.java
# or
code src/main/java/se331/lab/security/config/JwtAuthenticationFilter.java

# Restart backend
./mvnw clean spring-boot:run

# Test upload in another terminal
curl -i -X POST http://localhost:8080/uploadImage -F "image=@test.png"
```

---

## Summary

1. ✅ Frontend already fixed (no Authorization header sent)
2. 🔧 Backend needs fix: Set anonymous authentication in JWT filter
3. 🔄 Restart backend after changes
4. ✅ Test with curl or browser

The root cause is that Spring Security with STATELESS sessions requires an authentication object in the security context, even for `permitAll()` endpoints. By explicitly setting an anonymous authentication token, we satisfy this requirement.
