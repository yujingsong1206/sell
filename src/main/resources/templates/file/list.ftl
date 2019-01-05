<html>
<body>
<h2>上传文件测试</h2>
<form name="form1" action="/sell/file/upload" method="post" enctype="multipart/form-data">
    <input type="file" name="file" />
    是否压缩：<input type="text" name="isCompress" />
    宽：<input type="text" name="width" />
    高：<input type="text" name="height" />
    <input type="submit" value="上传文件" />
</form>
</body>
</html>