<html>
<head>
<#include "../common/header.ftl">
</head>
<body>
<div class="container">
    <div class="row clearfix">
        <div class="col-md-12 column">
            <form role="form" class="form-inline" method="post" action="/sell/user/login">
                <div class="form-group">
                    <label>用户名：</label>
                    <input type="text" class="form-control" name="username" />
                </div>
                <div class="form-group">
                    <label>密码：</label>
                    <input type="password" class="form-control" name="password" />
                </div>
                <div class="checkbox">
                </div> <button type="submit" class="btn btn-default">登录</button>
            </form>
        </div>
    </div>
</div>
</body>
</html>