package cn.liuyiyou.shop.order.vo.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author: liuyiyou.cn
 * @date: 2019/1/24
 * @version: V1.0
 */
@Data
@Accessors(chain = true)
public class OrderListRespVo {

    private Long orderId;
    private Integer status;
    private String statusName;
    private List<OrderProdListRespVo> prods;
}
