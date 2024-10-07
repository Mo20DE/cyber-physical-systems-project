#ifndef I2C_DISPLAY_HEADER
#define I2C_DISPLAY_HEADER

#include <Wire.h>
#include <I2C_LCD.h>

class Display 
{
public:
  Display(uint8_t I2C_address = 0x51) : I2C_address(I2C_address)
  {
    LCD.WorkingModeConf(ON, ON, WM_BitmapMode);
    LCD.CleanAll(WHITE);
  }

  enum Bitmap_E
  {
    Sad_Face = 0,
    Happy_Face = 1,
    Monopoly_Face = 2,
    Angry_Face = 3,
    Alpaca = 4,
    Cool_Face = 5
    
    
  };

  void Draw(Bitmap_E face, int padding = 35) 
  {
    LCD.DrawScreenAreaAt(&_bitmaps[face], padding, 0);
  }

  void ClearScreen()
  {
    LCD.CleanAll(WHITE);
  }

private:
  I2C_LCD LCD;
  uint8_t I2C_address;

  static GUI_Bitmap_t _bitmaps[6];
};

#endif
