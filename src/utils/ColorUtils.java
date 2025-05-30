package utils;

import java.awt.*;

public class ColorUtils {
    /**
     * Retorna uma cor RGB baseada em um valor HUE que muda com o tempo.
     *
     * @param currentTime O tempo atual em milissegundos.
     * @return A cor RGB correspondente.
     */
    public static Color getRainbowColor(long currentTime) {
        // Duração do ciclo de cor (ex: 5000ms = 5s para um arco-íris completo)
        long cycleDuration = 5000;

        // Calcula o valor de hue com base no tempo (de 0.0 a 1.0)
        float hue = (currentTime % cycleDuration) / (float) cycleDuration;

        // Saturação e brilho fixos para cores vivas
        float saturation = 1.0f;
        float brightness = 1.0f;

        // Converte HSB (Hue, Saturation, Brightness) para RGB
        return Color.getHSBColor(hue, saturation, brightness);
    }
}
