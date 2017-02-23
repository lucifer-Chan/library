/**
 * Created by 露青 on 2016/12/14.
 */
var app = angular.module('ttComponents',[]);


app.component('schoolSelector', {
    templateUrl: 'school-selector.html',
    bindings: {
        showed: '=',
        complete : '&',
        initData : '='
    },
    controller: function(CommonService) {
        var types = ['province', 'city', 'bureau', 'school'];
        var ctrl = this;
        ctrl.selected = [];
        ctrl.close = function () {
            ctrl.showed = false;
        }

        //如果有初始数据
        if(ctrl.initData){
            ctrl.selected.push(ctrl.initData.province);
            ctrl.selected.push(ctrl.initData.city);
            ctrl.selected.push(ctrl.initData.bureau);
            CommonService.getDepartment('school', ctrl.initData.bureau.key).then(function (schools) {
                ctrl.forselect = schools;
            });
        } else {
            CommonService.getDepartment('province').then(function (provinces) {
                ctrl.forselect = provinces;
            });
        }
        //选择组织
        ctrl.select = function (item) {
            removeFromSelected(item);
            if(item.type == 'school'){
                ctrl.complete({data: {province : hasOne('province'), city : hasOne('city'), bureau : hasOne('bureau'), school : item}});
                //ctrl.close();
            } else {
                ctrl.selected.push(item);
                CommonService.getDepartment(childType(item.type), item.key).then(function (departments) {
                    ctrl.forselect = departments;
                });
            }
        }
        //重选组织
        ctrl.reselect = function (item) {
            var type = item.type;
            for(var idx = types.indexOf(type); idx < types.length; idx ++){
                removeFromSelected(types[idx]);
            }
            var parent = hasOne(parentType(type));
            var parentKey = parent ? parent.key : null;
            CommonService.getDepartment(type, parentKey).then(function (department) {
                ctrl.forselect = department;
            });
        }

        /**
         * selected 中是否含有与输入参数相同等级的组织
         * @param item 可以是一个对象，也可以是一个字符串
         * @return 相同等级的那个组织|null
         */
        function hasOne(item){
            var result = null;
            angular.forEach(ctrl.selected, function (one) {
                var type = angular.isObject(item) ? item.type : item;
                if(one.type == type)
                    result = one;
            })
            return result;
        }

        function childType(type){
            var idx = types.indexOf(type) + 1;
            return idx > types.length ? type : types[idx];
        }

        function parentType(type){
            var idx = types.indexOf(type) - 1;
            return idx < 0 ? type : types[idx];
        }

        function removeFromSelected(item){
            var department = hasOne(item);
            if(department){
                var idx = ctrl.selected.indexOf(department);
                ctrl.selected.splice(idx,1);
            }
        }
    }
});