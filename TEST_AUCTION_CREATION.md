# 🧪 Test Auction Item Creation

## Quick Test to Verify Backend Works

### Step 1: Login and Get Token

```bash
curl -X POST http://localhost:8080/api/v1/auth/authenticate \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin"
  }'
```

**Copy the `access_token` from the response!**

Example response:
```json
{
  "access_token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY5NjUwMDAwMCwiZXhwIjoxNjk2NTAzNjAwfQ.abc123...",
  "refresh_token": "...",
  "user": {...}
}
```

---

### Step 2: Create Auction Item (Replace YOUR_TOKEN_HERE)

```bash
curl -X POST http://localhost:8080/auction-items \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{
    "description": "Test Auction Item from curl",
    "type": "Electronics"
  }'
```

**Expected Result:** ✅ 200 OK with created item

---

## 🔴 **Your Frontend Issue**

Looking at your browser Network tab, I can see:

### ❌ Problem: No Authorization Header
Your frontend is **NOT sending the Authorization header** with the JWT token!

### ✅ Solution: Fix Your Frontend

Your frontend needs to send the token in the **Authorization header**, not just cookies.

Check your frontend code (likely in a service or axios setup):

#### **Option 1: Set Authorization header per request**
```javascript
// In your auction service
async createAuction(auctionData) {
  const token = localStorage.getItem('token'); // or however you store it
  
  return axios.post('/auction-items', auctionData, {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
}
```

#### **Option 2: Set default header (Recommended)**
```javascript
// In your main.js or axios setup file
import axios from 'axios';

// Set default authorization header
const token = localStorage.getItem('token');
if (token) {
  axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
}

// Or use an interceptor
axios.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);
```

---

## 🔍 How to Verify Frontend is Sending Header

1. Open browser DevTools → Network tab
2. Try to create an auction item
3. Click on the `auction-items` request
4. Go to **Request Headers**
5. **Look for:** `Authorization: Bearer eyJ...`

If you don't see it → **Frontend is not sending the token!**

---

## 📋 Frontend Checklist

- [ ] Token is being saved after login (check localStorage/sessionStorage)
- [ ] Token is being retrieved before making requests
- [ ] Token is being added to Authorization header
- [ ] Format is: `Authorization: Bearer <token>` (note the space after "Bearer")
- [ ] Request is going to correct endpoint: `/auction-items`

---

## 💡 Quick Frontend Fix Example

If you're using the same pattern as your auth setup, check your auth store:

```javascript
// In your auth store (stores/auth.js or similar)
export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: null
  }),
  actions: {
    login(username, password) {
      return axios.post('/api/v1/auth/authenticate', {
        username,
        password
      }).then((response) => {
        this.token = response.data.access_token;
        localStorage.setItem('token', this.token);
        
        // ⭐ THIS IS CRITICAL - Set default header!
        axios.defaults.headers.common['Authorization'] = `Bearer ${this.token}`;
        
        return response;
      });
    },
    
    // Add this method if you don't have it
    initializeAuth() {
      const token = localStorage.getItem('token');
      if (token) {
        this.token = token;
        axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
      }
    }
  }
});
```

Then in your `main.js`:
```javascript
import { useAuthStore } from './stores/auth';

const app = createApp(App);
app.use(createPinia());

// Initialize auth before mounting
const authStore = useAuthStore();
authStore.initializeAuth();

app.mount('#app');
```

---

## ✅ Summary

**Backend:** ✅ Working correctly
- POST /auction-items endpoint exists
- Security configured to allow ADMIN role
- Expecting: `Authorization: Bearer <token>` header

**Frontend:** ❌ Not sending Authorization header
- Token may be stored in cookies only
- Need to add Authorization header to requests

**Fix:** Update your frontend to send the token in the Authorization header!
