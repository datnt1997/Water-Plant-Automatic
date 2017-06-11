const PORT = 3484;									//Đặt địa chỉ Port được mở ra để tạo ra chương trình mạng Socket Server

var http = require('http') 							//#include thư viện http - Tìm thêm về từ khóa http nodejs trên google nếu bạn muốn tìm hiểu thêm. Nhưng theo kinh nghiệm của mình, Javascript trong môi trường NodeJS cực kỳ rộng lớn, khi bạn bí thì nên tìm hiểu không nên ngồi đọc và cố gắng học thuộc hết cái reference (Tài liêu tham khảo) của nodejs làm gì. Vỡ não đó!
var socketio = require('socket.io')				    //#include thư viện socketio

var ip = require('ip');
var app = http.createServer();					//#Khởi tạo một chương trình mạng (app)
var io = socketio(app);	
var async = require("async");						//#Phải khởi tạo io sau khi tạo app!
app.listen(PORT);								// Cho socket server (chương trình mạng) lắng nghe ở port 3484
console.log("Server nodejs chay tai dia chi: " + ip.address() + ":" + PORT)

var id = false;

// tạo và kết nối với mongodb
var mongodb = require('mongodb');
 
var MongoClient = mongodb.MongoClient;
var url = 'mongodb://localhost:27017/WPA';
 
 
MongoClient.connect(url, function (err, db) {
  if (err) {
    console.log('Unable to connect to the mongoDB server. Error:', err);
  } else {
    //HURRAY!! We are connected. :)
    console.log('Connection established to', url);
    user = db.collection('users_login');
    water_data = db.collection('data_water');
    plant_data = db.collection('data_plant');
    date = db.collection('dateTime');
    manual_status = db.collection('manual_status')

  }
});

