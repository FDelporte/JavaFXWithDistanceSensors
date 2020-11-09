package be.webtechie.controllingtheduke.gpio;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Helper class which bundles the Pi4J functions.
 */
public class GpioHelper {

    private static final Logger logger = LogManager.getLogger(GpioHelper.class);

    /**
     * The connected hardware components.
     */
    private GpioController gpioController;

    /**
     * The GPIO handlers.
     */
    private DistanceMeasurements distanceMeasurements = null;

    /**
     * Constructor.
     */
    public GpioHelper() {
        try {
            // Initialize the GPIO controller
            this.gpioController = GpioFactory.getInstance();

            // Initialize the pins for the distance sensor and measurement scheduler
            // BCM 24, Header pin 18
            // BCM 18, Header pin 12
            var sensor1 = new DistanceSensor(1,
                    gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_05, "Trigger", PinState.LOW),
                    gpioController.provisionDigitalInputPin(RaspiPin.GPIO_01, "Echo", PinPullResistance.PULL_UP)
            );
            // BCM 24, Header pin 18
            // BCM 18, Header pin 12
            var sensor2 = new DistanceSensor(2,
                    gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_05, "Trigger", PinState.LOW),
                    gpioController.provisionDigitalInputPin(RaspiPin.GPIO_01, "Echo", PinPullResistance.PULL_UP)
            );
            this.distanceMeasurements = new DistanceMeasurements(Arrays.asList(sensor1, sensor2));
        } catch (UnsatisfiedLinkError | IllegalArgumentException ex) {
            logger.error("Probably running on non-Pi-device or Pi4J not installed: {}", ex.getMessage());
        }
    }

    /**
     * @return {@link GpioController}
     */
    public GpioController getGpioController() {
        return this.gpioController;
    }

    /**
     * @return {@link DistanceMeasurements}
     */
    public DistanceMeasurements getDistanceSensorMeasurement() {
        return this.distanceMeasurements;
    }
}