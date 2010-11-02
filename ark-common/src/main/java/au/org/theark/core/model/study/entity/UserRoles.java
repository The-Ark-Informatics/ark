package au.org.theark.core.model.study.entity;

import javax.persistence.*;

	@Entity
	@Table(name = "USER_ROLES", schema = "ETA")
	public class UserRoles implements java.io.Serializable {

		// Fields

		private long id;
		private EtaUser etaUser;
		private Role role;

		// Constructors

		/** default constructor */
		public UserRoles() {
		}

		/** minimal constructor */
		public UserRoles(long id) {
			this.id = id;
		}

		/** full constructor */
		public UserRoles(long id, EtaUser etaUser, Role role) {
			this.id = id;
			this.etaUser = etaUser;
			this.role = role;
		}

		// Property accessors
		@Id
		@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
		public long getId() {
			return this.id;
		}

		public void setId(long id) {
			this.id = id;
		}

		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "USER_ID")
		public EtaUser getEtaUser() {
			return this.etaUser;
		}

		public void setEtaUser(EtaUser etaUser) {
			this.etaUser = etaUser;
		}

		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "ROLE_ID")
		public Role getRole() {
			return this.role;
		}

		public void setRole(Role role) {
			this.role = role;
		}

	}

