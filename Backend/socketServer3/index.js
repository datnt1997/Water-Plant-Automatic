const PORT = 3484; //Đặt địa chỉ Port được mở ra để tạo ra chương trình mạng Socket Server

var http = require('http') //#include thư viện http - Tìm thêm về từ khóa http nodejs trên google nếu bạn muốn tìm hiểu thêm. Nhưng theo kinh nghiệm của mình, Javascript trong môi trường NodeJS cực kỳ rộng lớn, khi bạn bí thì nên tìm hiểu không nên ngồi đọc và cố gắng học thuộc hết cái reference (Tài liêu tham khảo) của nodejs làm gì. Vỡ não đó!
var socketio = require('socket.io') //#include thư viện socketio


var ip = require('ip');
var app = http.createServer(); //#Khởi tạo một chương trình mạng (app)
var io = socketio(app);
var async = require("async"); //#Phải khởi tạo io sau khi tạo app!
var schedule = require('node-schedule');
app.listen(3484);
app.listen(process.env.PORT || 3484); // Cho socket server (chương trình mạng) lắng nghe ở port 3484
console.log("Server nodejs chay tai dia chi: " + ip.address() + ":" + PORT)

var androidapp_nsp = io.of('/androidapp') //namespace của webapp
var esp8266_nsp = io.of('/esp8266') //namespace của esp8266

var middleware = require('socketio-wildcard')(); //Để có thể bắt toàn bộ lệnh!
esp8266_nsp.use(middleware); //Khi esp8266 emit bất kỳ lệnh gì lên thì sẽ bị bắt
androidapp_nsp.use(middleware); //Khi webapp emit bất kỳ lệnh gì lên thì sẽ bị bắt


// tạo và kết nối với mongodb
var mongodb = require('mongodb');

var MongoClient = mongodb.MongoClient;
var url = 'mongodb://localhost:27017/userdb';
var ObjectId = require('mongodb').ObjectID;


MongoClient.connect(url, function(err, db) {
    if (err) {
        console.log('Unable to connect to the mongoDB server. Error:', err);
    } else {
        //HURRAY!! We are connected. :)
        console.log('Connection established to', url);
        user = db.collection('UserInfo');
        //user = 'mongodb://thanh:123@ds121543.mlab.com:21543/heroku_0nk1qfl2/UserInfo'
        date = db.collection('Schedule');
        data_sensor = db.collection('SensorData');
        hum = db.collection('Humidity');
    }
});



//Khi có một kết nối được tạo giữa Socket Client và Socket Server
var id = false;
var time = false;
var manual = false;
var startBump, stopBump;
var intervalId;
var secondAction = 0;
var action;


var json3 = {
    "BumperEnable": "1"
};
var json4 = {
    "BumperEnable": "0"
};


