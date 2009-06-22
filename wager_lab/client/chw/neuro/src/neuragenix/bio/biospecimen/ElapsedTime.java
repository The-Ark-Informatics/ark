/*
 * ElapsedTime.java
 *
 * Created on 30 August 2004, 22:07
 * Designed for CHW-Neuro customisation to calculate diff
 * between two dates in a years, months, days format.
 *
 * @author  Dan Murley
 *
 */

package neuragenix.bio.biospecimen;

import java.util.*;

public class ElapsedTime {
    
    /** Creates a new instance of ElapsedTime */
    public ElapsedTime() 
    {
       // nill constructor for you!
        
        
        
    }
    
    private int finalDayDifference = 0;
    
    public int getDayDifference()
    {
        return finalDayDifference;
    }
    
    public int getDays (GregorianCalendar g1, GregorianCalendar g2)
    {
        int elapsed = 0;
        GregorianCalendar gc1, gc2;
        
        if (g2.after(g1))
        {
            gc2 = (GregorianCalendar) g2.clone();
            gc1 = (GregorianCalendar) g1.clone();
            
        }
        else
        {
            gc2 = (GregorianCalendar) g1.clone();
            gc1 = (GregorianCalendar) g2.clone();
        }
        
        gc1.clear(Calendar.MILLISECOND);
        gc1.clear(Calendar.SECOND);
        gc1.clear(Calendar.MINUTE);
        gc1.clear(Calendar.HOUR_OF_DAY);
        
        gc2.clear(Calendar.MILLISECOND);
        gc2.clear(Calendar.SECOND);
        gc2.clear(Calendar.MINUTE);
        gc2.clear(Calendar.HOUR_OF_DAY);
        
        while (gc1.before(gc2))
        {
            gc1.add(Calendar.DATE, 1);
            elapsed++;
        }
        return elapsed;
        
    }
    
    public int getMonths(GregorianCalendar g1, GregorianCalendar g2)
    {
        int elapsed = 0;
        GregorianCalendar gc1, gc2;
        
        if (g2.after(g1))
        {
            gc2 = (GregorianCalendar) g2.clone();
            gc1 = (GregorianCalendar) g1.clone();
            
        }
        else
        {
            gc2 = (GregorianCalendar) g1.clone();
            gc1 = (GregorianCalendar) g2.clone();
        }
        
        gc1.clear (Calendar.MILLISECOND);
        gc1.clear (Calendar.SECOND);
        gc1.clear (Calendar.MINUTE);
        gc1.clear (Calendar.HOUR_OF_DAY);
        gc1.clear(Calendar.DATE);
        
        gc2.clear (Calendar.MILLISECOND);
        gc2.clear (Calendar.SECOND);
        gc2.clear (Calendar.MINUTE);
        gc2.clear (Calendar.HOUR_OF_DAY);
        gc2.clear(Calendar.DATE);
        
        while (gc1.before(gc2))
        {
            
                gc1.add(Calendar.MONTH, 1);
                elapsed++;
            
        }
        int gcd1 = gc1.get(Calendar.DATE);
        int gcd2 = gc2.get(Calendar.DATE);
        finalDayDifference = gcd1 - gcd2;
        
        if (finalDayDifference < 0)
        {
            elapsed--;
            gc1.set(Calendar.MONTH, elapsed);
            finalDayDifference = gcd1 - gcd2;
        }
        
        if (finalDayDifference < 0)
           finalDayDifference = finalDayDifference * -1;
        
        return elapsed;
        
    }
// 
    
    /**
     *    This method finds the difference between 2 dates considering date 2
    to be greater than date 1
    The algorithm being year2-year1 then month2-month1 and if a negative value
    then reduce the year value by one and add the negative month to the 12(which is the number
    of months in that year).Then day2-day1 and if negative deduct the number of months by 1 find out the 
    max number of days for that month and add the day difference to the max number of days
     * @param dt1 is the first date
     * @param dt2 is the second date
     * @return a vector with the years,months and the years in that order
     */
    public Vector getDateDifference(Date dt1,Date dt2)
	{
		Vector vtdiff = new Vector();
		int iyear = 0;
		int imonth = 0;
		int iday = 0;
		
		Calendar cl1 = Calendar.getInstance();
		Calendar cl2 = Calendar.getInstance();
		
		cl1.setTime(dt1);
		cl2.setTime(dt2);
		
		if(cl1.before(cl2))
		{	
			iyear = cl2.get(Calendar.YEAR) - cl1.get(Calendar.YEAR);
			imonth = cl2.get(Calendar.MONTH) - cl1.get(Calendar.MONTH);
			if(imonth < 0)
			{
				iyear--;
				imonth = 12 + imonth;
			}
			iday = cl2.get(Calendar.DAY_OF_MONTH) - cl1.get(Calendar.DAY_OF_MONTH);
			if(iday < 0)
			{
				--imonth;
				cl1.add(Calendar.MONTH,imonth);
				int noofdays = cl1.getActualMaximum(cl1.DAY_OF_MONTH);
				iday = noofdays + iday;
			}
			vtdiff.add(Integer.toString(iyear));
			vtdiff.add(Integer.toString(imonth));
			vtdiff.add(Integer.toString(iday));
		}
		else if(cl1.equals(cl2))
		{
			vtdiff.add(Integer.toString(iyear));
			vtdiff.add(Integer.toString(imonth));
			vtdiff.add(Integer.toString(iday));
		}		
		return vtdiff;
	}
    
}
