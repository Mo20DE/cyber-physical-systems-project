/* Cyber-Physical-Systems Internship at TU Berlin
 *  
 *  Team: Mohammed, Niklas, Alex, Silvan
 */

 
#include "Arduino.h"

const int buttonPin = 2;     // the number of the pushbutton pin


//communication


bool ack_flag;
bool command_flag;

bool a;
uint8_t msg_code=0;
const uint8_t HEADER_LENGTH = 5;

const uint8_t ACK_MESSAGE_ID = 0;
const uint8_t ERROR_MESSAGE_ID = 1;

const uint8_t INFORMATION_MESSAGE_ID = 10;
const uint8_t INFORMATION_MESSAGE_BODY_LENGTH = 21;

const uint8_t COMMAND_MESSAGE_ID = 20;
const uint8_t COMMAND_MESSAGE_BODY_LENGTH = 3;

//packet received;
uint8_t pck[8];

/*    Main Part      */
//Struct of our Elements sending
struct DataPackage {
  uint8_t message_id;
  uint8_t checksum;
  uint8_t message_code;
  
  int time=0;
  int loops;
  bool button;

  float temp;
  float humi;
  
  int range;
  
  int vis;
  int ir;
  float uv;
};
struct Ack_package
{
  uint8_t message_id = ACK_MESSAGE_ID;
  uint8_t checksum;
  uint8_t message_code;
  uint8_t time_high = 0;
  uint8_t time_low = 0;
 }ack;

// make a datapackage
DataPackage data;

// variables will change:
int loops=0;
int buttonState = 0; // variable for reading the pushbutton status


/////////////////////////////////////////////////////////   Buffer struct begin    /////////////////////////////////////////////////////////

//Buffer Class from Type 'T' (generic)
template <class T>
struct RingBuffer {
  //Change for a different buffer size.
  static const int size = 20;

  T content[size];
  int head = 0;
  int tail = 0;
  bool isBufferFull = false;

  //Writes an element from generic type 'T' into buffer. Returns false if buffer is full.
  bool writeBuffer(T element) {
    if (isBufferFull == false) {
      content[head] = element;
      head = (head + 1) % size;
      if (head == tail) {
        isBufferFull = true;
      }
      return true;
    }
    return false;
  }

  //Reads and returns the element at the position of the tail variable. Returns first element if empty.
  T readBuffer() {
    if (isEmpty() == false) {
      T element = content[tail];
      tail = (tail + 1) % size;
      isBufferFull = false;
      return element;
    }
    return content[0];
  }

  bool isFull() {
    return isBufferFull;
  }

  bool isEmpty() {
    if ((head == tail) && (isBufferFull != true)) {
      return true;
    }
    return false;
  }
};

/////////////////////////////////////////////////////////    Buffer struct end     /////////////////////////////////////////////////////////

///////////////////////////////////////////////////////// Temperature Sensor Begin /////////////////////////////////////////////////////////

//Temperature
#include <Adafruit_Sensor.h>
#include <DHT.h>
#include <DHT_U.h>

#define DHTPIN 5     // Digital pin connected to the DHT sensor 
#define DHTTYPE    DHT22     // DHT 22 (AM2302)

DHT_Unified dht(DHTPIN, DHTTYPE);

unsigned long last_time_sent = 0;
const long interval = 2000; // 2 seconds delay


/*  Temperature */
void init_temp_sensor() {
  dht.begin();
  sensor_t sensor;
  Serial.println("Temperature-Sensor is ready!");
  dht.temperature().getSensor(&sensor);
  dht.humidity().getSensor(&sensor);
}

//Temperature
void run_temp_sensor() {

  sensors_event_t event;
  dht.temperature().getEvent(&event);

  if (isnan(event.temperature)) {
    Serial.println(F("Error reading temperature!"));
  }
  else {
    data.temp= event.temperature;
  }
  dht.humidity().getEvent(&event);
  if (isnan(event.relative_humidity)) {
    Serial.println(F("Error reading humidity!"));
  }
  else {
    data.humi= event.relative_humidity;
  }

}

///////////////////////////////////////////////////////// Temperature Sensor End /////////////////////////////////////////////////////////