// var json3 = {
// 			"BumperEnable": "1" 
// 			}
// var json4 = {
// "BumperEnable": "0" 
// }
//Khi có một kết nối được tạo giữa Socket Client và Socket Server
io.on('connection', function(socket) {	
	//hàm console.log giống như hàm Serial.println trên Arduino
    console.log("Connected"); //In ra màn hình console là đã có một Socket Client kết nối thành công.
	
     //định nghĩa một mảng 1 chiều có 2 phần tử: true, false. Mảng này sẽ được gửi đi nhằm thay đổi sự sáng tắt của 2 con đèn LED đỏ và xanh. Dựa vào cài đặt ở Arduino mà đèn LEd sẽ bị bật hoặc tắt. Hãy thử tăng hoạt giảm số lượng biến của mảng led này xem. Và bạn sẽ hiểu điều kỳ diệu của JSON!
	
	//Tạo một chu kỳ nhiệm vụ sẽ chạy lại sau mỗi 200ms
	
	
    var json3 = {
            "BumperEnable": "1" 
            };
    var json4 = {
            "BumperEnable": "0" 
            };

    //cu 3s thi gui request mot lan-----------------------------------------------------------------------------------------------
	// var interval2 = setInterval(function() {
	// 		var json2 = {
	// 		"Request": "SendData" //có một phần tử là "led", phần tử này chứa giá trị của mảng led.
	// 	}
	// 	var json3 = {
	// 		"BumperEnable": "1" 
	// 		}
	// 		var json4 = {
	// 		"BumperEnable": "0" 
	// 		}
	// 	socket.on('LED_STATUS', function(status) {
	// 	//Nhận được thì in ra thôi hihi.
	// 	console.log("recv LED", status)
	// 	// var myObj = JSON.parse(status);
	// 	var chisoSensor= Number(status["Sensor value:"]);
			
	// 	if (chisoSensor > 400){
	// 		id = true;
	// 		socket.emit('BumperEnable', json3) ;
	// 		console.log("bat may bom");
	// 	}
	// 	else {
	// 		id = false;
	// 		socket.emit('BumperEnable', json4) ;
	// 		console.log("tat may bom");		
	// 	}
	// })
	// 	if(id == true ){
	// 		socket.emit('Request', json3) //Gửi lệnh LED với các tham số của của chuỗi JSON
	// 		console.log("send Request")//Ghi ra console.log là đã gửi lệnh LED
	// 	}else if(id == false ){
	// 		socket.emit('Request', json4) //Gửi lệnh LED với các tham số của của chuỗi JSON
	// 		console.log("send Request")//Ghi ra console.log là đã gửi lệnh LED
	// 	}

	// 	}, 3000)
	//----------------------------------------------------------------------------------------------------------------------------
	//	Khi nhận được request manual

var today, dow, start, stop, countDownStop, countDownStart;

   async.series([
    function(callback){
            today = new Date();
            var dd = today.getDate();
            var mm = today.getMonth()+1; //January is 0!
            var yyyy = today.getFullYear();
            var weekday = new Array(7);
    weekday[0] = "Sunday";
    weekday[1] = "Monday";
    weekday[2] = "Tuesday";
    weekday[3] = "Wednesday";
    weekday[4] = "Thursday";
    weekday[5] = "Friday";
    weekday[6] = "Saturday";

    dow = weekday[today.getDay()];

            if(dd<10) {
                dd='0'+dd
            } 

            if(mm<10) {
                mm='0'+mm
            } 

            today = mm+'/'+dd+'/'+yyyy;
            console.log(today);
            console.log(dow);
            callback(null);
    },
    function(callback){
           var cursor = date.find({id:0});
    cursor.each(function (err, doc) {
      if (err) {
        console.log(err);
      } else {
         if(doc != null){
           start = doc.startTime;
           stop = doc.stopTime;
           countDownStop = new Date(stop).getTime();
           countDownStart = new Date(start).getTime();
            console.log("gio");
         }
      }
     });
            
            callback(null);
    }],
    function(error, results){
        if(error){
            console.log(error.toString());
        }else{
            console.log(results);
        }       
    }
);

// Update the count down every 1 second
var x = setInterval(function() {

	var json3 = {
			"BumperEnable": "1" 
			};
	var json4 = {
	"BumperEnable": "0" 
	}

    // Get todays date and time
    var now = new Date().getTime();
    
    // Find the distance between now an the count down date
    var distancestart = new Date(countDownStop - countDownStart).getTime();
    var distance = countDownStop - now;
    
    // Time calculations for days, hours, minutes and seconds
    var days = Math.floor(distance / (1000 * 60 * 60 * 24));
    var hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
    var minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
    var seconds = Math.floor((distance % (1000 * 60)) / 1000);
    
    

    var days1 = Math.floor(distancestart / (1000 * 60 * 60 * 24));
    var hours1 = Math.floor((distancestart % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
    var minutes1 = Math.floor((distancestart % (1000 * 60 * 60)) / (1000 * 60));
    var seconds1 = Math.floor((distancestart % (1000 * 60)) / 1000);

    console.log(days + " : "+ hours  + " : "+ minutes + " : " +seconds);
    
    if (days <= 0 && hours <= 0 && minutes <= 0 &&seconds <= 0) {
        console.log('tat may bom');
        socket.emit('Request', json4) //Gửi chuỗi JSON để bật máy bơm
        setTimeout(function () {
		  clearInterval(x);
		}, 8000)

    }
    if((days == days1 && hours == hours1 && minutes <= minutes1 &&seconds <= seconds1) && (days >= 0 && hours >= 0 && minutes >= 0 &&seconds >= 0)){
    	console.log('bat may bom');
    	socket.emit('Request', json3) //Gửi chuỗi JSON để tắt máy bơm
    	
    }
}, 1000);








	
	//----------------------------------------------------------------------------------------------------------------------------

	// socket.on('login', function (username, password) {
 //    console.log(username + "login");
 
 //    var cursor = user.find({username:username});
 //    cursor.each(function (err, doc) {
 //      if (err) {
 //        console.log(err);
 //        socket.emit('login', false);
 //      } else {
 //         if(doc != null){
 //             if(doc.password == password){
 //                 socket.emit('login', true);
 //                 			socket.emit('userob', doc);       
 //             }else{
 //                 socket.emit('login', false);
 //             }
 
 //         }
 //      }
 //     });
 
 //  });

var interval5 = setInterval(function() {
	socket.on("manual", () => {
	    socket.emit("Request", json3)
	    console.log(json3)
	    console.log("Server chay che do manual")
	})
}, 1000);




    
 // var interval5 = setInterval(function() {

    //         var cursor = manual_status.find({name:"manual"});
    // cursor.each(function (err, doc) {
    //   if (err) {
    //     console.log(err);
    //   } else {
    //      if(doc != null){
    //          if(doc.status == "enable"){
    //               socket.emit('Request', json3) //Gửi lệnh LED với các tham số của của chuỗi JSON
    //      console.log(doc.status)       
    //          }else{
    //              socket.emit('Request', json4) //Gửi lệnh LED với các tham số của của chuỗi JSON
    //      console.log(doc.status)
    //          }
 
    //      }
    //   }
    //  });
 
     // if(manual_status.status == "enable"){
     //     socket.emit('Request', json3) //Gửi lệnh LED với các tham số của của chuỗi JSON
     //     console.log("send Request")//Ghi ra console.log là đã gửi lệnh LED
     // }else if(manual_status.status == "disable"){
     //     socket.emit('Request', json4) //Gửi lệnh LED với các tham số của của chuỗi JSON
     //     console.log("send Request")//Ghi ra console.log là đã gửi lệnh LED
     //    clearInterval(interval5);
     // }
     //}, 1000)



	// socket.on('settime', function (date, startTime, stopTime, dow ) {
 //    console.log("1 item added");
 //    var startfull = date + " " + startTime;
 //    var stopfull = date + " " + stopTime;
 
 //    var setTimeOb = {date: date, startTime: startfull, stopTime: stopfull, dow: dow};
 
 //    date.insert(setTimeOb, function (err, result) {
 //      if (err) {
 //         console.log(err);
 //         socket.emit('settime', false);
 //      } else {
 //          console.log('Inserted new setTime ok');
 //          socket.emit('settime', true);
 //      }
 //      });
 //  });

	//Khi socket client bị mất kết nối thì chạy hàm sau.
	socket.on('disconnect', function() {
		console.log("disconnect") 	//in ra màn hình console cho vui
	// 	clearInterval(interval1)		//xóa chu kỳ nhiệm vụ đi, chứ không xóa là cái task kia cứ chạy mãi thôi đó!
	// })
})

// io.on('connected to android', function(socket) {

// 	//Khi socket client bị mất kết nối thì chạy hàm sau.
// 	socket.on('disconnect', function() {
// 		console.log("disconnect") 	//in ra màn hình console cho vui
// 		clearInterval(interval1)		//xóa chu kỳ nhiệm vụ đi, chứ không xóa là cái task kia cứ chạy mãi thôi đó!
// 	})

	

});