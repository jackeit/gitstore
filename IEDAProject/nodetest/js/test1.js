`use strict`
function f() {
    print("hello");
    return "hello1"
}
function wait(ms){
    let time = new Date();
    while(new Date()-time<ms){}
}
function print(p){
    console.log(p)
}
var k = "none"
//若是setTimeout(f(),1000),会报错，因为f()会立即执行并返回hello1,然后setTimeout函数报错
k = setTimeout(f,1000)
wait(4000)
print("end")
print(100)

var fs = require('fs');
fs.readFile("F:\\javatest\\output.txt","utf-8",function (err,data){
    if (err) {
        console.log(err);
    } else {
        console.log(data);
    }
})
