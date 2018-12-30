package com.yjs.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by sjyjs on 2018/12/27.
 */
@Entity
@DynamicUpdate
@Data
public class ProductInfo {

    @Id
    private String productId;
    /** 名字 */
    private String productName;
    /** 单价 */
    private BigDecimal productPrice;
    /** 库存 */
    private Integer productStock;
    /** 描述 */
    private String productDescription;
    /** 小图 */
    private String productIcon;
    /** 状态 0 正常 1下架 */
    private Integer productStatus;
    /** 类目编号 */
    private Integer categoryType;
    private Date createTime;
    private Date updateTime;

}
