package com.songoda.epicenchants.utils.itemnbtapi.utils.nmsmappings;

/**
 * Package enum
 * 
 * @author tr7zw
 *
 */
@SuppressWarnings("javadoc")
public enum PackageWrapper {
	NMS("net.minecraft.server"),
	CRAFTBUKKIT("org.bukkit.craftbukkit"),
	;
	
	private final String uri;

	PackageWrapper(String uri) {
		this.uri = uri;
	}

	/**
	 * @return The Uri for that package
	 */
	public String getUri() {
		return uri;
	}

}
