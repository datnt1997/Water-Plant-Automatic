#include <ArduinoJson.h>
#include <SoftwareSerial.h>
#include <SerialCommand.h>  // Thêm vào sketch thư viện Serial Command
const byte RX = 6;          // Chân 6 được dùng làm chân RX
const byte TX = 5;          // Chân 5 được dùng làm chân TX

SoftwareSerial mySerial = SoftwareSerial(RX, TX);


SerialCommand sCmd(mySerial); // Khai báo biến sử dụng thư viện Serial Command
int const SENSOR_MH_D_PIN = 4;
int const SENSOR_MH_A_PIN = A0;

volatile int flow_frequency; // Đo xung cảm biến lưu lượng
unsigned int l_hour; // Tính toán số lít/giờ
unsigned char flowsensor = 2; // Cảm biến nối với chân 2
unsigned long currentTime;
unsigned long cloopTime;

//hàm ngắt
void flow (){
   flow_frequency++;
}


int const T_RELAY_PIN = 7;


int const TIME_TO_GET_SAMPLE = 5000; //5s
int const SAMPLE_TIME = 500; //0.5 s

int const LED_PIN = 13;


int const LED_BLUE_PIN = 10;
int const LED_YEWLOW_PIN = 9;
int const LED_RED_PIN = 8;

int const TREE_WATER_LEVEL_HIGH = 600;
int const TREE_WATER_LEVEL_LOW = 300;

int sensorMHValue = 0;//store sensor value

int bumpStatus = 0;
int waterStatus = 0;// -1: less water, 0: enough water,  +1 : more water

void setup() {

  pinMode(SENSOR_MH_D_PIN, INPUT);
  pinMode(flowsensor, INPUT);
  digitalWrite(flowsensor, HIGH); // Optional Internal Pull-Up
    
  //Khởi tạo Serial ở baudrate 57600 để debug ở serial monitor
  Serial.begin(57600);

  //Khởi tạo Serial ở baudrate 57600 cho cổng Serial thứ hai, dùng cho việc kết nối với ESP8266
  mySerial.begin(57600);

  // Một số hàm trong thư viện Serial Command
  sCmd.addCommand("Request",   sendData); //Khi có lệnh Request thì sẽ thực thi hàm sendData
  Serial.println("Da san sang nhan lenh!");

  attachInterrupt(0, flow, RISING); // Setup Interrupt
  sei(); // Enable interrupts
  
  pinMode(T_RELAY_PIN, OUTPUT);

  pinMode(LED_PIN, OUTPUT);
  pinMode(LED_BLUE_PIN, OUTPUT);
  pinMode(LED_YEWLOW_PIN, OUTPUT);
  pinMode(LED_RED_PIN, OUTPUT);
}

void loop() {
    sCmd.readSerial();
  // process and checking to watering
  //  wateringProcess();

  //Send information to serial port
  //  printToSerialPort();

  //show the led status
  //  showLedInfo();
}

void wateringProcess() {
  int sensorStatus = digitalRead(SENSOR_MH_D_PIN);
  int sensorValue = getSensorSampleValue();
  if (sensorStatus == 0) {
    if (sensorValue > TREE_WATER_LEVEL_HIGH) {
      digitalWrite(T_RELAY_PIN, HIGH);
      bumpStatus = 1;
      waterStatus = -1;
    } else if (sensorValue < TREE_WATER_LEVEL_LOW) {
      digitalWrite(T_RELAY_PIN, LOW);
      bumpStatus = 0;
      waterStatus = +1;
    } else {
      digitalWrite(T_RELAY_PIN, LOW);
      bumpStatus = 0;
      waterStatus = 0;
    }
  } else {
    digitalWrite(T_RELAY_PIN, HIGH);
    bumpStatus = 1;
    waterStatus = -1;
  }
}

int getSensorWaterValue(){
    l_hour = (flow_frequency * 60 / 7.5); 
    // (Pulse frequency x 60 min) / 7.5Q = flowrate in L/hour
    flow_frequency = 0; // Reset Counter
    return l_hour;
}
int getSensorSampleValue() {
  int value = 0;
  int t = TIME_TO_GET_SAMPLE / SAMPLE_TIME;
  int total = 0;
  for (int i = 0; i < t ; i++) {
    total += analogRead(SENSOR_MH_A_PIN);
    delay(t);
  }
  return total / t;
}
void printToSerialPort() {
  Serial.print("Bumper enable: "); Serial.println(bumpStatus);
  Serial.print("Sensor value: "); Serial.println(sensorMHValue);
}

void showLedInfo() {
  if (bumpStatus == 1) {
    digitalWrite(LED_YEWLOW_PIN, HIGH);
  } else {
    digitalWrite(LED_YEWLOW_PIN, LOW);
  }

  if (waterStatus == 0) {
    digitalWrite(LED_RED_PIN, LOW);
    digitalWrite(LED_BLUE_PIN, HIGH);
  } else {
    digitalWrite(LED_RED_PIN, HIGH);
    digitalWrite(LED_BLUE_PIN, LOW);
  }
}

// hàm led_red sẽ được thực thi khi gửi hàm LED_RED
void sendData() {
  Serial.println("Send Data");
  char *json = sCmd.next(); //Chỉ cần một dòng này để đọc tham số nhận đươc
  Serial.println(json);
  StaticJsonBuffer<200> jsonBuffer; //tạo Buffer json có khả năng chứa tối đa 200 ký tự
  JsonObject& root = jsonBuffer.parseObject(json);//đặt một biến root mang kiểu json

  int bumperStatus = root["BumperEnable"];

  //kiểm thử giá trị
  Serial.print(F("bumperStatus "));
  Serial.println(bumperStatus);

  if (bumperStatus==1){
    digitalWrite(T_RELAY_PIN, HIGH);
  }else{
    digitalWrite(T_RELAY_PIN, LOW);
  }

  //lấy giá trị sensor
  int sensorValue = getSensorSampleValue();
  int percentageValue = map(sensorValue,1023,0,0,100);
  int waterValue = getSensorWaterValue();

  StaticJsonBuffer<200> jsonBuffer2;
  JsonObject& root2 = jsonBuffer2.createObject();
  root2["Bumper value:"] = bumperStatus;
  root2["Sensor value:"] = percentageValue;
  root2["Sensor water value:"] = waterValue;

  //Tạo một mảng trong JSON
  JsonArray& data = root2.createNestedArray("data");
  data.add(bumpStatus);
  data.add(percentageValue);
  data.add(waterValue);


  //in ra cổng software serial để ESP8266 nhận
  mySerial.print("LED_STATUS");   //gửi tên lệnh
  mySerial.print('\r');           // gửi \r
  root2.printTo(mySerial); //gửi chuỗi JSON
  mySerial.print('\r');           // gửi \r

  //in ra Serial để debug
  root2.printTo(Serial); //Xuống dòng

}


