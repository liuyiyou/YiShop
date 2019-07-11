package cn.liuyiyou.shop.order.service;

import cn.liuyiyou.shop.order.entity.Order;
import cn.liuyiyou.shop.order.vo.req.OrderAddReqVo;
import cn.liuyiyou.shop.order.vo.req.OrderListReqVo;
import cn.liuyiyou.shop.order.vo.resp.AdminOrderInfoRespVo;
import cn.liuyiyou.shop.order.vo.resp.AdminOrderListRespVo;
import cn.liuyiyou.shop.order.vo.resp.OrderCountRespVo;
import cn.liuyiyou.shop.order.vo.resp.OrderInfoRespVo;
import cn.liuyiyou.shop.order.vo.resp.OrderListRespVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * C端用户产品订单表。 服务类
 * </p>
 *
 * @author liuyiyou.cn
 * @since 2018-11-05
 */
public interface IOrderService extends IService<Order> {


    /**
     * 用户各个状态下的订单数量
     *
     * @param uid
     * @return
     */
    OrderCountRespVo orderCount(int uid);


    /**
     * 用户订单列表
     *
     * @param uid
     * @param pageNum
     * @param pageSize
     * @return
     */
    IPage<Order> orderPage(int uid, int pageNum, int pageSize);

    OrderInfoRespVo getOrderInfo(Long orderId);

    AdminOrderInfoRespVo getAdminOrderInfo(Long orderId);

    Long createOrder(OrderAddReqVo orderAddReqVo);

    Page<OrderListRespVo> getOrderList(OrderListReqVo orderListReqVo);

    Page<AdminOrderListRespVo> getAdminOrderList(OrderListReqVo orderListReqVo);

    /**
     * 取消订单
     *
     * @param orderId
     * @return
     */
    Boolean cancelOrder(Long orderId);

    Boolean payOrder(Long orderId);

    Boolean confrimOrder(Long orderId);
}
