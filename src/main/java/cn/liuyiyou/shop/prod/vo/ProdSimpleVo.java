package cn.liuyiyou.shop.prod.vo;

import lombok.Data;

/***
 *
 * @author: liuyiyou.cn
 * @date: 2019/2/7
 * @Copyright 2019 liuyiyou.cn Inc. All rights reserved
 */
@Data
public class ProdSimpleVo {

    private Long prodId;
    private String prodName;
    private String pic;
    private Float prodPrice;
    private Float referPrice;
}
