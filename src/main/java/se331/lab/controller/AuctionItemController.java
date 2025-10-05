package se331.lab.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;
import se331.lab.AuctionItem;
import se331.lab.service.AuctionItemService;

@Controller
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuctionItemController {
    final AuctionItemService auctionItemService;

    @GetMapping({"auction-items"})
    @ResponseBody
    public ResponseEntity<?> getItems(
            @RequestParam(value = "_limit", required = false) Integer perPage,
            @RequestParam(value = "_page", required = false) Integer page,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "maxSuccessful", required = false) Double maxSuccessful
    ) {
        perPage = perPage == null ? 5 : perPage;
        page = page == null ? 1 : page;
        Page<AuctionItem> output;
        if (description != null && type != null) {
            // Search by both description and type (OR condition)
            output = auctionItemService.getItemsByDescriptionOrType(description, type, PageRequest.of(page - 1, perPage));
        } else if (description != null) {
            // Search by description only
            output = auctionItemService.getItemsByDescription(description, PageRequest.of(page - 1, perPage));
        } else if (type != null) {
            // Search by type only (reuse the OR method with same value)
            output = auctionItemService.getItemsByDescriptionOrType(type, type, PageRequest.of(page - 1, perPage));
        } else if (maxSuccessful != null) {
            output = auctionItemService.getItemsBySuccessfulBidLessThan(maxSuccessful, PageRequest.of(page - 1, perPage));
        } else {
            output = auctionItemService.getItems(perPage, page);
        }
        HttpHeaders responseHeader = new HttpHeaders();
        responseHeader.set("x-total-count", String.valueOf(output.getTotalElements()));
        responseHeader.set("X-Total-Count", String.valueOf(output.getTotalElements()));
        responseHeader.setAccessControlExposeHeaders(java.util.List.of("X-Total-Count", "x-total-count"));
        return new ResponseEntity<>(output.getContent(), responseHeader, HttpStatus.OK);
    }

    @GetMapping({"auction-items/{id}"})
    @ResponseBody
    public ResponseEntity<?> getItem(@PathVariable("id") Long id) {
        AuctionItem item = auctionItemService.getItem(id);
        if (item != null) return ResponseEntity.ok(item);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The given id is not found");
    }

    @org.springframework.web.bind.annotation.PostMapping("/auction-items")
    @ResponseBody
    public ResponseEntity<?> addItem(@org.springframework.web.bind.annotation.RequestBody AuctionItem item) {
        AuctionItem savedItem = auctionItemService.save(item);
        return ResponseEntity.ok(savedItem);
    }
}


