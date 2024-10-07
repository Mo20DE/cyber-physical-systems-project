#ifndef I2C_DISPLAY_HEADER
#define I2C_DISPLAY_HEADER

#include "Arduino.h"
#include <Wire.h>
#include <I2C_LCD.h>

class Display 
{
public:
  Display(uint8_t I2C_address = 0x51) : I2C_address(I2C_address)
  {
    LCD.CleanAll(WHITE);
    delay(500);
    LCD.WorkingModeConf(ON, ON, WM_BitmapMode);
  }

  enum Bitmap_E
  {
    Sad_Face = 0,
    Happy_Face = 1,
    Panda_Face = 2,
    Monopoly_Face = 3,
    Angry_Face = 4,
    Alpaca = 5,
    Cold_Face = 6,
    Hot_Face = 7,
    NONE
    
  };

  void Draw(Bitmap_E face, int padding = 35) 
  {
    LCD.CleanAll(WHITE);
    delay(500);
    LCD.WorkingModeConf(ON, ON, WM_BitmapMode);
    LCD.DrawScreenAreaAt(&_bitmaps[face], padding, 0);
  }

  void ClearScreen()
  {
    LCD.CleanAll(WHITE);
  }

private:
  I2C_LCD LCD;
  uint8_t I2C_address;

  static GUI_Bitmap_t _bitmaps[10];
};

#endif
