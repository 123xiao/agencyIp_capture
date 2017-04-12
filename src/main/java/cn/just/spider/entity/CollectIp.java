package cn.just.spider.entity;

public class CollectIp {
	private String id;

	private String pid;

	private String remarks;

	private String localhost;

	private Integer prot;

	private String ipType;

	private String ipAddress;

	private String operator;

	public String getId() {
		return id;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public String getIpType() {
		return ipType;
	}

	public String getLocalhost() {
		return localhost;
	}

	public String getOperator() {
		return operator;
	}

	public String getPid() {
		return pid;
	}

	public Integer getProt() {
		return prot;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setId(String id) {
		this.id = id == null ? null : id.trim();
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress == null ? null : ipAddress.trim();
	}

	public void setIpType(String ipType) {
		this.ipType = ipType == null ? null : ipType.trim();
	}

	public void setLocalhost(String localhost) {
		this.localhost = localhost == null ? null : localhost.trim();
	}

	public void setOperator(String operator) {
		this.operator = operator == null ? null : operator.trim();
	}

	public void setPid(String pid) {
		this.pid = pid == null ? null : pid.trim();
	}

	public void setProt(Integer prot) {
		this.prot = prot;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks == null ? null : remarks.trim();
	}
}