var TUTORIAL_SAVVY ={          
	loadStudentData : function(){
		var formattedstudentListArray =[];	    
	    $.ajax({      
	    	async: false,       
	    	url: "StudentJsonDataServlet",
	    	dataType:"json",       
	    	success: function(studentJsonData) {  
	    		console.log(studentJsonData);      
	    		$.each(studentJsonData,function(index,aStudent){        
	    			formattedstudentListArray.push([aStudent.mathematicsMark,aStudent.computerMark,aStudent.historyMark,aStudent.litratureMark,aStudent.geographyMark]);
	    		});
	    	}
	    });
	    return formattedstudentListArray;
	},
	createChartData : function(jsonData){
		console.log(jsonData);
		return {     
			labels : ["Mathematics", "Computers", "History","Literature", "Geography"],     
			datasets : [{
				fillColor : "rgba(255,0,0,0.3)",       
				strokeColor : "rgba(0,255,0,1)",       
				pointColor : "rgba(0,0,255,1)",	       
				pointStrokeColor : "rgba(0,0,255,1)",
				data : jsonData[0]
			}]
		};
	},
	renderStudenrRadarChart:function(radarChartData){
		var context2D = document.getElementById("canvas").getContext("2d"),    
	    myRadar = new Chart(context2D).     
	    	Radar(radarChartData,{
	    		scaleShowLabels : false,        
	    		pointLabelFontSize : 10
	        });
	    return myRadar;
	},
	initRadarChart : function(){
		var studentData = TUTORIAL_SAVVY.loadStudentData();    
	    chartData = TUTORIAL_SAVVY.createChartData(studentData);     
	    radarChartObj = TUTORIAL_SAVVY.renderStudenrRadarChart(chartData);     
	}
};
 
$(document).ready(function(){   
	TUTORIAL_SAVVY.initRadarChart();
});