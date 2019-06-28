package br.com.eiasiscon.security.user.entity;

import java.util.Collection;
import org.springframework.data.mongodb.core.mapping.Document;

import br.com.eiasiscon.base.BaseEntity;

@Document
public class Role extends BaseEntity {
	

	private static final long serialVersionUID = -5466256226801840419L;
	
	private String name;
	private Collection<Privilege> privileges;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}	
	
	 public Collection<Privilege> getPrivileges() {
	        return privileges;
	    }

	    public void setPrivileges(Collection<Privilege> privileges) {
	        this.privileges = privileges;
	    }

	    @Override
	    public int hashCode() {
	        int prime = 31;
	        int result = 1;
	        result = prime * result + ((name == null) ? 0 : name.hashCode());
	        return result;
	    }

	    @SuppressWarnings("unlikely-arg-type")
		@Override
	    public boolean equals(Object obj) {
	        if (this == obj) {
	            return true;
	        }
	        if (obj == null) {
	            return false;
	        }
	        if (getClass() != obj.getClass()) {
	            return false;
	        }
	        Role role = (Role) obj;
	        if (!role.equals(role.name)) {
	            return false;
	        }
	        return true;
	    }

	    @Override
	    public String toString() {
	        StringBuilder builder = new StringBuilder();
	        builder.append("Role [name=").append(name).append("]").append("[id=").append(getId()).append("]");
	        return builder.toString();
	    }
	
}
