/**
 * Created by 露青 on 2016/11/21.
 */
var app = angular.module('ttCtrls', ['ttServices']);

/**
 * 个人信息展示及修改
 */
app.controller('MyInfoCtrl',['$scope','$location','TeacherService','Alert',
  function($scope, $location, teacher, Alert){
    teacher.getInfo().then(function(response){
        var data = response.data.ret;
        $scope.vm = data;
        $scope.vm.school_info = [data.bureau.province_name,data.bureau.city_name,data.bureau.name,data.school.name].join("  ");
        var grades = [];
        angular.forEach(data.grades, function(grade){
          if(grades.indexOf(grade.grade_name) == -1)
        	   grades.push(grade.grade_name);
        })
        $scope.vm.gradeInfo = grades.join(",");
        $scope.vm.changeInfo = false;
        $scope.vm.submit = function(){
            if(!$scope.vm.changeInfo)
                return;
            teacher.saveInfo({name:$scope.vm.teacher.name, mobile: $scope.vm.teacher.mobile})
                .then(function () {
                    Alert.show('success', '个人信息更新成功！');
                    $scope.vm.changeInfo = false;
                })
        }

        $scope.vm.toEditGrade = function(){
            $location.search({from : $location.path()}).path('/my.editInfo');
        }

        $scope.$watch('vm.teacher', function(newValue,oldValue){
            if(newValue && !angular.equals(newValue, oldValue))
                $scope.vm.changeInfo = true;
        }, true);
    })
  }
]);

/**
 * 修改年级
 */
app.controller('MyGradesCtrl',['$scope','$timeout','$location','$cacheFactory','TeacherService','Alert',
	function($scope, $timeout, $location, $cacheFactory, teacher, Alert){
        var from = $location.search().from;
        $scope.grades = $cacheFactory.get('ttLibraryCache').get('preGrades');
        $scope.selected = [];
        $scope.changeGrade = false;//是否改变了年级
        $scope.isSelected = function(id){
            return $scope.selected.indexOf(id) > -1;
        };

        $scope.updateSelection = function($event, id){
            var checkbox = $event.target;
            var action = (checkbox.checked?'add':'remove');
            updateSelected(action, id);

            function updateSelected(action, id){
                if(action == 'add' && $scope.selected.indexOf(id) == -1)
                    $scope.selected.push(id);
                if(action == 'remove' && $scope.selected.indexOf(id)!=-1){
                    var idx = $scope.selected.indexOf(id);
                    $scope.selected.splice(idx,1);
                }
                $scope.changeGrade = true;
            }
        }
        teacher.getGrades().then(function(data){
            angular.forEach(data, function (grade) {
                $scope.selected.push(grade.id);
            })
        });

        $scope.submit = function(){
            if(!$scope.changeGrade)
                return;
            if($scope.selected.length < 1){
                Alert.show('danger', '请选择年级！')
            } else {
                teacher.saveInfo({grades : $scope.selected.join()}).then(function(){
                    Alert.show('success', '个人信息更新成功！');
                    $scope.changeGrade = false;
                    $timeout(function(){
                        if(from){
                            $location.path(from).search({});
                        }
                    }, 1000);
                });
            }
        }
	}
]);

/**
 * 我的推荐
 */
