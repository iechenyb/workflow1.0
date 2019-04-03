var app = angular.module('app',[]);
app.controller('controller',main);
var page ;	    
var basePath ;   
var pushUrl;
var http;

function main($scope,$q,$http) {
	page=$scope;
	http = $http;
	basePath = $("#basePath").val();
	$.ajax({
		 url:basePath+"jbpm/lst.htm",
		 async:false,
		 success:function (data){
			 if(data!='noLogin'){
				 $scope.lst = data[0].data;
				 $scope.lst2 = data[0].data2;
				 $scope.lst3 = data[0].data3;
			 };
		 }
	 });
}