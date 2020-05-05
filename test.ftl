<!DOCTYPE html>
<html lang="en">
<head>
    <title>Bootstrap Example</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>

<div class="container">
    <h1>Подготовка к ЕГЭ по математике</h1>

</div>


<#list twofolders as twofolder>
    <div class="row">
        <div class="col-md-1">
        </div>
        <div class="col-md-5">
            <h3>${twofolder.firstPath.fileName}</h3>
            <ul>
                <#list twofolder.firstPathFiles as firstColumnFiles>
                <li><a href="${firstColumnFiles.relFileName}">${firstColumnFiles.path.fileName}</a>
                    </#list>
            </ul>
        </div>
        <div class="col-md-6">
            <#if twofolder.secondPath??>
                <h3>${twofolder.secondPath.fileName}</h3>

                <ul>
                    <#if twofolder.secondPathFiles??>

                    <#list twofolder.secondPathFiles as secondColumnFiles>
                    <li><a href="${secondColumnFiles.relFileName}">${secondColumnFiles.path.fileName}</a>
                        </#list>
                        </#if>

                </ul>

            </#if>

        </div>
    </div>

</#list>


</body>
</html>
