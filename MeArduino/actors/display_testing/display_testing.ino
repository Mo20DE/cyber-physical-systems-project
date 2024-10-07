
#include "I2C_Display.h"

uint8_t I2C_LCD_ADDRESS = 0x51;  //Device address configuration, the default value is 0x51.

Display* display_;

void setup() {
  Wire.begin();
  display_ = new Display();
  // put your setup code here, to run once:

}

void loop() {
  // put your main code here, to run repeatedly:
  display_->Draw(Display::Bitmap_E::Happy_Face);
  delay(1000);
  display_->ClearScreen();

  display_->DrawText("Helloooo", 0, 10);
}
