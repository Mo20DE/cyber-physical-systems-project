#include "defines.h"
#include "RobotCar.h"
#include "I2C_Display.h"
#include "Ultrasonic.h"
#include "Sound.h"

#define TESTING
// #define SERVER_COM

Display::Bitmap_E currentBitmap = Display::NONE;
Sounds currentSound = Sounds::NONE;

uint8_t I2C_LCD_ADDRESS = 0x51;  //Device address configuration, the default value is 0x51.

Display* screen;

RobotCar car(LEFT_EN,   LEFT_FORWARD,  LEFT_BACKWARD,
             RIGHT_EN,  RIGHT_FORWARD, RIGHT_BACKWARD,
             RIGHT_EN,  RIGHT_FORWARD, RIGHT_BACKWARD,
             LEFT_EN,   LEFT_FORWARD,  LEFT_BACKWARD);



struct I2C_Data_Packet
{
  uint8_t data1, data2, data3;
} packet;

void receive_data_i2c(int bytes)
{
  // Serial.println("Data received");
  byte* data = new byte[bytes];
  byte index = 0;
  while (Wire.available())
  {
    data[index++] = Wire.read();
  }

  memcpy(&packet, data, bytes);

  delete data;
}


//Range
/*
int run_ultra_sensor(){
  return ultrasonic.MeasureInCentimeters(); // two measurements should keep an interval
}
*/

void setup()
{
  Wire.begin(9);
  Wire.onReceive(receive_data_i2c);
  
  Serial.begin(9600);

  pinMode( VIB_MOTOR, OUTPUT );

  Serial.println("here");
  screen = new Display();
  Serial.println("there");
  car.EnableMotors(true);
}

uint8_t last_mood = 0xff;
uint8_t last_drive = 0xff;
uint8_t last_event = 0xff;

void loop()
{
  // Test();

  // Serial.println("MOOD: " + String(packet.data1) + " DRIVE: " + String(packet.data2) + " EVENT: " + String(packet.data3));
  if (packet.data1 != last_mood) {
    Serial.print("MOOD: ");
    Serial.println(packet.data1);
    last_mood = packet.data1;

    Serial.println("Mood: " + String(packet.data1));
    switch (last_mood)
    {
      case 0:
        Draw(screen, last_mood);
      break;
      case 1:
        Draw(screen, last_mood);
      break;
      case 2:
        Draw(screen, last_mood);
      break;
      case 3:
        Draw(screen, last_mood);
      break;
      case 4:
        Draw(screen, last_mood);
      break;
      case 5:
        Draw(screen, last_mood);
      break;
    }
    
    PlaySound(last_mood, 40);
  }
  if (packet.data2 != last_drive) {
    Serial.print("DRIVE: ");
    Serial.println(packet.data2);
    last_drive = packet.data2;

    switch (packet.data2)
    {
      case 1:
        car.Forward();
      break;
      case 2:
        car.Backward();
      break;
      case 3:
        car.TurnLeft();
      break;
      case 4:
        car.TurnRight();
      break;
        
    }
  }

  delay(500);
  car.Hold();
  
  if (packet.data3 != last_event) {
    Serial.print("EVENT: ");
    Serial.println(packet.data3);
    last_event = packet.data3;

    if (last_event == 5) {
      pinkpanther(40);
    } else if (last_event == 6) {
      starwars(40);
    }

  }

  screen->ClearScreen();
  delay(1000);
}


void Draw(Display* screen, byte info)
{
  Display::Bitmap_E select;

  Serial.println("Draw: " + String(info));

  if (info == 1)
  {
    select = Display::Happy_Face;
  }
  else if (info == 2)
  {
    select = Display::Angry_Face;
  }
  else if (info == 3)
  {
    select = Display::Cold_Face;
  }
  else if (info == 4)
  {
    select = Display::Sad_Face;
  }
  else if (info == 5)
  {
    select = Display::Hot_Face;
  }
  else if (info == 6)
  {
    select = Display::Panda_Face;
  }

  if (select != currentBitmap)
  {
    currentBitmap = select;
    screen->ClearScreen();
    screen->Draw(currentBitmap);
  }
}



void PlaySound(byte sound, int dauer)
{
  car.EnableMotors(false);
  
  Sounds select;

  if (sound == 1)
  {
    select = Sounds::HAPPY;
  }
  else if (sound == 2)
  {
    select = Sounds::ANGRY;
  }
  else if (sound == 3)
  {
    select = Sounds::COLD;
  }
  else if (sound == 4)
  {
    select = Sounds::SAD;
  }
  else if (sound == 5)
  {
    select = Sounds::HOT;
  } 

  if (currentSound != select)
  {
    currentSound = select;

    switch (currentSound)
    {
    case Sounds::HAPPY:
      takeonme(dauer);
      break;

    case Sounds::ANGRY:
      thegodfather(dauer);
      break;

    case Sounds::COLD:
      gameofthrones(dauer);
      break;

    case Sounds::SAD:
      furelise(dauer);
      break;

    case Sounds::HOT:
      coffindance(dauer);
      break;

    }
  }

  car.EnableMotors(true);
}

bool Test()
{
   Serial.println("Testing");

  for (int i = 1; i < 8; i++)
  {
    Draw(screen, i);
    delay(2000);

    // PlaySound(i, 40);
  }


/*
  car.Forward();
  delay(1000);
  car.Backward();
  delay(1000);
  car.TurnLeft();
  delay(1000);
  car.TurnRight();
  delay(1000);
  car.Hold();
*/


  /*
  if (run_ultra_sensor() < 15)
  {
    car.Hold();
  }
  else
  {
    car.Backward();
  }
  */

}
