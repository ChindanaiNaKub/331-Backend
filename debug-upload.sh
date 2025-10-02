#!/bin/bash

echo "======================================"
echo "Backend Upload Endpoint Debug Script"
echo "======================================"
echo ""

echo "1. Testing if backend is running..."
if curl -s http://localhost:8080/actuator/health > /dev/null 2>&1; then
    echo "✅ Backend is running"
else
    echo "❌ Backend is NOT running or health endpoint not available"
fi
echo ""

echo "2. Testing GET /uploadImage (should be 405 Method Not Allowed, not 403)..."
RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/uploadImage)
if [ "$RESPONSE" = "403" ]; then
    echo "❌ GET request returned 403 Forbidden (BAD - security blocking)"
elif [ "$RESPONSE" = "405" ]; then
    echo "✅ GET request returned 405 Method Not Allowed (GOOD - endpoint exists but GET not supported)"
else
    echo "⚠️  GET request returned $RESPONSE"
fi
echo ""

echo "3. Testing POST /uploadImage without Origin header..."
RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" -X POST http://localhost:8080/uploadImage)
if [ "$RESPONSE" = "403" ]; then
    echo "❌ POST request returned 403 Forbidden (BAD - security blocking)"
elif [ "$RESPONSE" = "400" ] || [ "$RESPONSE" = "415" ] || [ "$RESPONSE" = "500" ]; then
    echo "✅ POST request returned $RESPONSE (GOOD - security passed, but missing data/config)"
else
    echo "⚠️  POST request returned $RESPONSE"
fi
echo ""

echo "4. Testing POST /uploadImage with Origin header (simulating browser)..."
RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" -X POST \
    -H "Origin: http://localhost:5173" \
    http://localhost:8080/uploadImage)
if [ "$RESPONSE" = "403" ]; then
    echo "❌ POST request with Origin returned 403 Forbidden (BAD - CORS or security issue)"
elif [ "$RESPONSE" = "400" ] || [ "$RESPONSE" = "415" ] || [ "$RESPONSE" = "500" ]; then
    echo "✅ POST request with Origin returned $RESPONSE (GOOD - security passed)"
else
    echo "⚠️  POST request with Origin returned $RESPONSE"
fi
echo ""

echo "5. Full response headers for POST /uploadImage:"
curl -i -X POST \
    -H "Origin: http://localhost:5173" \
    http://localhost:8080/uploadImage 2>&1 | head -15
echo ""

echo "======================================"
echo "Diagnosis:"
echo "======================================"
echo "- If you see 403 errors above, the backend security is still blocking uploads"
echo "- If you see 400/415/500 errors, security is working but there's a different issue"
echo "- Check backend logs for more details"
echo ""
echo "Next steps:"
echo "1. If still getting 403, restart backend: cd ../331-Backend && ./mvnw spring-boot:run"
echo "2. Check backend console logs for any security exceptions"
echo "3. Verify JwtAuthenticationFilter has the AnonymousAuthenticationToken code"