///////////////////////////////////////////////////////// Sunlight Sensor Begin /////////////////////////////////////////////////////////

//Light
#include "SI114X.h"
SI114X SI1145 = SI114X();


void init_sun_sensor() {
  /*  Light-sensor  */
  Serial.println("Beginning Light-Sensor");
  if (!SI1145.Begin()) {
    Serial.println("Light-Sensor is not ready!");
    delay(1000);
  }
  else{
  Serial.println("Light-Sensor is ready!");
  } 
}

//Light-sensor
void run_sun_sensor() {
  data.vis= SI1145.ReadVisible();
  data.ir= SI1145.ReadIR();
  data.uv= (float)SI1145.ReadUV()/100;
}


///////////////////////////////////////////////////////// Sunlight Sensor End /////////////////////////////////////////////////////////




///////////////////////////////////////////////////////// Ultrasonic Sensor Begin /////////////////////////////////////////////////////////

// Ultrasonic Sensor
#include "Ultrasonic.h"
Ultrasonic ultrasonic(7);

void init_ultra_sensor() {
  Serial.println("Range-Sensor is ready!");
}

//Range
void run_ultra_sensor(){
  long RangeInCentimeters;
  RangeInCentimeters = ultrasonic.MeasureInCentimeters(); // two measurements should keep an interval
  data.range= RangeInCentimeters;
}

///////////////////////////////////////////////////////// Ultrasonic Sensor End /////////////////////////////////////////////////////////




///////////////////////////////////////////////////////// XBEE Begin /////////////////////////////////////////////////////////

//acknowlegement to be send


//init i2c for funkitions to work proparly




 void build_ack() {
   ack.message_code = pck[2];
 }


#include <SoftwareSerial.h>
SoftwareSerial xbee = SoftwareSerial(3, 4);




//timestamp if the
unsigned long time_send;
//calculates the checksum for a given package
void fill_checksum(uint8_t* data, int size)
  {
    uint8_t* pointer = (uint8_t*) data;
    data[1] = (uint8_t) 0;
    uint8_t checksum = 0;
    for (int i = 0; i < size; i++) {
        checksum += *pointer++;
    }
    data[1] = checksum;
  }







  //calculates the checksum for the package and compares it the checksum in the
  bool checksum_is_valid(uint8_t pck[], int size) {
    uint8_t checksum_recv = pck[1];
    pck[1] = (uint8_t) 0;
    uint8_t checksum_calculated = 0;
    for (int i = 0; i < size; i++) {
      checksum_calculated += pck[i];
    }
    Serial.print("checksum received: " + String(checksum_recv));
    Serial.print("checksum calulated: " + String(checksum_calculated));
    return checksum_calculated == checksum_recv;
  }
//print the package into the serial monitor
   void printPackage(uint8_t paket_length) {
     Serial.println("Bytes: ");
     for(int i = 0; i < paket_length; i++) {
       Serial.print(String(pck[i]) + ", ");
     }
   }
  //reads a package and saves it in the array
 bool read_package() {
   pck[0] = xbee.read();
   uint8_t message_id = pck[0];
   uint8_t paket_length;
   switch (message_id) {
      case ACK_MESSAGE_ID:
        Serial.println("Received ack message!");
        paket_length = HEADER_LENGTH;
        ack_flag = true;
        break;
      case COMMAND_MESSAGE_ID:
        Serial.println("Received command message!");
        paket_length = HEADER_LENGTH + COMMAND_MESSAGE_BODY_LENGTH;
        command_flag = true;
        break;
     default:
        Serial.println("Received unknown message id: " + String(message_id));
        return false;
   }
   uint8_t calculated_checksum = message_id;
   pck[1] = xbee.read();
   uint8_t received_checksum = pck[1];
   Serial.print("PACKET BYTES: " + String(pck[0]) + ", " + String(pck[1]));
   for (int i = 2; i < paket_length; i++) {
     pck[i] = xbee.read();
     calculated_checksum += pck[i];
     Serial.print(", " + String(pck[i]));
   }
   Serial.println(" | CALC VS CHECK: " + String(calculated_checksum) + " VS " + String(received_checksum));
   if (calculated_checksum != received_checksum) {
     Serial.println("Checksum INVALID!");
     return false;
   }
   return true;
 }

 struct I2C_Data_Packet
{
  uint8_t data1 = 0, data2 = 0, data3 = 0;
} packet;

 // writes the pck body to i2c
 void execute_command() {
   packet.data1=pck[5];
   packet.data2=pck[6];
   packet.data3=pck[7];
   
  Serial.println("Mood:"+String(packet.data1));
  Serial.println("Drive:"+String(packet.data2)); 
  Serial.println("Event:"+String(packet.data3)); 
 }

   






