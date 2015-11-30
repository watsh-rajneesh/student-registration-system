var StudentRegApp = angular.module('StudentRegApp', ['ngCookies', 'ngRoute']);

StudentRegApp.config(['$httpProvider', function ($httpProvider) {
  $httpProvider.defaults.withCredentials = true;
  $httpProvider.defaults.headers.post['Content-Type'] = 'application/json';
}]);

StudentRegApp.config(function($routeProvider, $locationProvider) {
  $routeProvider
    .when('/', {
      templateUrl : '/docs/cmpe272/main.html',
      controller  : 'mainController',
      activenav   : 'homeTab'
    })
    .when('/register', {
      templateUrl : '/docs/cmpe272/register.html',
      controller  : 'RegCtrl',
      activenav   : 'regTab'
    })
    .when('/register-success', {
      templateUrl : '/docs/cmpe272/register-success.html',
      controller  : 'RegSuccessCtrl',
      activenav   : 'regTab'
    })
    .when('/enroll', {
      templateUrl : '/docs/cmpe272/enroll.html',
      controller  : 'EnrollmentCtrl',
      activetab   : 'courses',
      activenav   : 'enrollTab'
    })
    .when('/profile', {
      templateUrl : '/docs/cmpe272/enroll.html',
      controller  : 'EnrollmentCtrl',
      activetab   : 'yourCourses',
      activenav   : 'profTab'
    })
    ;
});


StudentRegApp.controller('mainController', function($scope) {
});


StudentRegApp.controller('RegCtrl', ['$scope', '$http',"$cookieStore", function($scope, $http, $cookieStore) {

    $scope.Submitform = function() {
      console.log('SubmitForm()');
      //$scope.username = $scope.user.firstName + $scope.user.lastName;
      $scope.username = $scope.user.email;
      $scope.studentInfo = '{ "user" : ' +
        '{ "firstName":"' + $scope.user.firstName + '","lastName":"' + $scope.user.lastName + '","emailId":"' + $scope.user.email + '","token":"' + $scope.user.token + '"' 
          +',"userName":"' + $scope.username +'","role": {"role": "STUDENT"}}}';
      console.log($scope.username);
      console.log($scope.studentInfo);

     $http.post('http://localhost:8080/api/v1.0/students/', $scope.studentInfo)
      .success( function(data, status, headers, config){
        console.log(data);
        console.log(status);
        $cookieStore.put('newUser', data);
        location.href="#register-success";
      })
      .error( function(data, status, headers, config){
        console.log(data);
        console.log(status);
        $scope.regStatus = "Registration failed. Please try again."; 

      });


    };

}]);

StudentRegApp.controller('RegSuccessCtrl', ['$scope', '$http',"$cookieStore", '$cookies', function($scope, $http, $cookieStore, $cookies) {

    $scope.createdUser=$cookieStore.get('newUser');
    $scope.username=$scope.createdUser.entity.user.userName;

    $scope.enroll = function() {      
     $http.head('http://localhost:8080/api/v1.0/students')
      .success( function(data, status, headers, config){
        console.log(status);
        console.log("below is the get");
        console.log(data);
        console.log(headers);
        console.log(config);       
        location.href="#enroll";
      })
      .error( function(data, status, headers, config){
        console.log(data);
        console.log(status);
        $scope.enStatus = "Oops! Something went wrong.";

      });
    };

}]);

