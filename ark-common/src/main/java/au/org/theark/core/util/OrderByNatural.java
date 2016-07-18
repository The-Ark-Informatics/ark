package au.org.theark.core.util;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.type.Type;

/**
 * Extends {@link org.hibernate.criterion.Order} to allow ordering by an SQL formula passed by the user.
 * Is simply appends the <code>sqlFormula</code> passed by the user to the resulting SQL query, without any verification.
 * @author Sorin Postelnicu
 * @since Jun 10, 2008
 */
public class OrderByNatural extends Order {

    protected OrderByNatural(String propertyName, boolean ascending) {
        super(propertyName, ascending);
    }

    public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        String[] columns = criteriaQuery.getColumnsUsingProjection(criteria, getPropertyName());
        if (columns.length > 1) {
            throw new HibernateException("There should be one matching column");
        }
        String column = columns[0];
        Type type = criteriaQuery.getTypeUsingProjection(criteria, getPropertyName());
        String sqlString = "study.udf_NaturalSortFormat(" + column + ", 10, \".\") " + (isAscending() ? "asc" : "desc");
        return sqlString;
    }

    public static Order asc(String propertyName) {
        return new OrderByNatural(propertyName, true);
    }

    public static Order desc(String propertyName) {
        return new OrderByNatural(propertyName, false);
    }
}
