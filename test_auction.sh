#!/bin/bash
TOKEN=$(curl -s -X POST http://localhost:8080/api/v1/auth/authenticate \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin"}' | jq -r '.access_token')

echo "Token: ${TOKEN:0:50}..."
echo ""
#!/bin/bash

# Auction Items Backend Test Script
# Make executable: chmod +x test_auction.sh
# Run: ./test_auction.sh

BASE_URL="http://localhost:8080"
GREEN='\033[0;32m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo "🧪 Testing Auction Items Backend API"
echo "===================================="
echo ""

# Test 1: Get all auction items
echo -e "${BLUE}Test 1: Get all auction items${NC}"
echo "curl $BASE_URL/auction-items"
RESULT=$(curl -s $BASE_URL/auction-items | jq 'length')
echo -e "${GREEN}✓ Found $RESULT items${NC}"
echo ""

# Test 2: Pagination
echo -e "${BLUE}Test 2: Pagination (first 3 items)${NC}"
echo "curl \"$BASE_URL/auction-items?_limit=3&_page=1\""
RESULT=$(curl -s "$BASE_URL/auction-items?_limit=3&_page=1" | jq 'length')
echo -e "${GREEN}✓ Retrieved $RESULT items${NC}"
echo ""

# Test 3: Search by description
echo -e "${BLUE}Test 3: Search by description (macbook)${NC}"
echo "curl \"$BASE_URL/auction-items?description=macbook\""
RESULT=$(curl -s "$BASE_URL/auction-items?description=macbook" | jq -r '.[0].description')
echo -e "${GREEN}✓ Found: $RESULT${NC}"
echo ""

# Test 4: Search by type (NEW!)
echo -e "${BLUE}Test 4: Search by type (JEWELRY)${NC}"
echo "curl \"$BASE_URL/auction-items?type=jewelry\""
RESULT=$(curl -s "$BASE_URL/auction-items?type=jewelry" | jq 'length')
echo -e "${GREEN}✓ Found $RESULT JEWELRY items${NC}"
curl -s "$BASE_URL/auction-items?type=jewelry" | jq -r '.[].description' | sed 's/^/   - /'
echo ""

# Test 5: Search by type - ELECTRONICS
echo -e "${BLUE}Test 5: Search by type (ELECTRONICS)${NC}"
echo "curl \"$BASE_URL/auction-items?type=electronics\""
RESULT=$(curl -s "$BASE_URL/auction-items?type=electronics" | jq 'length')
echo -e "${GREEN}✓ Found $RESULT ELECTRONICS items${NC}"
curl -s "$BASE_URL/auction-items?type=electronics" | jq -r '.[].description' | sed 's/^/   - /'
echo ""

# Test 6: Filter by successful bid
echo -e "${BLUE}Test 6: Filter by successful bid < 1000${NC}"
echo "curl \"$BASE_URL/auction-items?maxSuccessful=1000\""
RESULT=$(curl -s "$BASE_URL/auction-items?maxSuccessful=1000" | jq 'length')
echo -e "${GREEN}✓ Found $RESULT items with successful bid < 1000${NC}"
echo ""

# Test 7: Get single item
echo -e "${BLUE}Test 7: Get single auction item (ID=1)${NC}"
echo "curl $BASE_URL/auction-items/1"
RESULT=$(curl -s $BASE_URL/auction-items/1 | jq -r '.description')
BIDS=$(curl -s $BASE_URL/auction-items/1 | jq '.bids | length')
echo -e "${GREEN}✓ Item: $RESULT${NC}"
echo -e "${GREEN}✓ Has $BIDS bids${NC}"
echo ""

# Test 8: Check total count header
echo -e "${BLUE}Test 8: Check X-Total-Count header${NC}"
TOTAL=$(curl -si "$BASE_URL/auction-items?_limit=3" | grep -i "x-total-count" | cut -d' ' -f2 | tr -d '\r')
echo -e "${GREEN}✓ Total items in database: $TOTAL${NC}"
echo ""

# Test 9: Test all item types
echo -e "${BLUE}Test 9: List all item types in database${NC}"
curl -s $BASE_URL/auction-items | jq -r '.[].type' | sort -u | while read TYPE; do
    COUNT=$(curl -s "$BASE_URL/auction-items?type=$TYPE" | jq 'length')
    echo -e "   ${GREEN}✓${NC} $TYPE: $COUNT items"
done
echo ""

echo -e "${GREEN}================================${NC}"
echo -e "${GREEN}✅ All tests completed!${NC}"
echo -e "${GREEN}================================${NC}"
