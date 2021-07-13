package hello.itemservice.upload;

import hello.itemservice.domain.uploaditem.UploadFile;
import hello.itemservice.domain.uploaditem.UploadItem;
import hello.itemservice.domain.uploaditem.UploadItemRepository;
import hello.itemservice.file.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.UriUtil;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UploadItemController {

    private final UploadItemRepository uploadItemRepository;
    private final FileStore fileStore;

    @GetMapping("/items/new")
    public String newItem(@ModelAttribute UploadItemForm form) {
        return "upload/item-form";
    }

    @PostMapping("/items/new")
    public String saveItem(@ModelAttribute UploadItemForm form, RedirectAttributes redirectAttributes) throws IOException {

        UploadFile attachFile = fileStore.storeFile(form.getAttachFile());
        List<UploadFile> storeImageFiles = fileStore.storeFiles(form.getImageFiles());

        // 디비에 저장
        UploadItem uploadItem = new UploadItem();
        uploadItem.setItemName(form.getItemName());
        uploadItem.setAttachFile(attachFile);
        uploadItem.setImageFiles(storeImageFiles);

        uploadItemRepository.save(uploadItem);

        redirectAttributes.addAttribute("itemId", uploadItem.getId());

        return "redirect:/items/{itemId}";
    }

    @GetMapping("/items/{id}")
    public String items(@PathVariable Long id, Model model) {
        UploadItem uploadItem = uploadItemRepository.findById(id);
        model.addAttribute("item", uploadItem);
        return "upload/item-view";
    }

    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(filename));
    }

    @GetMapping("/attach/{itemId}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable Long itemId) throws MalformedURLException {
        UploadItem item = uploadItemRepository.findById(itemId);

        String storeFileName = item.getAttachFile().getStoreFileName();
        String uploadFileName = item.getAttachFile().getUploadFileName();

        UrlResource urlResource = new UrlResource("file:" + fileStore.getFullPath(storeFileName));

        log.info("uploadFileName={}", uploadFileName);

        String encodedUploadFilename = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);

        String contentDisposition = "attachment; filename\"" + encodedUploadFilename + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(urlResource);
    }
}
