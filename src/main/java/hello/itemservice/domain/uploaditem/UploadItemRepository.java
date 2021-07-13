package hello.itemservice.domain.uploaditem;

import hello.itemservice.domain.item.Item;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UploadItemRepository {

    private final Map<Long, UploadItem> store = new ConcurrentHashMap<>();

    private Long sequence = 0L;

    public UploadItem save(UploadItem item) {
        item.setId(++sequence);
        store.put(item.getId(), item);
        return item;
    }
    public UploadItem findById(Long id) {
        return store.get(id);
    }
}