StudentRegApp.controller('EnrollmentCtrl', ['$scope', '$http',"$cookieStore", '$route', '$cookies', function($scope, $http, $cookieStore, $route, $cookies) {

  $scope.$route=$route;


  $(document).ready(function(){
      $(".nav-tabs a").click(function(){
          $(this).tab('show');
      });
  });
/*
  $scope.getStudentProfile = function() {      
   $http.get('http://localhost:8080/api/v1.0/students')
    .success( function(data, status, headers, config){
      console.log(status);
      console.log(data);
      $scope.userProfile = data;
      console.log($scope.userProfile);
      $scope.URI="http://localhost:8080/api/v1.0/students/"+$scope.userProfile[0].id;
      console.log("printing out URI");
      console.log($scope.URI);
    })
    .error( function(data, status, headers, config){
      console.log(data);
      console.log(status);
      $scope.enStatus = "Oops! Something went wrong.";

    });
  };
*/

  var promise = $http.get('http://localhost:8080/api/v1.0/students')
    .success( function(data, status, headers, config){
      console.log(status);
      console.log(data);
      $scope.userProfile = data;
      console.log($scope.userProfile);
      $scope.URI="http://localhost:8080/api/v1.0/students/"+$scope.userProfile[0].id;
      console.log("printing out URI");
      console.log($scope.URI);
    })
    .error( function(data, status, headers, config){
      console.log(data);
      console.log(status);
      $scope.enStatus = "Oops! Something went wrong.";

    });

  $scope.loadClasses = function() {
    $scope.enStatus = "";    
    $scope.showme=true;  
    $http.get('http://localhost:8080/api/v1.0/courses')
      .success( function(data, status, headers, config){
        console.log(data);
        console.log(status);
        $scope.classes = data;
      })
      .error( function(data, status, headers, config){
        console.log(data);
        console.log(status);
      });
  };

  $scope.loadEnrolledClasses = function() { 
   $scope.enStatus = "";
   $scope.showme=false;  
   $http.get($scope.URI)
    .success( function(data, status, headers, config){
      console.log(data);
      console.log(status);
      $scope.enrolledClasses = data.courseRefs;
    })
    .error( function(data, status, headers, config){
      console.log(data);
      console.log(status);
    });
    };

  if ($route.current.activetab == 'courses')
  {
    // $scope.getStudentProfile();
    // $scope.loadClasses();
      promise.then(
       function(){
      $scope.loadClasses();
    });
  }
  else
  {
    // $scope.getStudentProfile();
    // $scope.loadEnrolledClasses();
          promise.then(
       function(){
      $scope.loadEnrolledClasses();
    });
  }

  $scope.enroll = function() { 
    $scope.enrollInfo = '{ "user" : ' +
    '{ "emailId":"' + $scope.userProfile[0].user.emailId + '","userName":"' + $scope.userProfile[0].user.userName + '","token":"' + $scope.userProfile[0].user.token + '","firstName":"' + $scope.userProfile[0].user.firstName + '"' 
    +',"lastName":"' + $scope.userProfile[0].user.lastName +'","role": {"role": "STUDENT"}}, "courseRef" : { "enrollCourseName":"' + $scope.data.classSelect.toString()+ '" }}';

    console.log($scope.enrollInfo);
       
    $http.put($scope.URI, $scope.enrollInfo )
    .success( function(data, status, headers, config){
      console.log(data);
      console.log(status);
      $scope.enStatus = "Enrollment Success.";
    })
    .error( function(data, status, headers, config){
      console.log(data);
      console.log(status);
      $scope.enStatus = "Enrollment Failed. Please try again.";
    });
  };

  $scope.unenroll = function() { 
    $scope.unEnrollInfo = '{ "user" : ' +
    '{ "emailId":"' + $scope.userProfile[0].user.emailId + '","userName":"' + $scope.userProfile[0].user.userName + '","token":"' + $scope.userProfile[0].user.token + '","firstName":"' + $scope.userProfile[0].user.firstName + '"' 
    +',"lastName":"' + $scope.userProfile[0].user.lastName +'","role": {"role": "STUDENT"}}, "courseRef" : { "unEnrollCourseName":"' + $scope.data.classSelect.toString() + '" }}';

    console.log($scope.unEnrollInfo);

    $http.put($scope.URI, $scope.unEnrollInfo)
    .success( function(data, status, headers, config){
      console.log(data);
      console.log(status);
      $scope.loadEnrolledClasses();
      $scope.enStatus = "Unenrollment Success.";
    })
    .error( function(data, status, headers, config){
      console.log(data);
      console.log(status);
      $scope.enStatus = "Unenrollment Failed. Please try again.";
    });
  };

  }]);
