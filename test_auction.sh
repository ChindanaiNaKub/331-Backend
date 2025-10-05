#!/bin/bash
TOKEN=$(curl -s -X POST http://localhost:8080/api/v1/auth/authenticate \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin"}' | jq -r '.access_token')

echo "Token: ${TOKEN:0:50}..."
echo ""
echo "Testing POST to /auction-items..."

curl -i -X POST http://localhost:8080/auction-items \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"name":"Test Item","description":"Test","type":"AUCTION"}' 2>&1 | head -15
