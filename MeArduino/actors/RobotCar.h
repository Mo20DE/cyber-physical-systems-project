#ifndef ROBOTCAR_HEADER
#define ROBOTCAR_HEADER

#include <Arduino.h>

enum RobotCarActions 
{
    FORWARD,
    BACKWARD,
    TURN_RIGHT,
    TURN_LEFT,
    HOLD,
    _OFF
};

class RobotCar {

public:

    RobotCar(
        unsigned int fle, unsigned int flf, unsigned int flb,
        unsigned int fre, unsigned int frf, unsigned int frb,
        unsigned int rre, unsigned int rrf, unsigned int rrb,
        unsigned int rle, unsigned int rlf, unsigned int rlb 
    );

    // movements
    void Forward();
    void Backward();
    void Hold();

    void TurnLeft();
    void TurnRight();

    void EnableMotors(bool enable);


private:

    // motor pins
    unsigned int frontLeftEnablePin,  frontLeftForwardPin,  frontLeftBackwardPin;
    unsigned int frontRightEnablePin, frontRightForwardPin, frontRightBackwardPin;
    unsigned int rearRightEnablePin,  rearRightForwardPin,  rearRightBackwardPin;
    unsigned int rearLeftEnablePin,   rearLeftForwardPin,   rearLeftBackwardPin;

    RobotCarActions currentAction;
};

#endif
