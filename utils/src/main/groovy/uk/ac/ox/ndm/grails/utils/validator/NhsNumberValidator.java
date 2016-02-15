package uk.ac.ox.ndm.grails.utils.validator;

import com.google.common.base.Strings;

/**
 * @since 14/08/2015
 */
public class NhsNumberValidator implements Validator<String> {

    public Object isValid(String nhsNumberStr) {
        if (!Strings.isNullOrEmpty(nhsNumberStr)) {

            nhsNumberStr = nhsNumberStr.replaceAll(" ", "");
            if (nhsNumberStr.length() != 10) {
                return "validation.nhsnumber.wronglength";
            }
            try {
                int checkDigit = 0;
                int[] nhsNumber = new int[nhsNumberStr.length()];
                for (int i = 0; i < nhsNumberStr.length(); i++) {
                    nhsNumber[i] = Integer.parseInt(nhsNumberStr.charAt(i) + "");
                }
                if (nhsNumber.length == 10) {
                    for (int i = 0; i <= 8; i++) {
                        checkDigit += nhsNumber[i] * (10 - i);
                    }
                    checkDigit = (11 - (checkDigit % 11));
                    if (checkDigit == 11) checkDigit = 0;
                    if (checkDigit != 10) return checkDigit == nhsNumber[9];
                }
            } catch (NumberFormatException ignored) {
                // Just ignore this as we will return false
            }
        }
        return "validation.empty";
    }
}
