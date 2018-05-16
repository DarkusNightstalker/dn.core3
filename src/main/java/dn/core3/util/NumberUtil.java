/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dn.core3.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

/**
 *
 * @author Jhoan Brayam
 */
@UtilityClass
public class NumberUtil {

    public final static MathContext CURRENCY_CONTEXT = new MathContext(2, RoundingMode.HALF_UP);

    public double round(double value, int decimal) {
        return new BigDecimal(value, new MathContext(decimal, RoundingMode.HALF_UP)).doubleValue();
    }

    
    private static final String[] UNIDADES = {"", "UN ", "DOS ", "TRES ", "CUATRO ", "CINCO ", "SEIS ", "SIETE ", "OCHO ", "NUEVE ", "DIEZ ", "ONCE ", "DOCE ", "TRECE ", "CATORCE ", "QUINCE ", "DIECISEIS", "DIECISIETE", "DIECIOCHO", "DIECINUEVE", "VEINTE"};

    private static final String[] DECENAS = {"VENTI", "TREINTA ", "CUARENTA ", "CINCUENTA ", "SESENTA ", "SETENTA ", "OCHENTA ", "NOVENTA ", "CIEN "};

    private static final String[] CENTENAS = {"CIENTO ", "DOSCIENTOS ", "TRESCIENTOS ", "CUATROCIENTOS ", "QUINIENTOS ", "SEISCIENTOS ", "SETECIENTOS ", "OCHOCIENTOS ", "NOVECIENTOS "};

    public static String toLetter(@NonNull String number) throws NumberFormatException {
        return toLetter(Double.parseDouble(number));
    }

    public static String toLetter(@NonNull Double doubleNumber)  throws NumberFormatException {
        StringBuilder converted = new StringBuilder();

        String patternThreeDecimalPoints = "#.###";

        DecimalFormat format = new DecimalFormat(patternThreeDecimalPoints);
        DecimalFormatSymbols symbols = format.getDecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        format.setDecimalFormatSymbols(symbols);
        format.setRoundingMode(RoundingMode.DOWN);

        String formatedDouble = format.format(doubleNumber);
        doubleNumber = Double.parseDouble(formatedDouble);

        if (doubleNumber > 9.99999999E8D) {
            throw new NumberFormatException("El numero es mayor de 999'999.999, no es posible convertirlo");
        }

        if (doubleNumber < 0.0D) {
            throw new NumberFormatException("El numero debe ser positivo");
        }

        String[] splitNumber = String.valueOf(doubleNumber).replace('.', '#').split("#");

        int millon = Integer.parseInt(String.valueOf(getDigitAt(splitNumber[0], 8))
                + String.valueOf(getDigitAt(splitNumber[0], 7))
                + String.valueOf(getDigitAt(splitNumber[0], 6)));
        if (millon == 1) {
            converted.append("**UN MILLON ");
        } else if (millon > 1) {

            converted.append("**").append(convertNumber(String.valueOf(millon))).append("MILLONES ");
        }

        int miles = Integer.parseInt(String.valueOf(getDigitAt(splitNumber[0], 5))
                + String.valueOf(getDigitAt(splitNumber[0], 4))
                + String.valueOf(getDigitAt(splitNumber[0], 3)));
        if (millon >= 1) {
            if (miles == 1) {
                converted.append(convertNumber(String.valueOf(miles))).append("MIL ");
            } else if (miles > 1) {
                converted.append(convertNumber(String.valueOf(miles))).append("MIL ");
            }
        } else {
            if (miles == 1) {
                converted.append("**UN MIL ");
            }

            if (miles > 1) {

                converted.append("**").append(convertNumber(String.valueOf(miles))).append("MIL ");
            }
        }

        int cientos = Integer.parseInt(String.valueOf(getDigitAt(splitNumber[0], 2))
                + String.valueOf(getDigitAt(splitNumber[0], 1))
                + String.valueOf(getDigitAt(splitNumber[0], 0)));
        if ((miles >= 1) || (millon >= 1)) {
            if (cientos >= 1) {
                converted.append(convertNumber(String.valueOf(cientos)));
            }
        } else {
            if (cientos == 1) {
                converted.append("**UN ");
            }
            if (cientos > 1) {
                converted.append("**").append(convertNumber(String.valueOf(cientos)));
            }
        }

        if (millon + miles + cientos == 0) {
            converted.append("**CERO ");
        }
        converted.append("CON ");

        String valor = splitNumber[1];
        if (valor.length() == 1) {
            converted.append(splitNumber[1]).append("0").append("/100 ");
        } else {
            converted.append(splitNumber[1]).append("/100 ");
        }
        converted.append("NUEVOS SOLES**");
        return converted.toString();
    }

    private static String convertNumber(String number) {
        if (number.length() > 3) {
            throw new NumberFormatException("La longitud maxima debe ser 3 digitos");
        }

        if (number.equals("100")) {
            return "CIEN ";
        }

        StringBuilder output = new StringBuilder();
        if (getDigitAt(number, 2) != 0) {
            output.append(CENTENAS[(getDigitAt(number, 2) - 1)]);
        }

        int k = Integer.parseInt(String.valueOf(getDigitAt(number, 1))
                + String.valueOf(getDigitAt(number, 0)));

        if (k <= 20) {
            output.append(UNIDADES[k]);
        } else if ((k > 30) && (getDigitAt(number, 0) != 0)) {

            output.append(DECENAS[(getDigitAt(number, 1) - 2)]).append("Y ").append(UNIDADES[getDigitAt(number, 0)]);
        } else {
            output.append(DECENAS[(getDigitAt(number, 1) - 2)]).append(UNIDADES[getDigitAt(number, 0)]);
        }

        return output.toString();
    }

    private static int getDigitAt(String origin, int position) {
        if ((origin.length() > position) && (position >= 0)) {
            return origin.charAt(origin.length() - position - 1) - '0';
        }
        return 0;
    }
}
