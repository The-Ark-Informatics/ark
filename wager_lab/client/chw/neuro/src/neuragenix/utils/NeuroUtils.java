/*
 *  CHW-Neurogenetics Specific Utilities
 *  Copyright (C) Neuragenix, 2004
 *  
 *  Author : Daniel Murley
 *   Email : dmurley@neuragenix.com
 *
 * Description : Provides channel independant functionality that
 *               is specific to CHW-Neurogenetics
 *
 */

package neuragenix.utils;


public class NeuroUtils
{

    public static int getLocationID(int colNumber, int rowNumber, int numberOfRows, int numberOfCols)
    {
        int tempLocationID = 0;

        tempLocationID = (numberOfRows * (rowNumber - 1)) + colNumber;

        return tempLocationID;

    }

}
