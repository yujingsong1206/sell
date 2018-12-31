package com.yjs.dataobject;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yjs.utils.serializer.Date2StrSerializer;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单详情表
 */
@Entity
@DynamicUpdate
@Data
public class OrderDetail {

    @Id
    private String detailId;
    private String orderId;
    private String productId;
    /**
     * 商品名字
     */
    private String productName;
    /**
     * 商品单价
     */
    private BigDecimal productPrice;
    /**
     * 商品数量
     */
    private Integer productQuantity;
    /**
     * 商品小图
     */
    private String productIcon;

    @JsonSerialize(using = Date2StrSerializer.class)
    private Date createTime;

    @JsonSerialize(using = Date2StrSerializer.class)
    private Date updateTime;
}
