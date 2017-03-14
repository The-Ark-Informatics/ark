package au.org.theark.core.model.config.entity;

/**
 * Created by george on 28/2/17.
 *
 * Explanations of what each possible enum does added as a comment after the field
 *
 */
public enum ValidationType {

    //Number
    /**
     * For Numbers.
     * Ensures that the settings value is greater than the validation value
     * Value required.
     * DB Value = 0
     */
    NUMBER_GREATER_THAN,
    /**
     * For Numbers.
     * Ensures that the settings value is less than the validation value
     * Value required.
     * DB Value = 1
     */
    NUMBER_LESS_THAN,
    /**
     * For Numbers.
     * Ensures that the settings value is greater than or equal to the validation value
     * Value required.
     * DB Value = 2
     */
    NUMBER_GREATER_OR_EQUAL_THAN, //TODO: gramatically incorrect?
    /**
     * For Numbers.
     * Ensures that the settings value is less than or equal to the validation value
     * Value required.
     * DB Value = 3
     */
    NUMBER_LESS_OR_EQUAL_THAN,
    //BETWEEN, //TODO: Is this possible?

    //String
    /**
     * For Strings/Characters
     * Ensures that the settings value is non-empty
     * Value not used
     * DB Value = 4
     */
    CHAR_NON_EMPTY,
    /**
     * For Strings/Characters
     * Ensures that the settings value has a length at least equal to the validation value
     * Value required.
     * DB Value = 5
     */
    CHAR_MIN_LENGTH,
    /**
     * For Strings/Characters
     * Ensures that the settings value has a length no longer than the validation value
     * Value required.
     * DB Value = 6
     */
    CHAR_MAX_LENGTH,

    //Files
    /**
     * For files/directories
     * Ensures that the file exists
     * Value not used
     * DB Value = 7
     */
    FILE_EXISTS,
    /**
     * For files/directories
     * Ensures that the directory exists
     * Value not used
     * DB Value = 8
     */
    DIR_EXISTS,
    /**
     *  For files/directories
     *  Ensures that there is a file specified for the setting
     *  Value not used
     *  DB Value = 9
     */
    FILE_NON_EMPTY
}
