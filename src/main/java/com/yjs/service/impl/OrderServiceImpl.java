package com.yjs.service.impl;

import com.yjs.converter.OrderMaster2OrderDTOConverter;
import com.yjs.dataobject.OrderDetail;
import com.yjs.dataobject.OrderMaster;
import com.yjs.dataobject.ProductInfo;
import com.yjs.dto.CartDTO;
import com.yjs.dto.OrderDTO;
import com.yjs.enums.OrderStatusEnum;
import com.yjs.enums.PayStatusEnum;
import com.yjs.enums.ResultEnum;
import com.yjs.exception.SellException;
import com.yjs.repository.OrderDetailRepository;
import com.yjs.repository.OrderMasterRepository;
import com.yjs.service.OrderService;
import com.yjs.service.ProductInfoService;
import com.yjs.utils.EnumUtil;
import com.yjs.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMasterRepository orderMasterRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private ProductInfoService productInfoService;

    @Override
    @Transactional
    public OrderDTO create(OrderDTO orderDTO) {

        String orderId = KeyUtil.genUniqueKey();
        BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);
//        List<CartDTO> cartDTOList = new ArrayList<CartDTO>();

        for (OrderDetail orderDetail : orderDTO.getOrderDetailList()){
            ProductInfo productInfo = productInfoService.findById(orderDetail.getProductId());
            BeanUtils.copyProperties(productInfo, orderDetail);
            orderDetail.setOrderId(orderId);
            orderAmount = productInfo.getProductPrice().multiply(new BigDecimal(orderDetail.getProductQuantity())).add(orderAmount);
            orderDetail.setDetailId(KeyUtil.genUniqueKey());
            orderDetail.setCreateTime(null);
            orderDetail.setUpdateTime(null);
            orderDetailRepository.save(orderDetail);

//            CartDTO cartDTO = new CartDTO();
//            cartDTO.setProductId(orderDetail.getProductId());
//            cartDTO.setProductQuantity(orderDetail.getProductQuantity());
//            cartDTOList.add(cartDTO);
        }

        orderDTO.setOrderId(orderId);
        orderDTO.setOrderAmount(orderAmount);
        orderDTO.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderDTO.setPayStatus(PayStatusEnum.NOPAY.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);
