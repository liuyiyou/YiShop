package cn.liuyiyou.shop.order.controller;


import cn.liuyiyou.shop.common.response.Response;
import cn.liuyiyou.shop.common.response.Result;
import cn.liuyiyou.shop.order.service.IOrderService;
import cn.liuyiyou.shop.order.vo.req.OrderAddReqVo;
import cn.liuyiyou.shop.order.vo.req.OrderListReqVo;
import cn.liuyiyou.shop.order.vo.resp.OrderListRespVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/***
 *
 * 订单相关处理
 * @author: liuyiyou.cn
 * @date: 2019/2/8
 * @Copyright 2019 liuyiyou.cn Inc. All rights reserved
 */
@Api("订单控制器")
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    private IOrderService orderService;


    @ApiOperation("订单列表")
    @PostMapping("/list")
    public Result list(@RequestBody @NotNull OrderListReqVo orderListReqVo) {
        Page<OrderListRespVo> result = orderService.getOrderList(orderListReqVo);
        return Response.success().setData(result);
    }


    @ApiOperation("订单详情")
    @GetMapping("/{id}")
    public Result list(@PathVariable("id") Long orderId) {
        return Response.success(orderService.getOrderInfo(orderId));
    }

    @ApiOperation("订单提交")
    @PostMapping("/add")
    public Result<Long> add(@RequestBody OrderAddReqVo orderAddReqVo) {
        return Response.success(orderService.createOrder(orderAddReqVo));
    }


    @ApiOperation("取消订单")
    @GetMapping("/cancel/{id}")
    public Result cancel(@PathVariable("id") Long orderId) {
        return Response.success(orderService.cancelOrder(orderId));
    }

    @ApiOperation("订单支付")
    @GetMapping("/pay/{id}")
    public Result pay(@PathVariable("id") Long orderId) {
        return Response.success(orderService.payOrder(orderId));
    }

    @ApiOperation("确认收货")
    @GetMapping("/confirm/{id}")
    public Result confirm(@PathVariable("id") Long orderId) {
        return Response.success(orderService.confrimOrder(orderId));
    }
}

