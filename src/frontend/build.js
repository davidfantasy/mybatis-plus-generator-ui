var fs = require('fs');
var exec = require('child_process').exec;
var path = require("path");
var _ = require("lodash");
const colors = require('colors');

let distPath = path.resolve('../main/resources/generator-ui')
let staticPath = path.resolve('./src/static')
let necessaryFiles = ["_nuxt", "index.html"];

fs.rmdir(distPath, function (error) {
    console.log(colors.green('已清理static目录'));
    console.log('开始build前端资源');
    if (!fs.existsSync(distPath)) {
        console.log(colors.yellow("目标文件夹不存在，已自动创建：" + distPath));
        fs.mkdirSync(distPath);
    }
    exec("nuxt build", function (err, sto) {
        if (err != null) {
            console.error(colors.red('前端资源编译错误'));
            console.error(colors.red(err));
        } else {
            console.log(colors.green('前端资源编译成功'));
            let staticFiles = fs.readdirSync(staticPath);
            staticFiles.forEach(function (filename) {
                necessaryFiles.push(filename);
            });
            console.log(colors.yellow(necessaryFiles));
            let distFiles = fs.readdirSync(distPath);
            //删除自动生成的不必要的静态文件
            distFiles.forEach(function (filename) {
                let flag = _.findIndex(necessaryFiles, function (f) {
                    return f === filename;
                });
                if (flag === -1) {
                    let filePath = path.join(distPath, filename);
                    if (fs.statSync(filePath).isDirectory()) { // recurse
                        deleteFolder(filePath);
                    } else {
                        fs.unlinkSync(filePath);
                    }
                }
            });
            console.log(colors.green('已删除不必要的文件'));
        }
    });
})

function deleteFolder(path) {
    var files = [];
    if (fs.existsSync(path)) {
        files = fs.readdirSync(path);
        files.forEach(function (file, index) {
            var curPath = path + "/" + file;
            if (fs.statSync(curPath).isDirectory()) { // recurse
                deleteFolder(curPath);
            } else { // delete file
                fs.unlinkSync(curPath);
            }
        });
        fs.rmdirSync(path);
    }
}