<html>
<head>
    <meta charset="UTF-8"/>
    <title>订单详情</title>
    <link href="https://cdn.bootcss.com/twitter-bootstrap/3.0.1/css/bootstrap.min.css" rel="stylesheet" />
</head>
<body>
<div class="container">
    <div class="row clearfix">
        <div class="col-md-4 column">
        <table class="table table-bordered">
            <thead>
            <tr>
                <th>订单id</th>
                <th>订单总金额</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>${orderDTO.orderId}</td>
                <td>${orderDTO.orderAmount}</td>
            </tr>
            </tbody>
        </table>
    </div>

        <div class="col-md-12 column">
            <table class="table table-bordered">
                <thead>
                <tr>
                    <th>商品id</th>
                    <th>商品名称</th>
                    <th>价格</th>
                    <th>数量</th>
                    <th>总额</th>
                </tr>
                </thead>
                <tbody>
                <#list orderDTO.orderDetailList as orderDtail>
                <tr>
                    <td>${orderDtail.productId}</td>
                    <td>${orderDtail.productName}</td>
                    <td>${orderDtail.productPrice}</td>
                    <td>${orderDtail.productQuantity}</td>
                    <td>${orderDtail.productPrice * orderDtail.productQuantity}</td>
                </tr>
                </#list>
                </tbody>
            </table>
        </div>

        <div class="col-md-12 column">
            <#if orderDTO.orderStatus == 0>
                <a href="/sell/seller/order/finish?orderId=${orderDTO.orderId}" type="button" class="btn btn-default btn-primary">完结订单</a>
                <a href="/sell/seller/order/cancel?orderId=${orderDTO.orderId}" type="button" class="btn btn-default btn-danger">取消订单</a>
            </#if>
            <a href="javascript:history.go(-1)" type="button" class="btn btn-default btn-info">返回</a>
        </div>
    </div>
</div>
</body>
</html>