esp8266_nsp.on('connection', function(socket) {
    //hàm console.log giống như hàm Serial.println trên Arduino
    console.log("esp8266 Connected"); //In ra màn hình console là đã có một Socket Client kết nối thành công.

    //cu 3s thi gui request mot lan-----------------------------------------------------------------------------------------------
    var interval2 = setInterval(function() {

        var d = new Date();
        var n = d.getFullYear();
        var m = d.getMonth();
        var l = d.getDate();
        var now_hour = d.getHours();
        var now_minute = d.getMinutes();
        var now_second = d.getSeconds();

        var json2 = {
            "Request": "SendData" //có một phần tử là "led", phần tử này chứa giá trị của mảng led.
        }
        var json3 = {
            "BumperEnable": "1"
        }
        var json4 = {
            "BumperEnable": "0"
        }
        var chisoSensor;
        var chisoWater;
        var data_date;
        var data_time;
        var startHumid, stopHumid;

        async.series([
                function(callback) {
                    setTimeout(function() {

                        socket.on('LED_STATUS', function(status) {
                            //Nhận được thì in ra thôi hihi.
                            // console.log("recv LED", status)
                            // var myObj = JSON.parse(status);
                            chisoSensor = Number(status["Sensor value:"]);
                            chisoWater = Number(status["Sensor water value:"]);
                            // console.log(chisoSensor + "");
                            // console.log(chisoWater + "");


                            var k = m + 1;
                            if (k < 10) {
                                k = "0" + k;
                            }
                            if (l < 10) {
                                l = "0" + l;
                            }
                            data_date = k + "/" + l + "/" + n;
                            data_time = now_hour + ":" + now_minute + ":" + now_second;

                        })


                        callback(null);
                    }, 200);

                },
                function(callback) {
                    setTimeout(function() {

                        console.log(data_date);
                        console.log(data_time);
                        var data_insert = { dataDate: data_date, dataTime: data_time, sensorValue: chisoSensor, flow: chisoWater };

                        if (chisoSensor != null && chisoWater != null) {
                            data_sensor.insert(data_insert, function(err, result) {
                                if (err) {
                                    console.log(err);
                                } else {
                                    console.log('Inserted new value ok');
                                }
                            });
                        }

                        callback(null);
                    }, 1400);

                },
                function(callback) {
                    setTimeout(function() {

                        var cursor = hum.findOne({ sensor: 1 }, function(err, result) {
                            if (err) throw err;
                            startHumid = result.startWhen;
                            stopHumid = result.stopWhen;
                        });

                        callback(null);
                    }, 300);

                },
                function(callback) {
                    setTimeout(function() {
                        console.log(startHumid + " " + stopHumid);
                        if (chisoSensor < startHumid) {
                            id = true;
                        } else if (chisoSensor > stopHumid) {
                            id = false;
                        }
                        if (id == true && time == false && manual == false) {
                            esp8266_nsp.emit('Request', json3) //Gửi lệnh Request với các tham số của của chuỗi JSON
                            console.log("Độ ẩm tưới") //Ghi ra console.log là bật máy bơm
                            androidapp_nsp.emit('status', "Watering");

                        }
                        if (id == false && time == false && manual == false) {
                            esp8266_nsp.emit('Request', json4) //Gửi lệnh LED với các tham số của của chuỗi JSON
                            console.log("Độ ẩm tắt") //Ghi ra console.log là tắt máy bơm
                            androidapp_nsp.emit('status', "Off");
                        }

                        callback(null);
                    }, 1400);

                }

            ],
            function(error, results) {
                if (error) {
                    console.log(error.toString());
                } else {
                    console.log(results);

                }
            }
        )


    }, 3000)


    setTimeout(function() {
        Schedule().then(res => console.log(".........."),
            err => console.log(err + ''));
    }, 1000);


    var t = schedule.scheduleJob({ hour: 00, minute: 01 }, function() {
        console.log("gọi lại schedule");
        Schedule().then(res => console.log(".........."),
            err => console.log(err + ''));
    });


    //Khi socket client bị mất kết nối thì chạy hàm sau.
    socket.on('disconnect', function() {
        console.log("Disconnect socket esp8266") //in ra màn hình console cho vui
    })

});

