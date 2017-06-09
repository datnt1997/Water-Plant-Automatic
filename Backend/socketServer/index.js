const PORT = 3484;									//Đặt địa chỉ Port được mở ra để tạo ra chương trình mạng Socket Server

var http = require('http') 							//#include thư viện http - Tìm thêm về từ khóa http nodejs trên google nếu bạn muốn tìm hiểu thêm. Nhưng theo kinh nghiệm của mình, Javascript trong môi trường NodeJS cực kỳ rộng lớn, khi bạn bí thì nên tìm hiểu không nên ngồi đọc và cố gắng học thuộc hết cái reference (Tài liêu tham khảo) của nodejs làm gì. Vỡ não đó!
var socketio = require('socket.io')				    //#include thư viện socketio

var ip = require('ip');
var app = http.createServer();					//#Khởi tạo một chương trình mạng (app)
var io = socketio(app);							//#Phải khởi tạo io sau khi tạo app!
app.listen(PORT);								// Cho socket server (chương trình mạng) lắng nghe ở port 3484
console.log("Server nodejs chay tai dia chi: " + ip.address() + ":" + PORT)

var id = false;

//Khi có một kết nối được tạo giữa Socket Client và Socket Server
io.on('connection', function(socket) {	
	//hàm console.log giống như hàm Serial.println trên Arduino
    console.log("Connected"); //In ra màn hình console là đã có một Socket Client kết nối thành công.
	
	var led = [true, false] //định nghĩa một mảng 1 chiều có 2 phần tử: true, false. Mảng này sẽ được gửi đi nhằm thay đổi sự sáng tắt của 2 con đèn LED đỏ và xanh. Dựa vào cài đặt ở Arduino mà đèn LEd sẽ bị bật hoặc tắt. Hãy thử tăng hoạt giảm số lượng biến của mảng led này xem. Và bạn sẽ hiểu điều kỳ diệu của JSON!
	
	//Tạo một chu kỳ nhiệm vụ sẽ chạy lại sau mỗi 200ms
	var interval1 = setInterval(function() {
		//đảo trạng thái của mảng led, đảo cho vui để ở Arduino nó nhấp nháy cho vui.
		for (var i = 0; i < led.length; i++) {
			led[i] = !led[i]
		}
		
		//Cài đặt chuỗi JSON, tên biến JSON này là json 
		var json = {
			"led": led //có một phần tử là "led", phần tử này chứa giá trị của mảng led.
		}
		// socket.emit('LED', json) //Gửi lệnh LED với các tham số của của chuỗi JSON
		// console.log("send LED")//Ghi ra console.log là đã gửi lệnh LED
	}, 1000)//1000ms
	

    //cu 3s thi gui request mot lan-----------------------------------------------------------------------------------------------
	var interval2 = setInterval(function() {
			var json2 = {
			"Request": "SendData" //có một phần tử là "led", phần tử này chứa giá trị của mảng led.
		}
		var json3 = {
			"BumperEnable": "1" 
			}
			var json4 = {
			"BumperEnable": "0" 
			}
	// 	socket.on('LED_STATUS', function(status) {
	// 	//Nhận được thì in ra thôi hihi.
	// 	console.log("recv LED", status)
	// 	// var myObj = JSON.parse(status);
	// 	var chisoSensor= Number(status["Sensor value:"]);
			
	// 	socket.emit('BumperEnable', json3) ;
	// 	console.log("bat may bom");

	// 	if (chisoSensor < 600) {
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


socket.on('manual', function(status) {
		if (status == "on") {
			id = true;
			socket.emit('BumperEnable', json3) ;
			console.log("bat may bom");
		}
		else {
			id = false;
			socket.emit('BumperEnable', json4) ;
			console.log("tat may bom");		
		}
	})


		if(id == true ){
			socket.emit('Request', json3) //Gửi lệnh LED với các tham số của của chuỗi JSON
		}else if(id == false ){
			socket.emit('Request', json4) //Gửi lệnh LED với các tham số của của chuỗi JSON
		}

		}, 1000)
	//----------------------------------------------------------------------------------------------------------------------------
		//Khi nhận được lệnh LED_STATUS
	
	//----------------------------------------------------------------------------------------------------------------------------

	//Khi socket client bị mất kết nối thì chạy hàm sau.
	socket.on('disconnect', function() {
		console.log("disconnect") 	//in ra màn hình console cho vui
		clearInterval(interval2)		//xóa chu kỳ nhiệm vụ đi, chứ không xóa là cái task kia cứ chạy mãi thôi đó!
	})
});