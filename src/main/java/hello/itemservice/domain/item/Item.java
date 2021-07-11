package hello.itemservice.domain.item;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * > javax.validation 으로 시작하면 특정 구현에 관계없이 제공되는 표준 인터페이스이고,
 * org.hibernate.validator 로 시작하면 하이버네이트 validator 구현체를 사용할 때만 제공되는 검증
 * 기능이다. 실무에서 대부분 하이버네이트 validator를 사용하므로 자유롭게 사용해도 된다.
 */
@Getter @Setter
public class Item {

    private Long id;
    @NotBlank
    private String itemName;

    @NotNull
    @Range(min = 1000, max = 1000000)
    private Integer price;

    @NotNull
    @Max(9999)
    private Integer quantity;

    private Boolean open;  // 판매 여부
    private List<String> regions;  // 등록 지역
    private ItemType itemType;  // 상품종류
    private String deliveryCode;  // 배송 방식

    public Item() {}

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }

    public void updateItem(Item updateParam) {
        this.itemName = updateParam.getItemName();
        this.price = updateParam.getPrice();
        this.quantity = updateParam.getQuantity();
        this.open = updateParam.getOpen();
        this.regions = updateParam.getRegions();
        this.itemType = updateParam.getItemType();
        this.deliveryCode = updateParam.getDeliveryCode();
    }
}
