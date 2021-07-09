package hello.itemservice.web.basic;

import hello.itemservice.domain.item.DeliveryCode;
import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.ItemType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.message.ExitMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {
    private final ItemRepository itemRepository;

    @ModelAttribute("regions")
    public Map<String, String> regions() {
        Map<String, String> regions = new LinkedHashMap<>();
        regions.put("SEOUL", "서울");
        regions.put("BUSAN", "부산");
        regions.put("JEJU", "제주");
        return regions;
    }

    @ModelAttribute("itemTypes")
    public ItemType[] itemTypes() {
        return ItemType.values();
    }

    @ModelAttribute("deliveryCodes")
    public List<DeliveryCode> deliveryCodes() {
        List<DeliveryCode> deliveryCodes = new ArrayList<>();
        deliveryCodes.add(new DeliveryCode("FAST", "빠른 배송"));
        deliveryCodes.add(new DeliveryCode("NORMAL", "일반 배송"));
        deliveryCodes.add(new DeliveryCode("SLOW", "느린 배송"));
        return deliveryCodes;
    }

    @PostConstruct
    public void init() {
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm (Model model){
        model.addAttribute("item", new Item());
        return "basic/addForm";
    }

//    @PostMapping("/add")
    public String addItemV1(@RequestParam String itemName,
                            @RequestParam int price,
                            @RequestParam Integer quantity,
                            Model model
    ) {
        Item item = new Item(itemName, price, quantity);

        Item savedItem = itemRepository.save(item);
        model.addAttribute("item", savedItem);

        return "basic/item";
    }

    /**
     * @ModelAttribute("item") Item item
     * model.addAttribute("item", item); 자동 추가
     */
//    @PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item,  Model model) {
        itemRepository.save(item);
        //model.addAttribute("item", item); //자동 추가, 생략 가능
        // 파라미터의 Model model 도 생략 가능
        return "basic/item";
    }


    /**
     * @ModelAttribute name 생략 가능
     * model.addAttribute(item); 자동 추가, 생략 가능
     * 파라미터의 Model model 생략 가능
     * 생략시 model에 저장되는 name은 클래스명 첫글자만 소문자로 등록 Item -> item
     */
//    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item) {
        itemRepository.save(item);
        return "basic/item";
    }

    /**
     * @ModelAttribute 자체 생략 가능
     * model.addAttribute(item) 자동 추가
     * @ModelAttribute를 생략함 -> 기본 타입이 아닌 객체로 인식되어 자동으로 ModelAttribute 가 인식함
     * -> 대상 객체는 model 에 자동으로 addAttribute 되어진다 -> 때 클래스의 첫글자만 소문자로 변경해서 등록된다.
     */
//    @PostMapping("/add")
    public String addItemV4(Item item) {
        itemRepository.save(item);
        return "basic/item";
    }

    /**
     * PRG - Post/Redirect/Get
     */
//    @PostMapping("/add")
    public String addItemV5(Item item) {
        itemRepository.save(item);
        return "redirect:/basic/items/"+item.getId();
    }

    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes) {

        log.info("item.open={}", item.getOpen());
        log.info("item.regions={}", item.getRegions());
        log.info("item.itemType={}", item.getItemType());
        log.info("item.deliveryCode={}", item.getDeliveryCode());

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm (@PathVariable Long itemId, Model model) {
        Item findItem = itemRepository.findById(itemId);
        model.addAttribute("item", findItem);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String updateItem(@PathVariable Long itemId, Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}";
    }
}