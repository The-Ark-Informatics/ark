package au.org.theark.core.audit.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * An annotation used by the new Auditing system that allows
 * you to 'tag' a method which should be used as a representation
 * for the entity. 
 * 
 * @author george
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ArkAuditDisplay {
}
