package com.yjs.controller;

import com.yjs.dto.OrderDTO;
import com.yjs.enums.ResultEnum;
import com.yjs.exception.SellException;
import com.yjs.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 卖家端订单
 */
@Controller
@RequestMapping("/seller/order")
public class SellerOrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 订单列表
     * @param page 第几页
     * @param size 一页有多少数据
     */
    @GetMapping("/list")
    public ModelAndView list(@RequestParam(value = "page", defaultValue = "1") Integer page,
                             @RequestParam(value = "size", defaultValue = "10") Integer size,
                             Map<String, Object> map){
        Page<OrderDTO> orderDTOPage = orderService.findList(PageRequest.of(page - 1, size, new Sort(Sort.Direction.ASC, "createTime")));
        map.put("orderDTOPage", orderDTOPage);
        map.put("currentPage", page);
        map.put("size", size);
        return new ModelAndView("order/list", map);
    }

    /**
     * 取消订单
     */
    @GetMapping("/cancel")
    public ModelAndView cancel(@RequestParam("orderId") String orderId,
                               Map<String, Object> map, HttpServletResponse response) throws Exception{
        try {
            OrderDTO orderDTO = orderService.findById(orderId);
            orderService.cancel(orderDTO);
        } catch (SellException e){
            map.put("msg", e.getMessage());
            map.put("url","/sell/seller/order/list");
            return new ModelAndView("common/error", map);
        }

        map.put("msg", ResultEnum.ORDER_CANCEL_ERROR.getMsg());
        map.put("url","/sell/seller/order/list");
//        response.sendRedirect("/sell/seller/order/list");
        return new ModelAndView("common/success", map);
    }

    /**
     * 订单详情
     */
    @GetMapping("/detail")
    public ModelAndView detail(@RequestParam("orderId") String orderId,
                               Map<String, Object> map){
        OrderDTO orderDTO = new OrderDTO();
        try {
            orderDTO = orderService.findById(orderId);
        } catch (SellException e){
            map.put("msg", e.getMessage());
            map.put("url","/sell/seller/order/list");
            return new ModelAndView("common/error", map);
        }

        map.put("orderDTO", orderDTO);
        return new ModelAndView("/order/detail", map);
    }

    /**
     * 完结订单
     */
    @GetMapping("/finish")
    public ModelAndView finished(@RequestParam("orderId") String orderId,
                                 Map<String, Object> map){
        try {
            OrderDTO orderDTO = orderService.findById(orderId);
            orderService.finish(orderDTO);
        } catch (SellException e){
            map.put("msg", e.getMessage());
            map.put("url","/sell/seller/order/list");
            return new ModelAndView("common/error", map);
        }
        map.put("msg", ResultEnum.ORDER_FINISH_ERROR.getMsg());
        map.put("url","/sell/seller/order/list");
        return new ModelAndView("common/success", map);
    }

}
