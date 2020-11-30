/**
    返回格式 retCode 3 校验失败， 1 通过
*/
function result(retCode,fieldVal,fieldMsg){
    return JSON.stringify({code:retCode,message:null,date:{fieldVal:fieldVal,fieldMsg:fieldMsg}});
}
/**
    登录
    input jsonstr
    return jsonstr
*/
function login_validate(jsonstr){
    var json1 = eval('(' + jsonstr + ')');
    if(!json1.account)
        return result(3,"account","账号不能为空");
    if(!json1.password)
        return result(3,"password","密码不能为空");
    return result(1,"null","null");
}


function checkPhone(json){
    var json1 = eval('(' + json + ')');
    if(!(/^1[3456789]\d{9}$/.test(json1.phone))){
        return false;
    }
    return true;
}

function specail(date){
    var spec = new RegExp("[`~!@#$^&*()=|{}':;',\\[\\].<>《》/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？]");
    if(spec.test(date))
        return false;
    return true;
}
function check(jsonObj){
    return jsonObj;
}