androidapp_nsp.on('connection', function(socket) {
    console.log("Android connected");


    //----------------------------------------------------------------------------------------------------------------------------
    //  khi nhận được request setTime

    socket.on('addSchedule', function(sunday, monday, tuesday, wednesday, thursday, friday, saturday, status, starttime, stoptime, listday) {
        var st_add = { Sunday: sunday, Monday: monday, Tuesday: tuesday, Wednesday: wednesday, Thursday: thursday, Friday: friday, Saturday: saturday, status: status, startTime: starttime, stopTime: stoptime, listDay: listday };

        date.insert(st_add, function(err, result) {
            if (err) {
                console.log(err);
            } else {
                n = 1;
                console.log('Inserted new dog ok');
                showSchedule().then(res => console.log("show data"),
                    err => console.log(err + ''));
                Schedule().then(res => console.log(".........."),
                    err => console.log(err + ''));
            }
        });

    })

    socket.on('editSchedule', function(id, sunday, monday, tuesday, wednesday, thursday, friday, saturday, status, starttime, stoptime, listday) {
        console.log("Update " + id);
        var st_up = { Sunday: sunday, Monday: monday, Tuesday: tuesday, Wednesday: wednesday, Thursday: thursday, Friday: friday, Saturday: saturday, status: status, startTime: starttime, stopTime: stoptime, listDay: listday };

        date.update({ _id: ObjectId(id) }, st_up, function(err, result) {
            if (err) {
                console.log(err);
            } else {
                n = 2;
                console.log('Update schedule ok');
                showSchedule().then(res => console.log("show data"),
                    err => console.log(err + ''));
                Schedule().then(res => console.log(".........."),
                    err => console.log(err + ''));
            }
        });
    });

    socket.on('updateEnable', function(id, status) {
        console.log("Update " + id);
        var up_en = { $set: { status: status } };

        date.update({ _id: ObjectId(id) }, up_en, function(err, result) {
            if (err) {
                console.log(err);
            } else {
                n = 2;
                console.log('Update schedule ok');
                showSchedule().then(res => console.log("show data"),
                    err => console.log(err + ''));
                Schedule().then(res => console.log(".........."),
                    err => console.log(err + ''));
            }
        });
    });

    socket.on('deleteSchedule', function(Id) {
        console.log("Delete " + Id);
        var st_del = { _id: ObjectId(Id) };

        date.remove(st_del, function(err, result) {
            if (err) {
                console.log(err);
            } else {
                n = 3;
                console.log('Deleted schedule ok');
                showSchedule().then(res => console.log("show data"),
                    err => console.log(err + ''));
                Schedule().then(res => console.log(".........."),
                    err => console.log(err + ''));
            }
        });
    });

    socket.on('showSchedule', function() {
        n = 0;
        console.log("Client want show data");
        showSchedule().then(res => console.log("show data"),
            err => console.log(err + ''));
    });

    let showSchedule = function() {
        return new Promise((resolve, reject) => {
            var cursor = date.find();
            cursor.each(function(err, result) {
                if (err) {
                    console.log(err);
                    androidapp_nsp.emit('showSchedule', false);
                } else {
                    if (result != null) {
                        var stObject = JSON.parse(JSON.stringify(result));
                        console.log(stObject);
                        if (n == 0) {
                            androidapp_nsp.emit('show', stObject);
                        } else if (n == 1) {
                            androidapp_nsp.emit('add', stObject);
                        } else if (n == 2) {
                            androidapp_nsp.emit('edit', stObject);
                        } else if (n == 3) {
                            androidapp_nsp.emit('del', stObject);
                        }
                        androidapp_nsp.emit('showSchedule', stObject);
                    } else if (result == null) {
                        console.log("Null");
                    }

                }
            });
        });
    }

    socket.on('showHum', function() {
        getHumidity().then(res => console.log("show data"),
            err => console.log(err + ''));
    });

    let getHumidity = function() {
        return new Promise((resolve, reject) => {
            var cursor = hum.findOne({ sensor: 1 }, function(err, result) {
                if (err) throw err;
                var stObject = JSON.parse(JSON.stringify(result));
                androidapp_nsp.emit('showHum', stObject);
                // int startHum = result.startWhen;
                // int stopHum = result.stopWhen;
            });
        });
    }


    // Khi nhận được request manual

    //----------------------------------------------------------------------------------------------------------------------------

    socket.on('login', function(username, password) {
        console.log(username + "login");

        var cursor = user.find({ User: username });
        cursor.each(function(err, doc) {
            if (err) {
                console.log(err);
                socket.emit('login', false);
            } else {
                if (doc != null) {
                    if (doc.Pass == password) {
                        socket.emit('login', true);
                        socket.emit('login', doc);
                    } else {
                        socket.emit('login', false);
                    }

                }
            }
        });

    });

    var fontChange = function() {
        if (action == true) {
            //intervalId = setInterval(fontChange, 1000);
            secondAction += 1;
            console.log(secondAction);
        }

        if (action == false) {
            console.log("(-)");
        }
    };

    //if(manual == true){
    intervalId = setInterval(fontChange, 1000);
    //}            

    var manualsetting = setInterval(function() {

        var json3 = {
            "BumperEnable": "1"
        };
        var json4 = {
            "BumperEnable": "0"
        };

        socket.on('manual', function(status) {
            if (status == "on") {
                // if (time) {
                //     startBump.cancel();
                // }
                manual = true;
                action = true;

                socket.emit('BumperEnable', json3);
                console.log("bat may bom");


            } else {
                // if (time) {
                //     startBump.cancel();
                // }
                manual = false;
                action = false;
                socket.emit('BumperEnable', json4);
                console.log("tat may bom");
                //huy di ham dem thoi gian
                //clearInterval(interval_obj);
                clearInterval(intervalId);
                secondAction = 0;
                intervalId = setInterval(fontChange, 1000);
            }
        })

        //console.log(manual + " " + id + " " + time);
        if (manual == true) {
            //console.log(manual + " " + id + " " + time);
            id = false;
            time = false;
            esp8266_nsp.emit('Request', json3) //Gửi lệnh LED với các tham số của của chuỗi JSON
        } else if (manual == false && time == false && id == false) {
            //console.log(manual + " " + id + " " + time);
            console.log("Manual tat");
            esp8266_nsp.emit('Request', json4) //Gửi lệnh LED với các tham số của của chuỗi JSON
        }

    }, 1000);

    socket.on('setStartHumidity', function(humidity) {
        console.log("Update " + humidity);
        var up_en = { $set: { startWhen: humidity } };

        hum.update({ sensor: 1 }, up_en, function(err, result) {
            if (err) {
                console.log(err);
            } else {

            }
        });
    });

    socket.on('setStopHumidity', function(humidity) {
        console.log("Update " + humidity);
        var up_en = { $set: { stopWhen: humidity } };

        hum.update({ sensor: 1 }, up_en, function(err, result) {
            if (err) {
                console.log(err);
            } else {

            }
        });
    });


    socket.on('getSensorValues', function() {
        console.log("get vui");
        var getSensor = setInterval(function() {
            var result = data_sensor.find().sort({ _id: -1 }).limit(1);
            result.each(function(err, doc) {
                if (err) {
                    console.log(err);
                } else {
                    if (doc != null) {
                        var stObject = JSON.parse(JSON.stringify(doc));
                        console.log(stObject.sensorValue);
                        androidapp_nsp.emit('getSensorValues', stObject);
                    } else if (doc == null) {}

                }
            });
        }, 3000);

    });


    //Khi socket client bị mất kết nối thì chạy hàm sau.
    socket.on('disconnect', function() {
        console.log("Disconnect socket android") //in ra màn hình console cho vui
    })
});


