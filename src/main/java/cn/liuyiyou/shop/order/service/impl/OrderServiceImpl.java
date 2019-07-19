package cn.liuyiyou.shop.order.service.impl;

import cn.liuyiyou.shop.order.config.OrderPayTypeMap;
import cn.liuyiyou.shop.order.config.OrderStatusMap;
import cn.liuyiyou.shop.order.dto.OrderCountDto;
import cn.liuyiyou.shop.order.entity.Order;
import cn.liuyiyou.shop.order.entity.OrderProd;
import cn.liuyiyou.shop.order.enums.OrderStatusEnum;
import cn.liuyiyou.shop.order.mapper.OrderMapper;
import cn.liuyiyou.shop.order.service.IOrderProdService;
import cn.liuyiyou.shop.order.service.IOrderService;
import cn.liuyiyou.shop.order.vo.req.OrderAddReqVo;
import cn.liuyiyou.shop.order.vo.req.OrderListReqVo;
import cn.liuyiyou.shop.order.vo.resp.AdminOrderInfoRespVo;
import cn.liuyiyou.shop.order.vo.resp.AdminOrderListRespVo;
import cn.liuyiyou.shop.order.vo.resp.OrderCountRespVo;
import cn.liuyiyou.shop.order.vo.resp.OrderInfoRespVo;
import cn.liuyiyou.shop.order.vo.resp.OrderListRespVo;
import cn.liuyiyou.shop.order.vo.resp.OrderProdListRespVo;
import cn.liuyiyou.shop.prod.entity.Prod;
import cn.liuyiyou.shop.prod.entity.ProdSku;
import cn.liuyiyou.shop.prod.service.IProdService;
import cn.liuyiyou.shop.prod.service.IProdSkuService;
import cn.liuyiyou.shop.user.service.impl.UserDeliveryService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * <p>
 * C端用户产品订单表。 服务实现类
 * </p>
 *
 * @author liuyiyou.cn
 * @since 2018-11-05
 */
