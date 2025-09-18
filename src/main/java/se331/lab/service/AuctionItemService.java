package se331.lab.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import se331.lab.AuctionItem;

public interface AuctionItemService {
    Page<AuctionItem> getItems(Integer pageSize, Integer page);
    Page<AuctionItem> getItemsByDescription(String description, Pageable pageable);
    Page<AuctionItem> getItemsBySuccessfulBidLessThan(Double amount, Pageable pageable);
    AuctionItem getItem(Long id);
    AuctionItem save(AuctionItem item);
}


