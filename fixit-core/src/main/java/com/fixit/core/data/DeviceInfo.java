/**
 * 
 */
package com.fixit.core.data;

/**
 * @author 		Kostyantin
 * @createdAt 	2016/12/16 20:56:55 GMT+2
 */
public class DeviceInfo {

	private String brand;
	private String model;
	private String manufacturer;
	private String device;
	private VersionInfo androidVersion;
	
	public DeviceInfo() { }

	public DeviceInfo(String brand, String model, String manufacturer, String device, VersionInfo androidVersion) {
		this.brand = brand;
		this.model = model;
		this.manufacturer = manufacturer;
		this.device = device;
		this.androidVersion = androidVersion;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public VersionInfo getAndroidVersion() {
		return androidVersion;
	}

	public void setAndroidVersion(VersionInfo androidVersion) {
		this.androidVersion = androidVersion;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	@Override
	public String toString() {
		return "DeviceInfo{" +
				"brand='" + brand + '\'' +
				", model='" + model + '\'' +
				", manufacturer='" + manufacturer + '\'' +
				", device='" + device + '\'' +
				", androidVersion=" + androidVersion +
				'}';
	}
}