@Service
@Slf4j
@Transactional
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {


    @Autowired
    private IOrderProdService orderProdService;
    @Autowired
    private IProdService prodService;
    @Autowired
    private IProdSkuService prodSkuService;
    @Autowired
    private OrderMapper orderMapper;


    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private UserDeliveryService userDeliveryService;

    @Override
    public Page<OrderListRespVo> getOrderList(OrderListReqVo orderListReqVo) {
        Page<Order> pageQuery = new Page<>(orderListReqVo.getPageNum(), orderListReqVo.getPageSize());
        LambdaQueryWrapper<Order> wrapper = new QueryWrapper<Order>().lambda().select()
                .eq(Order::getUid, 1);
        if (orderListReqVo.getStatus() != null && orderListReqVo.getStatus() != 0) {
            wrapper.eq(Order::getStatus, orderListReqVo.getStatus());
        }
        wrapper.orderByDesc(Order::getOrderId);
        IPage<Order> orderIPage = this.page(pageQuery, wrapper);
        Page<OrderListRespVo> result = new Page<>(orderIPage.getCurrent(), orderIPage.getSize(), orderIPage.getTotal());
        List<OrderListRespVo> orderListRespVos = orderIPage.getRecords().stream().map(order -> {
            List<OrderProdListRespVo> orderProdListRespVos = getOrderProdListRespVos(order);
            return new OrderListRespVo()
                    .setOrderId(order.getOrderId())
                    .setStatus(order.getStatus())
                    .setStatusName(OrderStatusMap.STATUS_MAP.get(order.getStatus()))
                    .setProds(orderProdListRespVos);
        }).collect(Collectors.toList());
        result.setRecords(orderListRespVos);
        return result;
    }

    @Override
    public Page<AdminOrderListRespVo> getAdminOrderList(OrderListReqVo orderListReqVo) {
        Page<Order> pageQuery = new Page<>(orderListReqVo.getPageNum(), orderListReqVo.getPageSize());
        LambdaQueryWrapper<Order> wrapper = new QueryWrapper<Order>().lambda().select();
        if (orderListReqVo.getStatus() != null && orderListReqVo.getStatus() != 0) {
            wrapper.eq(Order::getStatus, orderListReqVo.getStatus());
        }
        if (orderListReqVo.getOrderId() != null) {
            wrapper.eq(Order::getOrderId, orderListReqVo.getOrderId());
        }
        wrapper.orderByDesc(Order::getCreateTime);
        IPage<Order> orderIPage = this.page(pageQuery, wrapper);
        Page<AdminOrderListRespVo> result = new Page<>(orderIPage.getCurrent(), orderIPage.getSize(), orderIPage.getTotal());
        List<AdminOrderListRespVo> orderListRespVos = orderIPage.getRecords().stream().map(order -> {
            List<OrderProdListRespVo> orderProdListRespVos = getOrderProdListRespVos(order);
            return new AdminOrderListRespVo()
                    .setOrderId(order.getOrderId())
                    .setConsignee(order.getConsignee())
                    .setCreateTime(order.getCreateTime())
                    .setTotalPrice(order.getTotalPrice())
                    .setStatus(order.getStatus())
                    .setPayType(order.getPayType())
                    .setPayTypeName(OrderPayTypeMap.STATUS_MAP.get(order.getPayType() == null ? -1 : order.getPayType()))
                    .setStatusName(OrderStatusMap.STATUS_MAP.get(order.getStatus()))
                    .setProds(orderProdListRespVos);
        }).collect(Collectors.toList());
        result.setRecords(orderListRespVos);
        return result;
    }

    private List<OrderProdListRespVo> getOrderProdListRespVos(Order order) {
        List<OrderProd> orderProds = orderProdService.list(new QueryWrapper<OrderProd>().eq("order_id", order.getOrderId()));
        return orderProds.stream().map(orderProd -> new OrderProdListRespVo().setOrderId(orderProd.getOrderId())
                .setProdId(orderProd.getProdId())
                .setProdName(orderProd.getProdName())
                .setProdNum(orderProd.getProdNum())
                .setAlbum(orderProd.getAlbum())
                .setRealPrice(orderProd.getRealPrice())
                .setSkuId(orderProd.getSkuId())).collect(toList());
    }

    @Override
    public OrderInfoRespVo getOrderInfo(Long orderId) {
        Order order = this.getById(orderId);
        List<OrderProdListRespVo> orderProdListRespVos = getOrderProdListRespVos(order);


        OrderInfoRespVo orderInfoRespVo = new OrderInfoRespVo()
                .setOrderId(order.getOrderId())
                .setAddress(getAddress(order.getConsignAddr()))
                .setConsignee(order.getConsignee())
                .setConsignPhone(order.getConsignPhone())
                .setStatus(order.getStatus())
                .setStatusName(OrderStatusMap.STATUS_MAP.get(order.getStatus()))
                .setProds(orderProdListRespVos);
        return orderInfoRespVo;
    }

    @Override
    public AdminOrderInfoRespVo getAdminOrderInfo(Long orderId) {
        Order order = this.getById(orderId);
        List<OrderProdListRespVo> orderProdListRespVos = getOrderProdListRespVos(order);

        AdminOrderInfoRespVo orderInfoRespVo = new AdminOrderInfoRespVo();
        BeanUtils.copyProperties(order, orderInfoRespVo);
        orderInfoRespVo.setProds(orderProdListRespVos);
        return orderInfoRespVo;
    }

    @Override
    public IPage<Order> orderPage(int uid, int pageNum, int pageSize) {
        Page page = new Page(pageNum, pageSize);
        IPage pageResult = this.page(page, new QueryWrapper<Order>().eq("uid", uid));
        return pageResult;
    }

    @Override
    public OrderCountRespVo orderCount(int uid) {
        List<OrderCountDto> orderCountDtos = orderMapper.getOrderCountByStatus(uid);
        OrderCountRespVo vo = new OrderCountRespVo();
        orderCountDtos.forEach(orderCountDto -> {
            switch (orderCountDto.getStatus()) {
                case 1:
                    vo.setNeedPay(orderCountDto.getCount());
                    break;
                case 2:
                    vo.setNeedSend(orderCountDto.getCount());
                    break;
                case 3:
                    vo.setNeedConfirm(orderCountDto.getCount());
                    break;
                case 4:
                    vo.setNeedComment(orderCountDto.getCount());
                    break;
            }

        });
        return vo;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Long createOrder(OrderAddReqVo orderAddReqVo) {

        ProdSku prodSku = prodSkuService.getById(orderAddReqVo.getSkuId());
        Prod prod = prodService.getById(prodSku.getProdId());

        prodSku.setFreez(prodSku.getFreez() + orderAddReqVo.getProdNum());
        prodSkuService.updateById(prodSku);

        //创建订单
        Order order = new Order();
        this.save(order);

        //创建订单商品
        OrderProd orderProd = new OrderProd();
        orderProd.setProdId(prod.getProdId())
                .setSkuId(orderAddReqVo.getSkuId())
                .setUid(1)
                .setOrderId(order.getOrderId())
                .setProdName(prod.getProdName())
                .setProdId(prod.getProdId())
                .setSkuId(prodSku.getSkuId())
                .setProdNum(orderAddReqVo.getProdNum())
                .setProdAttr(prod.getSpuAttr())
                .setAlbum(prod.getAlbum().split(",")[0]);

        orderProdService.save(orderProd);
        return order.getOrderId();

    }


    @Override
    public Boolean cancelOrder(Long orderId) {
        //修改订单状态
        Order order = this.getById(orderId);
        order.setStatus(OrderStatusEnum.CANCEL.getCode());
        this.updateById(order);


        //库存回滚
        List<OrderProd> orderProds = orderProdService.getByOrderId(orderId);
        orderProds.forEach(orderProd -> {
            ProdSku prodSku = prodSkuService.getById(orderProd.getSkuId());
            prodSku.setFreez(prodSku.getFreez() - orderProd.getProdNum());
            prodSku.setStore(prodSku.getStore() + orderProd.getProdNum());
            prodSkuService.updateById(prodSku);

        });
        return true;
    }


    @Override
    public Boolean payOrder(Long orderId) {
        //updateOrderStatus
        Order order = this.getById(orderId);
        order.setStatus(OrderStatusEnum.NEED_SEND.getCode());
        this.updateById(order);

        //已售库存增加
        List<OrderProd> orderProds = orderProdService.getByOrderId(orderId);
        orderProds.forEach(orderProd -> {
            ProdSku prodSku = prodSkuService.getById(orderProd.getSkuId());
            prodSku.setFreez(prodSku.getFreez() - orderProd.getProdNum());
            prodSku.setSaled(prodSku.getSaled() + orderProd.getProdNum());
            prodSkuService.updateById(prodSku);

        });

        //callThirdPay

        return true;

    }

    @Override
    public Boolean confrimOrder(Long orderId) {
        //updateOrderStatus
        Order order = this.getById(orderId);
        order.setStatus(OrderStatusEnum.SUCCESS.getCode());
        this.updateById(order);

        //userAddScore

        return true;
    }

    private String getAddress(String consignAddr) {
        if (StringUtils.isEmpty(consignAddr)) {
            return "";
        } else {
            JSONObject addrObject = JSONArray.parseObject(consignAddr);
            return addrObject.getString("prov") + addrObject.getString("city") + addrObject.getString("country") + addrObject.getString("addr");
        }
    }


    public static void main(String[] args) {
        String s = OrderPayTypeMap.STATUS_MAP.get(null);
        System.out.println(s);
    }

}
