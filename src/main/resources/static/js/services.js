/**
 * Created by 露青 on 2016/11/21.
 */
var app = angular.module('ttServices',[]);

app.factory('CommonService', ['$http','$q',function($http, $q){
	return {
		/**
		 * 获取全部年级信息
		 */
		getGrades : function(){
			 return $http.get('/preGrades',{cache :true}).then(function(response){
			 	var result = [];
			 	angular.forEach(response.data._embedded.preGrades, function(grade){
			 		result.push({name : grade.name, id : grade.code});
			 	})
			 	return result;
			 })
		},

		getDepartment : function(type, param){
			var map = {
				'province' : {path :'/search/provinces'},
				'city' : {path :'/search/cities', params: {province: param}},
				'bureau' : {path :'/search/bureaus', params: {city: param}},
				'school' : {path :'/search/schools' , params: {bureau : param}}
			}
			var conf = {cache : true};
			if(map[type].params)
				conf.params = map[type].params;
			return $http.get(map[type].path, conf).then(function (response) {
				var items = response.data.ret.list;
				angular.forEach(items, function (item) {
					item.type = type;
				});
				return items;
			})

		}
	}
}]);

app.factory('TeacherService', ['$http',function($http) {
	return {
		userInfo : function () {
			return $http.get('/teacher/userInfo',{cache :true}).then(function (response) {
				return response.data.ret.user;
			})
		},
		getInfo : function(){
			return $http.get('/teacher/info');
		},
		saveInfo : function(data){
			return $http.post('/teacher/save', data);
		},
		saveComment : function(data){
			return $http.post('/teacher/doComment', data).then(function (response) {
				return response.data.ret;
			});
		},
		getGrades : function (fullModel) {
			return $http.get('/teacher/grades').then(function (response) {
				var grades = [];
				angular.forEach(response.data.ret.list, function (grade) {
					if(fullModel || grade.id%2==1)
						grades.push({id:grade.id, name:grade.name, grade_name : grade.grade_name});
				})
				return grades;
			});
		},
		getCoursesGroupByHasRecommends : function () {
			return $http.get('/teacher/myCoursesGroupByHasRecommends').then(function (response) {
				return response.data.ret;
			});
		},
		getMyRecommends : function(materialId){
			return $http.get('/teacher/myRecommends',{params:{materialId: materialId}})
				.then(function (response) {
					return response.data.ret;
				});
		},

		getRecommends : function (teacherId) {
			return $http.get('/teacher/recommends', {params :{teacherId : teacherId}})
				.then(function (response) {
					var books = response.data.ret.list;
					//TODO 载入课本信息
					angular.forEach(books, function (book) {
						book.materials = [];
						angular.forEach(book.comments, function (comment) {
							if(comment.teacher_id == teacherId && comment.type == 0){
								$http.get('/search/materail', {params:{id : comment.material_id}}).then(function (response) {
									var material = response.data.ret;
									material.recommend_at = comment.created_at;
									material.reason = comment.content;
									book.materials.push({material : material});
								})
							}
						})
					});
					return books;
				})
		},

		//根据getCoursesGroupByHasRecommends构建数据模型
		buildCourses : function(data){
			var lessons = [];
			angular.forEach(data.list, function(course){
				var grade = course.grade;
				var gradeId = grade.gradeInfo.id;
				angular.forEach(grade.with.concat(grade.without), function(lesson){
					lessons.push({grade:gradeId,
						lesson : {name : lesson.lesson_name,
							number : lesson.lesson_number,
							id : lesson.id,
							author : lesson.author},
						recommendsNum : lesson.recomendsNum
					})
				});
			});
			return lessons;
		}
	}
}]);


app.factory('SearchService', ['$http', function ($http) {

	return {
		teacher : function (departmentId) {
			var params = {params : {department :departmentId}};
			//stats : [ [ 0, 11, 2 ], [ 1, 11, 37 ], [ 1, 12, 1 ] ] => type,teacher_id, num
			var addCommentStat = function (teacher, stats) {
				angular.forEach(stats, function (stat) {
					if(stat[1] == teacher.id){
						if(stat[0] == 1)
							teacher.commentNum = stat[2];
						if(stat[0] == 0)
							teacher.recommendNum = stat[2];
					}
				})
			};

			return $http.get('/search/teacher', params).then(function (response) {
				return response.data.ret.list;
			}).then(function (teachers) {
				return $http.get('/search/comment/stat', params).then(function (response) {
					angular.forEach(teachers, function (teacher) {
						addCommentStat(teacher, response.data);
					})
					teachers.sort(function (a,b) {
						function v(x){
							return x == undefined ? 0 : x;
						}
						return v(b.recommendNum)-v(a.recommendNum) || v(b.commentNum)-v(a.commentNum);
					})
					return teachers;
				});
			});
		}
	}
}]);

/**
 * 模态框
 */
app.factory('Modals',['$uibModal','TeacherService', function ($uibModal, teacher) {
	return {
		openNewComment : function (lessonid, bookid) {
			return $uibModal.open({
				templateUrl:"book-comment.html",
				controller: function($scope) {
					$scope.max = 200;
					$scope.content = '';
					$scope.submit = function () {
						console.log($scope.content);
						console.log($scope.content.length);
						teacher.saveComment({content: $scope.content, materialid : lessonid, bookid : bookid}).then(function (comment) {
							return teacher.userInfo().then(function (user) {
								comment.teacher_info = user;
								return comment;
							});
						}).then(function (data) {
							$scope.$close({comment : data});
						})
					};
					$scope.cancel = function () {
						$scope.$dismiss({content : $scope.content = ''});
					}
				}
			});
		}
	}
}])

/**
 * 消息提示
 */
app.factory('Alert', [
  '$rootScope', '$timeout', function($rootScope, $timeout) { 
    var alertService;
    $rootScope.alert = {};
    return alertService = {
      show: function(type, msg, timeout) {
		  timeout = timeout||1500;
		  $rootScope.alert.type = type;
      	  $rootScope.alert.msg = msg;
		  $rootScope.alert.showed = true;
      	  $rootScope.alert.close = function(){
      		alertService.close();
		  }
		  $timeout(function(){
			  alertService.close();
		  }, timeout);
	  },
      close: function() {
        $rootScope.alert = {};
      }
    };
  }
]);