///////////////////////////////////////////////////////// XBEE End /////////////////////////////////////////////////////////
///////////////////////////////////////////////////////// read_data /////////////////////////////////////////////////////////
void send_sensor_data(){
  buttonState = digitalRead(buttonPin);
  data.button= buttonState;
  
  data.loops = loops++;
  //Temperature-Sensor
  run_temp_sensor();

  //Sunlight-Sensor
  run_sun_sensor();

  //Ultrasonic-Sensor
  run_ultra_sensor();
  
 data.message_id = INFORMATION_MESSAGE_ID;
 data.message_code = msg_code;
 fill_checksum((uint8_t*) &data, HEADER_LENGTH + INFORMATION_MESSAGE_BODY_LENGTH);
   
 //Serial.println(data.checksum);
 //send newest data
 xbee.write((uint8_t*) &data, sizeof(data));
  msg_code++;
}


///////////////////////////////////////////////////////// Utility Begin /////////////////////////////////////////////////////////


///////////////////////////////////////////////////////// Utility End /////////////////////////////////////////////////////////

#include <Wire.h>


void send_data_i2c(int address = 9)
{


  byte bytes[sizeof packet];
  memcpy(bytes, &packet, sizeof packet);
Serial.println("printing i2c");
  Serial.println(bytes[0]);
  Serial.println(bytes[1]);
  Serial.println(bytes[2]);
  Wire.beginTransmission(address);
  
  Wire.write(bytes, sizeof packet);
  
  Wire.endTransmission();
}

//Button
// ring-buffer for buffering data
typedef RingBuffer<DataPackage> _buffer;


void setup(void)
{
  Wire.begin(9);

  Serial.begin(9600);

  /*  Temperature-Sensor */
  init_temp_sensor();

  /*  Light-Sensor  */
  init_sun_sensor();
  
  /*  Ultrasinic-Sensor */
  init_ultra_sensor();
  

  /*  Button  */
  // initialize the pushbutton pin as an input:
  pinMode(buttonPin, INPUT);
  /*KOmmunikation*/
  /*xbee*/
  xbee.begin(9600);
  delay(500);
  // init buffer
  //databuffer


}

/*      While loop functions         */


void loop(void)
{
  unsigned long current_time = millis();
  if (current_time - last_time_sent >= interval) {
    Serial.print("\n\n//--------------------------------------//\r\n");
    Serial.print("\n-----Loop nr.");
    Serial.println(loops);
    Serial.println("Sending sensor data ...");
    send_sensor_data();
    last_time_sent = millis();
    //_buffer::writeBuffer(data);
  }

    /*Serial.print((String)uno.loops+", ");
    Serial.print((String)uno.button+", ");
    Serial.print((String)uno.temp+", ");
    Serial.print((String)uno.humi+", ");
    Serial.print((String)uno.range+", ");
    Serial.print((String)uno.vis+", ");
    Serial.print((String)uno.ir+", ");
    Serial.println((String)uno.uv);*/
  while(xbee.available()) {
    ack_flag = false;
    command_flag = false;
    bool is_valid = read_package();
    if (!is_valid) {
      // delete package
      continue;
    }
    if (ack_flag) {
      // delete from msgq
    } else if (command_flag) {
      Serial.println("Executing command ...");
      execute_command();
      send_data_i2c();
      build_ack();
      fill_checksum((uint8_t*) &ack, HEADER_LENGTH);
      Serial.println("Sending ACK for command ...");
      xbee.write((uint8_t*) &ack, sizeof(ack));
      Serial.println("Sent ACK for command.");
    }
   } 
}
