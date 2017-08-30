/**
 * 
 */
package com.fixit.data;

/**
 * @author 		Kostyantin
 * @createdAt 	2016/12/16 20:59:13 GMT+2
 */
public class VersionInfo {

	private String name;
	private int code;
	
	public VersionInfo() { }
	
	public VersionInfo(String name, int code) {
		this.name = name;
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return name + "(" + code + ")";
	}
}
