#!/bin/bash

# Backend Bid Data Verification Test
# Tests that all Lab 9 requirements (1.7) are met

echo "🧪 Testing Backend Bid Data Implementation"
echo "=========================================="
echo ""

BASE_URL="http://localhost:8080"

# Test 1: Check if server is running
echo "📡 Test 1: Server Health Check"
if curl -s -f "$BASE_URL/auction-items" > /dev/null 2>&1; then
    echo "✅ Server is running"
else
    echo "❌ Server is not responding"
    exit 1
fi
echo ""

# Test 2: Count auction items (should be >= 5)
echo "📊 Test 2: Count Auction Items (Lab 9 requires >= 5)"
ITEM_COUNT=$(curl -s "$BASE_URL/auction-items" | jq 'length')
echo "Found: $ITEM_COUNT auction items"
if [ "$ITEM_COUNT" -ge 5 ]; then
    echo "✅ PASS: Has $ITEM_COUNT auction items (>= 5 required)"
else
    echo "❌ FAIL: Only $ITEM_COUNT items (need >= 5)"
    exit 1
fi
echo ""

# Test 3: Check each item has at least 3 bids
echo "🔢 Test 3: Each Item Has >= 3 Bids"
MIN_BIDS=$(curl -s "$BASE_URL/auction-items" | jq 'map(.bids | length) | min')
echo "Minimum bids per item: $MIN_BIDS"
if [ "$MIN_BIDS" -ge 3 ]; then
    echo "✅ PASS: All items have >= 3 bids"
else
    echo "❌ FAIL: Some items have < 3 bids"
    exit 1
fi
echo ""

# Test 4: Check at least 3 items have successful bids
echo "🏆 Test 4: At Least 3 Items Have Successful Bids"
SOLD_COUNT=$(curl -s "$BASE_URL/auction-items" | jq 'map(select(.successfulBid != null)) | length')
echo "Items with successful bids: $SOLD_COUNT"
if [ "$SOLD_COUNT" -ge 3 ]; then
    echo "✅ PASS: $SOLD_COUNT items have successful bids (>= 3 required)"
else
    echo "❌ FAIL: Only $SOLD_COUNT items have successful bids (need >= 3)"
    exit 1
fi
echo ""

# Test 5: Verify all required fields are present
echo "📋 Test 5: All Required Fields Present"
SAMPLE=$(curl -s "$BASE_URL/auction-items" | jq '.[0]')

# Check startingPrice
if echo "$SAMPLE" | jq -e '.startingPrice != null' > /dev/null; then
    echo "✅ startingPrice field exists"
else
    echo "❌ startingPrice field missing"
    exit 1
fi

# Check bids array
if echo "$SAMPLE" | jq -e '.bids | length > 0' > /dev/null; then
    echo "✅ bids array exists and has data"
else
    echo "❌ bids array missing or empty"
    exit 1
fi

# Check bidder field in bids
if echo "$SAMPLE" | jq -e '.bids[0].bidder != null' > /dev/null; then
    echo "✅ bidder field exists in bids"
else
    echo "❌ bidder field missing in bids"
    exit 1
fi

# Check successfulBid
if echo "$SAMPLE" | jq -e 'has("successfulBid")' > /dev/null; then
    echo "✅ successfulBid field exists"
else
    echo "❌ successfulBid field missing"
    exit 1
fi
echo ""

# Test 6: Display sample data
echo "📄 Test 6: Sample Auction Item Data"
echo "-----------------------------------"
curl -s "$BASE_URL/auction-items" | jq '.[0] | {
    id,
    description,
    type,
    startingPrice,
    bidCount: (.bids | length),
    bids: .bids | map({amount, bidder, datetime}),
    successfulBid: .successfulBid | {amount, bidder}
}'
echo ""

# Test 7: Summary statistics
echo "📊 Test 7: Summary Statistics"
echo "-----------------------------"
STATS=$(curl -s "$BASE_URL/auction-items" | jq '{
    totalItems: length,
    totalBids: ([.[] | .bids | length] | add),
    itemsWithSuccessfulBids: ([.[] | select(.successfulBid != null)] | length),
    avgBidsPerItem: (([.[] | .bids | length] | add) / length | round)
}')
echo "$STATS"
echo ""

# Final result
echo "✅ ALL TESTS PASSED!"
echo "===================="
echo ""
echo "Lab 9 Requirement 1.7: ✅ COMPLETE"
echo "- ✅ At least 5 AuctionItem created"
echo "- ✅ Each has at least 3 Bid"
echo "- ✅ At least 3 AuctionItems have successful bids"
echo ""
echo "Backend is ready for frontend integration! 🚀"