//        orderMaster.setOrderId(orderId);
//        orderMaster.setOrderAmount(orderAmount);
//        orderMaster.setBuyerOpenid(orderDTO.getBuyerOpenid());
//        orderMaster.setBuyerAddress(orderDTO.getBuyerAddress());
//        orderMaster.setBuyerPhone(orderDTO.getBuyerPhone());
//        orderMaster.setBuyerName(orderDTO.getBuyerName());
        orderMasterRepository.save(orderMaster);


        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream()
                .map(e -> new CartDTO(e.getProductId(),e.getProductQuantity())).collect(Collectors.toList());
        productInfoService.decreaseStcok(cartDTOList);

        return orderDTO;
    }

    @Override
    public OrderDTO findById(String orderId, String openid) {
//        Optional optional = orderMasterRepository.findById(orderId);
//        if(!optional.isPresent()){
//            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
//        }
//        OrderMaster orderMaster = (OrderMaster) optional.get();
        OrderMaster orderMaster = orderMasterRepository.findByOrderIdAndBuyerOpenid(orderId, openid);
        if(orderMaster == null){
            log.error("【订单查询】不存在，orderId={}，openid={}", orderId, openid);
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
        if(CollectionUtils.isEmpty(orderDetailList)){
            throw new SellException(ResultEnum.ORDERDETAIL_NOT_EXIST);
        }
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster, orderDTO);
        orderDTO.setOrderDetailList(orderDetailList);
        return orderDTO;
    }

    @Override
    public OrderDTO findById(String orderId) {
        Optional optional = orderMasterRepository.findById(orderId);
        if(!optional.isPresent()){
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        OrderMaster orderMaster = (OrderMaster) optional.get();
        if(orderMaster == null){
            log.error("【订单查询】不存在，orderId={}", orderId);
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
        if(CollectionUtils.isEmpty(orderDetailList)){
            throw new SellException(ResultEnum.ORDERDETAIL_NOT_EXIST);
        }
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster, orderDTO);
        orderDTO.setOrderDetailList(orderDetailList);
        return orderDTO;
    }

    @Override
    public Page<OrderDTO> findList(String buyerOpenid, Pageable pageable) {
        Page<OrderMaster> orderMasterPage = orderMasterRepository.findByBuyerOpenid(buyerOpenid, pageable);
        List<OrderDTO> orderDTOList = OrderMaster2OrderDTOConverter.convert(orderMasterPage.getContent());
        Page<OrderDTO> orderDTOPage = new PageImpl<OrderDTO>(orderDTOList, pageable, orderMasterPage.getTotalElements());
        return orderDTOPage;
    }

    @Override
    @Transactional
    public OrderDTO cancel(OrderDTO orderDTO) {
        OrderMaster orderMaster = new OrderMaster();

        if(orderDTO.getOrderStatus() != OrderStatusEnum.NEW.getCode()){
            log.error("【取消订单】订单状态不正确，orderId={}，orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        orderDTO.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        BeanUtils.copyProperties(orderDTO, orderMaster);
        orderMasterRepository.save(orderMaster);

        if(CollectionUtils.isEmpty(orderDTO.getOrderDetailList())){
            log.error("【取消订单】订单中无商品详情，orderDTO={}", orderDTO);
            throw new SellException(ResultEnum.ORDER_DETAIL_EMPTY);
        }
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream()
                .map(e -> new CartDTO(e.getProductId(), e.getProductQuantity())).collect(Collectors.toList());
        productInfoService.increaseStock(cartDTOList);

        if(orderDTO.getPayStatus() == PayStatusEnum.PAY.getCode()){
            //TODO 支付的时候再写，如果已支付需要退款
        }

        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO finish(OrderDTO orderDTO) {
        if(orderDTO.getOrderStatus() != OrderStatusEnum.NEW.getCode()){
            log.error("【完结订单】订单状态不正确，orderId={}，orderStatus={}({})", orderDTO.getOrderId(), orderDTO.getOrderStatus(), orderDTO.getOrderStatusEnum().getMsg());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        if(orderDTO.getPayStatus() != PayStatusEnum.PAY.getCode()){
            log.error("【完结订单】订单支付状态不正确，orderId={}，payStatus={}({})", orderDTO.getOrderId(), orderDTO.getPayStatus(), orderDTO.getPayStatusEnum().getMsg());
            throw new SellException(ResultEnum.ORDER_PAY_STATUS_ERROR);
        }

        orderDTO.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);
        orderMasterRepository.save(orderMaster);

        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO paid(OrderDTO orderDTO) {

        if(orderDTO.getOrderStatus() != OrderStatusEnum.NEW.getCode()){
            log.error("【订单支付成功】订单状态不正确，orderId={}，orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        if(orderDTO.getPayStatus() != PayStatusEnum.NOPAY.getCode()){
            log.error("【订单支付成功】订单支付状态不正确，orderStatus={}({})", orderDTO.getPayStatus(),
                    EnumUtil.getByCode(orderDTO.getPayStatus(), PayStatusEnum.class).getMsg());
            throw new SellException(ResultEnum.ORDER_PAY_STATUS_ERROR);
        }

        orderDTO.setPayStatus(PayStatusEnum.PAY.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);
        orderMasterRepository.save(orderMaster);

        return orderDTO;
    }

    @Override
    public Page<OrderDTO> findList(Pageable pageable) {
        Page<OrderMaster> orderMasterPage = orderMasterRepository.findAll(pageable);
        List<OrderDTO> orderDTOList = OrderMaster2OrderDTOConverter.convert(orderMasterPage.getContent());
        Page<OrderDTO> orderDTOPage = new PageImpl<OrderDTO>(orderDTOList, pageable, orderMasterPage.getTotalElements());
        return orderDTOPage;
    }
}
