package se331.lab.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import se331.lab.AuctionItem;
import se331.lab.repository.AuctionItemRepository;

@Service
@RequiredArgsConstructor
public class AuctionItemServiceImpl implements AuctionItemService {
    final AuctionItemRepository auctionItemRepository;

    @Override
    public Page<AuctionItem> getItems(Integer pageSize, Integer page) {
        return auctionItemRepository.findAll(PageRequest.of(page - 1, pageSize));
    }

    @Override
    public Page<AuctionItem> getItemsByDescription(String description, Pageable pageable) {
        return auctionItemRepository.findByDescriptionIgnoreCaseContaining(description, pageable);
    }

    @Override
    public Page<AuctionItem> getItemsBySuccessfulBidLessThan(Double amount, Pageable pageable) {
        return auctionItemRepository.findBySuccessfulBid_AmountLessThan(amount, pageable);
    }

    @Override
    public AuctionItem getItem(Long id) {
        return auctionItemRepository.findById(id).orElse(null);
    }

    @Override
    public Page<AuctionItem> getItemsByDescriptionOrType(String description, String type, Pageable pageable) {
        return auctionItemRepository.findByDescriptionIgnoreCaseContainingOrTypeIgnoreCaseContaining(description, type, pageable);
    }

    @Override
    public AuctionItem save(AuctionItem item) {
        return auctionItemRepository.save(item);
    }
}


