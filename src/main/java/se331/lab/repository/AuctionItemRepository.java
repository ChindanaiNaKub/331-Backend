package se331.lab.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import se331.lab.AuctionItem;

public interface AuctionItemRepository extends JpaRepository<AuctionItem, Long> {
    Page<AuctionItem> findByDescriptionIgnoreCaseContaining(String description, Pageable pageable);
    Page<AuctionItem> findBySuccessfulBid_AmountLessThan(Double amount, Pageable pageable);
    Page<AuctionItem> findByDescriptionIgnoreCaseContainingOrTypeIgnoreCaseContaining(String description, String type, Pageable pageable);
}


