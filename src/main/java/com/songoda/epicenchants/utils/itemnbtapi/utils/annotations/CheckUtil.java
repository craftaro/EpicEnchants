package com.songoda.epicenchants.utils.itemnbtapi.utils.annotations;

import java.lang.reflect.Method;

import com.songoda.epicenchants.utils.itemnbtapi.NbtApiException;
import com.songoda.epicenchants.utils.itemnbtapi.utils.MinecraftVersion;

public class CheckUtil {

	public static boolean isAvaliable(Method method) {
		if(MinecraftVersion.getVersion().getVersionId() < method.getAnnotation(AvaliableSince.class).version().getVersionId())
			throw new NbtApiException("The Method '" + method.getName() + "' is only avaliable for the Versions " + method.getAnnotation(AvaliableSince.class).version() + "+, but still got called!");
		return true;
	}
	
}
