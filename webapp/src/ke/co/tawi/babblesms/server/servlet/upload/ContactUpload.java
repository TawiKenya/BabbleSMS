package ke.co.tawi.babblesms.server.servlet.upload;

public class ContactUpload {
	   private String uuid;
	   private String name;
	   private String phone;
	   private String email;
	   private String network;
	   

	   public ContactUpload() {
	       // this empty constructor is required
	   }

	   public ContactUpload(String uuid, String name, String phone, String email,String network) {
		   this.uuid = uuid;
	       this.name = name;
	       this.phone = phone;
	       this.email= email;
	       this.network = network;
	   }

	public String getUuid() {
		return uuid;
	}

	public String getName() {
		return name;
	}

	public String getPhone() {
		return phone;
	}

	public String getEmail() {
		return email;
	}

	public String getNetwork() {
		return network;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Contacts [uuid=");
		builder.append(uuid);
		builder.append(", name=");
		builder.append(name);
		builder.append(", phone=");
		builder.append(phone);
		builder.append(", email=");
		builder.append(email);
		builder.append(", network=");
		builder.append(network);
		builder.append("]");
		return builder.toString();
	}

	

	

}
