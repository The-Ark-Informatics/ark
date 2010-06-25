package au.org.theark.core.security;

public enum ShiroConstraint {
	HasRole,
	HasPermission,
	IsAuthenticated,  // in this session
	LoggedIn  // could be from cookie or whatever
}
