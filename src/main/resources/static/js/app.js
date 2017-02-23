/**
 * Created by 露青 on 2016/11/21.
 */
var app = angular.module("ttLibrary",
    ["ngRoute","ngAnimate", "ttCtrls","ttDirectives","ttComponents","ui.bootstrap"]);
//http
app.config(function($httpProvider) {
    $httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=utf-8';
    var param = function(obj) {
        var query = '', name, value, fullSubName, subName, subValue, innerObj, i;

        for(name in obj) {
            value = obj[name];

            if(value instanceof Array) {
                for(i=0; i<value.length; ++i) {
                    subValue = value[i];
                    fullSubName = name + '[' + i + ']';
                    innerObj = {};
                    innerObj[fullSubName] = subValue;
                    query += param(innerObj) + '&';
                }
            }
            else if(value instanceof Object) {
                for(subName in value) {
                    subValue = value[subName];
                    fullSubName = name + '[' + subName + ']';
                    innerObj = {};
                    innerObj[fullSubName] = subValue;
                    query += param(innerObj) + '&';
                }
            }
            else if(value !== undefined && value !== null)
                query += encodeURIComponent(name) + '=' + encodeURIComponent(value) + '&';
        }

        return query.length ? query.substr(0, query.length - 1) : query;
    };
    $httpProvider.defaults.transformRequest = [function(data) {
        return angular.isObject(data) && String(data) !== '[object File]' ? param(data) : data;
    }];
});

app.config(function($routeProvider){
    $routeProvider.when("/my.info" , {
        templateUrl : "my/info.html",
        controller : "MyInfoCtrl"
    }).when("/my.editInfo", {
        templateUrl : "my/editInfo.html",
        controller : "MyGradesCtrl"
    }).when("/my.recommend", {
        templateUrl : "my/recommend.html",
        controller : "MyRecommendCtrl"
    }).when("/my.course" , {
        templateUrl : "my/course.html",
        controller : "MyCourseCtrl"
    }).when("/they.teachers" , {
        templateUrl : "they/teachers.html",
        controller : "TeachersCtrl"
    }).when("/teacher/:teacherId", {
        templateUrl:"they/teacher.html",
        controller : "TeacherCtrl"
    })
        .otherwise({
        redirectTo : "/my.info"
    })
});

app.run(['$cacheFactory',function($cacheFactory){
  var cache = $cacheFactory('ttLibraryCache');
  cache.put('preGrades', [{name:'一年级', id: 1},{name:'二年级', id: 3},{name:'三年级', id: 5},{name:'四年级', id: 7},{name:'五年级', id: 9},{name:'六年级', id: 11}]);
}]);