app.controller('MyRecommendCtrl',['$scope', '$location', 'TeacherService', 'Alert', 'Modals',
    function($scope, $location, teacher, Alert, Modals){
        var vm = $scope.vm = {};
        var allCourses;
        vm.selectTab = 0;
        vm.grades = [];
        //查看评论
        vm.showRecommend = function(index){
          vm.selectTab = index;
        };
        //展开课目
        vm.expandCourse = function(course){
            if(angular.isDefined(course.recomends))
                return;
            teacher.getMyRecommends(course.lesson.id).then(function (data) {
                course.recomends = data.list;
                //推荐的图书的操作：打开/收起评论
                angular.forEach(course.recomends, function (recomend) {
                    recomend.ctrl = {
                        //是否显示评论
                        bShowedComments : false,
                        //toggle显示评论
                        toggleShowComments : function(){
                            this.bShowedComments = !this.bShowedComments;
                        },
                        //添加评论
                        toAddComment : function () {
                            var a = Modals.openNewComment(course.lesson.id, recomend.id);
                            var ctrl = this;
                            a.result.then(function (ret) {
                                recomend.comments.unshift(ret.comment);
                                console.log(recomend.comments);
                                ctrl.bShowedComments = true;
                            });
                        }
                    };
                })
            }).then(function () {
                console.log(course.recomends);
            })
        }

        //课目列表-按照是否推荐分组
        teacher.getCoursesGroupByHasRecommends().then(function (data) {
            allCourses = teacher.buildCourses(data);
            return teacher.getGrades(true);
        }).then(function (data) {
            angular.forEach(data, function (grade) {
                vm.grades.push({name: grade.name, id:grade.id});
            });
            if(vm.grades.length > 0){
                vm.selectedGrade = vm.grades[0];
            } else {
                $location.search({from : $location.path()}).path('/my.editInfo');
            }
        });

        //观察年级selector和是否推荐tab
        $scope.$watch('vm.selectedGrade.id + vm.selectTab', function(){
            if(angular.isUndefined(vm.selectedGrade) || angular.isUndefined(allCourses))
                return;
            vm.courses = [];
            angular.forEach(allCourses, function (course) {
                if(vm.selectedGrade.id == course.grade){
                    if((course.recommendsNum > 0 && vm.selectTab == 0) || (vm.selectTab == 1 && course.recommendsNum == 0))
                        vm.courses.push(course);
                }
            });
        })
    }
]);

/**
 * 查询教师
 */
app.controller('TeachersCtrl', ['$scope', '$timeout', 'SearchService', function ($scope, $timeout, searcher) {
    $scope.department = {
        showed : false,
        clause : null,
        result : null,
        search : function () {
            this.showed = true;
        },
        complete : function (data) {
            this.clause = data;
            this.result = [data.province.value, data.city.value,data.bureau.value,data.school.value].join(',');
            this.showed = false;
            find(data.school.key, $scope.query.name);
        }
    };

    $scope.query = {
        name : '',
        result : []
    };

    //存储教师信息 key :department, value : []
    $scope.cache = {};

    var timeout;

    function find(departmentId, name){
        if(name == ''){
            if(angular.isUndefined($scope.cache[departmentId])){
                searcher.teacher(departmentId).then(function (data) {
                    $scope.query.result = data;
                    $scope.cache[departmentId] = data;
                });
            } else {
                $scope.query.result = $scope.cache[departmentId];
            }
        } else {
            $scope.query.result = [];
            angular.forEach($scope.cache[departmentId], function (teacher) {
                if(teacher.name.indexOf(name) > -1)
                    $scope.query.result.push(teacher);
            })
        }
    }

    $scope.$watch('query.name', function (newName) {
        var _department = $scope.department.clause;
        if(_department != null){
            if(timeout)
                $timeout.cancel(timeout);
            timeout = $timeout(function () {
                find(_department.school.key, newName);
            },350);
        }
    })
}]);


/**
 * 查看教师
 */
app.controller('TeacherCtrl', ['$scope', '$location', '$routeParams','TeacherService', function ($scope, $location, $routeParams, teacher) {
    var teacherId = $routeParams.teacherId;
    teacher.getRecommends(teacherId).then(function (data) {
        $scope.data = data;
        angular.forEach(data, function (recomend) {
            recomend.bShowedMaterial = false;
            recomend.toggleMaterial = function () {
                this.bShowedMaterial = !this.bShowedMaterial;
            }
        });
    })
}])


/**
 * 我的课程
 */
app.controller('MyCourseCtrl',['$scope',function($scope){
    //$scope.showed = false;
    //$scope.toggle = function () {
    //    $scope.showed = !$scope.showed;
    //}
    //$scope.complete = function (data) {
    //    console.log('in controller', data);
    //    $scope.clause = data;
    //}

}]);


