package com.yjs.service.impl;

import com.yjs.dataobject.OrderDetail;
import com.yjs.dto.OrderDTO;
import com.yjs.enums.OrderStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class OrderServiceImplTest {

    @Autowired
    private OrderServiceImpl orderService;

    @Test
    public void create() throws Exception {

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBuyerName("师妹");
        orderDTO.setBuyerAddress("云南艺术学院");
        orderDTO.setBuyerPhone("12345678950");
        orderDTO.setBuyerOpenid("666666");

        List<OrderDetail> orderDetailList = new ArrayList<>();
        OrderDetail o1 = new OrderDetail();
        o1.setProductId("123458");
        o1.setProductQuantity(1);
        orderDetailList.add(o1);
        OrderDetail o2 = new OrderDetail();
        o2.setProductId("123457");
        o2.setProductQuantity(2);
        orderDetailList.add(o2);

        orderDTO.setOrderDetailList(orderDetailList);

        OrderDTO result = orderService.create(orderDTO);
        log.info("【创建订单成功】 reuslt={}", result);
    }

    @Test
    public void findById() throws Exception {
        OrderDTO orderDTO = orderService.findById("1546047449515455296","6666666616");
        log.info("【查询单个订单】 result={}", orderDTO);
    }

    @Test
    public void findList() throws Exception {
        Page<OrderDTO> orderDTOPage = orderService
                .findList("666666", PageRequest.of(0,10, new Sort(Sort.Direction.ASC, "createTime")));
        log.info("【查询个人订单列表】 result={}", orderDTOPage.getContent());
//        log.info("订单状态：{}", OrderStatusEnum.codeOf(2).getMsg());
//        log.error("测试日志");
    }

    @Test
    public void cancel() throws Exception {
//        OrderDTO orderDTO = orderService.findById("1546047449515455296");
//        orderService.cancel(orderDTO);
    }

    @Test
    public void finish() throws Exception {
//        OrderDTO orderDTO = orderService.findById("1546047181552371314");
//        orderService.finish(orderDTO);
    }

    @Test
    public void paid() throws Exception {
//        OrderDTO orderDTO = orderService.findById("1546047181552371314");
//        orderService.paid(orderDTO);
    }

}