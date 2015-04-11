/**
 * Copyright 2015 Tawi Commercial Services Ltd
 * 
 * Licensed under the Open Software License, Version 3.0 (the “License”); you may
 * not use this file except in compliance with the License. You may obtain a copy
 * of the License at:
 * http://opensource.org/licenses/OSL-3.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an “AS IS” BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.
 * 
 * See the License for the specific language governing permissions and limitations
 * under the License.
 */
package ke.co.tawi.babblesms.server.servlet.upload;

/**
 * To capture a Contact upon upload.
 * <p>
 *
 * @author <a href="mailto:eugene@tawi.mobi">Eugene Chimita</a>
 */
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
