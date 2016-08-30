<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no"/>
    <script src="js/jquery.min.js"></script>

</head>
<body>
<h2>HXDZWXMaster Project</h2>
<div>
    <input type="button" value="OK" onclick="submitInfo()">
    <p id="abc"/>
</div>

<script type="text/javascript" charset="utf-8">
    function submitInfo() {
        var saveData = '{"playerBasicInfos":{"id":1,"curTeamid":1,"historyTeams":"1","age":27,"birthday":"1988-08-31","resume":"no","name":"LongSheng","sex":"m","jersyno":"27","height":178.0,"weight":74.0,"armspan":183.0,"position":"PG","games":5,"mvp":1,"prides":"no","statid":1,"iconimg":"no","grade":"d"},"playerCareerStats":{"id":1,"fga":5,"fgm":3,"fg":60.0,"pa3":5,"pm3":3,"fg3":60.0,"pa2":0,"pm2":0,"fg2":0.0,"pa1":0,"pm1":0,"fg1":0.0,"rebs":10,"orebs":8,"blks":2,"fouls":3,"ofouls":1,"stls":2,"asts":5,"tos":0,"totalScore":15}}';
        $.ajax({
            type: "POST",
            url: "playerinfos/test3",
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify(saveData),
            success: function (data) {
                $("#abc").html("现在接收到的data是：" + data);
            }
        });

    }

</script>

</body>
</html>