// set thời gian tưới nước      
let Schedule = function() {
    return new Promise((resolve, reject) => {
        d = new Date();
        n = d.getFullYear();
        m = d.getMonth();
        l = d.getDate();


        var d, n, m, l, today, start, stop, countDownStop, countDownStart;
        var days = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
        var arr_start = [];
        var arr_stop = [];


        async.series([
                function(callback) {
                    setTimeout(function() {
                        d = new Date();
                        n = d.getFullYear();
                        m = d.getMonth();
                        l = d.getDate();
                        today = days[d.getDay()];
                        console.log(today);
                        var query = {};
                        query[today] = "true";
                        query["status"] = "enable";
                        var cursor = date.find(query);
                        cursor.each(function(err, doc) {
                            if (err) {
                                console.log(err);
                            } else {
                                if (doc != null) {
                                    start = doc.startTime;
                                    stop = doc.stopTime;
                                    arr_start.push(start);
                                    arr_stop.push(stop);
                                    var stObject = JSON.parse(JSON.stringify(doc));
                                    console.log(stObject + "");
                                }
                            }
                        });

                        callback(null);
                    }, 200);
                },
                function(callback) {
                    setTimeout(function() {
                        var json3 = {
                            "BumperEnable": "1"
                        };
                        var json4 = {
                            "BumperEnable": "0"
                        }

                        for (var i = 0; i < arr_stop.length; i++) {

                            var hour = (arr_start[i]).substring(0, 2);
                            var minute = (arr_start[i]).substring(3, 5);
                            var second = (arr_start[i]).substring(6, 8);

                            var hourEnd = (arr_stop[i]).substring(0, 2);
                            var minuteEnd = (arr_stop[i]).substring(3, 5);
                            var secondEnd = (arr_stop[i]).substring(6, 8);

                            console.log(n + " " + m + " " + l);
                            console.log(hour + " " + minute + " " + second);
                            var date = new Date(n, m, l, hour, minute, second);
                            var dateEnd = new Date(n, m, l, hourEnd, minuteEnd, secondEnd);
                            console.log(date + "");

                            startBump = schedule.scheduleJob({ start: date, end: dateEnd, rule: '*/1 * * * * *' }, function() {
                                time = true;
                                console.log("Bat may bom");
                                esp8266_nsp.emit('Request', json3) //Gửi chuỗi JSON để bat máy bơm
                                androidapp_nsp.emit('status', "Watering");
                                if (manual) {
                                    console.log("Tat Time");
                                    console.log(id + " - " + time + " - " + manual);
                                    time = false;
                                    startBump.cancel();
                                }
                            });

                            stopBump = schedule.scheduleJob({ hour: hourEnd, minute: minuteEnd }, function() {
                                time = false;
                                console.log("Tat may bom");
                                esp8266_nsp.emit('Request', json4);
                                androidapp_nsp.emit('status', "Off");
                            });

                        }
                        callback(null);
                    }, 1000);

                }
            ],
            function(error, results) {
                if (error) {
                    console.log(error.toString());
                } else {
                    console.log(results);

                }
            }
        );
    });
}