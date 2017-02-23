/**
 * Created by 露青 on 2016/11/21.
 */
var app = angular.module("ttDirectives", []);

app.directive('ttBooks', [function() {
    return {
        replace: true,
        templateUrl: 'books-template.html',
        transclude: true
    };
}]).directive('ttBook', [function() {
    return {
        scope: {
            book: '=book'
        },
        templateUrl: 'book-template.html',
        replace: true,
        transclude: true
    };
}]);

/**
 * http事件时显示“加载中”
 */
app.directive('loading', ['$http', function ($http) {
    return {
        template: '<div ng-show="loading"><div class="weui-mask_transparent"></div><div class="weui-toast"><i class="weui-loading weui-icon_toast"></i><p class="weui-toast__content">数据加载中</p></div></div>',
        restrict: 'E',
        scope : {},
        controller : function($scope){
            $scope.show = function () {
                $scope.loading = true;
            }

            $scope.hide = function () {
                $scope.loading = false;
            }
        },
        link: function (scope) {
            scope.isLoading = function () { //绘制loading的函数
                return $http.pendingRequests.length > 0;
            };
            scope.$watch(scope.isLoading, function (v) {
                if (v) {//检测是否已经加载如果没有，则显示
                    scope.show();
                } else {//如果加载完成，则隐藏。
                    scope.hide();
                }
            });
        }
    }
}])