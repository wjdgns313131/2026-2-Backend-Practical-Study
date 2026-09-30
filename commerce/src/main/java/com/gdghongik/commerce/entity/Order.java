package com.gdghongik.commerce.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.gdghongik.commerce.entity.OrderStatus.CREATED;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    private Order(OrderStatus status) {
        this.status = status;
    }

    public static Order place(OrderItem item) {
        Order order = new Order(OrderStatus.CREATED);
        order.addItem(item);
        return order;

    }

    public void addItem(OrderItem item) {
        if(item == null) {
            throw new IllegalArgumentException("주문 항목이 있어야 주문할 수 있습니다.");
        }
        if(this.status != OrderStatus.CREATED){
            throw new IllegalStateException("주문 확정 이후에는 항목을 변경할 수 없습니다.");
        }
        this.orderItems.add(item);
        item.assignTo(this);
    }

    public void cancelItem(OrderItem item) {
        if (item == null) {
            throw new IllegalArgumentException("취소할 주문 항목이 필요합니다.");
        }
        if (this.status != OrderStatus.CREATED) {
            throw new IllegalStateException("주문 확정 이후에는 항목을 취소할 수 없습니다.");
        }
        if (!this.orderItems.contains(item)) {
            throw new IllegalArgumentException("주문에 존재하지 않는 항목입니다.");
        }

        // 주문에는 최소 하나 이상의 주문 항목이 존재해야 한다는 불변식을 지킨다.
        // 마지막 주문 항목까지 삭제되면 유효하지 않은 주문이 되므로 취소를 허용하지 않는다.
        if (this.orderItems.size() == 1) {
            throw new IllegalStateException("주문에는 최소 하나 이상의 주문 항목이 있어야 합니다.");
        }

        this.orderItems.remove(item);
    }

    public void cancel() {
       if(this.status == OrderStatus.SHIPPED || this.status == OrderStatus.DELIVERED){
           throw new IllegalStateException("배송이 시작된 주문은 취소할 수 없습니다.");
       }
       this.status = OrderStatus.CANCELED;
    }

    public Money totalAmount() {
        return orderItems.stream()
                .map(OrderItem::subtotal)
                .reduce(Money.ZERO, Money::add);
    }

    public List<OrderItem> getOrderItems() {
        return Collections.unmodifiableList(orderItems);
    }

    public void ship() {
        this.status = OrderStatus.SHIPPED;
    }

    public void deliver() {
        this.status = OrderStatus.DELIVERED;
    }
}
