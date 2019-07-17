var page = require('webpage').create();
var system = require('system');

////读取命令行参数，也就是js文件路径。
if (system.args.length === 1) {
    console.log('Usage: loadspeed.js <some URL>');
//这行代码很重要。凡是结束必须调用。否则phantomjs不会停止
    phantom.exit();
}
page.settings.loadImages = true;  //加载图片
page.settings.resourceTimeout = 30000;//超过10秒放弃加载
//截图设置，
//page.viewportSize = {
//  width: 1000,
//  height: 3000
//};
var address = system.args[1];
var width = system.args[2];
var height = system.args[3];
var outDir = system.args[4];
page.open(address, function (status) {

    function checkReadyState() {//等待加载完成将页面生成pdf
        setTimeout(function () {
            var readyState = page.evaluate(function () {
                return document.readyState;
            });

            if ("complete" === readyState) {

                page.paperSize = {width: width + 'px', height: height + 'px'};
                page.render(outDir);
                //console.log就是传输回去的内容。
                console.log("生成成功");
                console.log("$" + outDir + "$");
                phantom.exit();

            } else {
                checkReadyState();
            }
        }, 1000);
    }
    checkReadyState();
});